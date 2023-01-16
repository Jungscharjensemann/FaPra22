package gcat.editor.controller;

import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import gcat.editor.util.XmlUtil;
import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.dialog.ExportDialog;
import gcat.editor.view.filechooser.Filter;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportXMLController implements ActionListener {

    private final EditorMainFrame reference;

    public ExportXMLController(EditorMainFrame editorMainFrame) {
        this.reference = editorMainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String error = reference.getEditorGraphComponent().validateGraph();
        if(error == null) {
            Document source = reference.getEditorGraph().createDocument();
            ExportDialog exportDialog = new ExportDialog(source);
            int optionDialog = JOptionPane.showOptionDialog(null, exportDialog, "Ergebnis",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] {"Exportieren", "Abbrechen"}, null);
            if(optionDialog == 0) {
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
                    BufferedImage image = mxCellRenderer.createBufferedImage(reference.getEditorGraph(),
                            null, 1, reference.getEditorGraphComponent().getBackground(), reference.getEditorGraphComponent().isAntiAlias(), null,
                            reference.getEditorGraphComponent().getCanvas());
                    mxPngEncodeParam param = mxPngEncodeParam
                            .getDefaultEncodeParam(image);
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(new File(
                                "C:\\Users\\Jens\\Downloads\\png_dec.png"));
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    try
                    {
                        mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream,
                                param);

                        try {
                            encoder.encode(image);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    finally
                    {
                        try {
                            outputStream.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    XmlUtil.createXML(f, source);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Erstellter Graph ist nicht valide!",
                    "Fehler beim Exportieren!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
