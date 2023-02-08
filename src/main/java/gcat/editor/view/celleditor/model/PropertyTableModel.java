package gcat.editor.view.celleditor.model;

import gcat.editor.graph.EditorGraph;
import gcat.editor.graph.processingflow.components.asset.IAssetComponent;
import gcat.editor.graph.processingflow.components.processing.ProcessingFlowComponent;
import gcat.editor.graph.processingflow.components.processing.interfaces.IPFComponent;
import gcat.editor.graph.processingflow.components.processing.interfaces.IProcessingComponent;
import gcat.editor.view.EditorMainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PropertyTableModel extends DefaultTableModel {

    private IPFComponent component;

    private final String[] columnIdentifiers = new String[] {"Eigenschaft", "Wert"};

    private final EditorMainFrame editorMainFrame;

    private final EditorGraph editorGraph;

    public PropertyTableModel(EditorMainFrame reference) {
        editorMainFrame = reference;
        editorGraph = reference.getEditorGraph();
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
                            // Es kann nur ein Knoten im Graphen als Startknoten markiert werden.
                            // Anzahl an vorhandenen Startknoten.
                            long starts = editorGraph.numberOfStartVertices();
                            // Der erste gefundene Startknoten.
                            Object startVertex = editorGraph.getStartVertex();
                            if(startVertex instanceof ProcessingFlowComponent) {
                                // Startknoten ist nicht aktuelle im Editor gezeigte Komponente.
                                if(((ProcessingFlowComponent) startVertex).getPFComponent() != component) {
                                    // Startknoten bereits gesetzt?
                                    if(starts > 0) {
                                        // Startknoten schon vorhanden, Fehler!
                                        JOptionPane.showMessageDialog(null, "Es kann nur ein " +
                                                "Startknoten definiert werden!");
                                    } else {
                                        // Noch kein Startknoten → Start gesetzt und End genaues Gegenteil.
                                        ((IAssetComponent) component).setStart((Boolean) aValue);
                                        ((IAssetComponent) component).setEnd(!((IAssetComponent) component).isStart());
                                    }
                                }
                                // Startknoten ist aktuelle im Editor gezeigte Komponente.
                                else {
                                    // Start setzen.
                                    ((IAssetComponent) component).setStart((Boolean) aValue);
                                }
                            }
                            // Kein Startknoten vorhanden.
                            else if(startVertex == null) {
                                // Start setzen → End genaues Gegenteil.
                                ((IAssetComponent) component).setStart((Boolean) aValue);
                                ((IAssetComponent) component).setEnd(!((IAssetComponent) component).isStart());
                            }
                            /* ((IAssetComponent) component).setStart((Boolean) aValue);
                            //((IAssetComponent) component).setEnd(!((IAssetComponent) component).isStart());
                            //editorMainFrame.getEditorGraph().updateStart((IAssetComponent) component);
                            //fireTableCellUpdated(4, 1); */
                            fireTableDataChanged();
                            break;
                        case 4:
                            // Mehrere Knoten im Graphen können Endknoten sein.
                            ((IAssetComponent) component).setEnd((Boolean) aValue);
                            ((IAssetComponent) component).setStart(false);
                            /* editorMainFrame.getEditorGraph().updateEnd((IAssetComponent) component);
                            fireTableCellUpdated(3, 1); */
                            fireTableDataChanged();
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
        } else {
            setRowCount(0);
            fireTableDataChanged();
        }
    }
}
