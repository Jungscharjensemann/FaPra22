package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class TextPalette extends EditorPalette {

    public TextPalette() {
        setTitle("Text");

        addPluginTemplate(new PluginElement(EnumPlugin.BAGOFWORDSDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.SENTENCEDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.TFIDFCALCULATION));
        addPluginTemplate(new PluginElement(EnumPlugin.WASHINGTONPOSTINDEXER));
        addPluginTemplate(new PluginElement(EnumPlugin.DOCXEXTENSIONPLUGIN));
        addPluginTemplate(new PluginElement(EnumPlugin.RSSEXTENSIONPLUGIN));
        addPluginTemplate(new PluginElement(EnumPlugin.TXTEXTENSIONPLUGIN));
        addPluginTemplate(new PluginElement(EnumPlugin.WAPOEXTENSIONPLUGIN));
    }
}
