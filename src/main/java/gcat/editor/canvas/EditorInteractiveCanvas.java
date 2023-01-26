package gcat.editor.canvas;

import com.mxgraph.shape.mxITextShape;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import gcat.editor.canvas.shapes.PentagonShape;

import java.awt.*;
import java.util.Map;

/**
 * Klasse für eigene Leinwand
 * für den Graphen, um die Labels, bzw.
 * dessen Hintergrund besser zu zeichnen,
 * und um einen Fehler zu umgehen.
 */
public class EditorInteractiveCanvas extends mxInteractiveCanvas {

    public EditorInteractiveCanvas() {
        putShape("quinquennium", new PentagonShape());
    }

    @Override
    public Object drawLabel(String text, mxCellState state, boolean html) {
        Map<String, Object> style = state.getStyle();
        mxITextShape shape = getTextShape(style, html);

        if (g != null && shape != null && drawLabels && text != null
                && text.length() > 0)
        {
            // Creates a temporary graphics instance for drawing this shape
            float opacity = mxUtils.getFloat(style,
                    mxConstants.STYLE_TEXT_OPACITY, 100);
            Graphics2D previousGraphics = g;
            g = createTemporaryGraphics(style, opacity, null);

            // Draws the label background and border
            Color bg = mxUtils.getColor(style,
                    mxConstants.STYLE_LABEL_BACKGROUNDCOLOR);
            Color border = mxUtils.getColor(style,
                    mxConstants.STYLE_LABEL_BORDERCOLOR);
            Rectangle labelBounds = state.getLabelBounds().getRectangle();

            // Feste Höhe.
            labelBounds.height = 15;
            paintRectangle(labelBounds, bg, border);

            // Paints the label and restores the graphics object
            shape.paintShape(this, text, state, style);
            g.dispose();
            g = previousGraphics;
        }

        return shape;
    }
}
