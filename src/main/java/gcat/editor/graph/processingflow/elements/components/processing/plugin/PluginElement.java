package gcat.editor.graph.processingflow.elements.components.processing.plugin;

import com.mxgraph.model.mxGeometry;
import gcat.editor.graph.cell_component.MultiMediaType;
import gcat.editor.graph.processingflow.elements.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IProcessingComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class PluginElement implements IProcessingComponent {

    private final EnumPlugin enumPlugin;

    private String name;

    private final TreeMap<String, Object> parameters;

    public PluginElement(EnumPlugin plugin) {
        this.enumPlugin = plugin;
        parameters = new TreeMap<>();
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
        if(enumPlugin != null) {
            enumPlugin.setClassPath(classPath);
        }
    }

    @Override
    public String getClassPath() {
        if(enumPlugin != null) {
            return enumPlugin.getClassPath();
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
    public void removeParameter(String key) {
        if(key != null) {
            parameters.remove(key);
        }
    }

    @Override
    public TreeMap<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public Element generateDefinition(Document document) {
        Element pluginDefinition = document.createElement("plugin-definition");
        pluginDefinition.setAttribute("name", getName());
        pluginDefinition.setAttribute("class", getClassPath());
        return pluginDefinition;
    }

    @Override
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

    @Override
    public String getLabel() {
        if(enumPlugin != null) {
            return enumPlugin.getLabel();
        }
        return "";
    }

    @Override
    public mxGeometry getGeometry() {
        return new mxGeometry(0, 0, 160, 40);
    }

    @Override
    public String getStyle() {
        return "pluginVertex";
    }

    @Override
    public Set<MultiMediaType> getInput() {
        if(enumPlugin != null) {
            return enumPlugin.getInput();
        }
        return null;
    }

    @Override
    public Set<MultiMediaType> getOutput() {
        if(enumPlugin != null) {
            return enumPlugin.getOutput();
        }
        return null;
    }


}
