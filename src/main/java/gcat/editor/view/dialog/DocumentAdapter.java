package gcat.editor.view.dialog;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@FunctionalInterface
public interface DocumentAdapter extends DocumentListener {

    void onInsert();

    @Override
    default void insertUpdate(DocumentEvent e) {
        onInsert();
    }
    @Override
    default void removeUpdate(DocumentEvent e) {}
    @Override
    default void changedUpdate(DocumentEvent e) {}
}
