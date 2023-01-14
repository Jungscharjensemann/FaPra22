package gcat.editor.view.palletes;

import static gcat.editor.graph.cell_component.MultiMediaType.*;

import gcat.editor.graph.processingflow.elements.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.elements.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class AudioPalette extends EditorPalette {

    public AudioPalette() {
        setTitle("Audio");

        /*addPluginTemplate("AudioExtractor", "de.swa.gmaf.plugin.audio.AudioExtractor",
                set(MPG, MPEG, MOV, FLV, MP4, MXF, QT, M4V), set(MMFG),
                null);

        addPluginTemplate("AudioTranscriber", "de.swa.gmaf.plugin.audio.AudioTranscriber",
                set(WAV, MP3, AIFF, M4A), set(MMFG),
                null);*/

        addPluginTemplate(new PluginElement(EnumPlugin.AUDIOEXTRACTOR));
        addPluginTemplate(new PluginElement(EnumPlugin.AUDIOTRANSCRIBER));
        addPluginTemplate(new PluginElement(EnumPlugin.SHAZAMPLUGIN));
    }
}
