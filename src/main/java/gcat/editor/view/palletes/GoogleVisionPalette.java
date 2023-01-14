package gcat.editor.view.palletes;

import gcat.editor.graph.processingflow.elements.components.asset.CollectionElement;
import gcat.editor.graph.processingflow.elements.components.asset.EnumAsset;
import gcat.editor.graph.processingflow.elements.components.processing.enums.EnumPlugin;
import gcat.editor.graph.processingflow.elements.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorPalette;

public class GoogleVisionPalette extends EditorPalette {

    public GoogleVisionPalette() {
        setTitle("Google Vision");

        /*addPluginTemplate("DominantColorDetection", "de.swa.gmaf.plugin.googlevision.DominantColorDetection",
                null, null, null);

        addPluginTemplate("FaceDetection", "de.swa.gmaf.plugin.googlevision.FaceDetection",
                null, null, null);

        addPluginTemplate("ImageTextDetection", "de.swa.gmaf.plugin.googlevision.ImageTextDetection",
                null, null, null);

        TreeMap<String, Object> p1 = new TreeMap<>();
        p1.put("lod", 5);
        p1.put("lod", 5);

        addPluginTemplate("LabelDetection", "de.swa.gmaf.plugin.googlevision.LabelDetection",
                set(MultiMediaType.IMAGE), set(MultiMediaType.MMFG), p1);

        addPluginTemplate("LandmarkDetection", "de.swa.gmaf.plugin.googlevision.LandmarkDetection",
                null, null, p1);

        addPluginTemplate("LogoDetection", "de.swa.gmaf.plugin.googlevision.LogoDetection",
                null, null, null);

        addPluginTemplate("MoodDetection", "de.swa.gmaf.plugin.googlevision.MoodDetection",
                null, null, null);

        addPluginTemplate("ObjectDetection", "de.swa.gmaf.plugin.googlevision.ObjectDetection",
                null, null, null);

        //ProcessingFlowComponent pf = new ProcessingFlowComponent(new PluginElement(EnumPlugin.LABEL_DETECTION))*/

        addPluginTemplate(new PluginElement(EnumPlugin.DOMINANTCOLORDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.FACEDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.IMAGETEXTDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.LABELDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.LANDMARKDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.LOGODETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.MOODDETECTION));
        addPluginTemplate(new PluginElement(EnumPlugin.OBJECTDETECTION));

        //addFolderTemplate(new CollectionElement(EnumAsset.COLLECTION));


    }
}
