package gcat.editor.graph.processingflow.elements.components.processing.enums;

import gcat.editor.graph.cell_component.MultiMediaType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gcat.editor.graph.cell_component.MultiMediaType.*;

public enum EnumPlugin {

    /**
     * Audio
     */

    AUDIOEXTRACTOR("AudioExtractor",
            "de.swa.gmaf.plugin.audio.AudioExtractor",
            set(),
            set(MMFG)),
    AUDIOTRANSCRIBER("AudioTranscriber",
            "de.swa.gmaf.plugin.audio.AudioTranscriber",
            set(),
            set(MMFG)),
    SHAZAMPLUGIN("ShazamPlugin",
            "de.swa.gmaf.plugin.audio.ShazamPlugin",
            set(),
            set(MMFG)),

    /**
     * GoogleVision
     */

    DOMINANTCOLORDETECTION("DominantColorDetection", "", set(), set(MMFG)),
    FACEDETECTION("FaceDetection", "", set(), set(MMFG)),
    IMAGETEXTDETECTION("ImageTextDetection", "", set(), set(MMFG)),

    LABELDETECTION("LabelDetection",
            "de.swa.gmaf.plugin.googlevision.LabelDetection",
            set(JPG, JPEG, PNG, TIFF, GIF),
            set(MMFG)),
    LANDMARKDETECTION("LandmarkDetection", "", set(), set(MMFG)),
    LOGODETECTION("LogoDetection", "", set(), set(MMFG)),
    MOODDETECTION("MoodDetection", "", set(), set(MMFG)),
    OBJECTDETECTION("ObjectDetection", "", set(), set(MMFG)),

    /**
     * Text
     */

    BAGOFWORDSDETECTION("BagOfWordsDetection", "", set(), set()),
    SENTENCEDETECTION("SentenceDetection", "", set(), set()),
    TFIDFCALCULATION("TFIDFCalculation", "", set(), set()),
    WASHINGTONPOSTINDEXER("WashintonPostIndexer", "", set(), set()),
    DOCXEXTENSIONPLUGIN("DocxExtensionPlugin", "", set(), set()),
    RSSEXTENSIONPLUGIN("RSSExtensionPlugin", "", set(), set()),
    TXTEXTENSIONPLUGIN("TXTExtensionPlugin", "", set(), set()),
    WAPOEXTENSIONPLUGIN("WapoExtensionPlugin", "", set(), set()),

    /**
     * Video
     */

    VIDEOEXTRACTOR("VideoExtractor", "", set(), set()),


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
