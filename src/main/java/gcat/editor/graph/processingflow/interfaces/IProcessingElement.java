package gcat.editor.graph.processingflow.interfaces;

import java.util.TreeMap;

public interface IProcessingElement {

    String getName();

    String getClassName();

    TreeMap<String, Object> getParameters();

}
