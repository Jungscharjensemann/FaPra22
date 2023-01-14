package gcat.editor.graph.processingflow;

import gcat.editor.graph.processingflow.elements.processing.type.plugins.Plugin;
import gcat.editor.graph.processingflow.interfaces.IProcessingElement;

import java.util.TreeMap;

public class PluginElement implements IProcessingElement {

    private Plugin plugin;

    public PluginElement(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public TreeMap<String, Object> getParameters() {
        return null;
    }
}
