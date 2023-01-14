package gcat.editor.view.celleditor;

import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IPFComponent;
import gcat.editor.graph.processingflow.elements.components.processing.interfaces.IProcessingComponent;
import gcat.editor.graph.processingflow.elements.components.processing.plugin.PluginElement;
import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.EditorPalette;
import gcat.editor.view.celleditor.model.ParameterTableModel;
import gcat.editor.view.celleditor.model.PropertyTableModel;
import gcat.editor.view.celleditor.renderer.HeaderRenderer;
import gcat.editor.view.celleditor.table.CellTable;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.TreeMap;

public class CellEditor extends JPanel {

    private final ParameterTableModel parameterTableModel;

    private final PropertyTableModel propertyTableModel;

    private final JPanel parameterPanel;

    private IPFComponent component;

    private EditorMainFrame editorMainFrame;

    public CellEditor(EditorMainFrame reference) {
        this.editorMainFrame = reference;
        setLayout(new MigLayout("", "[grow]", "[]"));
        JLabel lblNewLabel = new JLabel("Cell Editor");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblNewLabel, "cell 0 1, growx");

        JScrollPane propertyScrollPane = new JScrollPane();
        propertyScrollPane.setBorder(new TitledBorder("Eigenschaften"));

        CellTable propertyTable = new CellTable();
        propertyTable.getTableHeader().setDefaultRenderer(new HeaderRenderer(propertyTable.getTableHeader()));
        propertyTableModel = new PropertyTableModel(editorMainFrame);
        propertyTable.setModel(propertyTableModel);

        propertyScrollPane.setViewportView(propertyTable);

        add(propertyScrollPane, "cell 0 2, growx");


        parameterPanel = new JPanel();
        parameterPanel.setVisible(false);
        parameterPanel.setBorder(new TitledBorder("Parameter"));
        parameterPanel.setLayout(new BorderLayout());

        JScrollPane paramScrollPane = new JScrollPane();
        CellTable paramTable = new CellTable();
        paramTable.getTableHeader().setDefaultRenderer(new HeaderRenderer(paramTable.getTableHeader()));

        parameterTableModel = new ParameterTableModel();
        paramTable.setModel(parameterTableModel);

        paramScrollPane.setViewportView(paramTable);

        parameterPanel.add(paramScrollPane, BorderLayout.NORTH);

        JPanel addParamPanel = new JPanel();
        addParamPanel.setLayout(new MigLayout("", "[left][grow]"));

        JLabel keyLabel = new JLabel("Parameter:");
        JLabel valueLabel = new JLabel("Wert:");

        JTextField keyField = new JTextField();
        JTextField valueField = new JTextField();

        JButton addButton = new JButton("Hinzufügen");
        addButton.setSize(new Dimension(70, 20));


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

        addParamPanel.add(keyLabel, "cell 0 0");
        addParamPanel.add(keyField, "cell 1 0, growx");

        addParamPanel.add(valueLabel, "cell 0 1");
        addParamPanel.add(valueField, "cell 1 1, growx");

        addParamPanel.add(addButton, "cell 1 2, span, align center");

        parameterPanel.add(addParamPanel, BorderLayout.CENTER);

        add(parameterPanel, "cell 0 3, growx");
    }

    public void setParameters(TreeMap<String, Object> parameters) {
        parameterTableModel.setParameters(parameters);
        revalidate();
    }

    public void setCurrentIPFComponent(IPFComponent component) {
        if(component != null) {
            this.component = component;
            propertyTableModel.setCurentIPFComponent(component);
            revalidate();
            if(component instanceof PluginElement) {
                parameterPanel.setVisible(true);
                //parameterTableModel.setParameters(((IProcessingComponent) component).getParameters());
                setParameters(((PluginElement) component).getParameters());
            } else {
                parameterPanel.setVisible(false);
            }
        }
    }

    public IPFComponent getCurrentIPFComponent() {
        return component;
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getMinimumSize();
    }
}
