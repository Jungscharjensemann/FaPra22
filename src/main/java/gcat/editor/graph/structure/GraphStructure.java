package gcat.editor.graph.structure;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxGraphStructure;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.view.mxGraph;

import java.util.Arrays;
import java.util.List;

public class GraphStructure extends mxGraphStructure {

    public static List<Object> cycleCheck(mxAnalysisGraph aGraph)
    {
        mxGraph graph = aGraph.getGraph();
        mxIGraphModel model = graph.getModel();
        Object[] cells = model.cloneCells(aGraph.getChildCells(graph.getDefaultParent(), true, true), true);
        mxGraphModel modelCopy = new mxGraphModel();
        mxGraph graphCopy = new mxGraph(modelCopy);
        Object parentCopy = graphCopy.getDefaultParent();
        graphCopy.addCells(cells);
        mxAnalysisGraph aGraphCopy = new mxAnalysisGraph();
        aGraphCopy.setGraph(graphCopy);
        aGraphCopy.setGenerator(aGraph.getGenerator());
        aGraphCopy.setProperties(aGraph.getProperties());

        Object[] leaf = new Object[1];

        do
        {
            leaf[0] = getDirectedLeaf(aGraphCopy, parentCopy);

            if (leaf[0] != null)
            {
                graphCopy.removeCells(leaf);
            }
        }
        while (leaf[0] != null);

        return Arrays.asList(aGraphCopy.getChildCells(parentCopy, false, true));
    }
}
