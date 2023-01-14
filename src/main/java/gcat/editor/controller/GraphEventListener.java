package gcat.editor.controller;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraphView;
import gcat.editor.graph.EditorGraph;
import gcat.editor.view.EditorMainFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class GraphEventListener implements mxEventSource.mxIEventListener, MouseListener {

    private final EditorMainFrame editorMainFrame;

    private final mxCodec encoder;

    public GraphEventListener(EditorMainFrame editorGraphComponentParent) {
        this.editorMainFrame = editorGraphComponentParent;
        encoder = new mxCodec();
    }

    @Override
    public void invoke(Object sender, mxEventObject evt) {
        System.out.println("Name: " + evt.getName());
        EditorGraph graph = editorMainFrame.getEditorGraph();
        switch(evt.getName()) {
            case mxEvent.CHANGE:
                editorMainFrame.getEditorGraphComponent().validateGraph();
                editorMainFrame.setModified(true);
                //editorMainFrame.getErrorLabel().setText(null);
                System.out.println("Change occured!");
                //Node node = encoder.encode(graph.getModel());
                //System.out.println(mxUtils.getPrettyXml(node));

                break;
            case mxEvent.CELLS_ADDED:
                System.out.println("Added");
                break;
            case mxEvent.CELL_CONNECTED:
                System.out.println("Connected");
                break;
            case mxEvent.CONNECT_CELL:
                System.out.println("Connecting...?");
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
                    editorMainFrame.getErrorLabel().setText(null);
                }
                break;
            case mxEvent.REPAINT:
                System.out.println("Repaint...");
                break;
            case mxEvent.SELECT:
                System.out.println(evt);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
