package gcat.editor.graph.cell_component.processing;

import com.mxgraph.model.mxGeometry;
import gcat.editor.graph.cell_component.MultiMediaType;
import gcat.editor.graph.cell_component.PFProcessingComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Set;

public class PFFusionComponent extends PFProcessingComponent {

    private Set<MultiMediaType> input, output;

    public PFFusionComponent(String name, String className, Set<MultiMediaType> input, Set<MultiMediaType> output, Object value, mxGeometry geometry, String style) {
        super(name, className, value, geometry, style);
        this.input = input;
        this.output = output;
    }

    @Override
    public Element generateDefinition() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element element = document.createElement("fusion-definition");
            element.setAttribute("name", getName());
            element.setAttribute("class", getClassName());
            return element;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<MultiMediaType> getInputType() {
        return input;
    }

    @Override
    public Set<MultiMediaType> getOutputType() {
        return output;
    }
}
