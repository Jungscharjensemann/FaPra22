package gcat.editor.controller;

import gcat.editor.graph.processingflow.components.processing.ProcessingFlowComponent;
import gcat.editor.graph.processingflow.components.processing.interfaces.IPFComponent;
import gcat.editor.view.EditorMainFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Controller zum Auswählen des aktuellen
 * Knotens in anderen Benutzerschnittstellen,
 * wie dem CellEditor.
 */
public class GraphVertexController extends MouseAdapter {

    /**
     * EditorMainFrame.
     */
    private final EditorMainFrame editorMainFrame;

    public GraphVertexController(EditorMainFrame reference) {
        editorMainFrame = reference;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        // Komponente an Mausklick.
        Object cell = editorMainFrame.getEditorGraphComponent().getCellAt(e.getX(), e.getY());
        if(cell != null) {
            if(cell instanceof ProcessingFlowComponent) {
                ProcessingFlowComponent component = (ProcessingFlowComponent) cell;
                IPFComponent pfComponent = component.getPFComponent();
                // CellEditor - aktuelle Komponente setzen.
                editorMainFrame.getCellEditor().setCurrentIPFComponent(pfComponent);
                // CellTree - aktuelle Komponente auswählen.
                editorMainFrame.getCellTree().setSelectedNode(component);
            }
        } else {
            // CellEditor - keine aktuelle Komponente.
            editorMainFrame.getCellEditor().setCurrentIPFComponent(null);
        }
    }
}
