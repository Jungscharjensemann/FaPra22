package gcat.editor.controller.keyboard;

import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxUndoManager;
import gcat.editor.view.EditorMainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EditorKeyBoardHandler extends mxKeyboardHandler {

    private final EditorMainFrame editorMainFrame;

    /**
     * @param graphComponent
     */
    public EditorKeyBoardHandler(EditorMainFrame editorGraphComponentParent, mxGraphComponent graphComponent) {
        super(graphComponent);
        editorMainFrame = editorGraphComponentParent;
    }

    protected InputMap getInputMap(int condition)
    {
        InputMap map = super.getInputMap(condition);

        if (condition == JComponent.WHEN_FOCUSED && map != null)
        {
            /*map.put(KeyStroke.getKeyStroke("control S"), "save");
            map.put(KeyStroke.getKeyStroke("control shift S"), "saveAs");
            map.put(KeyStroke.getKeyStroke("control N"), "new");
            map.put(KeyStroke.getKeyStroke("control O"), "open");*/

            map.put(KeyStroke.getKeyStroke("control Z"), "undo");
            map.put(KeyStroke.getKeyStroke("control Y"), "redo");

            /*map.put(KeyStroke.getKeyStroke("control shift V"), "selectVertices");
            map.put(KeyStroke.getKeyStroke("control shift E"), "selectEdges");*/
        }

        return map;
    }


    protected ActionMap createActionMap()
    {
        ActionMap map = super.createActionMap();

        /*map.put("save", new EditorActions.SaveAction(false));
        map.put("saveAs", new EditorActions.SaveAction(true));
        map.put("new", new EditorActions.NewAction());
        map.put("open", new EditorActions.OpenAction());*/
        map.put("undo", new UndoRedoAction(true));
        map.put("redo", new UndoRedoAction(false));
        map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
        map.put("selectEdges", mxGraphActions.getSelectEdgesAction());

        return map;
    }

    class UndoRedoAction extends AbstractAction {

        private final boolean undo;

        public UndoRedoAction(boolean undo) {
            this.undo = undo;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mxUndoManager undoManager = editorMainFrame.getUndoManager();
            if(undo) {
                undoManager.undo();
            } else {
                undoManager.redo();
            }
        }
    }

}
