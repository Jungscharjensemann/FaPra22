package gcat.editor.graph.cell_component.processing;

import com.mxgraph.model.mxGeometry;
import gcat.editor.graph.cell_component.MultiMediaType;
import gcat.editor.graph.cell_component.PFProcessingComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class PFPluginComponent extends PFProcessingComponent {

    private Set<MultiMediaType> input, output;

    private TreeMap<String, Object> parameters;

    public PFPluginComponent(String name, String className, Set<MultiMediaType> input, Set<MultiMediaType> output, TreeMap<String, Object> parameters, Object value, mxGeometry geometry, String style) {
        super(name, className, value, geometry, style);
        this.input = input;
        this.output = output;
        this.parameters = parameters;
    }

    @Override
    public Element generateDefinition() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element element = document.createElement("plugin-definition");

            element.setAttribute("name", getName());
            element.setAttribute("class", getClassName());
            return element;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Element> generateInformation() {
        List<Element> elementList = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            if(parameters != null) {
                parameters.forEach((key, value) -> {
                    Element element = document.createElement("param");
                    element.setAttribute("name", key);
                    element.setAttribute("value", String.valueOf(value));
                    elementList.add(element);
                });
                return elementList;
            }
        } catch(ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Set<MultiMediaType> getInputType() {
        return input;
    }

    @Override
    public Set<MultiMediaType> getOutputType() {
        return output;
    }

    public TreeMap<String, Object> getParameters() {
        return parameters;
    }
}
