package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.components.processing.enums.EnumFusion;
import gcat.editor.graph.processingflow.components.processing.fusion.FusionElement;
import gcat.editor.view.EditorPalette;

public class FusionPalette extends EditorPalette {

    public FusionPalette() {
        setTitle("Fusion");

        addFusionTemplate(new FusionElement(EnumFusion.UNIONFEATUREFUSION));

        addFusionTemplate(new FusionElement(EnumFusion.SPACIALFEATUREFUSION));
    }
}
