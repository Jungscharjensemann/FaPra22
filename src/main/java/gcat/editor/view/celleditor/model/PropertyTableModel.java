package gcat.editor.view.celleditor.model;

import gcat.editor.graph.processingflow.elements.components.asset.IAssetComponent;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IPFComponent;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IProcessingComponent;
import gcat.editor.view.EditorMainFrame;

import javax.swing.table.DefaultTableModel;

public class PropertyTableModel extends DefaultTableModel {

    private IPFComponent component;

    private final String[] columnIdentifiers = new String[] {"Eigenschaft", "Wert"};

    private final EditorMainFrame editorMainFrame;

    public PropertyTableModel(EditorMainFrame reference) {
        editorMainFrame = reference;
        setColumnIdentifiers(columnIdentifiers);
    }

    @Override
    public int getRowCount() {
        if(component != null) {
            if(component instanceof IProcessingComponent) {
                return 2;
            } else if(component instanceof IAssetComponent) {
                return 5;
            }
        }
        return 0;
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
        if(component != null) {
            if(component instanceof IProcessingComponent) {
                switch(column) {
                    case 0:
                        switch(row) {
                            case 0:
                                return "name";
                            case 1:
                                return "classPath";
                        }
                        break;
                    case 1:
                        switch(row) {
                            case 0:
                                return ((IProcessingComponent) component).getName();
                            case 1:
                                return ((IProcessingComponent) component).getClassPath();
                        }
                        break;
                }
            } else if(component instanceof IAssetComponent) {
                switch(column) {
                    case 0:
                        switch(row) {
                            case 0:
                                return "name";
                            case 1:
                                return "type";
                            case 2:
                                return "location";
                            case 3:
                                return "isStart";
                            case 4:
                                return "isEnd";
                        }
                        break;
                    case 1:
                        switch(row) {
                            case 0:
                                return ((IAssetComponent) component).getName();
                            case 1:
                                return ((IAssetComponent) component).getType();
                            case 2:
                                return ((IAssetComponent) component).getLocation();
                            case 3:
                                return ((IAssetComponent) component).isStart();
                            case 4:
                                return ((IAssetComponent) component).isEnd();
                        }
                        break;
                }
            }
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if(component != null) {
            if(component instanceof IProcessingComponent) {
                if(column == 1) {
                    switch(row) {
                        case 0:
                            ((IProcessingComponent) component).setName(String.valueOf(aValue));
                            break;
                        case 1:
                            ((IProcessingComponent) component).setClassPath(String.valueOf(aValue));
                            break;
                    }
                }
            } else if(component instanceof IAssetComponent) {
                if(column == 1) {
                    switch(row) {
                        case 0:
                            ((IAssetComponent) component).setName(String.valueOf(aValue));
                            break;
                        case 1:
                            ((IAssetComponent) component).setType(String.valueOf(aValue));
                            break;
                        case 2:
                            ((IAssetComponent) component).setLocation(String.valueOf(aValue));
                            break;
                        case 3:
                            ((IAssetComponent) component).setStart((Boolean) aValue);
                            ((IAssetComponent) component).setEnd(!((IAssetComponent) component).isStart());
                            editorMainFrame.getEditorGraph().updateStart((IAssetComponent) component);
                            fireTableCellUpdated(4, 1);
                            break;
                        case 4:
                            ((IAssetComponent) component).setEnd((Boolean) aValue);
                            ((IAssetComponent) component).setStart(!((IAssetComponent) component).isEnd());
                            editorMainFrame.getEditorGraph().updateEnd((IAssetComponent) component);
                            fireTableCellUpdated(3, 1);
                            break;
                    }
                }
            }
        }
    }

    public void setCurentIPFComponent(IPFComponent component) {
        if(component != null) {
            this.component = component;
            fireTableDataChanged();
        }
    }
}
