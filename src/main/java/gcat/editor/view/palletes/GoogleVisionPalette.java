package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.elements.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.elements.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class GoogleVisionPalette extends EditorPalette {

    public GoogleVisionPalette() {
        setTitle("Google Vision");

        addPluginTemplate(new PluginElement(EnumPlugin.DOMINANTCOLORDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.FACEDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.IMAGETEXTDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.LABELDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.LANDMARKDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.LOGODETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.MOODDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.OBJECTDETECTION));

    }
}
