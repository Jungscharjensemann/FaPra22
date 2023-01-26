package gcat.editor.view.console.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse, die das Model für die
 * Konsole darstellt.
 */
public class EditorConsoleModel {

    /**
     * Listener.
     */
    private final List<ITextInsertListener> textInsertList;

    public EditorConsoleModel() {
        textInsertList = new ArrayList<>();
    }

    /**
     * Listener registrieren.
     * @param textInsertListener Listener.
     */
    public void addListener(ITextInsertListener textInsertListener) {
        if(textInsertListener != null) {
            textInsertList.add(textInsertListener);
        }
    }

    @SuppressWarnings("unused")
    public void removeListener(ITextInsertListener textInsertListener) {
        if(textInsertListener != null) {
            textInsertList.remove(textInsertListener);
        }
    }

    /**
     * Text hinzufügen.
     * @param text Hinzuzufügender Text.
     */
    public void insertText(String text) {
        textInsertList.forEach(l -> l.onInsert(text));
    }

    /**
     * Löschen.
     */
    public void clear() {
        textInsertList.forEach(ITextInsertListener::onClear);
    }
}
