package gcat.editor.view.dialog;

import gcat.editor.graph.processingflow.components.media.MultiMediaType;
import gcat.editor.util.XmlUtil;
import net.miginfocom.swing.MigLayout;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.japura.gui.CheckComboBox;
import org.japura.gui.event.ListCheckListener;
import org.japura.gui.event.ListEvent;
import org.japura.gui.renderer.CheckListRenderer;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.StringWriter;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

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

        CheckComboBox checkComboBox = new CheckComboBox();
        checkComboBox.setTextFor(CheckComboBox.CheckState.NONE, "Kein Dateityp ausgewählt.");
        checkComboBox.setTextFor(CheckComboBox.ALL, "Alle Dateitypen ausgewählt.");
        checkComboBox.setRenderer(new CheckListRenderer() {
            @Override
            public String getText(Object value) {
                return "." + value.toString().toLowerCase();
            }
        });
        optionPanel.add(checkComboBox, "cell 1 1, grow");

        //JTextField extensionField = new JTextField();
        //optionPanel.add(extensionField, "cell 1 1, grow");

        JLabel generalLabel = new JLabel("isGeneral: ");
        optionPanel.add(generalLabel, "span, split 4, cell 0 2");

        JCheckBox generalCheckBox = new JCheckBox();
        optionPanel.add(generalCheckBox);

        EnumSet<MultiMediaType> types = EnumSet.allOf(MultiMediaType.class);
        types.remove(MultiMediaType.IMAGE);
        types.remove(MultiMediaType.MMFG);
        types.remove(MultiMediaType.FILE);

        types.forEach(m -> checkComboBox.getModel().addElement(m));

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

        /*
         * Listener.
         */

        nameField.getDocument().addDocumentListener((DocumentAdapter) () -> {
            document.getDocumentElement().setAttribute("name", nameField.getText());
            syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), omitCheckBox.isSelected()));
        });

        /*extensionField.getDocument().addDocumentListener((DocumentAdapter) () -> {
            document.getDocumentElement().setAttribute("extension", extensionField.getText());
            syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), omitCheckBox.isSelected()));
        });*/

        checkComboBox.getModel().addListCheckListener(new ListCheckListener() {
            @Override
            public void removeCheck(ListEvent listEvent) {
                List<Object> checkeds = checkComboBox.getModel().getCheckeds();
                String extension = join(checkeds);
                if(checkComboBox.getModel().getChecksCount() > 0) {
                    checkComboBox.setTextFor(CheckComboBox.MULTIPLE, extension);
                }
                document.getDocumentElement().setAttribute("extension", extension);
                syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), omitCheckBox.isSelected()));
            }

            @Override
            public void addCheck(ListEvent listEvent) {
                List<Object> checkeds = checkComboBox.getModel().getCheckeds();
                String extension = join(checkeds);
                if(checkComboBox.getModel().getChecksCount() > 0) {
                    checkComboBox.setTextFor(CheckComboBox.MULTIPLE, extension);
                }
                document.getDocumentElement().setAttribute("extension", extension);
                syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), omitCheckBox.isSelected()));
            }
        });

        generalCheckBox.addItemListener(e -> {
            document.getDocumentElement().setAttribute("isGeneral", String.valueOf(generalCheckBox.isSelected()));
            syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), omitCheckBox.isSelected()));
        });

        omitCheckBox.addItemListener(e -> syntaxTextArea.setText(XmlUtil.getXML(document, new StringWriter(), omitCheckBox.isSelected())));

        add(cp, BorderLayout.CENTER);
    }

    private String join(List<Object> types) {
        return types.stream().filter(o -> o instanceof MultiMediaType)
                .map(o -> (MultiMediaType) o)
                .map(o -> "." + o.name().toLowerCase())
                .collect(Collectors.joining(","));
    }
}
