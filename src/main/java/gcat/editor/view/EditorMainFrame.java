package gcat.editor.view;

import com.mxgraph.shape.mxStencil;
import com.mxgraph.shape.mxStencilRegistry;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import gcat.editor.controller.*;
import gcat.editor.controller.keyboard.EditorKeyBoardHandler;
import gcat.editor.graph.EditorGraph;
import gcat.editor.graph.EditorGraphComponent;
import gcat.editor.view.celleditor.CellEditor;
import gcat.editor.view.palletes.*;
import gcat.editor.view.table.CellParameterEditor;
import net.miginfocom.swing.MigLayout;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class EditorMainFrame extends JFrame {

    private JPanel mainPanel;

    private EditorGraph editorGraph;

    private EditorGraphComponent editorGraphComponent;

    private CellParameterEditor cellParameterEditor;

    private CellEditor cellEditor;

    private mxUndoManager undoManager;

    private boolean modified = false;

    private JSplitPane outerSplitPane, innerSplitPane, rightOuterSplitPane;

    private JPanel scrollPanePortView;

    private JScrollPane leftScrollPane;

    private mxGraphOutline graphOutline;

    private mxRubberband mxRubberband;

    private EditorKeyBoardHandler editorKeyBoardHandler;

    private JLabel errorLabel;

    public EditorMainFrame() {
        initFrame();
        configureFrame();
        initShapes();
        initMenuBar();
        initComponents();
        initListeners();
        initHandlers();
    }

    private void initFrame() {
        // relative Höhe des Fensters (60%).
        double heightPercentage = 0.65;
        // Seitenverhältnis
        double aspectRatio = 16.0 / 10.0;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) (dim.height * heightPercentage);
        int width = (int) (height * aspectRatio);

        // Parameter des Haupt-Frames konfigurieren.
        setTitle("GCAT (GMAF Configuration and Administration Tool) - FaPra [Jens Nathan Andreß, 9763180]");
        setBounds((dim.width - width) / 2, (dim.height - height) / 2, width, height);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void configureFrame() {
        try {
            //UIManager.put("TaskPane.titleBackgroundGradientStart", new Color(162, 158, 158));
            //UIManager.put("TaskPane.titleBackgroundGradientEnd", new Color(162, 158, 158));
            //UIManager.put("TaskPane.borderColor", Color.GRAY);
            //UIManager.put("TaskPane.titleOver", Color.GREEN);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/gcat.png")));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initShapes() {
        String fileName = Objects.requireNonNull(getClass().getClassLoader().getResource("shapes/shapes.xml")).getPath();

        Document doc;
        try {
            doc = mxXmlUtils.parseXml(mxUtils.readFile(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Element shapes = doc.getDocumentElement();
        NodeList list = shapes.getElementsByTagName("shape");

        for (int i = 0; i < list.getLength(); i++)
        {
            Element shape = (Element) list.item(i);
            mxStencilRegistry.addStencil(shape.getAttribute("name"),
                    new mxStencil(shape));
        }
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Datei");
        menuBar.add(fileMenu);

        /*
         * Exportieren
         */

        JMenu exportMenuItem = new JMenu("Exportieren");

        JMenuItem xmlExportItem = new JMenuItem("XML");
        xmlExportItem.addActionListener(new ExportXMLController(this));

        exportMenuItem.add(xmlExportItem);
        exportMenuItem.add(new JMenuItem("GMAF"));

        /*
         * Layout
         */

        LayoutController layoutController = new LayoutController(this);

        JMenu layoutMenuItem = new JMenu("Layout");

        JMenuItem verticalHierarchicalLayout = new JMenuItem("Vertical Hierarchical Layout");
        verticalHierarchicalLayout.setActionCommand("hierarchVertical");
        verticalHierarchicalLayout.addActionListener(layoutController);
        layoutMenuItem.add(verticalHierarchicalLayout);

        JMenuItem horizontalHierarchicalLayout = new JMenuItem("Horizontal Hierarchical Layout");
        horizontalHierarchicalLayout.setActionCommand("hierarchHorizontal");
        horizontalHierarchicalLayout.addActionListener(layoutController);
        layoutMenuItem.add(horizontalHierarchicalLayout);

        JMenuItem circleItem = new JMenuItem("Circle Layout");
        circleItem.setActionCommand("circle");
        circleItem.addActionListener(layoutController);
        layoutMenuItem.add(circleItem);

        JMenuItem organicItem = new JMenuItem("Organic Layout");
        organicItem.setActionCommand("organic");
        organicItem.addActionListener(layoutController);
        layoutMenuItem.add(organicItem);

        JMenuItem verticalPartitionItem = new JMenuItem("Vertical Partition Layout");
        verticalPartitionItem.setActionCommand("verticalPartition");
        verticalPartitionItem.addActionListener(layoutController);
        layoutMenuItem.add(verticalPartitionItem);

        JMenuItem horizontalPartitionItem = new JMenuItem("Horizontal Partition Layout");
        verticalPartitionItem.setActionCommand("horizontalPartition");
        verticalPartitionItem.addActionListener(layoutController);
        layoutMenuItem.add(horizontalPartitionItem);

        JMenuItem verticalStackItem = new JMenuItem("Vertical Stack Layout");
        verticalPartitionItem.setActionCommand("verticalStack");
        verticalPartitionItem.addActionListener(layoutController);
        layoutMenuItem.add(verticalStackItem);

        JMenuItem horizontalStackItem = new JMenuItem("Horizontal Stack Layout");
        verticalPartitionItem.setActionCommand("horizontalStack");
        verticalPartitionItem.addActionListener(layoutController);
        layoutMenuItem.add(horizontalStackItem);

        JMenuItem compactItem = new JMenuItem("CompactTree Layout");
        compactItem.setActionCommand("compact");
        compactItem.addActionListener(layoutController);
        layoutMenuItem.add(compactItem);

        fileMenu.add(exportMenuItem);
        fileMenu.add(layoutMenuItem);

        setJMenuBar(menuBar);
    }

    private void initComponents() {

        editorGraph = new EditorGraph();

        editorGraphComponent = new EditorGraphComponent(editorGraph);

        mainPanel = new JPanel();

        getContentPane().add(mainPanel);

        mainPanel.setLayout(new BorderLayout());

        // Outer SplitPane

        outerSplitPane = new JSplitPane();
        outerSplitPane.setOneTouchExpandable(true);
        outerSplitPane.setDividerLocation(220);
        outerSplitPane.setDividerSize(6);
        outerSplitPane.setBorder(null);

        // Inner SplitPane

        innerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        innerSplitPane.setOneTouchExpandable(true);
        //innerSplitPane.setDividerLocation(0.5);
        innerSplitPane.setResizeWeight(0.84);
        innerSplitPane.setDividerSize(6);
        //innerSplitPane.setMinimumSize(new Dimension(220, -1));
        innerSplitPane.setBorder(null);

        JPanel leftPane = new JPanel();
        leftPane.setLayout(new BorderLayout());

        JLabel paletteLabel = new JLabel("Palette");
        paletteLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        paletteLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPane.add(paletteLabel, BorderLayout.NORTH);

        scrollPanePortView = new JPanel();

        // SplitPane - Inner Left Component

        leftScrollPane = new JScrollPane(scrollPanePortView);
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leftScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        leftScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        leftScrollPane.setBorder(null);

        leftPane.add(leftScrollPane, BorderLayout.CENTER);

        // Graph Outline - Inner Right Component

        graphOutline = new mxGraphOutline(editorGraphComponent);
        graphOutline.setFitPage(true);

        // Left / Right Inner Component

        innerSplitPane.setLeftComponent(leftPane);


        innerSplitPane.setRightComponent(graphOutline);


        leftScrollPane.setViewportView(scrollPanePortView);

        /*GoogleVisionAPIJXTaskPane c1 = new GoogleVisionAPIJXTaskPane();
        GoogleVisionAPIJXTaskPane c2 = new GoogleVisionAPIJXTaskPane();
        GoogleVisionAPIJXTaskPane c3 = new GoogleVisionAPIJXTaskPane();

        c1.setCollapsed(true);

        //scrollPanePortView.setLayout(new MigLayout("", "[grow]", "[]".repeat(1)));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.rowWeights = new double[] {0.0, 0.0};
        gridBagLayout.columnWeights = new double[] {1.0};
        scrollPanePortView.setLayout(gridBagLayout);

        GoogleVisionPallete googleVisionPallete = new GoogleVisionPallete();
        //googleVisionPallete.setPreferredWidth(220);

        //scrollPanePortView.add(googleVisionPallete, "growx, aligny top");
        //scrollPanePortView.add(googleVisionPallete, "growx, aligny top");
        //scrollPanePortView.add(c1, "cell 0 1, growx, aligny top");

        GridBagConstraints gbc_c1 = new GridBagConstraints();
        gbc_c1.anchor = GridBagConstraints.NORTH;
        gbc_c1.fill = GridBagConstraints.HORIZONTAL;
        gbc_c1.gridx = 0;
        gbc_c1.gridy = 0;
        gbc_c1.gridwidth = 1;

        GridBagConstraints gbc_c2 = new GridBagConstraints();
        gbc_c2.anchor = GridBagConstraints.NORTH;
        gbc_c2.fill = GridBagConstraints.HORIZONTAL;
        gbc_c2.gridx = 0;
        gbc_c2.gridy = 1;
        gbc_c2.gridwidth = 1;
        gbc_c2.weighty = 1;

        scrollPanePortView.add(googleVisionPallete, gbc_c1);
        scrollPanePortView.add(c2, gbc_c2)*/

        ArrayList<EditorPalette> paletteList = new ArrayList<>();

        {
            //paletteList.add(new PluginPalette());
            paletteList.add(new FusionPalette());
            //paletteList.add(new FilterPalette());
            //paletteList.add(new ExtensionsPalette());
            paletteList.add(new MiscellaneousPalette());
            //paletteList.add(new GoogleVisionPallete());

            paletteList.add(new AudioPalette());
            paletteList.add(new GoogleVisionPalette());
            paletteList.add(new TextPalette());
            paletteList.add(new VideoPalette());
        }

        paletteList.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));

        scrollPanePortView.setLayout(new MigLayout("" , "[grow]", "[]".repeat(paletteList.size())));

        //GoogleVisionPallete googleVisionPallete = new GoogleVisionPallete();
        //googleVisionPallete.setBorder(new PaneB);
        //GoogleVisionAPIJXTaskPane c1 = new GoogleVisionAPIJXTaskPane();

        for(int i = 0; i < paletteList.size(); i++) {
            scrollPanePortView.add(paletteList.get(i), String.format("cell 0 %s, growx, aligny top", i));
        }

        //scrollPanePortView.add(googleVisionPallete, "cell 0 1, growx, aligny top");
        //scrollPanePortView.add(c1, "cell 0 2, growx, aligny top");

        // Left / Right Outer Component

        outerSplitPane.setLeftComponent(innerSplitPane);


        rightOuterSplitPane = new JSplitPane();
        rightOuterSplitPane.setOneTouchExpandable(true);
        rightOuterSplitPane.setResizeWeight(0.9);
        rightOuterSplitPane.setDividerSize(6);
        rightOuterSplitPane.setBorder(null);

        JPanel rightOuterLeft = new JPanel();
        rightOuterLeft.setLayout(new BorderLayout());

        errorLabel = new JLabel();
        errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        errorLabel.setHorizontalAlignment(JLabel.CENTER);

        rightOuterLeft.add(errorLabel, BorderLayout.NORTH);
        rightOuterLeft.add(editorGraphComponent, BorderLayout.CENTER);

        rightOuterSplitPane.setLeftComponent(rightOuterLeft);

        cellParameterEditor = new CellParameterEditor();
        cellEditor = new CellEditor(this);

        rightOuterSplitPane.setRightComponent(cellEditor);

        outerSplitPane.setRightComponent(rightOuterSplitPane);


        mainPanel.add(outerSplitPane, BorderLayout.CENTER);
        mainPanel.add(new JLabel("Status"), BorderLayout.SOUTH);
    }

    private void initHandlers() {
        mxRubberband = new mxRubberband(editorGraphComponent);

        editorKeyBoardHandler = new EditorKeyBoardHandler(this, editorGraphComponent);
    }

    private void initListeners() {
        undoManager = new mxUndoManager();

        GraphEventListener graphEventListener = new GraphEventListener(this);

        //editorGraph.addListener(mxEvent.CELLS_ADDED, graphEventListener);
        //editorGraph.addListener(mxEvent.CELL_CONNECTED, graphEventListener);
        editorGraph.addListener(mxEvent.CELL_CONNECTED, new GraphEdgeEventController(this));

        editorGraph.getModel().addListener(mxEvent.CONNECT_CELL, graphEventListener);

        /*editorGraphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mxCell cell = (mxCell) editorGraphComponent.getCellAt(e.getX(), e.getY());
                System.out.println(cell);
            }
        });*/

        editorGraphComponent.getGraphControl().addMouseListener(new TestController(this));
        editorGraph.getSelectionModel().addListener(mxEvent.MARK, graphEventListener);

        editorGraph.getModel().addListener(mxEvent.CHANGE, graphEventListener);

        editorGraph.getModel().addListener(mxEvent.UNDO, graphEventListener);

        editorGraph.getView().addListener(mxEvent.UNDO, graphEventListener);

        undoManager.addListener(mxEvent.UNDO, graphEventListener);
        undoManager.addListener(mxEvent.REDO, graphEventListener);
    }

    public Action bind(String name, final Action action)
    {
        return bind(name, action, null);
    }

    /**
     *
     * @param name
     * @param action
     * @return a new Action bound to the specified string name and icon
     */
    @SuppressWarnings("serial")
    public Action bind(String name, final Action action, String iconUrl)
    {
        AbstractAction newAction = new AbstractAction(name, (iconUrl != null) ? new ImageIcon(
                Objects.requireNonNull(getClass().getClassLoader().getResource(iconUrl))) : null)
        {
            public void actionPerformed(ActionEvent e)
            {
                action.actionPerformed(new ActionEvent(getEditorGraphComponent(), e
                        .getID(), e.getActionCommand()));
            }
        };

        newAction.putValue(Action.SHORT_DESCRIPTION, action.getValue(Action.SHORT_DESCRIPTION));

        return newAction;
    }

    public void setModified(boolean modified)
    {
        boolean oldValue = this.modified;
        this.modified = modified;

        firePropertyChange("modified", oldValue, modified);

        if (oldValue != modified)
        {
            System.out.println("Modified");
            //updateTitle();
        }
    }

    public EditorGraph getEditorGraph() {
        return editorGraph;
    }

    public mxUndoManager getUndoManager() {
        return undoManager;
    }

    public EditorGraphComponent getEditorGraphComponent() {
        return editorGraphComponent;
    }

    public CellParameterEditor getCellParameterEditor() {
        return cellParameterEditor;
    }

    public CellEditor getCellEditor() {
        return cellEditor;
    }

    public JLabel getErrorLabel() {
        return errorLabel;
    }
}
