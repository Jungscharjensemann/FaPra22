package gcat.editor.controller;

import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import gcat.editor.graph.EditorGraph;
import gcat.editor.graph.EditorGraphComponent;
import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.filechooser.Filter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Controller zum Exportieren des
 * Graphen in eine PNG-Datei.
 */
public class ExportPNGController implements ActionListener {

    private final EditorMainFrame reference;

    public ExportPNGController(EditorMainFrame reference) {
        this.reference = reference;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorGraph graph = reference.getEditorGraph();
        EditorGraphComponent graphComponent = reference.getEditorGraphComponent();
        JFileChooser fileChooser = new JFileChooser();
        Filter filter = new Filter(".png", ".png (Portable Network Graphics)");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
        int saveDialog = fileChooser.showSaveDialog(reference);
        if(saveDialog == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            if (!f.getName().endsWith(".png")) {
                f = new File(f.getParent() + File.separator + f.getName() + ".png");
            }
            // Bild erstellen
            BufferedImage image =
                    mxCellRenderer.createBufferedImage(
                            graph,
                            null, 1,
                            graphComponent.getBackground(),
                            graphComponent.isAntiAlias(),
                            null,
                            graphComponent.getCanvas());
            mxPngEncodeParam param = mxPngEncodeParam
                    .getDefaultEncodeParam(image);
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(f);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            try {
                mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream, param);
                try {
                    encoder.encode(image);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } finally {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
