package gcat.editor.graph.processingflow.elements.components.processing.interfaces;

import com.mxgraph.model.mxGeometry;
import gcat.editor.graph.cell_component.MultiMediaType;

import java.io.Serializable;
import java.util.Set;

public interface IPFComponent extends Serializable {

    String getLabel();

    mxGeometry getGeometry();

    String getStyle();

    Set<MultiMediaType> getInput();

    Set<MultiMediaType> getOutput();
}
