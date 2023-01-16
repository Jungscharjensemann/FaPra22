package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class AudioPalette extends EditorPalette {

    public AudioPalette() {
        setTitle("Audio");

        addPluginTemplate(new PluginElement(EnumPlugin.AUDIOEXTRACTOR));
        addPluginTemplate(new PluginElement(EnumPlugin.AUDIOTRANSCRIBER));
        addPluginTemplate(new PluginElement(EnumPlugin.SHAZAMPLUGIN));
    }
}
