package gcat.editor.view.table;

import gcat.editor.view.table.model.CellParameterModel;
import gcat.editor.view.table.model.ParameterTableModel;
import gcat.editor.view.table.renderer.CellParameterTableModel;
import gcat.editor.view.table.renderer.HeaderRenderer;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.TreeMap;

public class CellParameterEditor extends JPanel {

    private CellParameterTable cellParameterTable;

    private CellParameterTableModel cellParameterTableModel;

    private CellParameterModel cellParameterModel;

    private ParameterTableModel parameterTableModel;

    public CellParameterEditor() {
        setLayout(new BorderLayout());

        cellParameterTable = new CellParameterTable();

        //cellParameterModel = new CellParameterModel();
        //cellParameterTable.setModel(cellParameterModel);

        //cellParameterTableModel = new CellParameterTableModel();
        //cellParameterTable.setModel(cellParameterTableModel);
        parameterTableModel = new ParameterTableModel();
        cellParameterTable.setModel(parameterTableModel);

        JTableHeader tableHeader = cellParameterTable.getTableHeader();
        tableHeader.setDefaultRenderer(new HeaderRenderer(cellParameterTable));



        //table.setDefaultRenderer(Object.class, new OddRenderer());

        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) cellParameterTable.getDefaultRenderer(String.class);
        //centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        //cellParameterTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);


        JLabel lblNewLabel = new JLabel("Cell Editor");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblNewLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(cellParameterTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setCellParameterTableModel(DefaultTableModel tableModel) {
        if(tableModel != null) {
            cellParameterTable.setModel(tableModel);
        }
    }

    public void addData(TreeMap<String, Object> parameters) {
        cellParameterTableModel.setRowCount(0);

        if(parameters != null) {
            parameters.keySet().forEach(e -> {
                try {
                    Object o = parameters.get(e);
                    if(o != null) {
                        cellParameterTableModel.addRow(new Object[]{e, o});
                    }
                } catch(NullPointerException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    public CellParameterModel getCellParameterModel() {
        return cellParameterModel;
    }

    public ParameterTableModel getParameterTableModel() {
        return parameterTableModel;
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getMinimumSize();
    }

    private static class OddRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JComponent jComponent = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            switch(column) {
                case 0:
                    jComponent = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, 0);
                    jComponent.setBorder(new MatteBorder(2, 2, 2, 2, Color.WHITE));
                    break;
                case 1:
                    jComponent = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,1);
                    jComponent.setBorder(new MatteBorder(2, 0, 2, 2, Color.WHITE));
                    break;
            }
            return jComponent;
        }
    }
}
