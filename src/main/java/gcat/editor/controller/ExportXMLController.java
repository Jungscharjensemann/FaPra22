package gcat.editor.controller;

import gcat.editor.util.XmlUtil;
import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.dialog.ExportDialog;
import gcat.editor.view.filechooser.Filter;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Controller zum Exportieren
 * eines Processing Flows in eine
 * XML-Datei.
 */
public class ExportXMLController implements ActionListener {

    private final EditorMainFrame reference;

    public ExportXMLController(EditorMainFrame editorMainFrame) {
        this.reference = editorMainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String error = reference.getEditorGraphComponent().validateGraph();
        Object startVertex = reference.getEditorGraph().getStartVertex();
        if(error == null && startVertex != null) {
            Document source = reference.getEditorGraph().createDocument();
            ExportDialog exportDialog = new ExportDialog(source);
            int optionDialog = JOptionPane.showOptionDialog(null, exportDialog, "Ergebnis",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    new Object[] {"Exportieren", "Abbrechen"}, null);
            if(optionDialog == JOptionPane.YES_NO_OPTION) {
                JFileChooser fileChooser = new JFileChooser();
                Filter filter = new Filter(".xml", ".xml (Extensible Markup Language)");
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setFileFilter(filter);
                int saveDialog = fileChooser.showSaveDialog(reference);
                if(saveDialog == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();
                    if(!f.getName().endsWith(".xml")) {
                        f = new File(f.getParent() + File.separator + f.getName() + ".xml");
                    }
                    XmlUtil.createXML(f, source);
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                    null, "Erstellter Graph ist nicht valide!",
                    "Fehler beim Exportieren!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
