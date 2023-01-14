package gcat.editor.graph.processingflow.elements.processing.type;

public enum Element {

    LABEL_DETECTION("LabelDetection", "de.swa.gmaf.plugin.googlevision.LabelDetection")

    ;

    final String label, classPath;

    Element(String label, String classPath) {
        this.label = label;
        this.classPath = classPath;
    }

    public String getLabel() {
        return label;
    }

    public String getClassPath() {
        return classPath;
    }
}
