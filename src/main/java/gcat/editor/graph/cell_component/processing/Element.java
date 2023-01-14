package gcat.editor.graph.cell_component.processing;

import gcat.editor.graph.processingflow.interfaces.IProcessingElement;

import java.util.TreeMap;

public class Element {

    public enum PluginElement implements IProcessingElement {

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
        };

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

    public enum FusionElement implements IProcessingElement {

        FUSION {

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
        };
    }
}
