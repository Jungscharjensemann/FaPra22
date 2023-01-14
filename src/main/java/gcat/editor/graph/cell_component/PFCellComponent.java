package gcat.editor.graph.cell_component;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

import java.util.Set;

public abstract class PFCellComponent extends mxCell {

    private Set<MultiMediaType> input, output;

    public PFCellComponent(Object value, mxGeometry geometry, String style) {
        super(value, geometry, style);
    }

    public Set<MultiMediaType> getInputType() {
        return input;
    }

    public Set<MultiMediaType> getOutputType() {
        return output;
    }
}
