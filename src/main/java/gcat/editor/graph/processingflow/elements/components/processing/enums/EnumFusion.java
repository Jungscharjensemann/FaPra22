package gcat.editor.graph.processingflow.elements.components.processing.enums;

import gcat.editor.graph.cell_component.MultiMediaType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gcat.editor.graph.cell_component.MultiMediaType.*;

public enum EnumFusion {

    UNIONFEATUREFUSION("UnionFeatureFusion", "de.swa.gmaf.plugin.fusion.UnionFeatureFusion",
            set(MMFG), set(MMFG)),
    SPACIALFEATUREFUSION("SpacialFeatureFusion", "de.swa.gmaf.plugin.fusion.SpacialFeatureFusion",
            set(MMFG), set(MMFG))
    ;

    private final String label;
    private String classPath;

    private final Set<MultiMediaType> input, output;

    EnumFusion(String label, String classPath, Set<MultiMediaType> input, Set<MultiMediaType> output) {
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
