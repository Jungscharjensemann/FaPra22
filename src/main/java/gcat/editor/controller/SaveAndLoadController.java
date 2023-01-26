package gcat.editor.controller;

import com.mxgraph.model.mxGraphModel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.filechooser.Filter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SaveAndLoadController implements ActionListener {

    private final EditorMainFrame reference;

    public SaveAndLoadController(EditorMainFrame reference) {
        this.reference = reference;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int result;
        XStream xStream = new XStream(new DomDriver());
        xStream.addPermission(AnyTypePermission.ANY);
        File f;
        Filter filter = new Filter(".pfgm", ".pfgm (Processing Flow GraphModel)");
        switch(e.getActionCommand()) {
            case "saveFile":
                JFileChooser saveFileChooser = new JFileChooser();
                saveFileChooser.addChoosableFileFilter(filter);
                saveFileChooser.setFileFilter(filter);
                result = saveFileChooser.showSaveDialog(reference);
                if(result == JFileChooser.APPROVE_OPTION) {
                    f = saveFileChooser.getSelectedFile();
                    if(!f.getName().endsWith(".pfgm")) {
                        f = new File(f.getParent() + File.separator + f.getName() + ".pfgm");
                    }
                    reference.getEditorConsoleModel().insertText(String.format("Speichere Datei %s...", f.getName()));
                    String xml = null;
                    try {
                        xml = xStream.toXML(reference.getEditorGraph().getModel());
                    } catch(XStreamException xStreamException) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        xStreamException.printStackTrace(pw);
                        reference.getEditorConsoleModel().insertText(
                                String.format("Fehler beim Speichern der Datei %s", f.getName()));
                        reference.getEditorConsoleModel().insertText(sw.toString());
                    }
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter(f);
                        if(xml != null) {
                            writer.write(xml);
                            reference.getEditorConsoleModel().insertText(
                                    String.format("Datei %s gespeichert unter %s", f.getName(), f.getAbsolutePath()));
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } finally {
                        if(writer != null) {
                            try {
                                writer.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case "loadFile":
                JFileChooser loadFileChooser = new JFileChooser();
                loadFileChooser.addChoosableFileFilter(filter);
                loadFileChooser.setFileFilter(filter);
                result = loadFileChooser.showOpenDialog(reference);
                if(result == JFileChooser.APPROVE_OPTION) {
                    f = loadFileChooser.getSelectedFile();
                    if(f.getName().endsWith(".pfgm")) {
                        reference.getEditorConsoleModel().insertText(String.format("Lade Datei %s...", f.getName()));
                        try {
                            Object fromXML = xStream.fromXML(loadFileChooser.getSelectedFile());
                            reference.getEditorConsoleModel().insertText(String.format("Datei %s geladen!", f.getName()));
                            mxGraphModel model = (mxGraphModel) fromXML;
                            reference.getEditorGraph().setModel(model);
                            reference.getCellTree().update();
                            reference.initModelListeners();
                            reference.getEditorGraph().refresh();
                        } catch(XStreamException xStreamException) {
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            xStreamException.printStackTrace(pw);
                            reference.getEditorConsoleModel().insertText(
                                    String.format("Fehler beim Laden der Datei %s", f.getName()));
                            reference.getEditorConsoleModel().insertText(sw.toString());
                        }
                    }
                }
                break;
        }
    }
}
