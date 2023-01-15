package gcat.editor.controller;

import com.mxgraph.model.mxGraphModel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.filechooser.Filter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
                    String xml = xStream.toXML(reference.getEditorGraph().getModel());
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter(f);
                        writer.write(xml);
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
                        Object fromXML = xStream.fromXML(loadFileChooser.getSelectedFile());
                        mxGraphModel model = (mxGraphModel) fromXML;
                        reference.getEditorGraph().setModel(model);
                        reference.initModelListeners();
                        reference.getEditorGraph().refresh();
                    }
                }
                break;
        }
    }
}
