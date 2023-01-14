package gcat.editor.view.celleditor.renderer;

import gcat.editor.graph.processingflow.elements.components.asset.IAssetComponent;
import gcat.editor.view.celleditor.CellEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CellRenderer extends DefaultTableCellRenderer {

    private final CellEditor cellEditor;

    public CellRenderer(CellEditor cellEditor) {
        this.cellEditor = cellEditor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(cellEditor.getCurrentIPFComponent() instanceof IAssetComponent) {
            if((row == 3 || row == 4) && column == 1) {
                return new JCheckBox();
            }
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
