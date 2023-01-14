package gcat.editor.graph.cell_component;

import java.util.TreeMap;

public interface IProcessingElement extends IProcessFlowElement {

    TreeMap<String, Object> getAttributes();
}
