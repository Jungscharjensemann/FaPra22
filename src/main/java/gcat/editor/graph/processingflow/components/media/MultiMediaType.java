package gcat.editor.graph.processingflow.components.media;

public enum MultiMediaType {

    DOC("doc"),
    DOCX("docx"),
    RSS("rss"),
    JSON("json"),
    WAPO("wapo"),
    MPG("mpg"),
    MPEG("mpeg"),
    MOV("mov"),
    FLV("flv"),
    MP4("mp4"),
    MXF("mxf"),
    QT("qt"),
    M4V("m4v"),
    XML("xml"),
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    TIFF("tiff"),
    GIF("gif"),
    WAV("wav"),
    AIFF("aiff"),
    MP3("mp3"),
    M4A("m4a"),

    IMAGE("image"),
    MMFG("mmfg"),
    TXT("text"),
    FILE("file");

    private final String type;

    MultiMediaType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
