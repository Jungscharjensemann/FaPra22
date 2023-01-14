package gcat.editor.graph.processingflow.elements.processing.type.plugins;

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
