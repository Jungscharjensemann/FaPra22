package gcat.editor.view.celleditor.table;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CellTable extends JTable {

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        JComponent jComponent = (JComponent) super.prepareRenderer(renderer, row, column);
        jComponent.setBorder(new EmptyBorder(0, 5, 0, 0));
        if(!isRowSelected(row)) {
            if(row % 2 == 0) {
                jComponent.setBackground(Color.decode("#d9e1f2"));
            } else {
                jComponent.setBackground(Color.WHITE);
            }
        }
        return jComponent;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return super.getColumnClass(column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(getPreferredSize().width, getRowHeight() * (Math.min(getRowCount(), 5)));
    }

}
