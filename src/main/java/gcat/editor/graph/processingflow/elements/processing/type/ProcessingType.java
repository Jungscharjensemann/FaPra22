package gcat.editor.graph.processingflow.elements.processing.type;

import gcat.editor.graph.processingflow.interfaces.IProcessingElement;

import java.util.TreeMap;

public enum ProcessingType implements IProcessingElement {

    PLUGIN {

        String name, className;

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
            return new TreeMap<>();
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    },

    FUSION {

        String name, className;

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
    },

    EXPORTER {

        String name, className;

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

    ProcessingType() {

    }

    ProcessingType(String name, Element element) {

    }
}
