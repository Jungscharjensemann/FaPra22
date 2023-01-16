package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class AlgorithmicPalette extends EditorPalette {

    public AlgorithmicPalette() {
        setTitle("Algorithmik");

        addPluginTemplate(new PluginElement(EnumPlugin.BAGOFWORDSDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.SENTENCEDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.TFIDFCALCULATION));
    }
}
