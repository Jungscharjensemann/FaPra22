package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.elements.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.elements.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class TextPalette extends EditorPalette {

    public TextPalette() {
        setTitle("Text");

        /*addPluginTemplate("BagOfWordsDetection", "de.swa.gmaf.plugin.text.BagOfWordsDetection",
                null, null, null);

        addPluginTemplate("SentenceDetection", "de.swa.gmaf.plugin.text.SentenceDetection",
                null, null, null);

        addPluginTemplate("SyntacticDetection", "de.swa.gmaf.plugin.text.SyntacticDetection",
                null, null, null);

        addPluginTemplate("WashingtonPostIndexer", "de.swa.gmaf.plugin.text.WashingtonPostIndexer",
                null, null, null);*/

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
