package gcat.editor.graph;

import com.google.common.collect.Sets;
import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxGraphStructure;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import gcat.editor.graph.cell_component.MultiMediaType;
import gcat.editor.graph.cell_component.PFCellComponent;
import gcat.editor.graph.cell_component.processing.PFExporterComponent;
import gcat.editor.graph.cell_component.processing.PFFusionComponent;
import gcat.editor.graph.cell_component.processing.PFPluginComponent;
import gcat.editor.graph.processingflow.elements.components.asset.IAssetComponent;
import gcat.editor.graph.processingflow.elements.components.processing.ProcessingFlowComponent;
import gcat.editor.graph.processingflow.elements.components.processing.fusion.FusionElement;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IPFComponent;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IProcessingComponent;
import gcat.editor.graph.processingflow.elements.components.processing.plugin.PluginElement;
import gcat.editor.graph.traversal.IPFVisitor;
import gcat.editor.graph.traversal.PFVisitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EditorGraph extends mxGraph {

    /**
     * Holds the edge to be used as a template for inserting new edges.
     */
    protected Object edgeTemplate;

    public static final NumberFormat numberFormat = NumberFormat.getInstance();

    /**
     * Custom graph that defines the alternate edge style to be used when
     * the middle control point of edges is double clicked (flipped).
     */
    public EditorGraph()
    {
        setAllowLoops(true);
        setMultigraph(false);
        setAllowDanglingEdges(false);
        setResetViewOnRootChange(false);
        setEdgeLabelsMovable(false);
        setKeepEdgesInBackground(true);
        setCellsEditable(false);

        getStylesheet().setDefaultEdgeStyle(createDefaultEdgeStyle());
    }

    protected Map<String, Object> createDefaultEdgeStyle()
    {
        Map<String, Object> style = new Hashtable<String, Object>();

        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        style.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        //style.put(mxConstants.STYLE_ENDFILL, "#000000");
        style.put(mxConstants.STYLE_ENDSIZE, 11);
        //style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM);
        style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style.put(mxConstants.STYLE_STROKECOLOR, "#6400B9");
        //style.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "#abd8f0");
        style.put(mxConstants.STYLE_FONTCOLOR, "#42edd6");

        return style;
    }

    /**
     * Sets the edge template to be used to inserting edges.
     */
    public void setEdgeTemplate(Object template)
    {
        edgeTemplate = template;
    }

    /**
     * Prints out some useful information about the cell in the tooltip.
     */
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

    /*/**
     * Overrides the method to use the currently selected edge template for
     * new edges.
     *
     * @param parent
     * @param id
     * @param value
     * @param source
     * @param target
     * @param style
     * @return

    public Object createEdge(Object parent, String id, Object value,
                             Object source, Object target, String style)
    {
        if (edgeTemplate != null)
        {
            mxCell edge = (mxCell) cloneCells(new Object[] { edgeTemplate })[0];
            edge.setId(id);

            return edge;
        }

        return super.createEdge(parent, id, value, source, target, style);
    }*/

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

        if(source instanceof PFCellComponent && target instanceof PFCellComponent) {

            // Casting...
            PFCellComponent sourceComponent = (PFCellComponent) source;
            PFCellComponent targetComponent = (PFCellComponent) target;

            // Ein- und Ausgabe
            Set<MultiMediaType> outputSet = sourceComponent.getOutputType();
            Set<MultiMediaType> inputSet = targetComponent.getInputType();

            if(outputSet != null && inputSet != null) {
                // Prüfen, ob sich Ein- und Ausgabe überschneiden...
                Sets.SetView<?> set = Sets.intersection(outputSet, inputSet);
                if(!set.isEmpty()) {
                    // Label der Kante setzen...
                    ((mxCell) edge).setValue(outputSet.stream().map(MultiMediaType::getType).map(String::toLowerCase).collect(Collectors.joining(",")));
                } else {
                    return "Ein- und Ausgabe sind inkompatibel!";
                }
            }
        }
        return null;
    }

    public void updateStart() {
        //mxAnalysisGraph analysisGraph = getAnalysisGraph();
        //Object[] cells = analysisGraph.getChildCells(getDefaultParent(), true, false);
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
            if(e instanceof PFPluginComponent) {
                ((PFPluginComponent) e).setName(String.format("plugin%s", countInstances(PFPluginComponent.class)));
            } else if(e instanceof PFFusionComponent) {
                ((PFFusionComponent) e).setName(String.format("merge%s", countInstances(PFFusionComponent.class)));
            } else if(e instanceof PFExporterComponent) {
                ((PFExporterComponent) e).setName(String.format("export%s", countInstances(PFExporterComponent.class)));
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

    @Override
    public void traverse(Object vertex, boolean directed, mxICellVisitor visitor) {
        //super.traverse(vertex, directed, visitor);
        //traverseGraph(vertex, directed, (IPFVisitor) visitor, null, null);
    }

    public void traverseGraph(Object vertex, boolean directed,
                              IPFVisitor visitor, Object edge, Set<Object> notVisited) {
        if(vertex != null && visitor != null) {
            if(notVisited == null) {
                mxAnalysisGraph aGraph = getAnalysisGraph();
                Stream<Object> cellStream = Arrays.stream(aGraph.getChildCells(getDefaultParent(), true, false));
                notVisited = cellStream.collect(Collectors.toSet());
            }
            if(notVisited.contains(vertex)) {
                System.out.printf("Knoten %s wurde noch nicht besucht!\n", vertex);
                boolean visited = visitor.visit(vertex, edge, notVisited);
                if(visited) {
                    notVisited.remove(vertex);
                    System.out.printf("Knoten %s wurde erfolgreich besucht und wird entfernt!\n", vertex);
                    int edgeCount = model.getEdgeCount(vertex);
                    if(edgeCount > 0) {
                        for (int i = 0; i < edgeCount; i++)
                        {
                            Object e = model.getEdgeAt(vertex, i);
                            boolean isSource = model.getTerminal(e,
                                    true) == vertex;

                            if (!directed || isSource)
                            {
                                Object next = model.getTerminal(e, !isSource);
                                traverseGraph(next, directed, visitor, e, notVisited);
                            }
                        }
                    }
                }
            }
        }
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
        PFVisitor visitor = new PFVisitor(this, document);
        Object startVertex = getStartVertex();

        //System.out.println(getNeighbours(startVertex));
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
                    if(pfComp instanceof PluginElement) {
                        pluginDefinitions.add(((PluginElement) pfComp).generateDefinition(document));
                        paramDefinitions.add(((PluginElement) pfComp).generateParamDefinition(document));
                    } else if(pfComp instanceof FusionElement) {
                        fusionDefinitions.add(((FusionElement) pfComp).generateDefinition(document));
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

        /*if(startVertex != null) {
            //System.out.println("Traversing");
            traverse(startVertex, true, visitor);
        }*/

        Element root = document.createElement("process-flow");
        document.appendChild(root);
        root.appendChild(document.createComment("Plugin-Definitionen"));
        for(Element e : pluginDefinitions) {
            //System.out.println(e);
            root.appendChild(e);
        }
        root.appendChild(document.createComment("Fusion-Definitionen"));
        for(Element e : fusionDefinitions) {
            //System.out.println(e);
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

    @Override
    public Object updateCellSize(Object cell) {
        return super.updateCellSize(cell);
    }

    /*if(source instanceof CustomCell && target instanceof CustomCell) {
            return "Nö";
        }

        mxAnalysisGraph analysisGraph = new mxAnalysisGraph();
        analysisGraph.setGraph(view.getGraph());

        mxGraph graph = analysisGraph.getGraph();
        mxIGraphModel model = graph.getModel();
        Object[] cells = model.cloneCells(analysisGraph.getChildCells(graph.getDefaultParent(), true, true), true);

        System.out.println("Graph: " + Arrays.toString(cells));

        mxGraphModel modelCopy = new mxGraphModel();
        mxGraph graphCopy = new mxGraph(modelCopy);
        Object parentCopy = graphCopy.getDefaultParent();
        graphCopy.addCells(cells);

        //graphCopy.insertEdge(parentCopy, null, null, source, target);

        mxAnalysisGraph analysisGraphCopy = new mxAnalysisGraph();
        analysisGraphCopy.setGraph(graphCopy);
        //analysisGraphCopy.setGenerator(analysisGraph.getGenerator());
        //analysisGraphCopy.setProperties(analysisGraph.getProperties());

        graph.insertEdge(graph.getDefaultParent(), null, null, source, target);

        System.out.println("Graph: " + Arrays.toString(analysisGraphCopy.getChildCells(graphCopy.getDefaultParent(), true, true)));

        System.out.println(mxGraphStructure.isCyclicDirected(analysisGraphCopy));


        mxAnalysisGraph extractChildrenAnalysisGraph = new mxAnalysisGraph();
        extractChildrenAnalysisGraph.setGraph(this);

        mxGraph currentGraph = extractChildrenAnalysisGraph.getGraph();
        mxIGraphModel currentModel = currentGraph.getModel();
        Object[] extractedCells = currentModel.cloneCells(extractChildrenAnalysisGraph.getChildCells(currentGraph.getDefaultParent(), true, true), true);


        mxGraphModel futureModel = new mxGraphModel();
        mxGraph futureGraph = new mxGraph(futureModel);
        futureGraph.addCells(extractedCells);

        mxCell newEdge = new mxCell();
        newEdge.setSource((mxICell) source);
        newEdge.setTarget((mxICell) target);

        futureGraph.addCells(new Object[] {newEdge});

        mxAnalysisGraph futureAnalysisGraph = new mxAnalysisGraph();
        futureAnalysisGraph.setGraph(futureGraph);

        System.out.println(mxGraphStructure.isCyclicDirected(futureAnalysisGraph));



        if(mxGraphStructure.isCyclicDirected(futureAnalysisGraph)) {
            return "Previously added edge induced a cycle!";
        }

        mxAnalysisGraph analysisGraph = new mxAnalysisGraph();
        analysisGraph.setGraph(this);

        if(mxGraphStructure.isCyclicDirected(analysisGraph)) {
            return "Previously added edge induced a cycle!";
        }*/
}
