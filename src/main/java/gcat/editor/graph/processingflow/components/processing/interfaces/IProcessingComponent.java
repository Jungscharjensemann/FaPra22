package gcat.editor.graph.processingflow.components.processing.interfaces;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;
import java.util.TreeMap;

public interface IProcessingComponent extends IPFComponent {

    void setName(String name);

    String getName();

    void setClassPath(String classPath);

    String getClassPath();

    void addParameter(String key, Object value);

    void removeParameter(String key);

    TreeMap<String, Object> getParameters();

    Element generateDefinition(Document document);

    List<Element> generateParamDefinition(Document document);
}
