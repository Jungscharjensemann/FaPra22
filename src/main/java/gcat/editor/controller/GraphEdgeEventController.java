package gcat.editor.controller;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxGraphStructure;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import gcat.editor.graph.EditorGraph;
import gcat.editor.view.EditorMainFrame;

import java.awt.*;

public class GraphEdgeEventController implements mxEventSource.mxIEventListener {

    private mxIGraphModel graphModel;

    public GraphEdgeEventController(EditorMainFrame reference) {
        EditorGraph editorGraph = reference.getEditorGraph();
        mxAnalysisGraph analysisGraph = new mxAnalysisGraph();
        analysisGraph.setGraph(editorGraph);
    }

    @Override
    public void invoke(Object sender, mxEventObject evt) {
        /*graphModel = editorGraph.getModel();
        Object[] cells = graphModel.cloneCells(analysisGraph.getChildCells(editorGraph.getDefaultParent(), true, true), true);
        //System.out.println(Arrays.toString(cells));
        //System.out.println(mxGraphStructure.isCyclicDirected(analysisGraph));
        for(Object cell : cells) {
            if(cell instanceof mxCell) {
                mxCell edge = (mxCell) cell;
                if(edge.getSource() != null && edge.getTarget() != null) {
                    mxCell source = (mxCell) edge.getSource();
                    mxCell target = (mxCell) edge.getTarget();
                    System.out.printf("Validating [edge(id:%s)] with [source(id:%s,val:%s)] and [target(id:%s,val:%s)]\n",
                            edge.getId(), source.getId(), source.getValue(), target.getId(), target.getValue());
                    editorGraph.validateEdge(edge, source, target);
                }
            }
        }
        if(mxGraphStructure.isCyclicDirected(analysisGraph)) {
            //editorMainFrame.getErrorLabel().setOpaque(true);
            //editorMainFrame.getErrorLabel().setBackground(Color.RED);
            //editorMainFrame.getErrorLabel().setText("Graph contains cycle!");
        }*/
        //editorMainFrame.getEditorGraphComponent().validateGraph();
        //System.out.println("Test");
    }
}
