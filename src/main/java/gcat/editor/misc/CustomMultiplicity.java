package gcat.editor.misc;

import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxMultiplicity;

import java.util.Collection;

public class CustomMultiplicity extends mxMultiplicity {
    /**
     * @param source
     * @param type
     * @param attr
     * @param value
     * @param min
     * @param max
     * @param validNeighbors
     * @param countError
     * @param typeError
     * @param validNeighborsAllowed
     */
    public CustomMultiplicity(boolean source, String type, String attr, String value, int min, String max, Collection<String> validNeighbors, String countError, String typeError, boolean validNeighborsAllowed) {
        super(source, type, attr, value, min, max, validNeighbors, countError, typeError, validNeighborsAllowed);
    }

    @Override
    public String check(mxGraph graph, Object edge, Object source, Object target, int sourceOut, int targetIn) {
        return super.check(graph, edge, source, target, sourceOut, targetIn);
    }
}
