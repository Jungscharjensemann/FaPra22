package gcat.editor.view;

import com.mxgraph.shape.mxStencil;
import com.mxgraph.shape.mxStencilRegistry;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.util.*;
import gcat.editor.controller.*;
import gcat.editor.controller.keyboard.EditorKeyBoardHandler;
import gcat.editor.graph.EditorGraph;
import gcat.editor.graph.EditorGraphComponent;
import gcat.editor.view.celleditor.CellEditor;
import gcat.editor.view.console.EditorConsole;
import gcat.editor.view.console.model.EditorConsoleModel;
import gcat.editor.view.palletes.*;
import gcat.editor.view.tree.CellTree;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Das MainFrame des Editors.
 */
public class EditorMainFrame extends JFrame {

    /**
     * Der Graph im Editor.
     */
    private EditorGraph editorGraph;

    /**
     * Die Swing-Komponente, in dem der Graph
     * dargestellt wird.
     */
    private EditorGraphComponent editorGraphComponent;

    /**
     * Der Editor, in welchem Eigenschaften eines
     * Processing Flows modifiziert werden können.
     */
    private CellEditor cellEditor;

    /**
     * Manager für Undo/Redo.
     */
    private mxUndoManager undoManager;

    /**
     * Listener für Events im Graphen.
     */
    private GraphEventListener graphEventListener;

    private EditorConsoleModel editorConsoleModel;

    private CellTree cellTree;

    public EditorMainFrame() {
        initFrame();
        configureFrame();
        initShapes();
        initMenuBar();
        initComponents2();
        initListeners();
        initHandlers();
    }

    /**
     * Frame initialisieren.
     */
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

