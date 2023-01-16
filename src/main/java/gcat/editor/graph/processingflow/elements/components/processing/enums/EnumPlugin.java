package gcat.editor.graph.processingflow.elements.components.processing.enums;

import gcat.editor.graph.cell_component.MultiMediaType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gcat.editor.graph.cell_component.MultiMediaType.*;

public enum EnumPlugin {

    /**
     * Allgemeine GMAF Plugins
     */

    AUDIOEXTRACTOR("AudioExtractor",
            "de.swa.gmaf.plugin.audio.AudioExtractor",
            set(FILE),
            set(MMFG)),
    AUDIOTRANSCRIBER("AudioTranscriber",
            "de.swa.gmaf.plugin.audio.AudioTranscriber",
            set(FILE),
            set(MMFG)),
    SHAZAMPLUGIN("ShazamPlugin",
            "de.swa.gmaf.plugin.audio.ShazamPlugin",
            set(FILE),
            set(MMFG)),
    DOMINANTCOLORDETECTION("DominantColorDetection",
            "de.swa.gmaf.plugin.googlevision.DominantColorDetection",
            set(FILE),
            set(MMFG)),
    FACEDETECTION("FaceDetection",
            "de.swa.gmaf.plugin.googlevision.FaceDetection",
            set(FILE),
            set(MMFG)),
    IMAGETEXTDETECTION("ImageTextDetection",
            "de.swa.gmaf.plugin.googlevision.ImageTextDetection",
            set(FILE),
            set(MMFG)),

    LABELDETECTION("LabelDetection",
            "de.swa.gmaf.plugin.googlevision.LabelDetection",
            set(FILE),
            set(MMFG)),
    LANDMARKDETECTION("LandmarkDetection",
            "de.swa.gmaf.plugin.googlevision.LandmarkDetection",
            set(FILE),
            set(MMFG)),
    LOGODETECTION("LogoDetection",
            "de.swa.gmaf.plugin.googlevision.LogoDetection",
            set(FILE),
            set(MMFG)),
    MOODDETECTION("MoodDetection",
            "de.swa.gmaf.plugin.googlevision.MoodDetection",
            set(FILE),
            set(MMFG)),
    OBJECTDETECTION("ObjectDetection",
            "de.swa.gmaf.plugin.googlevision.ObjectDetection",
            set(FILE),
            set(MMFG)),
    WASHINGTONPOSTINDEXER("WashintonPostIndexer",
            "de.swa.gmaf.plugin.text.WashingtonPostIndexer",
            set(FILE),
            set(MMFG)),
    VIDEOEXTRACTOR("VideoExtractor",
            "de.swa.gmaf.plugin.video.VideoExtractor",
            set(FILE),
            set(MMFG)),
    EXIFHANDLER("ExifHandler",
            "de.swa.gmaf.plugin.ExifHandler",
            set(FILE),
            set(MMFG)),
    MPEG7IMPORT("Mpeg7Import",
            "de.swa.gmaf.plugin.Mpeg7Import",
            set(FILE),
            set(MMFG)),
    VIDEOSHOTDETECTION("VideoShotDetection",
            "de.swa.gmaf.plugin.VideoShotDetection",
            set(FILE),
            set(MMFG)
    ),
    VIDEOSPLITTER("VideoSplitter",
            "de.swa.gmaf.plugin.VideoSplitter",
            set(FILE),
            set(MMFG)
    ),
    REKOGNITIONBASEPLUGIN_JAVAV2("RekognitionBasePlugin_JavaV2",
            "de.swa.fuh.plugins.RekonitionBasePlugin_JavaV2",
            set(FILE),
            set(MMFG)
    ),
    CLARIFAIBASEPLUGIN("ClarifaiBasePlugin",
            "de.swa.fuh.plugins.ClarifaiBasePlugin",
            set(FILE),
            set(MMFG)
    ),
    CARAPI("CarApi",
            "de.swa.fuh.carnet.CarAPI",
            set(FILE),
            set(MMFG)
    ),
    CLARIFAIOBJECTDETECTION("ClarifaiObjectDetection",
            "de.swa.fuh.clarifai.ClarifaiObjectDetection",
            set(FILE),
            set(MMFG)
    ),
    COMPUTERVISION("ComputerVision",
            "de.swa.fuh.microsoft.ComputerVision",
            set(FILE),
            set(MMFG)
    ),
    OPENCVOBJECTDETECTOR("OpenCvObjectDetector",
            "de.swa.fuh.opencv.OpenCVObjectDetector",
            set(FILE),
            set(MMFG)
    ),
    GENERICXMLIMPORT("GenericXmlImport",
            "de.swa.fuh.xml.GenericXMLImporter",
            set(FILE),
            set(MMFG)
    ),
    YOLOOBJECTDETECTION("YoloObjectDetection",
            "de.swa.fuh.yolo.YoloObjectDetection",
            set(FILE),
            set(MMFG)
    ),

    /**
     * Spezielle Varianten / Vorverarbeitung
     */

    DOCXEXTENSIONPLUGIN("DocxExtensionPlugin",
            "de.swa.gmaf.plugin.text.extension.DOCX_ExtensionPlugin",
            set(FILE),
            set(TXT)),
    RSSEXTENSIONPLUGIN("RSSExtensionPlugin",
            "de.swa.gmaf.plugin.text.extension.RSS_ExtensionPlugin",
            set(FILE),
            set(TXT)),
    TXTEXTENSIONPLUGIN("TXTExtensionPlugin",
            "de.swa.gmaf.plugin.text.extension.TXT_ExtensionPlugin",
            set(FILE),
            set(TXT)),
    WAPOEXTENSIONPLUGIN("WapoExtensionPlugin",
            "de.swa.gmaf.plugin.text.extension.WAPO_ExtensionPlugin",
            set(FILE),
            set(TXT)),

    /**
     * Spezielle Varianten / Algorithmik
     */

    BAGOFWORDSDETECTION("BagOfWordsDetection",
            "de.swa.gmaf.plugin.text.BagOfWordsDetection",
            set(TXT),
            set(MMFG)),
    SENTENCEDETECTION("SentenceDetection",
            "de.swa.gmaf.plugin.text.SentenceDetection",
            set(TXT),
            set(MMFG)),
    TFIDFCALCULATION("TFIDFCalculation",
            "de.swa.gmaf.plugin.text.TFIDFCalculation",
            set(TXT),
            set(MMFG)),
    ;

    private final String label;
    private String classPath;

    private final Set<MultiMediaType> input, output;

    EnumPlugin(String label, String classPath, Set<MultiMediaType> input, Set<MultiMediaType> output) {
        this.label = label;
        this.classPath = classPath;
        this.input = input;
        this.output = output;
    }

    public String getLabel() {
        return label;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public Set<MultiMediaType> getInput() {
        return input;
    }

    public Set<MultiMediaType> getOutput() {
        return output;
    }

    private static Set<MultiMediaType> set(MultiMediaType... types) {
        return new HashSet<>(List.of(types));
    }
}
