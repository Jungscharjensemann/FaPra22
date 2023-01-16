package gcat.editor.graph;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxUtils;
import gcat.editor.canvas.EditorInteractiveCanvas;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.print.PageFormat;
import java.util.Objects;

public class EditorGraphComponent extends mxGraphComponent {

    EditorGraph editorGraph;

    public EditorGraphComponent(EditorGraph graph) {
        super(graph);
        editorGraph = graph;

        // Sets switches typically used in an editor
        setPageVisible(true);
        setGridVisible(true);
        setToolTips(true);
        PageFormat format = new PageFormat();
        format.setOrientation(PageFormat.LANDSCAPE);
        setPageFormat(format);

        // Loads the defalt stylesheet from an external file
        mxCodec codec = new mxCodec();
        String uri = Objects.requireNonNull(getClass().getClassLoader().getResource("default-style.xml")).toString();
        Document doc = mxUtils.loadDocument(uri);
        codec.decode(doc.getDocumentElement(), graph.getStylesheet());

        // Sets the background to white
        getViewport().setOpaque(true);
        getViewport().setBackground(Color.WHITE);
    }

    @Override
    protected Dimension getPreferredSizeForPage() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Object[] importCells(Object[] cells, double dx, double dy, Object target, Point location) {
        if (target == null && cells.length == 1 && location != null) {
            target = getCellAt(location.x, location.y);

            if (target instanceof mxICell && cells[0] instanceof mxICell) {
                mxICell targetCell = (mxICell) target;
                mxICell dropCell = (mxICell) cells[0];

                boolean targetVertexValid = targetCell.isVertex() == dropCell.isVertex();
                boolean targetEdgeValid = targetCell.isEdge() == dropCell.isEdge();

                if (targetVertexValid || targetEdgeValid) {
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
    protected mxGraphHandler createGraphHandler() {
        mxGraphHandler mxGraphHandler = new mxGraphHandler(this);
        mxGraphHandler.setCloneEnabled(false);
        return super.createGraphHandler();
    }

    @Override
    protected mxConnectionHandler createConnectionHandler() {
        mxConnectionHandler mxConnectionHandler = new mxConnectionHandler(this);
        // Disable creation of the target, if edge has no specified target.
        mxConnectionHandler.setCreateTarget(false);
        return super.createConnectionHandler();
    }

    @Override
    public mxInteractiveCanvas createCanvas() {
        return new EditorInteractiveCanvas();
    }

}
