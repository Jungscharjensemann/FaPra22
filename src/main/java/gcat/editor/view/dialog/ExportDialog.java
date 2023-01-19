package gcat.editor.view.dialog;

import gcat.editor.util.XmlUtil;
import net.miginfocom.swing.MigLayout;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.StringWriter;

/**
 * Dialog für das Exportieren.
 */
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
        optionPanel.add(generalLabel, "span, split 4, cell 0 2");

        JCheckBox generalCheckBox = new JCheckBox();
        optionPanel.add(generalCheckBox);

        JLabel omitLabel = new JLabel("Omit XML");
        optionPanel.add(omitLabel);

        JCheckBox omitCheckBox = new JCheckBox();
        optionPanel.add(omitCheckBox);

        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        cp.setBorder(new TitledBorder("Preview"));

        RSyntaxTextArea syntaxTextArea = new RSyntaxTextArea();
        syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        syntaxTextArea.setCodeFoldingEnabled(true);
        syntaxTextArea.setEditable(false);

        syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), false));

        RTextScrollPane scrollPane = new RTextScrollPane(syntaxTextArea);
        cp.add(scrollPane);

        nameField.getDocument().addDocumentListener((DocumentAdapter) () -> {
            document.getDocumentElement().setAttribute("name", nameField.getText());
            syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), omitCheckBox.isSelected()));
        });

        extensionField.getDocument().addDocumentListener((DocumentAdapter) () -> {
            document.getDocumentElement().setAttribute("extension", extensionField.getText());
            syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), omitCheckBox.isSelected()));
        });

        generalCheckBox.addItemListener(e -> {
            document.getDocumentElement().setAttribute("isGeneral", String.valueOf(generalCheckBox.isSelected()));
            syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), omitCheckBox.isSelected()));
        });

        omitCheckBox.addItemListener(e -> syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), omitCheckBox.isSelected())));

        add(cp, BorderLayout.CENTER);
    }
}
