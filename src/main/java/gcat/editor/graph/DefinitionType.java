package gcat.editor.graph;

public enum DefinitionType {

    PLUGIN_DEFINITION("plugin-definition"),
    FUSION_DEFINITION("fusion-definition"),
    EXPORT_DEFINITION("export-definition"),
    RESOURCE_DEFINITION("resource-definition"),
    PARAM_DEFINITION("param"),
    FLOW_SOURCE_DEFINITION("flow-source"),
    MMFG_PROCESSOR_DEFINITION("mmfg processor"),
    FUSION_PROCESSOR_DEFINITION("fusion processor"),
    EXPORT_TARGET_DEFINITION("export target");

    private final String typeName;

    DefinitionType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
