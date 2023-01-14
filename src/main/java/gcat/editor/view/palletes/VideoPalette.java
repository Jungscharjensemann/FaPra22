package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.elements.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.elements.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class VideoPalette extends EditorPalette {

    public VideoPalette() {
        setTitle("Video");

        /*addPluginTemplate("VideoExtractor", "de.swa.gmaf.plugin.video.VideoExtractor",
                null, null, null);

        addPluginTemplate("VideoShotDetection", "de.swa.gmaf.plugin.VideoShotDetection",
                null, null, null);

        addPluginTemplate("VideoSplitter", "de.swa.gmaf.plugin.VideoSplitter",
                null, null, null);*/

        addPluginTemplate(new PluginElement(EnumPlugin.VIDEOEXTRACTOR));
    }
}
