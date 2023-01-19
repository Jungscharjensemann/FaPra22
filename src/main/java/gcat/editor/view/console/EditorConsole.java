package gcat.editor.view.console;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

/**
 * Konsole für den Editor.
 */
public class EditorConsole extends JPanel {

    private final RSyntaxTextArea rSyntaxTextArea;

    public EditorConsole() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Konsole"));

        rSyntaxTextArea = new RSyntaxTextArea();
        rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        rSyntaxTextArea.setCodeFoldingEnabled(true);
        rSyntaxTextArea.setEditable(false);

        RTextScrollPane scrollPane = new RTextScrollPane(rSyntaxTextArea);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setFocusable(false);

        URL url = getClass().getClassLoader().getResource("images/trash.png");
        ImageIcon trashIcon = new ImageIcon(Objects.requireNonNull(url));
        JButton clearButton = new JButton(trashIcon);
        clearButton.setToolTipText("Löschen");
        clearButton.addActionListener(e -> clear());

        toolBar.add(clearButton);

        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Einfügen von Text in die Konsole.
     * @param text Der einufügende Text
     */
    public void insert(String text) {
        if(rSyntaxTextArea != null) {
            Document doc = rSyntaxTextArea.getDocument();
            try {
                doc.insertString(doc.getLength(), text + "\n", null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Löschen des Textes in der Konsole.
     */
    public void clear() {
        if(rSyntaxTextArea != null) {
            rSyntaxTextArea.setText("");
        }
    }
}
