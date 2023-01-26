package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class Miscellaneous extends EditorPalette {

    public Miscellaneous() {
        setTitle("Verschiedenes");

        addPluginTemplate(new PluginElement(EnumPlugin.EXIFHANDLER));
        addPluginTemplate(new PluginElement(EnumPlugin.MPEG7IMPORT));
        addPluginTemplate(new PluginElement(EnumPlugin.REKOGNITIONBASEPLUGIN_JAVAV2));
        addPluginTemplate(new PluginElement(EnumPlugin.CLARIFAIBASEPLUGIN));
        addPluginTemplate(new PluginElement(EnumPlugin.CARAPI));
        addPluginTemplate(new PluginElement(EnumPlugin.CLARIFAIOBJECTDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.COMPUTERVISION));
        addPluginTemplate(new PluginElement(EnumPlugin.OPENCVOBJECTDETECTOR));
        addPluginTemplate(new PluginElement(EnumPlugin.GENERICXMLIMPORT));
        addPluginTemplate(new PluginElement(EnumPlugin.YOLOOBJECTDETECTION));
    }
}
