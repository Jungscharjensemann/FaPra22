package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class VideoPalette extends EditorPalette {

    public VideoPalette() {
        setTitle("Video");

        addPluginTemplate(new PluginElement(EnumPlugin.VIDEOEXTRACTOR));
        addPluginTemplate(new PluginElement(EnumPlugin.VIDEOSPLITTER));
        addPluginTemplate(new PluginElement(EnumPlugin.VIDEOSHOTDETECTION));
    }
}
