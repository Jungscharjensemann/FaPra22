package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.elements.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.elements.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class PreProcessingPalette extends EditorPalette {

    public PreProcessingPalette() {
        setTitle("Vorverarbeitung");

        addPluginTemplate(new PluginElement(EnumPlugin.DOCXEXTENSIONPLUGIN));
        addPluginTemplate(new PluginElement(EnumPlugin.RSSEXTENSIONPLUGIN));
        addPluginTemplate(new PluginElement(EnumPlugin.TXTEXTENSIONPLUGIN));
        addPluginTemplate(new PluginElement(EnumPlugin.WAPOEXTENSIONPLUGIN));
    }
}
