package gcat.editor.graph.processingflow.elements.components.asset;

import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IPFComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface IAssetComponent extends IPFComponent {

    void setName(String name);

    String getName();

    void setType(String type);

    String getType();

    void setLocation(String location);

    String getLocation();

    Element generateDefinition(Document document);

    boolean isStart();

    void setStart(boolean start);

    boolean isEnd();

    void setEnd(boolean end);
}
