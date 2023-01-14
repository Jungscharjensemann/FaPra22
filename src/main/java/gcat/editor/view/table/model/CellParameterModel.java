package gcat.editor.view.table.model;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

public class CellParameterModel extends DefaultTableModel implements TableModel {

    private TreeMap<String, Object> parameters;

    private final ArrayList<TableModelListener> tableModelListenerList = new ArrayList<>();

    public CellParameterModel() {
        super();
        parameters = new TreeMap<>();
        setColumnIdentifiers(new String[] {"Parameter", "Wert"});
    }

    public void setParameters(TreeMap<String, Object> parameters) {
        this.parameters = parameters;
        System.out.println(parameters);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        int rowCount = 0;
        if(parameters != null) {
            rowCount = parameters.size();;
        }
        System.out.println(rowCount);
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return "Parameter";
            case 1:
                return "Wert";
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return parameters.keySet().toArray()[rowIndex];
            case 1:
                return parameters.values().toArray()[rowIndex];
        }
        return null;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        if(!tableModelListenerList.contains(l)) {
            tableModelListenerList.add(l);
        }
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        tableModelListenerList.remove(l);
    }
}
