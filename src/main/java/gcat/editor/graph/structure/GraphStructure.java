package gcat.editor.graph.structure;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.view.mxGraph;

public class GraphStructure {

    public static boolean isFutureGraphCyclicDirected(mxAnalysisGraph aGraph, Object edge, Object source, Object target) {
        mxGraph graph = aGraph.getGraph();
        mxIGraphModel model = graph.getModel();
        Object[] cells = model.cloneCells(aGraph.getChildCells(graph.getDefaultParent(), true, true), true);

        mxGraphModel modelCopy = new mxGraphModel();
        mxGraph graphCopy = new mxGraph(modelCopy);
        Object parentCopy = graphCopy.getDefaultParent();
        graphCopy.addCells(cells);
        mxCell newEdge = (mxCell) edge;
        System.out.printf("Source: %s, Target: %s\n", source, target);
        graphCopy.addCells(new Object[] {newEdge});

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

        int vertexNum = aGraphCopy.getChildVertices(parentCopy).length;

        if (vertexNum > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static Object getDirectedLeaf(mxAnalysisGraph aGraph, Object parent)
    {
        Object[] vertices = aGraph.getChildVertices(parent);
        int vertexNum = vertices.length;
        Object currVertex;

        for (Object vertex : vertices) {
            currVertex = vertex;
            int inEdgeCount = aGraph.getEdges(currVertex, parent, true, false, false, true).length;
            int outEdgeCount = aGraph.getEdges(currVertex, parent, false, true, false, true).length;

            if (outEdgeCount == 0 || inEdgeCount == 0) {
                return currVertex;
            }
        }

        return null;
    };

}
