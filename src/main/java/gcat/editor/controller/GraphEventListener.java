package gcat.editor.controller;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraphView;
import gcat.editor.graph.EditorGraph;
import gcat.editor.view.EditorMainFrame;

import java.util.List;

public class GraphEventListener implements mxEventSource.mxIEventListener {

    private final EditorMainFrame editorMainFrame;

    public GraphEventListener(EditorMainFrame editorGraphComponentParent) {
        this.editorMainFrame = editorGraphComponentParent;
    }

    @Override
    public void invoke(Object sender, mxEventObject evt) {
        System.out.println("Name: " + evt.getName());
        EditorGraph graph = editorMainFrame.getEditorGraph();
        switch(evt.getName()) {
            case mxEvent.CHANGE:
                editorMainFrame.getEditorGraphComponent().validateGraph();
                editorMainFrame.getCellTree().update();
                System.out.println("Change occured!");
                break;
            case mxEvent.UNDO:
                if(sender instanceof mxIGraphModel || sender instanceof mxGraphView) {
                    mxUndoManager undoManager = editorMainFrame.getUndoManager();
                    undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
                    System.out.println("Undoable edit happend!");
                }
                if(sender instanceof mxUndoManager) {
                    mxUndoableEdit undoableEdit = (mxUndoableEdit) evt.getProperty("edit");
                    List<mxUndoableEdit.mxUndoableChange> changes = undoableEdit.getChanges();
                    graph.setSelectionCells(graph.getSelectionCellsForChanges(changes));
                    System.out.println("Undo from UndoManager!");
                }
                break;
        }
    }
}
