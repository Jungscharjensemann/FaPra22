package gcat.editor.graph.processingflow.elements.components.processing.fusion;

import com.mxgraph.model.mxGeometry;
import gcat.editor.graph.cell_component.MultiMediaType;
import gcat.editor.graph.processingflow.elements.components.processing.enums.EnumFusion;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IProcessingComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class FusionElement implements IProcessingComponent {

    private final EnumFusion enumFusion;

    private String name;

    private final TreeMap<String, Object> parameters;


    public FusionElement(EnumFusion enumFusion) {
        this.enumFusion = enumFusion;
        this.parameters = new TreeMap<>();
    }

    @Override
    public String getLabel() {
        if(enumFusion != null) {
            return enumFusion.getLabel();
        }
        return "";
    }

    @Override
    public mxGeometry getGeometry() {
        return new mxGeometry(0, 0, 160, 40);
    }

    @Override
    public String getStyle() {
        return "fusionVertex";
    }

    @Override
    public Set<MultiMediaType> getInput() {
        if(enumFusion != null) {
            return enumFusion.getInput();
        }
        return null;
    }

    @Override
    public Set<MultiMediaType> getOutput() {
        if(enumFusion != null) {
            return enumFusion.getOutput();
        }
        return null;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setClassPath(String classPath) {
        if(enumFusion != null) {
            enumFusion.setClassPath(classPath);
        }
    }

    @Override
    public String getClassPath() {
        if(enumFusion != null) {
            return enumFusion.getClassPath();
        }
        return "Hier stimmt was nicht! o.0";
    }

    @Override
    public void addParameter(String key, Object value) {
        if(key != null && value != null) {
            parameters.put(key, value);
        }
    }

    @Override
    public TreeMap<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public Element generateDefinition(Document document) {
        Element fusionDefinition = document.createElement("fusion-definition");
        fusionDefinition.setAttribute("name", getName());
        fusionDefinition.setAttribute("class", getClassPath());
        return fusionDefinition;
    }

    public List<Element> generateParamDefinition(Document document) {
        List<Element> paramDefinitions = new ArrayList<>();
        parameters.forEach((k, v) -> {
            Element e = document.createElement("param");
            //e.setAttribute(String.format("%s.%s", getName(), k), String.valueOf(v));
            e.setAttribute("name", String.format("%s.%s", getName(), k));
            e.setAttribute("value", String.valueOf(v));
            paramDefinitions.add(e);
        });
        return paramDefinitions;
    }
}