    /**
     * Frame konfigurieren.
     */
    private void configureFrame() {
        try {
            // Look and Feel.
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/gcat.png")));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Extern definierte Formen in einer XML-Datei einlesen.
     */
    private void initShapes() {
        String fileName = Objects.requireNonNull(getClass().getClassLoader().getResource("shapes/shapes.xml")).getPath();

        // Parsen der Datei.
        Document doc;
        try {
            doc = mxXmlUtils.parseXml(mxUtils.readFile(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Element shapes = doc.getDocumentElement();
        NodeList list = shapes.getElementsByTagName("shape");

        // Registrieren der Shapes.
        for (int i = 0; i < list.getLength(); i++)
        {
            Element shape = (Element) list.item(i);
            mxStencilRegistry.addStencil(shape.getAttribute("name"),
                    new mxStencil(shape));
        }
    }

    /**
     * Menü initialisieren.
     */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Datei");
        menuBar.add(fileMenu);

        /*
         * Speichern
         */

        JMenuItem saveMenuItem = new JMenuItem("Speichern");
        saveMenuItem.addActionListener(new SaveAndLoadController(this));
        saveMenuItem.setActionCommand("saveFile");

        /*
         * Laden
         */

        JMenuItem loadMenuItem = new JMenuItem("Laden");
        loadMenuItem.addActionListener(new SaveAndLoadController(this));
        loadMenuItem.setActionCommand("loadFile");

        /*
         * Exportieren
         */

        JMenu exportMenuItem = new JMenu("Exportieren");

        JMenuItem xmlExportItem = new JMenuItem("XML");
        xmlExportItem.addActionListener(new ExportXMLController(this));

        JMenuItem pngExportItem = new JMenuItem("PNG");
        pngExportItem.addActionListener(new ExportPNGController(this));

        JMenuItem gmafExportItem = new JMenuItem("GMAF");
        gmafExportItem.addActionListener(new ExportGMAFController());

        exportMenuItem.add(xmlExportItem);
        exportMenuItem.add(pngExportItem);
        exportMenuItem.add(gmafExportItem);

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

        // MenuItems.
        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exportMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(layoutMenuItem);

        /*JMenu languageMenu = new JMenu("Sprache");

        JRadioButtonMenuItem germanRadioButtonMenuItem = new JRadioButtonMenuItem("Deutsch");
        JRadioButtonMenuItem englishRadioButtonMenuItem = new JRadioButtonMenuItem("Englisch");

        germanRadioButtonMenuItem.setActionCommand("german");
        germanRadioButtonMenuItem.addActionListener(new ChangeLanguageController());

        englishRadioButtonMenuItem.setActionCommand("english");
        englishRadioButtonMenuItem.addActionListener(new ChangeLanguageController());

        ButtonGroup languageGroup = new ButtonGroup();

        languageGroup.add(germanRadioButtonMenuItem);
        languageGroup.add(englishRadioButtonMenuItem);

        languageMenu.add(germanRadioButtonMenuItem);
        languageMenu.add(englishRadioButtonMenuItem);

        menuBar.add(languageMenu);*/

        setJMenuBar(menuBar);
    }

    /**
     * Komponenten initialisieren.
     */
    @SuppressWarnings("unused")
    private void initComponents() {
        // Graph
        editorGraph = new EditorGraph();
        editorGraphComponent = new EditorGraphComponent(editorGraph);

        // Main
        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel);
        mainPanel.setLayout(new BorderLayout());

        // Outer SplitPane
        JSplitPane outerSplitPane = new JSplitPane();
        outerSplitPane.setOneTouchExpandable(true);
        outerSplitPane.setDividerLocation(220);
        outerSplitPane.setDividerSize(6);
        outerSplitPane.setBorder(null);

        // Inner SplitPane
        JSplitPane innerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        innerSplitPane.setOneTouchExpandable(true);
        innerSplitPane.setResizeWeight(0.84);
        innerSplitPane.setDividerSize(6);
        innerSplitPane.setBorder(null);

        JPanel leftPane = new JPanel();
        leftPane.setLayout(new BorderLayout());

        JLabel paletteLabel = new JLabel("Palette");
        paletteLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        paletteLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPane.add(paletteLabel, BorderLayout.NORTH);

        JPanel scrollPanePortView = new JPanel();

        // SplitPane - Inner Left Component
        JScrollPane leftScrollPane = new JScrollPane(scrollPanePortView);
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leftScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        leftScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        leftScrollPane.setBorder(null);

        leftPane.add(leftScrollPane, BorderLayout.CENTER);

        // Graph Outline - Inner Right Component
        mxGraphOutline graphOutline = new mxGraphOutline(editorGraphComponent);
        graphOutline.setFitPage(true);

        // Left / Right Inner Component
        innerSplitPane.setLeftComponent(leftPane);
        innerSplitPane.setRightComponent(graphOutline);

        //leftScrollPane.setViewportView(scrollPanePortView);

        ArrayList<EditorPalette> paletteList = new ArrayList<>();
        {
            paletteList.add(new FusionPalette());

            // Weitere Paletten für eventuelle Extensions, Filter etc.

            paletteList.add(new AssetPalette());
            paletteList.add(new AlgorithmicPalette());
            paletteList.add(new AudioPalette());
            paletteList.add(new GoogleVisionPalette());
            paletteList.add(new TextPalette());
            paletteList.add(new PreProcessingPalette());
            paletteList.add(new VideoPalette());
        }
        paletteList.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));

        scrollPanePortView.setLayout(new MigLayout("" , "[grow]", "[]".repeat(paletteList.size())));

        for(int i = 0; i < paletteList.size(); i++) {
            scrollPanePortView.add(paletteList.get(i), String.format("cell 0 %s, growx, aligny top", i));
        }

        // Left / Right Outer Component
        outerSplitPane.setLeftComponent(innerSplitPane);

        // SplitPane - Right Outer Component
        JSplitPane rightOuterSplitPane = new JSplitPane();
        rightOuterSplitPane.setOneTouchExpandable(true);
        rightOuterSplitPane.setResizeWeight(0.9);
        rightOuterSplitPane.setDividerSize(6);
        rightOuterSplitPane.setBorder(null);

        JPanel rightOuterLeft = new JPanel();
        rightOuterLeft.setLayout(new BorderLayout());

        rightOuterLeft.add(editorGraphComponent, BorderLayout.CENTER);

        rightOuterSplitPane.setLeftComponent(rightOuterLeft);

        cellEditor = new CellEditor(this);

        rightOuterSplitPane.setRightComponent(cellEditor);

        // Right / Right Outer Component
        outerSplitPane.setRightComponent(rightOuterSplitPane);

        mainPanel.add(outerSplitPane, BorderLayout.CENTER);
    }

    /**
     * Komponenten initialisieren.
     */
    private void initComponents2(){
        editorGraph = new EditorGraph();
        editorGraphComponent = new EditorGraphComponent(editorGraph);
        editorGraphComponent.setBorder(new TitledBorder("Graph"));

        //Main Panel
        JPanel mainPanel = new JPanel();
        {
            mainPanel.setLayout(new BorderLayout());
            getContentPane().add(mainPanel);

            // MultiSplitPane (ohne Nested SplitPanes).
            JXMultiSplitPane multiSplitPane = new JXMultiSplitPane();
            {
                /*
                 * Datenstruktur für den Aufbau
                 * der SplitPanes im MultiSplitPane.
                 */
                Split main = new Split();
                {
                    // Erste Spalte.
                    main.setRowLayout(false);

                    // Erste Zeile in der ersten Spalte.
                    Split mainRow = new Split();
                    {
                        mainRow.setWeight(0.7);

                        // Linke Spalte in erster Zeile.
                        Split left = new Split();
                        left.setRowLayout(false);
                        left.setWeight(0.128);

                        // Links oben.
                        Leaf leaf_top = new Leaf("left.top");
                        leaf_top.setWeight(0.85);

                        // Links unten.
                        Leaf leaf_bottom = new Leaf("left.bottom");
                        leaf_bottom.setWeight(0.1);

                        // (Vertikal: links oben, Trenner, links unten).
                        left.setChildren(leaf_top, new Divider(), leaf_bottom);

                        // Mitte.
                        Leaf center = new Leaf("center");
                        center.setWeight(0.8);

                        // Rechts.
                        Split right = new Split();
                        right.setRowLayout(false);
                        right.setWeight(0);

                        Leaf right_top = new Leaf("right.top");
                        right_top.setWeight(0.5);

                        Leaf right_bottom = new Leaf("right.bottom");
                        right_bottom.setWeight(0.5);

                        right.setChildren(right_top, new Divider(), right_bottom);

                        // (Horizontal: links, Trenner, mitte, Trenner, rechts).
                        mainRow.setChildren(left, new Divider(), center, new Divider(), right);
                    }

                    // Zweite Zeile in der ersten Spalte.
                    Leaf bottom = new Leaf("bottom");
                    bottom.setWeight(0.3);

                    // (Vertikal: Erste Zeile, Trenner, Zweite Zeile).
                    main.setChildren(mainRow, new Divider(), bottom);

                    // Layout des MultiSplitPanes.
                    MultiSplitLayout layout = new MultiSplitLayout(main);
                    multiSplitPane.setLayout(layout);
                }

                // Panel links oben.
                JPanel leftTopPane = new JPanel();
                {
                    leftTopPane.setLayout(new BorderLayout());
                    leftTopPane.setBorder(new TitledBorder("Palette"));

                    // Label.
                    JLabel paletteLabel = new JLabel("Palette");
                    paletteLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
                    paletteLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    // Panel für das ScrollPane.
                    JPanel scrollPanePortView = new JPanel();
                    {
                        // Liste an Paletten.
                        ArrayList<EditorPalette> paletteList = new ArrayList<>();
                        {
                            paletteList.add(new FusionPalette());

                            // Weitere Paletten für eventuelle Extensions, Filter etc.

                            paletteList.add(new AssetPalette());
                            paletteList.add(new AlgorithmicPalette());
                            paletteList.add(new AudioPalette());
                            paletteList.add(new GoogleVisionPalette());
                            paletteList.add(new TextPalette());
                            paletteList.add(new PreProcessingPalette());
                            paletteList.add(new VideoPalette());
                            paletteList.add(new Miscellaneous());

                            // Alphabetisches Sortieren der Paletten nach ihren Titeln.
                            paletteList.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
                        }

                        // Besonderes MigLayout für die Paletten.
                        MigLayout migLayout = new MigLayout("" , "[grow]", "[]".repeat(paletteList.size()));
                        scrollPanePortView.setLayout(migLayout);

                        // Hinzufügen der Paletten.
                        for(int i = 0; i < paletteList.size(); i++) {
                            scrollPanePortView.add(paletteList.get(i), String.format("cell 0 %s, growx, aligny top", i));
                        }
                    }

                    // ScrollPane für die Paletten.
                    JScrollPane leftScrollPane = new JScrollPane(scrollPanePortView);
                    {
                        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        leftScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        leftScrollPane.getVerticalScrollBar().setUnitIncrement(10);
                        leftScrollPane.setBorder(null);
                    }

                    //leftTopPane.add(paletteLabel, BorderLayout.NORTH);
                    leftTopPane.add(leftScrollPane, BorderLayout.CENTER);
                }

                // Graph OutLine für den Graphen und seine Komponente.
                JPanel outlinePanel = new JPanel();
                outlinePanel.setBorder(new TitledBorder("Umriss"));
                outlinePanel.setLayout(new BorderLayout());

                mxGraphOutline leftBottomGraphOutline = new mxGraphOutline(editorGraphComponent);
                leftBottomGraphOutline.setToolTipText("Umriss des Graphen");
                leftBottomGraphOutline.setPreferredSize(new Dimension(180, 180));
                leftBottomGraphOutline.setFitPage(true);
                outlinePanel.add(leftBottomGraphOutline, BorderLayout.CENTER);

                // CellEditor.
                cellEditor = new CellEditor(this);

                // Konsole.
                editorConsoleModel = new EditorConsoleModel();
                EditorConsole editorConsole = new EditorConsole(editorConsoleModel);

                // CellTree (Struktur).
                cellTree = new CellTree(this);

                multiSplitPane.add(editorConsole, "bottom");
                multiSplitPane.add(leftTopPane, "left.top");
                multiSplitPane.add(outlinePanel, "left.bottom");
                multiSplitPane.add(editorGraphComponent, "center");
                multiSplitPane.add(cellTree, "right.top");
                multiSplitPane.add(cellEditor, "right.bottom");

                multiSplitPane.setContinuousLayout(true);
                multiSplitPane.getMultiSplitLayout().layoutByWeight(editorGraphComponent);
            }
            mainPanel.add(multiSplitPane, BorderLayout.CENTER);
        }
    }

    /* Handler */

    private void initHandlers() {
        mxRubberband mxRubberband = new mxRubberband(editorGraphComponent);
        Color fill = new Color(255, 255, 255, 80);
        mxRubberband.setFillColor(fill);
        mxRubberband.setBorderColor(Color.red);

        new EditorKeyBoardHandler(this, editorGraphComponent);
    }

    /* Listener */

    /**
     * Listener initialisieren.
     */
    public void initListeners() {
        graphEventListener = new GraphEventListener(this);

        // UndoManager.
        undoManager = new mxUndoManager();
        undoManager.addListener(mxEvent.UNDO, graphEventListener);
        undoManager.addListener(mxEvent.REDO, graphEventListener);

        // Unbenutzt, nicht notwendig.
        editorGraph.getModel().addListener(mxEvent.CONNECT_CELL, graphEventListener);

        // Controller für das Klicken auf Knoten im Graphen.
        editorGraphComponent.getGraphControl().addMouseListener(new GraphVertexController(this));

        initModelListeners();
        initViewListeners();
    }

    /**
     * Listener für das Model des Graphen
     * initialisieren.
     */
    public void initModelListeners() {
        editorGraph.getModel().addListener(mxEvent.CHANGE, graphEventListener);
        editorGraph.getModel().addListener(mxEvent.UNDO, graphEventListener);
    }

    private void initViewListeners() {
        editorGraph.getView().addListener(mxEvent.UNDO, graphEventListener);
    }

    /* Getter */

    public EditorGraph getEditorGraph() {
        return editorGraph;
    }

    public mxUndoManager getUndoManager() {
        return undoManager;
    }

    public EditorGraphComponent getEditorGraphComponent() {
        return editorGraphComponent;
    }

    public CellEditor getCellEditor() {
        return cellEditor;
    }

    public EditorConsoleModel getEditorConsoleModel() {
        return editorConsoleModel;
    }

    public CellTree getCellTree() {
        return cellTree;
    }
}
