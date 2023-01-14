package gcat.editor.view.table.model.cell;

import com.mxgraph.model.mxGeometry;
import org.w3c.dom.Element;

import java.util.HashMap;

public class BagOfWordsDetection extends CellComponent {

    public BagOfWordsDetection(Object value, mxGeometry geometry, String style, String name) {
        super(value, geometry, style, name);
    }

    @Override
    protected Element createElement() {
        return super.createElement("plugin-definition", new HashMap<>());
    }
}
