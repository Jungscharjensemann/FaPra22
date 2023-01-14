package gcat.editor.graph.processingflow.elements.components.processing.interfaces;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.TreeMap;

public interface IProcessingComponent extends IPFComponent {

    void setName(String name);

    String getName();

    void setClassPath(String classPath);

    String getClassPath();

    void addParameter(String key, Object value);

    TreeMap<String, Object> getParameters();

    Element generateDefinition(Document document);
}
