package gcat.editor.graph.traversal;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import gcat.editor.graph.processingflow.elements.components.asset.IAssetComponent;
import gcat.editor.graph.processingflow.elements.components.processing.ProcessingFlowComponent;
import gcat.editor.graph.processingflow.elements.components.processing.fusion.FusionElement;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IPFComponent;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IProcessingComponent;
import gcat.editor.graph.processingflow.elements.components.processing.plugin.PluginElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PFVisitor implements IPFVisitor {

    List<Element> pluginDefinitions;
    List<List<Element>> paramDefinitions;
    List<Element> fusionDefinitions;

    List<Element> resourceDefinitions;

    List<Element> sequence;

    private final Document document;

    private final mxGraph graph;

    public PFVisitor(mxGraph graph, Document document) {
        this.graph = graph;
        this.document = document;
        pluginDefinitions = new ArrayList<>();
        paramDefinitions = new ArrayList<>();
        fusionDefinitions = new ArrayList<>();
        resourceDefinitions = new ArrayList<>();
        sequence = new ArrayList<>();
    }

    @Override
    public boolean visit(Object vertex, Object edge, Set<Object> notVisited) {
        if(vertex instanceof ProcessingFlowComponent) {
            ProcessingFlowComponent comp = (ProcessingFlowComponent) vertex;
            IPFComponent pfComponent = comp.getPFComponent();
            if(pfComponent instanceof PluginElement) {
                pluginDefinitions.add(((PluginElement) pfComponent).generateDefinition(document));
                paramDefinitions.add(((PluginElement) pfComponent).generateParamDefinition(document));
            } else if(pfComponent instanceof FusionElement) {
                fusionDefinitions.add(((FusionElement) pfComponent).generateDefinition(document));
                Element mmfg = document.createElement("mmfg");
                String processor = Arrays.stream(graph.getIncomingEdges(vertex))
                        .filter(e -> ((mxCell) e).getSource() instanceof ProcessingFlowComponent)
                        .map(cell -> (ProcessingFlowComponent) ((mxCell) cell).getSource())
                        .map(cell -> (IProcessingComponent) cell.getPFComponent())
                        .map(IProcessingComponent::getName).collect(Collectors.joining(","));
                mmfg.setAttribute("processor", processor);
                Element fusion = document.createElement("fusion");
                fusion.setAttribute("processor", ((FusionElement) pfComponent).getName());
                sequence.add(mmfg);
                sequence.add(fusion);
            } else if(pfComponent instanceof IAssetComponent) {
                resourceDefinitions.add(((IAssetComponent) pfComponent).generateDefinition(document));
                if(((IAssetComponent) pfComponent).isStart()) {
                    Element start = document.createElement("flow-source");
                    start.setAttribute("name", ((IAssetComponent) pfComponent).getName());
                    sequence.add(start);
                } else if(((IAssetComponent) pfComponent).isEnd()) {
                    /*if(!notVisited.isEmpty()) {
                        return false;
                    }*/
                    System.out.println("Endknoten erreicht!");
                    if(!notVisited.isEmpty()) {
                        System.out.println("Es sind noch Knoten vorhanden, die nicht besucht wurden!");
                        if(notVisited.contains(vertex)) {
                            System.out.println("Nur noch der Endknoten wurde noch nicht besucht!");
                            if(notVisited.size() == 1) {
                                Element end = document.createElement("export");
                                end.setAttribute("target", ((IAssetComponent) pfComponent).getName());
                                sequence.add(end);
                                return true;
                            } else {
                                return false;
                            }
                        }
                        return false;
                        /*if(notVisited.contains(vertex) && notVisited.size() == 1) {
                            System.out.println("Nur noch der Endknoten wurde noch nicht besucht!");
                            Element end = document.createElement("export");
                            end.setAttribute("target", ((IAssetComponent) pfComponent).getName());
                            sequence.add(end);
                            return true;
                        }*/
                        //return false;
                    }
                    /*Element end = document.createElement("export");
                    end.setAttribute("target", ((IAssetComponent) pfComponent).getName());
                    sequence.add(end);*/
                }
            }
        }
        System.out.printf("Knoten %s wurde erfolgreich besucht!\n", vertex);
        return true;
    }

    @Override
    public boolean visit(Object vertex, Object edge) {
        if(vertex instanceof ProcessingFlowComponent) {
            ProcessingFlowComponent comp = (ProcessingFlowComponent) vertex;
            IPFComponent pfComponent = comp.getPFComponent();
            if(pfComponent instanceof PluginElement) {
                pluginDefinitions.add(((PluginElement) pfComponent).generateDefinition(document));
                paramDefinitions.add(((PluginElement) pfComponent).generateParamDefinition(document));
            } else if(pfComponent instanceof FusionElement) {
                fusionDefinitions.add(((FusionElement) pfComponent).generateDefinition(document));
                Element mmfg = document.createElement("mmfg");
                String processor = Arrays.stream(graph.getIncomingEdges(vertex))
                        .filter(e -> ((mxCell) e).getSource() instanceof ProcessingFlowComponent)
                        .map(cell -> (ProcessingFlowComponent) ((mxCell) cell).getSource())
                        .map(cell -> (IProcessingComponent) cell.getPFComponent())
                        .map(IProcessingComponent::getName).collect(Collectors.joining(","));
                mmfg.setAttribute("processor", processor);
                Element fusion = document.createElement("fusion");
                fusion.setAttribute("processor", ((FusionElement) pfComponent).getName());
                sequence.add(mmfg);
                sequence.add(fusion);
            } else if(pfComponent instanceof IAssetComponent) {
                resourceDefinitions.add(((IAssetComponent) pfComponent).generateDefinition(document));
                if(((IAssetComponent) pfComponent).isStart()) {
                    Element start = document.createElement("flow-source");
                    start.setAttribute("name", ((IAssetComponent) pfComponent).getName());
                    sequence.add(start);
                } else if(((IAssetComponent) pfComponent).isEnd()) {
                    Element end = document.createElement("export");
                    end.setAttribute("target", ((IAssetComponent) pfComponent).getName());
                    sequence.add(end);
                }
            }
        }
        //System.out.println("Vertex: " + vertex + ((edge != null) ? (edge instanceof mxCell ? ", Target: " + ((mxCell) edge).getTarget() : "") : ""));*/
        System.out.println("Vertex: " + vertex);
        return true;
    }

    public List<Element> getPluginDefinitions() {
        return pluginDefinitions;
    }

    public List<List<Element>> getParamDefinitions() {
        return paramDefinitions;
    }

    public List<Element> getFusionDefinitions() {
        return fusionDefinitions;
    }

    public List<Element> getResourceDefinitions() {
        return resourceDefinitions;
    }

    public List<Element> getSequence() {
        return sequence;
    }
}
