package gcat.editor.view.table.model;

import javax.swing.table.DefaultTableModel;
import java.util.TreeMap;

public class ParameterTableModel extends DefaultTableModel {

    private TreeMap<String, Object> parameters;

    private final String[] columnIdentifiers = new String[] {"Parameter", "Wert"};

    public ParameterTableModel() {
        parameters = new TreeMap<>();
        setColumnIdentifiers(columnIdentifiers);
    }

    @Override
    public int getRowCount() {
        return (parameters != null ? parameters.size() : 0);
    }

    @Override
    public int getColumnCount() {
        return columnIdentifiers.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnIdentifiers[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Object.class;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(parameters != null) {
            Object[] keys = parameters.keySet().toArray();
            Object[] values = parameters.values().toArray();
            switch(column) {
                case 0:
                    //System.out.println(keys[row]);
                    return keys[row];
                case 1:
                    //System.out.println(values[row]);
                    return values[row];
            }
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        System.out.printf("Editing (Row, Column): (%s, %s)\n", row, column);
        if(parameters != null) {
            Object[] keys = parameters.keySet().toArray();
            parameters.put((String) keys[row], aValue);
        }
    }

    public void setParameters(TreeMap<String, Object> parameters) {
        if(parameters != null) {
            if(!parameters.isEmpty()) {
                this.parameters = parameters;
            } else {
                setRowCount(0);
            }
            //this.parameters = parameters;
            fireTableDataChanged();
        } else {
            setRowCount(0);
            fireTableDataChanged();
        }
    }
}
