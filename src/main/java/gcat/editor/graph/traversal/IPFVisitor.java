package gcat.editor.graph.traversal;

import com.mxgraph.view.mxGraph;

import java.util.Set;

public interface IPFVisitor extends mxGraph.mxICellVisitor {

    boolean visit(Object vertex, Object edge, Set<Object> notVisited);
}
