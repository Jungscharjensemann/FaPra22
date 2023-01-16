package gcat.editor.view.palletes;

import gcat.editor.graph.cell_component.MultiMediaType;
import gcat.editor.view.EditorPalette;

import java.util.TreeMap;

public class PluginPalette extends EditorPalette {

    public PluginPalette() {
        setTitle("Plugins");

        /*addTemplateWithAttribute("TextExtractor", null, "shape=plugin", 100, 40, "TextExtraxtor");
        addTemplateWithAttribute("MusicExtractor", null, "shape=plugin", 100, 40, "MusicExtraxtor");
        addTemplateWithAttribute("DominantColors", null, "shape=plugin", 100, 40, "DominantColors");
        addTemplateWithAttribute("EXIFHandler", null, "shape=plugin", 100, 40, "EXIFHandler");
        addTemplateWithAttribute("GoogleLabelDetection", null, "shape=plugin", 100, 40, "GoogleLabelDetection");
        addTemplateWithAttribute("BagOfWordsDetection", null, "shape=plugin", 100, 40, "BagOfWordsDetection");*/

        TreeMap<String, Object> p1 = new TreeMap<>();
        p1.put("lod", 5);
        p1.put("param2", "nüx");

        addPluginTemplate("GoogleLabelDetection", "de.swa.gmaf.plugin.googlevision.LabelDetection",
                set(MultiMediaType.IMAGE), set(MultiMediaType.MMFG), p1);

        addPluginTemplate("TextExtractor","de.swa.gmaf.text.TextExtractor",
                set(MultiMediaType.TXT), set(MultiMediaType.MMFG), null);

        addPluginTemplate("WatchFolder","de.swa.gmaf.text.TextExtractor",
                set(MultiMediaType.TXT), set(MultiMediaType.MMFG), null);

        addPluginTemplate("CollectionFolder","nix",
                null, null, null);

    }
}
