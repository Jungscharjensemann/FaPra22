package gcat.editor.graph.processingflow.components.asset;

import com.mxgraph.model.mxGeometry;
import gcat.editor.graph.processingflow.components.media.MultiMediaType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AssetElement implements IAssetComponent {

    private String name, location;

    private boolean isStart, isEnd;

    private final EnumAsset enumAsset;

    public AssetElement(EnumAsset enumAsset) {
        this.enumAsset = enumAsset;
    }


    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setType(String type) {
        if(enumAsset != null) {
            enumAsset.setType(type);
        }
    }

    @Override
    public String getType() {
        if(enumAsset != null) {
            return enumAsset.getType();
        }
        return "";
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public Element generateDefinition(Document document) {
        Element resourceDefinition = document.createElement("resource-definition");
        resourceDefinition.setAttribute("name", getName());
        resourceDefinition.setAttribute("type", getType());
        resourceDefinition.setAttribute("location", getLocation());
        return resourceDefinition;
    }

    @Override
    public boolean isStart() {
        return isStart;
    }

    @Override
    public void setStart(boolean start) {
        this.isStart = start;
    }

    @Override
    public boolean isEnd() {
        return isEnd;
    }

    @Override
    public void setEnd(boolean end) {
        this.isEnd = end;
    }

    @Override
    public String getLabel() {
        if(enumAsset != null) {
            return enumAsset.getLabel();
        }
        return "";
    }

    @Override
    public mxGeometry getGeometry() {
        if(enumAsset != null) {
            return enumAsset.getGeometry();
        }
        return new mxGeometry(0, 0, 100, 40);
    }

    @Override
    public String getStyle() {
        if(enumAsset != null) {
            return enumAsset.getStyle();
        }
        return "";
    }

    public String getIcon() {
        if(enumAsset != null) {
            return enumAsset.getIcon();
        }
        return "collection.png";
    }

    @Override
    public Set<MultiMediaType> getInput() {
        return null;
    }

    @Override
    public Set<MultiMediaType> getOutput() {
        return new HashSet<>(List.of(MultiMediaType.FILE));
    }
}
