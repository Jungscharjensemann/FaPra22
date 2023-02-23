package gcat.editor.graph.processingflow.components.asset;

import com.mxgraph.model.mxGeometry;

public enum EnumAsset {

    COLLECTION("Collection", "folder", "collectionVertex", "collection.png", new mxGeometry(0, 0, 100, 40)),
    WATCHFOLDER("Watchfolder", "folder", "watchfolderVertex", "watchfolder.png", new mxGeometry(0, 0, 100, 40)),
    DATABASE("SQL-DB", "database", "databaseVertex", "sql_db.png", new mxGeometry(0, 0, 80, 60));

    private final String label, style, icon;
    private String type;

    private final mxGeometry geometry;

    EnumAsset(String label, String type, String style, String icon, mxGeometry geometry) {
        this.label = label;
        this.type = type;
        this.style = style;
        this.icon = icon;
        this.geometry = geometry;
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

    public String getIcon() {
        return icon;
    }
    public mxGeometry getGeometry() {
        return geometry;
    }
}
