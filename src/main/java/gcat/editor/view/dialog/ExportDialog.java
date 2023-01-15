package gcat.editor.view.dialog;

import gcat.editor.util.XmlUtil;
import net.miginfocom.swing.MigLayout;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.StringWriter;

public class ExportDialog extends JPanel {

    public ExportDialog(Document document) {
        setLayout(new BorderLayout());
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new MigLayout("", "[left]5[25%]"));
        optionPanel.setBorder(new TitledBorder("Optionen"));
        add(optionPanel, BorderLayout.NORTH);

        JLabel nameLabel = new JLabel("Name: ");
        optionPanel.add(nameLabel, "cell 0 0");

        JTextField nameField = new JTextField();
        optionPanel.add(nameField, "cell 1 0, grow");

        JLabel extensionLabel = new JLabel("Extension: ");
        optionPanel.add(extensionLabel, "cell 0 1");

        JTextField extensionField = new JTextField();
        optionPanel.add(extensionField, "cell 1 1, grow");

        JLabel generalLabel = new JLabel("isGeneral: ");
        optionPanel.add(generalLabel, "cell 0 2");

        JCheckBox generalCheckBox = new JCheckBox();
        optionPanel.add(generalCheckBox, "cell 1 2");


        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        cp.setBorder(new TitledBorder("Preview"));

        RSyntaxTextArea syntaxTextArea = new RSyntaxTextArea();
        syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        syntaxTextArea.setCodeFoldingEnabled(true);
        syntaxTextArea.setEditable(false);

        syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter()));

        RTextScrollPane scrollPane = new RTextScrollPane(syntaxTextArea);
        cp.add(scrollPane);

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                document.getDocumentElement().setAttribute("name", nameField.getText());
                syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        extensionField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                document.getDocumentElement().setAttribute("extension", extensionField.getText());
                syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        generalCheckBox.addItemListener(e -> {
            document.getDocumentElement().setAttribute("isGeneral", String.valueOf(generalCheckBox.isSelected()));
            syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter()));
        });

        add(cp, BorderLayout.CENTER);
    }
}
