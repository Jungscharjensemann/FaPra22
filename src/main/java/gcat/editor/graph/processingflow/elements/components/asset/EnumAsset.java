package gcat.editor.graph.processingflow.elements.components.asset;

public enum EnumAsset {

    COLLECTION("Collection", "folder", "collectionVertex"),
    WATCHFOLDER("Watchfolder", "folder", "watchfolderVertex");

    ;

    private final String label, style;
    private String type;

    EnumAsset(String label, String type, String style) {
        this.label = label;
        this.type = type;
        this.style = style;
    }

    public String getLabel() {
        return label;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getStyle() {
        return style;
    }
}
