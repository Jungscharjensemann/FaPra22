package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.components.asset.AssetElement;
import gcat.editor.graph.processingflow.components.asset.EnumAsset;
import gcat.editor.view.EditorPalette;

public class AssetPalette extends EditorPalette {

    public AssetPalette() {
        setTitle("Asset");

        addFolderTemplate(new AssetElement(EnumAsset.COLLECTION));
        addFolderTemplate(new AssetElement(EnumAsset.DATABASE));
        addFolderTemplate(new AssetElement(EnumAsset.WATCHFOLDER));
    }
}
