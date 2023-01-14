package gcat.editor.graph.cell_component;

import com.mxgraph.model.mxGeometry;
import org.w3c.dom.Element;

public abstract class PFProcessingComponent extends PFCellComponent {

    private String name;

    private String className;

    public PFProcessingComponent(String name, String className, Object value, mxGeometry geometry, String style) {
        super(value, geometry, style);
        this.name = name;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public abstract Element generateDefinition();

    @Override
    public String toString() {
        return "PFProcessingComponent{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
