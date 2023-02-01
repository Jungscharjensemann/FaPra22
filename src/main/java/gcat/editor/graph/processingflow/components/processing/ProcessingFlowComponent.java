package gcat.editor.graph.processingflow.components.processing;

import com.mxgraph.model.mxCell;
import gcat.editor.graph.processingflow.components.processing.interfaces.IPFComponent;

public class ProcessingFlowComponent extends mxCell {

    private final IPFComponent component;

    public ProcessingFlowComponent(IPFComponent comp) {
        super(comp.getLabel(), comp.getGeometry(), comp.getStyle());
        this.component = comp;
    }

    public IPFComponent getPFComponent() {
        return component;
    }

    @Override
    public String toString() {
        return component.getClass().getSimpleName() +
                "(" +
                String.format("id=%s", getId()) +
                String.format(", label=%s", component.getLabel()) +
                ")";
    }
}
