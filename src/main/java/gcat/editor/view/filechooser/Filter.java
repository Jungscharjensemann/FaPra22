package gcat.editor.view.filechooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class Filter extends FileFilter {

    private final String ext, desc;

    public Filter(String ext, String desc) {
        this.ext = ext;
        this.desc = desc;
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().endsWith(ext);
    }

    @Override
    public String getDescription() {
        return desc;
    }
}
