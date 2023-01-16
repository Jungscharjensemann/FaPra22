package gcat.editor.controller;

import gcat.editor.graph.processingflow.components.processing.ProcessingFlowComponent;
import gcat.editor.graph.processingflow.components.processing.interfaces.IPFComponent;
import gcat.editor.view.EditorMainFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GraphVertexController extends MouseAdapter {

    private final EditorMainFrame editorMainFrame;

    public GraphVertexController(EditorMainFrame reference) {
        editorMainFrame = reference;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        Object cell = editorMainFrame.getEditorGraphComponent().getCellAt(e.getX(), e.getY());
        if(cell != null) {
            if(cell instanceof ProcessingFlowComponent) {
                ProcessingFlowComponent component = (ProcessingFlowComponent) cell;
                IPFComponent pfComponent = component.getPFComponent();
                editorMainFrame.getCellEditor().setCurrentIPFComponent(pfComponent);
            }
        }
    }
}
