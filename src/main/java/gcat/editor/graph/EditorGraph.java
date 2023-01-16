package gcat.editor.graph;

import com.google.common.collect.Sets;
import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxGraphStructure;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import gcat.editor.graph.processingflow.components.media.MultiMediaType;
import gcat.editor.graph.processingflow.components.asset.AssetElement;
import gcat.editor.graph.processingflow.components.asset.IAssetComponent;
import gcat.editor.graph.processingflow.components.processing.ProcessingFlowComponent;
import gcat.editor.graph.processingflow.components.processing.fusion.FusionElement;
import gcat.editor.graph.processingflow.components.processing.interfaces.IPFComponent;
import gcat.editor.graph.processingflow.components.processing.interfaces.IProcessingComponent;
import gcat.editor.graph.processingflow.components.processing.plugin.PluginElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class EditorGraph extends mxGraph {

    public static final NumberFormat numberFormat = NumberFormat.getInstance();

    public EditorGraph() {
        setAllowLoops(true);
        setMultigraph(false);
        setAllowDanglingEdges(false);
        setResetViewOnRootChange(false);
        setEdgeLabelsMovable(false);
        setKeepEdgesInBackground(true);
        setCellsEditable(false);
        setCellsResizable(false);
    }

    /**
     * Prints out some useful information about the cell in the tooltip.
     */
    @Override
    public String getToolTipForCell(Object cell)
    {
        String tip = "<html>";
        mxGeometry geo = getModel().getGeometry(cell);
        mxCellState state = getView().getState(cell);

        if (getModel().isEdge(cell))
        {
            tip += "points={";

            if (geo != null)
            {
                List<mxPoint> points = geo.getPoints();

                if (points != null)
                {

                    for (mxPoint point : points) {
                        tip += "[x=" + numberFormat.format(point.getX())
                                + ",y=" + numberFormat.format(point.getY())
                                + "],";
                    }

                    tip = tip.substring(0, tip.length() - 1);
                }
            }

            tip += "}<br>";
            tip += "absPoints={";

            if (state != null)
            {

                for (int i = 0; i < state.getAbsolutePointCount(); i++)
                {
                    mxPoint point = state.getAbsolutePoint(i);
                    tip += "[x=" + numberFormat.format(point.getX())
                            + ",y=" + numberFormat.format(point.getY())
                            + "],";
                }

                tip = tip.substring(0, tip.length() - 1);
            }

            tip += "}";
        }
        else
        {
            tip += "geo=[";

            if (geo != null)
            {
                tip += "x=" + numberFormat.format(geo.getX()) + ",y="
                        + numberFormat.format(geo.getY()) + ",width="
                        + numberFormat.format(geo.getWidth()) + ",height="
                        + numberFormat.format(geo.getHeight());
            }

            tip += "]<br>";
            tip += "state=[";

            if (state != null)
            {
                tip += "x=" + numberFormat.format(state.getX()) + ",y="
                        + numberFormat.format(state.getY()) + ",width="
                        + numberFormat.format(state.getWidth())
                        + ",height="
                        + numberFormat.format(state.getHeight());
            }

            tip += "]";
        }

        mxPoint trans = getView().getTranslate();

        tip += "<br>scale=" + numberFormat.format(getView().getScale())
                + ", translate=[x=" + numberFormat.format(trans.getX())
                + ",y=" + numberFormat.format(trans.getY()) + "]";
        tip += "</html>";

        return tip;
    }

    @Override
    public String validateEdge(Object edge, Object source, Object target) {
        System.out.printf("Validating %s with source %s and target %s%n", edge, source, target);

        mxAnalysisGraph analysisGraph = new mxAnalysisGraph();
        analysisGraph.setGraph(this);

        if(mxGraphStructure.isCyclicDirected(analysisGraph)) {
            return "Zuvor hinzugefügte Kante induziert einen Kreis!";
        }

        if(source instanceof ProcessingFlowComponent && target instanceof ProcessingFlowComponent) {

            ProcessingFlowComponent sourceComponent = (ProcessingFlowComponent) source;
            ProcessingFlowComponent targetComponent = (ProcessingFlowComponent) target;

            IPFComponent pfSource = sourceComponent.getPFComponent();
            IPFComponent pfTarget = targetComponent.getPFComponent();

            Set<MultiMediaType> outputSet = pfSource.getOutput();
            Set<MultiMediaType> inputSet = pfTarget.getInput();

            if(pfSource instanceof AssetElement && pfTarget instanceof AssetElement) {
                if(pfSource == pfTarget) {
                    return "Assets erlauben keine Schlaufen auf sich selbst!";
                }
                return "Zwei Assets können nicht miteinander verbunden werden!";
            }
            if(pfSource instanceof AssetElement && pfTarget instanceof IProcessingComponent) {
                if(((AssetElement) pfSource).isEnd()) {
                    return "Als Endknoten markiertes Asset kann nicht mit \n" +
                            "anderen Komponenten verbunden werden!";
                }
            }

            if(outputSet == null && inputSet != null) {
                if(!inputSet.isEmpty()) {
                    ((mxCell) edge).setValue(
                            inputSet.stream()
                                    .map(MultiMediaType::getType)
                                    .map(String::toLowerCase)
                                    .collect(Collectors.joining(","))
                    );
                }
            } else if(outputSet != null && inputSet == null) {
                if(!outputSet.isEmpty()) {
                    ((mxCell) edge).setValue(
                            outputSet.stream()
                                    .map(MultiMediaType::getType)
                                    .map(String::toLowerCase)
                                    .collect(Collectors.joining(","))
                    );
                }
            }

            if(outputSet != null && inputSet != null) {
                Sets.SetView<MultiMediaType> set = Sets.intersection(outputSet, inputSet);

                if(!set.isEmpty()) {
                    ((mxCell) edge).setValue(
                            outputSet.stream()
                                    .map(MultiMediaType::getType)
                                    .map(String::toLowerCase)
                                    .collect(Collectors.joining(","))
                    );
                } else {
                    return "Ein- und Ausgabe sind inkompatibel!";
                }
            }
        }
        return null;
    }

    public void updateStart(IAssetComponent assetComponent) {
        mxAnalysisGraph aGraph = getAnalysisGraph();
        Arrays.stream(aGraph.getChildCells(getDefaultParent(), true, false))
                .filter(v -> v instanceof ProcessingFlowComponent)
                .map(v -> (ProcessingFlowComponent) v)
                .map(ProcessingFlowComponent::getPFComponent)
                .filter(v -> v instanceof IAssetComponent)
                .map(v -> (IAssetComponent) v)
                .filter(v -> v != assetComponent && v.isStart())
                .forEach(v -> {
                    v.setStart(false);
                    v.setEnd(true);
                });
    }

    public void updateEnd(IAssetComponent assetComponent) {
        mxAnalysisGraph aGraph = getAnalysisGraph();
        Arrays.stream(aGraph.getChildCells(getDefaultParent(), true, false))
                .filter(v -> v instanceof ProcessingFlowComponent)
                .map(v -> (ProcessingFlowComponent) v)
                .map(ProcessingFlowComponent::getPFComponent)
                .filter(v -> v instanceof IAssetComponent)
                .map(v -> (IAssetComponent) v)
                .filter(v -> v != assetComponent && v.isEnd())
                .forEach(v -> {
                    v.setStart(true);
                    v.setEnd(false);
                });
    }

    @Override
    public void cellsAdded(Object[] cells, Object parent, Integer index, Object source, Object target, boolean absolute, boolean constrain) {
        super.cellsAdded(cells, parent, index, source, target, absolute, constrain);

        Arrays.asList(cells).forEach(e -> {
            if(e instanceof ProcessingFlowComponent) {
                ProcessingFlowComponent comp = (ProcessingFlowComponent) e;
                IPFComponent processingComponent = comp.getPFComponent();
                if(processingComponent instanceof PluginElement) {
                    ((PluginElement) processingComponent).setName(String.format("plugin%s", countInstances(PluginElement.class)));
                }
                if(processingComponent instanceof FusionElement) {
                    ((FusionElement) processingComponent).setName(String.format("merge%s", countInstances(FusionElement.class)));
                }
            }
        });
    }

    private long countInstances(Class<?> clazz) {
        mxAnalysisGraph analysisGraph = getAnalysisGraph();
        List<Object> extractedCells = Arrays.asList(analysisGraph.getChildCells(getDefaultParent(), true, false));

        return extractedCells.stream()
                .filter(cell -> cell instanceof ProcessingFlowComponent)
                .map(cell -> (ProcessingFlowComponent) cell)
                .filter(pfComp -> pfComp.getPFComponent().getClass().isAssignableFrom(clazz)).count();
    }

    private mxAnalysisGraph getAnalysisGraph() {
        mxAnalysisGraph analysisGraph = new mxAnalysisGraph();
        analysisGraph.setGraph(this);
        return analysisGraph;
    }

    private void topologialSort(Object vertex, Set<Object> visited, Stack<Object> stack) {
        if(vertex != null) {
            if(visited == null) {
                visited = new HashSet<>();
            }
            List<Object> neighbours = getNeighbours(vertex);
            for (Object v : neighbours) {
                if (v != null && !visited.contains(v)) {
                    topologialSort(v, visited, stack);
                    visited.add(v);
                }
            }
            stack.push(vertex);
        }

    }

    private List<Object> getNeighbours(Object cell) {
        return Arrays.stream(getOutgoingEdges(cell)).map(c -> ((mxCell) c).getTarget()).collect(Collectors.toList());
    }

    public Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document document = builder.newDocument();
        Object startVertex = getStartVertex();

        Stack<Object> stack = new Stack<>();
        topologialSort(startVertex, null, stack);
        Collections.reverse(stack);

        System.out.println("Stack: " + stack);

        List<Element> pluginDefinitions = new ArrayList<>();
        List<List<Element>> paramDefinitions = new ArrayList<>();
        List<Element> fusionDefinitions = new ArrayList<>();
        List<Element> resourceDefinitions = new ArrayList<>();
        List<Element> sequence = new ArrayList<>();

        stack.stream()
                .filter(o -> o instanceof ProcessingFlowComponent)
                .map(o -> (ProcessingFlowComponent) o)
                .forEach(comp -> {
                    IPFComponent pfComp = comp.getPFComponent();
                    /*if(pfComp instanceof PluginElement) {
                        pluginDefinitions.add(((PluginElement) pfComp).generateDefinition(document));
                        paramDefinitions.add(((PluginElement) pfComp).generateParamDefinition(document));
                    } else if(pfComp instanceof FusionElement) {
                        fusionDefinitions.add(((FusionElement) pfComp).generateDefinition(document));
                        paramDefinitions.add(((FusionElement) pfComp).generateParamDefinition(document));
                        Element mmfg = document.createElement("mmfg");
                        String processor = Arrays.stream(getIncomingEdges(comp))
                                .filter(e -> ((mxCell) e).getSource() instanceof ProcessingFlowComponent)
                                .map(cell -> (ProcessingFlowComponent) ((mxCell) cell).getSource())
                                .map(cell -> (IProcessingComponent) cell.getPFComponent())
                                .map(IProcessingComponent::getName).collect(Collectors.joining(","));
                        mmfg.setAttribute("processor", processor);
                        Element fusion = document.createElement("fusion");
                        fusion.setAttribute("processor", ((FusionElement) pfComp).getName());
                        sequence.add(mmfg);
                        sequence.add(fusion);
                    }*/
                    if(pfComp instanceof IProcessingComponent) {
                        paramDefinitions.add(((IProcessingComponent) pfComp).generateParamDefinition(document));
                        if(pfComp instanceof PluginElement) {
                            pluginDefinitions.add(((PluginElement) pfComp).generateDefinition(document));
                        } else if(pfComp instanceof FusionElement) {
                            fusionDefinitions.add(((FusionElement) pfComp).generateDefinition(document));
                        }
                        long count = Arrays.stream(getIncomingEdges(comp))
                                .filter(e -> ((mxCell) e).getSource() instanceof ProcessingFlowComponent)
                                .map(cell -> (ProcessingFlowComponent) ((mxCell) cell).getSource())
                                .map(ProcessingFlowComponent::getPFComponent)
                                .filter(cell -> cell instanceof IProcessingComponent).count();
                        if(count > 0) {
                            if(pfComp instanceof PluginElement) {
                                Element text = document.createElement("text");
                                String processor = Arrays.stream(getIncomingEdges(comp))
                                        .filter(e -> ((mxCell) e).getSource() instanceof ProcessingFlowComponent)
                                        .map(cell -> (ProcessingFlowComponent) ((mxCell) cell).getSource())
                                        .map(cell -> (IProcessingComponent) cell.getPFComponent())
                                        .map(IProcessingComponent::getName).collect(Collectors.joining(","));
                                text.setAttribute("preprocessor", processor);
                                Element plugin = document.createElement("mmfg");
                                plugin.setAttribute("processor", ((PluginElement) pfComp).getName());
                                sequence.add(text);
                                sequence.add(plugin);
                            } else if(pfComp instanceof FusionElement) {
                                Element mmfg = document.createElement("mmfg");
                                String processor = Arrays.stream(getIncomingEdges(comp))
                                        .filter(e -> ((mxCell) e).getSource() instanceof ProcessingFlowComponent)
                                        .map(cell -> (ProcessingFlowComponent) ((mxCell) cell).getSource())
                                        .map(cell -> (IProcessingComponent) cell.getPFComponent())
                                        .map(IProcessingComponent::getName).collect(Collectors.joining(","));
                                mmfg.setAttribute("processor", processor);
                                Element fusion = document.createElement("fusion");
                                fusion.setAttribute("processor", ((FusionElement) pfComp).getName());
                                sequence.add(mmfg);
                                sequence.add(fusion);
                            }
                        }
                    } else if(pfComp instanceof IAssetComponent) {
                        resourceDefinitions.add(((IAssetComponent) pfComp).generateDefinition(document));
                        if(((IAssetComponent) pfComp).isStart()) {
                            Element start = document.createElement("flow-source");
                            start.setAttribute("name", ((IAssetComponent) pfComp).getName());
                            sequence.add(start);
                        } else if(((IAssetComponent) pfComp).isEnd()) {
                            Element end = document.createElement("export");
                            end.setAttribute("target", ((IAssetComponent) pfComp).getName());
                            sequence.add(end);
                        }
                    }
                });

        /*
         * Duplikate entfernen
         */

        for(int i = sequence.size() - 1; i >= 1; i--) {
            if(sequence.get(i).isEqualNode(sequence.get(i - 1))) {
                sequence.remove(i);
            }
        }

        Element root = document.createElement("process-flow");
        document.appendChild(root);
        root.appendChild(document.createComment("Plugin-Definitionen"));
        for(Element e : pluginDefinitions) {
            root.appendChild(e);
        }
        root.appendChild(document.createComment("Fusion-Definitionen"));
        for(Element e : fusionDefinitions) {
            root.appendChild(e);
        }
        root.appendChild(document.createComment("Resource-Definitionen"));
        for(Element e : resourceDefinitions) {
            root.appendChild(e);
        }
        root.appendChild(document.createComment("Parameter"));
        for(List<Element> paramList : paramDefinitions) {
            for(Element element : paramList) {
                root.appendChild(element);
            }
        }
        root.appendChild(document.createComment("Ablauf"));
        for(Element e : sequence) {
            root.appendChild(e);
        }

        return document;
    }

    public Object getStartVertex() {
        mxAnalysisGraph analysisGraph = getAnalysisGraph();
        List<Object> vertices = Arrays.asList(analysisGraph.getChildCells(getDefaultParent(), true, false));

        Optional<Object> optional = vertices.stream().filter(cell -> {
            if(cell instanceof ProcessingFlowComponent) {
                ProcessingFlowComponent component = (ProcessingFlowComponent) cell;
                IPFComponent pfComponent = component.getPFComponent();
                if(pfComponent instanceof IAssetComponent) {
                    IAssetComponent assetComponent = (IAssetComponent) pfComponent;
                    return assetComponent.isStart();
                }
            }
            return false;
        }).findFirst();

        return optional.orElse(null);
    }
}
