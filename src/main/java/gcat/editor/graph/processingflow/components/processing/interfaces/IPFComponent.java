package gcat.editor.graph.processingflow.components.processing.interfaces;

import com.mxgraph.model.mxGeometry;
import gcat.editor.graph.processingflow.components.media.MultiMediaType;

import java.io.Serializable;
import java.util.Set;

public interface IPFComponent extends Serializable {

    String getLabel();

    mxGeometry getGeometry();

    String getStyle();

    Set<MultiMediaType> getInput();

    Set<MultiMediaType> getOutput();
}
