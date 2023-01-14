package gcat.editor.controller;

import gcat.editor.graph.processingflow.elements.components.asset.IAssetComponent;
import gcat.editor.graph.processingflow.elements.components.processing.ProcessingFlowComponent;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IPFComponent;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IProcessingComponent;
import gcat.editor.graph.processingflow.elements.components.processing.plugin.PluginElement;
import gcat.editor.view.table.model.cell.CellComponent;
import gcat.editor.graph.cell_component.PFProcessingComponent;
import gcat.editor.graph.cell_component.processing.PFPluginComponent;
import gcat.editor.graph.traversal.PFVisitor;
import gcat.editor.view.EditorMainFrame;
import org.w3c.dom.Element;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TestController extends MouseAdapter {

    private final EditorMainFrame editorMainFrame;

    public TestController(EditorMainFrame reference) {
        editorMainFrame = reference;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        Object cell = editorMainFrame.getEditorGraphComponent().getCellAt(e.getX(), e.getY());
        if(cell != null) {
            if(cell instanceof ProcessingFlowComponent) {
                ProcessingFlowComponent component = (ProcessingFlowComponent) cell;
                IPFComponent pfComponent = component.getPFComponent();
                editorMainFrame.getCellEditor().setCurrentIPFComponent(pfComponent);
                if(pfComponent instanceof IProcessingComponent) {
                    try {
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                        //transformer.transform(
                                //new DOMSource(((IProcessingComponent) pfComponent).generateDefinition()),
                                //new StreamResult(System.out));
                    } catch (TransformerException ex) {
                        throw new RuntimeException(ex);
                    }

                    /*if(pfComponent instanceof PluginElement) {
                        List<Element> elements = ((PluginElement) pfComponent).generateParamDefinition();
                        for(Element element : elements) {
                            try {
                                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                                transformer.transform(
                                        new DOMSource(element),
                                        new StreamResult(System.out));
                            } catch (TransformerException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }*/
                    //System.out.println(((IProcessingComponent) pfComponent).getParameters());
                    //((IProcessingComponent) processingComponent).addParameter("test", "hallo");
                    //editorMainFrame.getCellEditor().setCurrentIPFComponent(pfComponent);
                    //editorMainFrame.getCellEditor().setParameters(((IProcessingComponent) processingComponent).getParameters());
                }

                /*else if(pfComponent instanceof IAssetComponent) {
                    try {
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                        transformer.transform(
                                new DOMSource(((IAssetComponent) pfComponent).generateDefinition()),
                                new StreamResult(System.out));
                    } catch (TransformerException ex) {
                        throw new RuntimeException(ex);
                    }
                }*/
            }
            if(cell instanceof CellComponent) {
                CellComponent cellComponent = (CellComponent) cell;
                //editorMainFrame.getCellParameterEditor().setCellParameterTableModel(cellComponent.getCellTableModel());
            }
            if(cell instanceof PFProcessingComponent) {
                //System.out.println(((PFProcessingComponent) cell).generateDefinition().getAttributes().getLength());

                /*mxAnalysisGraph analysisGraph = new mxAnalysisGraph();
                analysisGraph.setGraph(editorMainFrame.getEditorGraph());
                mxTraversal.dfs(analysisGraph, cell, (vertex, edge) -> {
                    System.out.println(vertex);
                    return false;
                });*/

                /*mxAnalysisGraph analysisGraph = new mxAnalysisGraph();
                analysisGraph.setGraph(editorMainFrame.getEditorGraph());
                mxTraversal.dfs(analysisGraph, cell, new PFVisitor());*/

                //editorMainFrame.getEditorGraph().traverse(cell, true, new PFVisitor());

                if(cell instanceof PFPluginComponent) {
                    //System.out.println(((PFPluginComponent) cell).getParameters());
                    //editorMainFrame.getCellParameterEditor().addData(((PFPluginComponent) cell).getParameters());
                    //editorMainFrame.getCellParameterEditor().getCellParameterModel().setParameters(((PFPluginComponent) cell).getParameters());

                    editorMainFrame.getCellParameterEditor().getParameterTableModel().setParameters(((PFPluginComponent) cell).getParameters());

                    /*Element element = ((PFPluginComponent) cell).generateDefinition();
                    System.out.println(element.getTextContent());

                    List<Element> list = ((PFPluginComponent) cell).generateInformation();

                    list.forEach(el -> {
                        Transformer transformer;
                        try {
                            transformer = TransformerFactory.newInstance().newTransformer();
                            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                            StreamResult result = new StreamResult(new StringWriter());
                            DOMSource source = new DOMSource(el);
                            transformer.transform(source, result);
                            String xmlString = result.getWriter().toString();
                            System.out.println(xmlString);
                        } catch (TransformerException ex) {
                            throw new RuntimeException(ex);
                        }
                    });*/

                }
            }
        }
        /*if(cell != null) {
            editorMainFrame.getCellParameterEditor().getCellParameterTableModel().addRow(new Object[] {"value", cell.getValue()});
            editorMainFrame.getCellParameterEditor().setCellParameterTableModel();
        }*/
    }
}
