package gcat.editor.view.celleditor;

import gcat.editor.graph.processingflow.components.processing.interfaces.IPFComponent;
import gcat.editor.graph.processingflow.components.processing.interfaces.IProcessingComponent;
import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.celleditor.model.ParameterTableModel;
import gcat.editor.view.celleditor.model.PropertyTableModel;
import gcat.editor.view.celleditor.renderer.HeaderRenderer;
import gcat.editor.view.celleditor.table.CellPropertyTable;
import gcat.editor.view.celleditor.table.CellTable;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.TreeMap;

/**
 * Editor für das Modifizieren von
 * Eigenschaften einer Komponente.
 */
public class CellEditor extends JPanel {

    /**
     * Model für Parameter.
     */
    private final ParameterTableModel parameterTableModel;

    /**
     * Model für Eigenschaften.
     */
    private final PropertyTableModel propertyTableModel;

    private final JScrollPane propertyScrollPane;

    /**
     * Panel für Parameter.
     */
    private final JPanel parameterPanel;

    /**
     * Aktuelle Komponente im PF.
     */
    private IPFComponent component;

    public CellEditor(EditorMainFrame reference) {
        setLayout(new MigLayout("", "[grow]", "[]"));
        setBorder(new TitledBorder("Cell Editor"));

        propertyScrollPane = new JScrollPane();
        propertyScrollPane.setBorder(new TitledBorder("Eigenschaften"));
        propertyScrollPane.setVisible(false);

        // Table für die Eigenschaften einer Komponente.
        CellPropertyTable propertyTable = new CellPropertyTable();
        propertyTable.getTableHeader().setDefaultRenderer(new HeaderRenderer(propertyTable.getTableHeader()));
        propertyTableModel = new PropertyTableModel(reference);
        propertyTable.setModel(propertyTableModel);


        propertyScrollPane.setViewportView(propertyTable);
        add(propertyScrollPane, "cell 0 2, growx");

        // Eigenes Panel für Parameter.
        parameterPanel = new JPanel();
        parameterPanel.setVisible(false);
        parameterPanel.setBorder(new TitledBorder("Parameter"));
        parameterPanel.setLayout(new BorderLayout());

        CellTable paramTable = new CellTable();
        JScrollPane paramScrollPane = new JScrollPane(paramTable);
        paramTable.getTableHeader().setDefaultRenderer(new HeaderRenderer(paramTable.getTableHeader()));

        // Model für die Parameter.
        parameterTableModel = new ParameterTableModel();
        paramTable.setModel(parameterTableModel);

        parameterPanel.add(paramScrollPane, BorderLayout.NORTH);
        JPanel addParamPanel = new JPanel();
        addParamPanel.setLayout(new MigLayout("", "[left][grow]"));

        // Labels.

        JLabel keyLabel = new JLabel("Parameter:");
        JLabel valueLabel = new JLabel("Wert:");

        JTextField keyField = new JTextField();
        JTextField valueField = new JTextField();

        // Buttons.

        JButton addButton = new JButton("Hinzufügen");
        addButton.setSize(new Dimension(70, 20));

        JButton removeButton = new JButton("Entfernen");
        removeButton.setSize(new Dimension(70, 20));

        addButton.addActionListener(e -> {
            if(component != null) {
                if(component instanceof IProcessingComponent) {
                    if(!keyField.getText().isEmpty() && !valueField.getText().isEmpty()) {
                        ((IProcessingComponent) component).addParameter(keyField.getText(), valueField.getText());
                        setParameters(((IProcessingComponent) component).getParameters());
                    }
                }
            }
        });

        removeButton.addActionListener(e -> {
            if(component != null) {
                if(component instanceof IProcessingComponent) {
                    if(!keyField.getText().isEmpty()) {
                        ((IProcessingComponent) component).removeParameter(keyField.getText());
                        setParameters(((IProcessingComponent) component).getParameters());
                    }
                }
            }
        });

        addParamPanel.add(keyLabel, "cell 0 0");
        addParamPanel.add(keyField, "cell 1 0, growx");

        addParamPanel.add(valueLabel, "cell 0 1");
        addParamPanel.add(valueField, "cell 1 1, growx");

        addParamPanel.add(addButton, "span, split 2, cell 0 2, align center");
        addParamPanel.add(removeButton, "cell 1 2, align center");

        parameterPanel.add(addParamPanel, BorderLayout.CENTER);

        add(parameterPanel, "cell 0 3, growx");
    }

    /**
     * Setzt Parameter im Model.
     * @param parameters Parameter.
     */
    public void setParameters(TreeMap<String, Object> parameters) {
        parameterTableModel.setParameters(parameters);
        revalidate();
    }

    /**
     * Setzt die aktuelle Komponente zum Editieren.
     * @param component Aktuelle Komponente im PF.
     */
    public void setCurrentIPFComponent(IPFComponent component) {
        if(component != null) {
            this.component = component;
            propertyTableModel.setCurentIPFComponent(component);
            propertyScrollPane.setVisible(true);
            // Notwendig.
            revalidate();
            if(component instanceof IProcessingComponent) {
                parameterPanel.setVisible(true);
                setParameters(((IProcessingComponent) component).getParameters());
            } else {
                parameterPanel.setVisible(false);
            }
        } else {
            propertyScrollPane.setVisible(false);
            propertyTableModel.setCurentIPFComponent(null);
            parameterTableModel.setParameters(null);
            parameterPanel.setVisible(false);
        }
    }

    @SuppressWarnings("unused")
    public IPFComponent getCurrentIPFComponent() {
        return component;
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getMinimumSize();
    }
}
