package gcat.editor.controller;

import com.mxgraph.io.mxCodec;
import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.dialog.ExportDialog;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExportXMLController implements ActionListener {

    private final EditorMainFrame reference;

    public ExportXMLController(EditorMainFrame editorMainFrame) {
        this.reference = editorMainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Document source = reference.getEditorGraph().createDocument();
        ExportDialog exportDialog = new ExportDialog(source);
        int result = JOptionPane.showOptionDialog(null, exportDialog, "Ergebnis",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] {"Exportieren", "Abbrechen"}, null);
        if(result == 0) {
            mxCodec codec = new mxCodec();
            codec.encode(reference.getEditorGraph());
        }
    }
}
