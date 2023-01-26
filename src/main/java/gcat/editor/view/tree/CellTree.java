package gcat.editor.view.tree;

import gcat.editor.graph.EditorGraph;
import gcat.editor.graph.processingflow.components.processing.ProcessingFlowComponent;
import gcat.editor.view.EditorMainFrame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.*;
import java.awt.*;
import java.util.Objects;

public class CellTree extends JPanel {

    /**
     * EditorGraph.
     */
    private final EditorGraph graph;

    /**
     * CellTree.
     */
    private final JTree graphTree;

    /**
     * Wurzelknoten.
     */
    private final DefaultMutableTreeNode root;

    public CellTree(EditorMainFrame reference) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new TitledBorder("Struktur"));

        graph = reference.getEditorGraph();

        root = new DefaultMutableTreeNode("Graph");

        graphTree = new JTree(root);
        graphTree.setOpaque(false);
        // Einzelauswahl.
        graphTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        /*
         * Listener.
         */
        graphTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) graphTree.getLastSelectedPathComponent();
            if (node != null) {
                Object o = node.getUserObject();
                if(o instanceof ProcessingFlowComponent) {
                    ProcessingFlowComponent comp = (ProcessingFlowComponent) o;
                    if(node.isLeaf()) {
                        reference.getCellEditor().setCurrentIPFComponent(comp.getPFComponent());
                    }
                }
            }
        });
        addNodes(graph);

        /*
         * Icons setzen.
         */
        ImageIcon leafIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/vertex.png")));
        leafIcon = new ImageIcon(leafIcon.getImage().getScaledInstance(16, 16, 0));
        ImageIcon graphIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/graph.png")));
        graphIcon = new ImageIcon(graphIcon.getImage().getScaledInstance(20, 20, 0));
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(leafIcon);
        renderer.setOpenIcon(graphIcon);
        renderer.setClosedIcon(graphIcon);
        graphTree.setCellRenderer(renderer);

        JScrollPane scrollPane = new JScrollPane(graphTree);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Methode zum Hervorheben der
     * im Graphen ausgewählten Komponente.
     * @param component Hervorzuhebene Komponente.
     */
    public void setSelectedNode(ProcessingFlowComponent component) {
        for(int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            if(child.getUserObject().equals(component)) {
                TreeNode[] nodes = ((DefaultTreeModel) graphTree.getModel()).getPathToRoot(child);
                TreePath path = new TreePath(nodes);
                graphTree.setExpandsSelectedPaths(true);
                graphTree.setSelectionPath(path);
                graphTree.scrollPathToVisible(path);
            }
        }
    }

    /**
     * Dem CellTree alle im Graphen vorhandenen
     * Komponenten hinzufügen.
     * @param graph Graph.
     */
    private void addNodes(EditorGraph graph) {
        if(graph != null) {
            root.removeAllChildren();
            for(ProcessingFlowComponent cell : graph.getCells()) {
                root.add(new DefaultMutableTreeNode(cell));
            }
            if(graphTree != null) {
                graphTree.updateUI();
            }
        }
    }

    /**
     * Methode zum Aktualisieren des
     * CellTrees.
     */
    public void update() {
        addNodes(graph);
    }
}
