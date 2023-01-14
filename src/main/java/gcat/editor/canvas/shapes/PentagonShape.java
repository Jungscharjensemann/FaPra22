package gcat.editor.canvas.shapes;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxBasicShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import java.awt.*;

public class PentagonShape extends mxBasicShape {

    @Override
    public Shape createShape(mxGraphics2DCanvas canvas, mxCellState state) {
        Rectangle temp = state.getRectangle();
        int x = temp.x;
        int y = temp.y;
        int w = temp.width;
        int h = temp.height;
        String direction = mxUtils.getString(state.getStyle(),
                mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_EAST);
        Polygon hexagon = new Polygon();

        hexagon.addPoint(x, y);
        hexagon.addPoint(x + (int) (0.87 * w), y);
        hexagon.addPoint(x + w, y + (int) (0.5 * h));
        hexagon.addPoint(x + (int) (0.87 * w), y + h);
        hexagon.addPoint(x, y + h);

        return hexagon;
    }
}
