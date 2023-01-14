package gcat.editor.graph.processingflow;

import gcat.editor.graph.processingflow.interfaces.IProcessingElement;

import java.util.TreeMap;

public enum ProcessingFlowPlugin implements IProcessingElement {

    PLUGIN {

        private String name, className;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getClassName() {
            return className;
        }

        @Override
        public TreeMap<String, Object> getParameters() {
            return null;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    ;

    ProcessingFlowPlugin() {

    }

    public enum Plugin {

        LABEL_DETECTION("LabelDetection"),

        ;

        private final String label;

        Plugin(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
