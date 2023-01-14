package gcat.editor.graph.processingflow.elements.components.processing;

import com.mxgraph.model.mxCell;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IPFComponent;

public class ProcessingFlowComponent extends mxCell {

    //TODO: Interface IPFComponent oder so hinzufügen,
    // um einfaches hinzufügen und testen und casten
    // in Controllern zu ermöglichen.

    private final IPFComponent component;

    public ProcessingFlowComponent(IPFComponent comp) {
        super(comp.getLabel(), comp.getGeometry(), comp.getStyle());
        this.component = comp;
    }

    public IPFComponent getPFComponent() {
        return component;
    }
}
