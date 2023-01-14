package gcat.editor.view.celleditor.renderer;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HeaderRenderer implements TableCellRenderer {

    private final DefaultTableCellRenderer renderer;

    public HeaderRenderer(JTableHeader tableHeader) {
        renderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        tableHeader.setOpaque(false);
        tableHeader.setBackground(Color.LIGHT_GRAY);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JComponent jComponent = (JComponent) renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(row == -1 && column == 0) {
            jComponent = (JComponent) renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, -1, 0);
            jComponent.setBorder(new MatteBorder(2, 2, 2, 2, Color.WHITE));
        } else if(row == -1 && column == 1) {
            jComponent = (JComponent) renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, -1,1);
            jComponent.setBorder(new MatteBorder(2, 0, 2, 2, Color.WHITE));
        }
        return jComponent;
    }
}
