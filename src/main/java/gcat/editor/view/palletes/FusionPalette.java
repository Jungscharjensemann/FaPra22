package gcat.editor.view.palletes;

import gcat.editor.graph.cell_component.MultiMediaType;
import gcat.editor.graph.processingflow.elements.components.processing.enums.EnumFusion;
import gcat.editor.graph.processingflow.elements.components.processing.fusion.FusionElement;
import gcat.editor.view.EditorPalette;

public class FusionPalette extends EditorPalette {

    public FusionPalette() {
        setTitle("Fusion");

        /*addFusionTemplate("UnionFeatureFusion", "de.swa.gmaf.plugin.fusion.UnionFeatureFusion",
                set(MultiMediaType.MMFG), set(MultiMediaType.MMFG), "UnionFeatureFusion", 160, 40, "fusionVertex");

        addFusionTemplate("SpacialFeatureFusion", "de.swa.gmaf.plugin.fusion.SpacialFeatureFusion",
                set(MultiMediaType.MMFG), set(MultiMediaType.MMFG), "SpacialFeatureFusion", 160, 40, "fusionVertex");*/

        addFusionTemplate(new FusionElement(EnumFusion.UNIONFEATUREFUSION));

        addFusionTemplate(new FusionElement(EnumFusion.SPACIALFEATUREFUSION));
    }
}
