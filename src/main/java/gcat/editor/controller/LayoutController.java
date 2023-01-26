package gcat.editor.controller;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.*;
import com.mxgraph.util.mxRectangle;
import gcat.editor.graph.EditorGraph;
import gcat.editor.graph.EditorGraphComponent;
import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.dialog.Swirl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller zum Layouten
 * des Graphen.
 */
public class LayoutController implements ActionListener {

    private final EditorMainFrame reference;

    public LayoutController(EditorMainFrame reference) {
        this.reference = reference;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorGraph graph = reference.getEditorGraph();
        EditorGraphComponent graphComponent = reference.getEditorGraphComponent();
        if(graph != null && e.getSource() instanceof JMenuItem) {
            mxIGraphLayout layout;
            switch(e.getActionCommand()) {
                case "hierarchVertical":
                    layout = new mxHierarchicalLayout(graph);
                    break;
                case "hierarchHorizontal":
                    layout = new mxHierarchicalLayout(graph, JLabel.WEST);
                    break;
                case "circle":
                    JOptionPane.showOptionDialog(null,
                            new Swirl(), "Schwindelig?",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE, null,
                            new String[] {"Ja!", "Nein!"}, null);

                    layout = new mxCircleLayout(graph);
                    break;
                case "organic":
                    layout = new mxOrganicLayout(graph);
                    break;
                case "verticalPartition":
                    layout = new mxPartitionLayout(graph, false, 10) {
                        @Override
                        public mxRectangle getContainerSize() {
                            return graphComponent.getLayoutAreaSize();
                        }
                    };
                    break;
                case "horizontalPartition":
                    layout = new mxPartitionLayout(graph, true, 10) {
                        @Override
                        public mxRectangle getContainerSize() {
                            return graphComponent.getLayoutAreaSize();
                        }
                    };
                    break;
                case "verticalStack":
                    layout = new mxStackLayout(graph, false, 10, 50, 50, 0) {
                        @Override
                        public mxRectangle getContainerSize() {
                            return graphComponent.getLayoutAreaSize();
                        }
                    };
                    break;
                case "horizontalStack":
                    layout = new mxStackLayout(graph, true) {
                        @Override
                        public mxRectangle getContainerSize() {
                            return graphComponent.getLayoutAreaSize();
                        }
                    };
                    break;
                case "compact":
                default:
                    layout = new mxCompactTreeLayout(graph);
                    break;
            }
            graph.getModel().beginUpdate();
            try {
                layout.execute(graph.getDefaultParent());
            } catch(Exception exp) {
                exp.printStackTrace();
            }
            graph.getModel().endUpdate();
        }
    }
}
