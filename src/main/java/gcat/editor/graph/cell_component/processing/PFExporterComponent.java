package gcat.editor.graph.cell_component.processing;

import com.mxgraph.model.mxGeometry;
import gcat.editor.graph.cell_component.PFProcessingComponent;
import org.w3c.dom.Element;

public class PFExporterComponent extends PFProcessingComponent {

    public PFExporterComponent(String name, String className, Object value, mxGeometry geometry, String style) {
        super(name, className, value, geometry, style);
    }

    @Override
    public Element generateDefinition() {
        return null;
    }
}
