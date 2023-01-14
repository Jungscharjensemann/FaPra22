package gcat.editor.graph.cell_component;

import java.util.TreeMap;

public enum ProcessingElement implements IProcessingElement {

    PLUGIN("", "") {

        @Override
        public TreeMap<String, Object> getAttributes() {
            return new TreeMap<>();
        }
    };

    private final String name, className;

    ProcessingElement(String name, String className) {
        this.name = name;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }
}
