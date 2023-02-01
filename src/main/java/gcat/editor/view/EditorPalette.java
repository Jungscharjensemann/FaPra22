package gcat.editor.view;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxRectangle;
import gcat.editor.graph.processingflow.components.asset.AssetElement;
import gcat.editor.graph.processingflow.components.media.MultiMediaType;
import gcat.editor.graph.processingflow.components.processing.ProcessingFlowComponent;
import gcat.editor.graph.processingflow.components.processing.fusion.FusionElement;
import gcat.editor.graph.processingflow.components.processing.plugin.PluginElement;
import graph_editor.editor.ShadowBorder;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Grundklasse für eine Palette im Editor.
 */
public class EditorPalette extends JXTaskPane {

    protected JLabel selectedEntry = null;

    protected mxEventSource eventSource = new mxEventSource(this);

    protected Color gradientColor = null;


    public EditorPalette() {
        setLayout(new WrapLayout(WrapLayout.LEADING));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clearSelection();
            }
        });
        setFocusable(false);
        setTransferHandler(new TransferHandler() {

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
                return true;
            }
        });
        setSpecial(true);
    }

    /**
     * Auswahl leeren.
     */
    public void clearSelection()
    {
        setSelectionEntry(null, null);
    }

    /**
     * Auswahl setzen.
     */
    public void setSelectionEntry(JLabel entry, mxGraphTransferable t)
    {
        JLabel previous = selectedEntry;
        selectedEntry = entry;

        if (previous != null)
        {
            previous.setBorder(null);
            previous.setOpaque(false);
        }

        if (selectedEntry != null)
        {
            selectedEntry.setBorder(ShadowBorder.getSharedInstance());
            selectedEntry.setOpaque(true);
        }

        // Event fürs Transferen der Auswahl.
        eventSource.fireEvent(new mxEventObject(mxEvent.SELECT, "entry",
                selectedEntry, "transferable", t, "previous", previous));
    }

    /**
     * Template für das Hinzufügen eines AssetElements.
     * @param collection AssetElement.
     */
    public void addFolderTemplate(AssetElement collection) {
        ProcessingFlowComponent cell = new ProcessingFlowComponent(collection);
        cell.setVertex(true);

        addTemplate(collection.getLabel(),
                new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(String.format("images/%s", collection.getIcon())))), cell);
    }

    /**
     * Template für das Hinzufügen eines PluginElements.
     * @param plugin PluginElement.
     */
    public void addPluginTemplate(PluginElement plugin) {
        ProcessingFlowComponent cell = new ProcessingFlowComponent(plugin);
        cell.setVertex(true);

        addTemplate(plugin.getLabel(), new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/plugin.png"))), cell);
    }

    /**
     * Template für das Hinzufügen eines FusionElements.
     * @param fusion PluginElement.
     */
    public void addFusionTemplate(FusionElement fusion) {
        ProcessingFlowComponent cell = new ProcessingFlowComponent(fusion);
        cell.setVertex(true);

        addTemplate(fusion.getLabel(), new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/fusion.png"))), cell);
    }

    protected Set<MultiMediaType> set(MultiMediaType... types) {
        return new HashSet<>(List.of(types));
    }

    public void paintComponent(Graphics g) {
        if (gradientColor == null) {
            super.paintComponent(g);
        }
        else {
            Rectangle rect = getVisibleRect();
            if (g.getClipBounds() != null) {
                rect = rect.intersection(g.getClipBounds());
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(), 0, gradientColor));
            g2.fill(rect);
        }
    }

    /**
     * Methode zum Hinzufügen einer Schablone, die
     * später ausgewählt und in den Graphen gezogen
     * werden kann.
     * @param name Name der Schablone.
     * @param icon Icon/Bild der Schablone.
     * @param cell Knoten des Processing Flows, der
     *             durch die Schablone dargestellt wird.
     */
    public void addTemplate(final String name, ImageIcon icon, mxCell cell) {
        mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
        final mxGraphTransferable t = new mxGraphTransferable(new Object[] { cell }, bounds);

        // Skalieren des Bildes, wenn es zu groß ist.
        if (icon != null) {
            if (icon.getIconWidth() > 32 || icon.getIconHeight() > 32) {
                icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32, 0));
            }
        }

        // Label für einen Eintrag in der Palette.
        final JLabel entry = new JLabel(icon);
        entry.setPreferredSize(new Dimension(50, 50));
        entry.setBackground(EditorPalette.this.getBackground().brighter());
        entry.setFont(new Font(entry.getFont().getFamily(), Font.PLAIN, 10));

        entry.setVerticalTextPosition(JLabel.BOTTOM);
        entry.setHorizontalTextPosition(JLabel.CENTER);
        entry.setIconTextGap(0);

        entry.setToolTipText(name);
        entry.setText(name);

        // Aktuelle Auswahl setzen.
        entry.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setSelectionEntry(entry, t);
            }
        });

        // Handler für das Ziehen eines Knotens in den Graphen.
        DragGestureListener dragGestureListener =
                e -> e.startDrag(null, mxSwingConstants.EMPTY_IMAGE, new Point(), t, null);

        DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(entry, DnDConstants.ACTION_COPY, dragGestureListener);

        getContentPane().add(entry);
    }
}
