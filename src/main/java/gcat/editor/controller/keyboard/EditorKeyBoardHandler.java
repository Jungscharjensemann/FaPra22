package gcat.editor.controller.keyboard;

import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxUndoManager;
import gcat.editor.view.EditorMainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Klasse zum Kontrollieren von
 * Eingaben der Tastatur.
 */
public class EditorKeyBoardHandler extends mxKeyboardHandler {

    private final EditorMainFrame editorMainFrame;

    public EditorKeyBoardHandler(EditorMainFrame editorGraphComponentParent, mxGraphComponent graphComponent) {
        super(graphComponent);
        editorMainFrame = editorGraphComponentParent;
    }

    /**
     * Mapping für die Tastatureingaben.
     * @param condition Bedingung.
     * @return InputMap.
     */
    protected InputMap getInputMap(int condition)
    {
        InputMap map = super.getInputMap(condition);

        if (condition == JComponent.WHEN_FOCUSED && map != null)
        {
            map.put(KeyStroke.getKeyStroke("control Z"), "undo");
            map.put(KeyStroke.getKeyStroke("control Y"), "redo");
        }

        return map;
    }

    /**
     * Mapping für die Aktionen.
     * @return ActionMap.
     */
    protected ActionMap createActionMap()
    {
        ActionMap map = super.createActionMap();

        map.put("undo", new UndoRedoAction(true));
        map.put("redo", new UndoRedoAction(false));
        map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
        map.put("selectEdges", mxGraphActions.getSelectEdgesAction());

        return map;
    }

    /**
     * Undo/Redo-Aktion.
     */
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
