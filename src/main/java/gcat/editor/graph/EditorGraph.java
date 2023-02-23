package gcat.editor.graph;

import com.google.common.collect.Sets;
import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import gcat.editor.graph.processingflow.components.asset.AssetElement;
import gcat.editor.graph.processingflow.components.asset.IAssetComponent;
import gcat.editor.graph.processingflow.components.media.MultiMediaType;
import gcat.editor.graph.processingflow.components.processing.ProcessingFlowComponent;
import gcat.editor.graph.processingflow.components.processing.fusion.FusionElement;
import gcat.editor.graph.processingflow.components.processing.interfaces.IPFComponent;
import gcat.editor.graph.processingflow.components.processing.interfaces.IProcessingComponent;
import gcat.editor.graph.processingflow.components.processing.plugin.PluginElement;
import gcat.editor.graph.structure.GraphStructure;
import j2html.tags.specialized.HtmlTag;
import j2html.tags.specialized.TableTag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class EditorGraph extends mxGraph {

    private boolean tooltipsEnabled = true;

    public EditorGraph() {
        // Eigenschaften des Graphen setzen.
        setAllowLoops(true);
        setMultigraph(false);
        setAllowDanglingEdges(false);
        setResetViewOnRootChange(false);
        setEdgeLabelsMovable(false);
        setKeepEdgesInBackground(true);
        setCellsEditable(false);
        setCellsResizable(false);
        setSplitEnabled(false);
        setSwimlaneNesting(false);
    }

    public void setTooltipsEnabled(boolean enable) {
        this.tooltipsEnabled = enable;
    }

    public boolean isTooltipsEnabled() {
        return tooltipsEnabled;
    }

    /**
     * Tooltip für eine Zelle.
     */
    @Override
    public String getToolTipForCell(Object cell) {
        HtmlTag html = new HtmlTag();
        if(cell instanceof ProcessingFlowComponent) {
            ProcessingFlowComponent pfComponent = (ProcessingFlowComponent) cell;
            IPFComponent component = pfComponent.getPFComponent();

            TableTag propertyTable = new TableTag();
            TableTag parameterTable = new TableTag();
            if(component instanceof IProcessingComponent) {
                propertyTable = table(
                        thead(
                                tr(
                                        th("Parameter"),
                                        th("Wert")
                                )
                        ),
                        tbody(
                                tr(
                                        td("name"),
                                        td(((IProcessingComponent) component).getName())
                                ),
                                tr(
                                        td("classPath"),
                                        td(((IProcessingComponent) component).getClassPath())
                                )
                        )
                ).attr("border", "1");
                Set<Map.Entry<String, Object>> entry = ((IProcessingComponent) component).getParameters().entrySet();
                if(!entry.isEmpty()) {
                    parameterTable = table(
                            thead(
                                    tr(
                                            th("Parameter"),
                                            th("Wert")
                                    )
                            ),
                            tbody(
                                    each(entry, next ->
                                            tr(
                                                    td(next.getKey()),
                                                    td(next.getValue().toString())
                                            ))
                            )
                    ).attr("border", "1");
                }
            } else if(component instanceof IAssetComponent) {
                propertyTable = table(
                        thead(
                                tr(
                                        th("Eigenschaften"),
                                        th("Wert")
                                )
                        ),
                        tbody(
                                tr(
                                        td("name"),
                                        td(((IAssetComponent) component).getName())
                                ),
                                tr(
                                        td("type"),
                                        td(((IAssetComponent) component).getType())
                                ),
                                tr(
                                        td("location"),
                                        td(((IAssetComponent) component).getLocation())
                                ),
                                tr(
                                        td("Startknoten"),
                                        td(((IAssetComponent) component).isStart() ? "wahr" : "falsch")
                                ),
                                tr(
                                        td("Endknoten"),
                                        td(((IAssetComponent) component).isEnd() ? "wahr" : "falsch")
                                )
                        )
                ).attr("border", "1");
            }
            html = html(
                    body(
                            p(b(u(component.getLabel()))).attr("align", "center"),
                            br(),
                            propertyTable,
                            br(),
                            parameterTable
                    )
            );
        } else if(cell instanceof mxCell){
            html = html(
                    head(
                            meta().withCharset("UTF-8")
                    ),
                    body(
                            table(
                                    thead(
                                            tr(
                                                    th("Quelle"),
                                                    th(""),
                                                    th("Ziel")
                                            )
                                    ),
                                    tbody(
                                            tr(
                                                    td(((mxCell) cell).getSource().toString()),
                                                    td(p("\uD83E\uDC9C".repeat(5) + "\u27A4")),
                                                    td(((mxCell) cell).getTarget().toString())
                                            )
                                    )
                            )
                    )
            );
        }
        return isTooltipsEnabled() ? html.render() : null;
    }

    /**
     * Methode zum Validieren einer Verbindung zwischen Start und Zielknoten, sobald eine
     * neue Verbindung hinzugefügt wurde.
     * @param edge Die zu validierende Kante in Form einer Zelle.
     * @param source Startknoten.
     * @param target Zielknoten.
     * @return Fehlermeldung.
     */
    @Override
    public String validateEdge(Object edge, Object source, Object target) {
        System.out.printf("Validating edge with source %s and target %s%n", source, target);

        // Analysis Graph zum Analysieren des Graphen.
        mxAnalysisGraph analysisGraph = new mxAnalysisGraph();
        analysisGraph.setGraph(this);

        List<Object> remainingEdgesAfterCycleCheck = GraphStructure.cycleCheck(analysisGraph);
        boolean edgeInCycle = remainingEdgesAfterCycleCheck.stream()
                .anyMatch(o -> {
                    if(o instanceof mxCell) {
                        if(((mxCell) o).isEdge() && edge instanceof mxCell) {
                            return ((mxCell) o).getId().equals(((mxCell) edge).getId());
                        }
                    }
                    return false;
                });

        if(edgeInCycle) {
            return "Kante ist Teil eines Kreises!";
        }

        // Prüfen, ob Graph Kreis enthält.
        // (Veraltet, da hier nach Entdecken eines Kreises alle Kanten
        // als im Kreis enthalten markiert werden.)

        // Start- und Zielknoten sind vom Typ ProcessingFlowComponent.
        if(source instanceof ProcessingFlowComponent && target instanceof ProcessingFlowComponent) {

            ProcessingFlowComponent sourceComponent = (ProcessingFlowComponent) source;
            ProcessingFlowComponent targetComponent = (ProcessingFlowComponent) target;

            // Die eigentlichen Komponenten.
            IPFComponent pfSource = sourceComponent.getPFComponent();
            IPFComponent pfTarget = targetComponent.getPFComponent();

            // Ein- und Ausgabe.
            Set<MultiMediaType> outputSet = pfSource.getOutput();
            Set<MultiMediaType> inputSet = pfTarget.getInput();

            /*
             * Spezielle Prüfungen.
             */

            if(pfSource instanceof AssetElement && pfTarget instanceof AssetElement) {
                if(pfSource == pfTarget) {
                    return "Assets erlauben keine Schlaufen auf sich selbst!";
                }
                return "Zwei Assets können nicht miteinander verbunden werden!";
            }
            if(pfSource instanceof AssetElement && pfTarget instanceof IProcessingComponent) {
                if(((AssetElement) pfSource).isEnd()) {
                    return "Als Endknoten markiertes Asset kann nicht mit " +
                            "anderen Komponenten verbunden werden!";
                }
            }

            /*
             * Label setzen.
             */
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

    /**
     * Diese Methode setzt das Attribut isStart
     * für alle anderen Asset-Komponenten im Processing
     * Flow auf falsch und das Attribut isEnd für alle anderen
     * Asset-Komponenten auf wahr, außer die angegebene Komponente,
     * dessen Attribut isStart wahr ist.
     * @param assetComponent Die Asset-Komponente, die
     *                       der neue Start ist.
     */
    @SuppressWarnings("unused")
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

    /**
     * Diese Methode setzt das Attribut isEnd
     * für alle anderen Asset-Komponenten im Processing
     * Flow auf falsch und das Attribut isStart für alle anderen
     * Asset-Komponenten auf wahr, außer die angegebene Komponente,
     * dessen Attribut isEnd wahr ist.
     * @param assetComponent Die Asset-Komponente, die
     *                       das neue Ziel ist.
     */
    @SuppressWarnings("unused")
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

        // Namen für neue hinzugefügte ProcessingFlowKomponente
        // setzen.
        Arrays.asList(cells).forEach(e -> {
            if(e instanceof ProcessingFlowComponent) {
                ProcessingFlowComponent comp = (ProcessingFlowComponent) e;
                IPFComponent component = comp.getPFComponent();
                if(component instanceof PluginElement) {
                    ((PluginElement) component).setName(String.format("plugin%s", countInstances(PluginElement.class)));
                }
                if(component instanceof FusionElement) {
                    ((FusionElement) component).setName(String.format("merge%s", countInstances(FusionElement.class)));
                }
                if(component instanceof AssetElement) {
                    AssetElement asset = (AssetElement) component;
                    String type = asset.getType();
                    asset.setName(String.format("asset%s_%s", countInstances(AssetElement.class), type));
                }
            }
        });
    }

    /**
     * Diese Methode zählt die Anzahl von Komponenten
     * eines bestimmten Typs.
     * @param clazz Der angegebene zu zählende Typ.
     * @return Anzahl des Typs.
     */
    private long countInstances(Class<?> clazz) {
        mxAnalysisGraph analysisGraph = getAnalysisGraph();
        List<Object> extractedCells = Arrays.asList(analysisGraph.getChildCells(getDefaultParent(), true, false));

        return extractedCells.stream()
                .filter(cell -> cell instanceof ProcessingFlowComponent)
                .map(cell -> (ProcessingFlowComponent) cell)
                .filter(pfComp -> pfComp.getPFComponent().getClass().isAssignableFrom(clazz)).count();
    }

    public mxAnalysisGraph getAnalysisGraph() {
        mxAnalysisGraph analysisGraph = new mxAnalysisGraph();
        analysisGraph.setGraph(this);
        return analysisGraph;
    }

    public List<ProcessingFlowComponent> getCells() {
        return Arrays.stream(getAnalysisGraph().getChildCells(getDefaultParent(), true, false))
                .filter(cell -> cell instanceof ProcessingFlowComponent)
                .map(cell -> (ProcessingFlowComponent) cell)
                .collect(Collectors.toList());
    }

    /**
     * Generiert aus dem Graph eine topologische
     * Sortierung, die später beim Generieren des
     * Ablaufs Verwendung findet.
     * @param vertex Startknoten.
     * @param visited Bereits besuchte Knoten.
     * @param stack Aktuelle Abfolge.
     */
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

    /**
     * Diese Methode gibt die direkten ausgehenden
     * Nachbarn einer Zelle zurück.
     * @param cell Zelle.
     * @return Direkten Nachbarn.
     */
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

        // Topologische Sortierung.
        Stack<Object> stack = new Stack<>();
        topologialSort(startVertex, null, stack);
        Collections.reverse(stack);

        System.out.println("Topologische Sortierung: " + stack);

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
                    if(pfComp instanceof IProcessingComponent) {

                        /*
                         * Definitionen sammeln.
                         */

                        paramDefinitions.add(((IProcessingComponent) pfComp).generateParamDefinition(document));
                        if(pfComp instanceof PluginElement) {
                            pluginDefinitions.add(((PluginElement) pfComp).generateDefinition(document));
                        } else if(pfComp instanceof FusionElement) {
                            fusionDefinitions.add(((FusionElement) pfComp).generateDefinition(document));
                        }

                        // Funktion zum Zählen von Processing-Komponenten.
                        Function<List<Object>, Long> countFunction = l -> l.stream()
                                .filter(c -> ((mxCell) c).getSource() instanceof ProcessingFlowComponent)
                                .map(c -> (ProcessingFlowComponent) ((mxCell) c).getSource())
                                .map(ProcessingFlowComponent::getPFComponent)
                                .filter(c -> c instanceof IProcessingComponent)
                                .count();

                        // Funktion zum Konkatenieren der Namen
                        // mehrerer Processing-Komponenten.
                        Function<List<Object>, String> joinFunction = l -> l.stream()
                                .filter(c -> ((mxCell) c).getSource() instanceof ProcessingFlowComponent)
                                .map(c -> (ProcessingFlowComponent) ((mxCell) c).getSource())
                                .map(c -> (IProcessingComponent) c.getPFComponent())
                                .map(IProcessingComponent::getName)
                                .collect(Collectors.joining(","));

                        List<Object> inEdges = Arrays.asList(getIncomingEdges(comp));

                        // Anzahl an Processing-Komponenten, die eine eingehende
                        // Verbindung zum aktuellen Knoten haben.
                        long count = countFunction.apply(inEdges);
                        if(count > 0) {
                            if(pfComp instanceof PluginElement) {
                                // MMFG Element.
                                Element mmfg = document.createElement("mmfg");
                                String processor = joinFunction.apply(inEdges);
                                mmfg.setAttribute("processor", processor);
                                // Plugin Element.
                                Element plugin = document.createElement("mmfg");
                                plugin.setAttribute("processor", ((PluginElement) pfComp).getName());
                                // Zur Sequenz (Ablauf) hinzufügen.
                                sequence.add(mmfg);
                                sequence.add(plugin);
                            } else if(pfComp instanceof FusionElement) {
                                // MMFG Element
                                Element mmfg = document.createElement("mmfg");
                                String processor = joinFunction.apply(inEdges);
                                mmfg.setAttribute("processor", processor);
                                // Fusion Element
                                Element fusion = document.createElement("fusion");
                                fusion.setAttribute("processor", ((FusionElement) pfComp).getName());
                                // Zur Sequenz (Ablauf) hinzufügen.
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

        /*
         * Dokument zusammenfügen.
         */

        Element root = document.createElement("process-flow");
        document.appendChild(root);
        // Standard-Werte setzen.
        root.setAttribute("extension", ".?");
        root.setAttribute("isGeneral", String.valueOf(false));
        root.setAttribute("name", "Standard-Name");

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

    /**
     * Diese Methode gibt den ersten gefunden
     * Knoten zurück, der ein Startknoten ist.
     * @return Startknoten.
     */
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

    public boolean hasAnEndVertex() {
        return getCells().stream()
                .map(ProcessingFlowComponent::getPFComponent)
                .filter(c -> c instanceof IAssetComponent)
                .map(c -> (IAssetComponent) c).anyMatch(IAssetComponent::isEnd);
    }

    public long numberOfStartVertices() {
        return getCells().stream().map(ProcessingFlowComponent::getPFComponent)
                .filter(c -> c instanceof IAssetComponent)
                .filter(c -> ((IAssetComponent) c).isStart()).count();
    }

    @SuppressWarnings("unused")
    public long numberOfEndVertices() {
        return getCells().stream().map(ProcessingFlowComponent::getPFComponent)
                .filter(c -> ((IAssetComponent) c).isEnd()).count();
    }
}
