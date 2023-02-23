package gcat.editor.graph;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraphView;
import gcat.editor.canvas.EditorInteractiveCanvas;
import gcat.editor.graph.processingflow.components.processing.ProcessingFlowComponent;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.print.PageFormat;
import java.util.Hashtable;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class EditorGraphComponent extends mxGraphComponent {

    EditorGraph editorGraph;

    public EditorGraphComponent(EditorGraph graph) {
        super(graph);
        editorGraph = graph;

        // Visuelle Eigenschaften setzen.
        setPageVisible(true);
        setPageBreaksVisible(false);
        setGridVisible(true);
        setToolTips(true);
        PageFormat format = new PageFormat();
        format.setOrientation(PageFormat.LANDSCAPE);
        setPageFormat(format);
        setZoomPolicy(ZOOM_POLICY_PAGE);
        setCenterZoom(true);
        setCenterPage(true);

        setPreferPageSize(true);

        // Eigenes (blinkendes) Warn-Icon.
        //warningIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/yellow_warning.gif")));
        //warningIcon = new ImageIcon(warningIcon.getImage().getScaledInstance(24, 24, Image.SCALE_FAST));

        // Stylesheet laden.
        mxCodec codec = new mxCodec();
        String uri = Objects.requireNonNull(getClass().getClassLoader().getResource("default-style.xml")).toString();
        Document doc = mxUtils.loadDocument(uri);
        codec.decode(doc.getDocumentElement(), graph.getStylesheet());

        // Hintergrund setzen.
        getViewport().setOpaque(true);
        getViewport().setBackground(Color.WHITE);
    }

    @Override
    protected Dimension getPreferredSizeForPage() {
        int w = farthestWidth(), h = farthestHeight();
        return new Dimension(Math.max(w, getWidth()), Math.max(h, getHeight() - 15));
    }

    private int farthestWidth() {
        return (int) editorGraph.getCells().stream()
                .mapToDouble(cell -> cell.getGeometry().getX() + cell.getGeometry().getWidth())
                .max().orElse(0);
    }

    private int farthestHeight() {
        return (int) editorGraph.getCells().stream()
                .mapToDouble(cell -> cell.getGeometry().getY() + cell.getGeometry().getHeight())
                .max().orElse(0);
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getMinimumSize();
    }

    /**
     * Zellen einfügen. (Übernommen vom Beispiel)
     * @param cells Zellen.
     * @param dx X-Position.
     * @param dy Y-Position.
     * @param target Ziel-Zelle.
     * @param location Punkt.
     * @return Eingefügte Zellen.
     */
    @Override
    public Object[] importCells(Object[] cells, double dx, double dy, Object target, Point location) {
        if(target == null && cells.length == 1 && location != null) {
            target = getCellAt(location.x, location.y);

            if(target instanceof ProcessingFlowComponent) {
                return super.importCells(new Object[]{ cells[0] }, dx, dy, graph.getCurrentRoot(), location);
            }

            if(target instanceof mxICell && cells[0] instanceof mxICell) {
                mxICell targetCell = (mxICell) target;
                mxICell dropCell = (mxICell) cells[0];

                boolean targetVertexValid = targetCell.isVertex() == dropCell.isVertex();
                boolean targetEdgeValid = targetCell.isEdge() == dropCell.isEdge();

                if(targetVertexValid || targetEdgeValid) {
                    mxIGraphModel model = graph.getModel();
                    model.setStyle(target, model.getStyle(cells[0]));
                    graph.setSelectionCell(target);

                    return null;
                }
            }
        }

        return super.importCells(cells, dx, dy, target, location);
    }

    @Override
    public String validateGraph(Object cell, Hashtable<Object, Object> context) {
        mxIGraphModel model = graph.getModel();
        mxGraphView view = graph.getView();
        boolean isValid = true;
        int childCount = model.getChildCount(cell);

        TreeSet<String> errors = new TreeSet<>();

        for (int i = 0; i < childCount; i++)
        {
            Object tmp = model.getChildAt(cell, i);
            Hashtable<Object, Object> ctx = context;

            if (graph.isValidRoot(tmp))
            {
                ctx = new Hashtable<>();
            }

            String warn = validateGraph(tmp, ctx);
            if(warn != null) {
                errors.add(warn);
            }

            // Knoten mit Id 1 wird ignoriert (andernfalls wird in der Ecke
            // oben links ebenfalls eine Fehlermeldung angezeigt, wenn im
            // Graphen splitEnabled true ist).
            boolean ignoreCellId1 = Objects.equals(((mxICell) cell).getId(), "1");

            if (warn != null && ignoreCellId1)
            {
                String html = "<html>" + warn.replaceAll("\n", "<br>") + "</html>";
                int len = html.length();
                setCellWarning(tmp, html.substring(0, len));
            }
            else
            {
                setCellWarning(tmp, null);
            }

            isValid = isValid && warn == null;
        }

        StringBuilder warning = new StringBuilder();

        // Adds error for invalid children if collapsed (children invisible)
        if (graph.isCellCollapsed(cell) && !isValid)
        {
            warning.append(mxResources.get("containsValidationErrors",
                    "Contains Validation Errors")).append("\n");
        }

        // Checks edges and cells using the defined multiplicities
        if (model.isEdge(cell))
        {
            String tmp = graph.getEdgeValidationError(cell,
                    model.getTerminal(cell, true),
                    model.getTerminal(cell, false));

            if (tmp != null)
            {
                warning.append(tmp);
            }
        }
        else
        {
            String tmp = graph.getCellValidationError(cell);

            if (tmp != null)
            {
                warning.append(tmp);
            }
        }

        // Checks custom validation rules
        String err = graph.validateCell(cell, context);

        if (err != null)
        {
            warning.append(err);
        }

        // Updates the display with the warning icons before any potential
        // alerts are displayed
        if (model.getParent(cell) == null)
        {
            view.validate();
        }

        if(warning.length() > 0) {
            errors.add(warning.toString());
        }

        String finalWarning = errors.stream().filter(Objects::nonNull).collect(Collectors.joining("\n"));

        return (finalWarning.length() > 0 || !isValid) ? finalWarning : null;
    }

    @Override
    protected mxGraphHandler createGraphHandler() {
        mxGraphHandler mxGraphHandler = new mxGraphHandler(this);
        mxGraphHandler.setCloneEnabled(false);
        return super.createGraphHandler();
    }

    @Override
    protected mxConnectionHandler createConnectionHandler() {
        mxConnectionHandler mxConnectionHandler = new mxConnectionHandler(this);
        // Keine Kopie erstellen, wenn kein Zielknoten gefunden.
        mxConnectionHandler.setCreateTarget(false);
        return super.createConnectionHandler();
    }

    /**
     * Eigene Landwand, um Labels besser zu zeichnen.
     * @return Leinwand.
     */
    @Override
    public mxInteractiveCanvas createCanvas() {
        return new EditorInteractiveCanvas();
    }

}
