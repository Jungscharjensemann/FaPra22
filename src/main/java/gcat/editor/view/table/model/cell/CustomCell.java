package gcat.editor.view.table.model.cell;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

public class CustomCell extends mxCell {

    // DONT OVERRIDE getId(). Id's are unique!

    public CustomCell(Object value, mxGeometry mxGeometry, String style) {
        super(value, mxGeometry, style);
    }


}
