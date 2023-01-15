package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.elements.components.asset.AssetElement;
import gcat.editor.graph.processingflow.elements.components.asset.EnumAsset;
import gcat.editor.view.EditorPalette;

public class MiscellaneousPalette extends EditorPalette {

    public MiscellaneousPalette() {
        setTitle("Sonstiges");

        //addTemplateWithAttribute("FeatureUnion", null, "shape=fusion", 100, 40, "FeatureUnion");
        //addTemplateWithAttribute("RelevanzOptimizer", null, "shape=fusion", 100, 40, "RelevanzOptimizer");

        /*addPluginTemplate("ExifHandler", "de.swa.gmaf.plugin.ExifHandler",
                null, null, null);

        addPluginTemplate("Mpeg7Import", "de.swa.gmaf.plugin.Mpeg7Import",
                null, null, null);

        addPluginTemplate("ProcessFlowPlugin", "de.swa.gmaf.plugin.ProcessFlowPlugin",
                null, null, null);*/

        addFolderTemplate(new AssetElement(EnumAsset.COLLECTION));
        addFolderTemplate(new AssetElement(EnumAsset.DATABASE));
    }
}
