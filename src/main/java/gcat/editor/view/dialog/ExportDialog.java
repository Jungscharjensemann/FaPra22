package gcat.editor.view.dialog;

import net.miginfocom.swing.MigLayout;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.StringWriter;

public class ExportDialog extends JPanel {

    private Document document;

    public ExportDialog(Document document) {
        this.document = document;
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

        syntaxTextArea.setText(getXML(document));

        RTextScrollPane scrollPane = new RTextScrollPane(syntaxTextArea);
        cp.add(scrollPane);

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                document.getDocumentElement().setAttribute("name", nameField.getText());
                syntaxTextArea.setText(getXML(document));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        /*nameField.addActionListener(e -> {
            document.getDocumentElement().setAttribute("name", nameField.getText());
            syntaxTextArea.setText(getXML(document));
        });*/

        /*extensionField.addActionListener(e -> {
            document.getDocumentElement().setAttribute("extension", extensionField.getText());
            syntaxTextArea.setText(getXML(document));
        });*/

        extensionField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                document.getDocumentElement().setAttribute("extension", extensionField.getText());
                syntaxTextArea.setText(getXML(document));
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
            syntaxTextArea.setText(getXML(document));
        });

        add(cp, BorderLayout.CENTER);
    }

    private String getXML(Document document) {
        StringWriter writer = new StringWriter();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

            transformer.transform(
                    new DOMSource(document),
                    new StreamResult(writer));
        } catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }



        return writer.toString();
    }
}
