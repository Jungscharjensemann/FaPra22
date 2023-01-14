package ui;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxGraphicsCanvas2D;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.shape.mxStencil;
import com.mxgraph.shape.mxStencilRegistry;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraph;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTaskPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ui.taskpanes.CustomJXTaskPane;
import ui.taskpanes.GoogleVisionAPIJXTaskPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private JPanel mainPanel;

    public MainFrame() {
        initFrame();
        initComponents();
    }

    private void initFrame() {
        // relative Höhe des Fensters (60%).
        double heightPercentage = 0.65;
        // Seitenverhältnis
        double aspectRatio = 16.0 / 10.0;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) (dim.height * heightPercentage);
        int width = (int) (height * aspectRatio);

        // Parameter des Haupt-Frames konfigurieren.
        setTitle("FaPra [Jens Nathan Andreß, 9763180]");
        setBounds((dim.width - width) / 2, (dim.height - height) / 2, width, height);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        // Hauptpanel
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JSplitPane splitPane = new JSplitPane();
        splitPane.setOneTouchExpandable(false);
        mainPanel.add(splitPane);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(270, 2));
        scrollPane.setMinimumSize(new Dimension(270, -1));
        splitPane.setLeftComponent(scrollPane);

        JPanel panel_1 = new JPanel();
        scrollPane.setViewportView(panel_1);

        //panel_1.setLayout(new MigLayout("", "[grow]", "[][][][]"));

        //CustomJXTaskPane c1 = new CustomJXTaskPane("Google Vision API");
        GoogleVisionAPIJXTaskPane c1 = new GoogleVisionAPIJXTaskPane();
        //panel_1.add(c1, "cell 0 0, growx, aligny top");

        CustomJXTaskPane c2 = new CustomJXTaskPane("RSS / Text Detection");
        //panel_1.add(c2, "cell 0 1, growx, aligny top");

        CustomJXTaskPane c3 = new CustomJXTaskPane("Other");
        //panel_1.add(c3, "cell 0 2, growx, aligny top");

        CustomJXTaskPane c4 = new CustomJXTaskPane("Test4");
        //panel_1.add(c4, "cell 0 3, growx, aligny top");

        List<JXTaskPane> taskPanes = new ArrayList<>();
        taskPanes.add(c1);
        taskPanes.add(c2);
        taskPanes.add(c3);
        taskPanes.add(c4);

        panel_1.setLayout(new MigLayout("", "[grow]", "[]".repeat(taskPanes.size())));

        for(int i = 0; i < taskPanes.size(); i++) {
            panel_1.add(taskPanes.get(i), "cell 0 " + i + ", growx, aligny top");
        }

        JPanel panel = new JPanel();
        splitPane.setRightComponent(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));


        /*GraphModel model = new DefaultGraphModel();
        GraphLayoutCache view = new GraphLayoutCache(model, new DefaultCellViewFactory());

        JGraph jGraph = new JGraph(model, view);
        jGraph.setAntiAliased(true);

        DefaultGraphCell cell = new DefaultGraphCell("Hello");

        GraphConstants.setBounds(cell.getAttributes(), );


        jGraph.getGraphLayoutCache().insert(cell);*/

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        panel.add(tabbedPane);

        //tabbedPane.addTab("New Tab", null, jGraph, null);



        /*mxGraph graph = new mxGraph();

        graph.setResetEdgesOnMove(false);

        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30);
            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150, 80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);
        } finally {
            graph.getModel().endUpdate();
        }
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        tabbedPane.addTab("New Tab", null, graphComponent, null);*/

        try {
            String fileName = getClass().getClassLoader().getResource("shapes/shapes.xml").getPath();

            Document doc = mxXmlUtils.parseXml(mxUtils.readFile(fileName));

            Element shapes = (Element) doc.getDocumentElement();
            NodeList list = shapes.getElementsByTagName("shape");

            for (int i = 0; i < list.getLength(); i++)
            {
                Element shape = (Element) list.item(i);
                mxStencilRegistry.addStencil(shape.getAttribute("name"),
                        new mxStencil(shape));
            }

            mxGraph graph = new mxGraph();

            graph.setAllowDanglingEdges(false);
            graph.setDisconnectOnMove(false);

            mxGraphComponent graphComponent = new mxGraphComponent(graph);
            //graphComponent.setConnectable(false);

            new mxRubberband(graphComponent);

            Object parent = graph.getDefaultParent();

            graph.getModel().beginUpdate();
            try
            {
                mxIGraphModel model = graph.getModel();
                Object v1 = graph
                        .insertVertex(parent, null, "Hello", 20, 20, 80, 30,
                                "shape=and;fillColor=#ffdd00;strokeColor=#ff0000");
                Object v2 = graph.insertVertex(parent, null, "World!", 240, 150, 80, 30, "shape=xor;");
                //String edgeStyle= String.format("rounded=%b;edgeStyle=%s;endArrow=%s;", true, mxConstants.EDGESTYLE_SIDETOSIDE, mxConstants.ARROW_BLOCK);
                Object edge = graph.insertEdge(parent, null, "Edge", v1, v2, null);



                StringBuilder styleSB = new StringBuilder();
                //styleSB.append(mxConstants.STYLE_DASHED).append("=true;");
                styleSB.append(mxConstants.STYLE_ROUNDED).append("=true;");
                styleSB.append(mxConstants.STYLE_EDGE).append("=").append(mxConstants.EDGESTYLE_SIDETOSIDE).append(";");
                styleSB.append(mxConstants.STYLE_ENDARROW).append("=").append(mxConstants.ARROW_BLOCK).append(";");
                styleSB.append(mxConstants.STYLE_STROKEWIDTH).append("=").append("0.7").append(";");
                styleSB.append(mxConstants.STYLE_STROKECOLOR).append("=").append("black").append(";");
                styleSB.append(mxConstants.STYLE_ENDFILL).append("=").append("white").append(";");
                model.setStyle(edge, styleSB.toString());
            }
            finally
            {
                graph.getModel().endUpdate();
            }

            mxCodec encoder = new mxCodec();
            Node node = encoder.encode(graph.getModel());
            System.out.println(mxUtils.getPrettyXml(node));

            tabbedPane.addTab("New Tab", null, graphComponent, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
