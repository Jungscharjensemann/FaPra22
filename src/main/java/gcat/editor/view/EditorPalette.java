package gcat.editor.view;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxRectangle;
import gcat.editor.graph.processingflow.components.media.MultiMediaType;
import gcat.editor.graph.processingflow.components.asset.AssetElement;
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
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    }

    public void clearSelection()
    {
        setSelectionEntry(null, null);
    }

    /**
     *
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

        eventSource.fireEvent(new mxEventObject(mxEvent.SELECT, "entry",
                selectedEntry, "transferable", t, "previous", previous));
    }

    public void addFolderTemplate(AssetElement collection) {
        ProcessingFlowComponent cell = new ProcessingFlowComponent(collection);
        cell.setVertex(true);

        addTemplate(collection.getLabel(),
                new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(String.format("images/%s", collection.getIcon())))), cell);
    }

    public void addPluginTemplate(PluginElement plugin) {
        ProcessingFlowComponent cell = new ProcessingFlowComponent(plugin);
        cell.setVertex(true);

        addTemplate(plugin.getLabel(), new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/plugin.png"))), cell);
    }

    public void addFusionTemplate(FusionElement fusion) {
        ProcessingFlowComponent cell = new ProcessingFlowComponent(fusion);
        cell.setVertex(true);

        addTemplate(fusion.getLabel(), new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/fusion.png"))), cell);
    }

    protected Set<MultiMediaType> set(MultiMediaType... types) {
        return new HashSet<>(List.of(types));
    }

    public void paintComponent(Graphics g)
    {
        if (gradientColor == null)
        {
            super.paintComponent(g);
        }
        else
        {
            Rectangle rect = getVisibleRect();

            if (g.getClipBounds() != null)
            {
                rect = rect.intersection(g.getClipBounds());
            }

            Graphics2D g2 = (Graphics2D) g;

            g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(), 0,
                    gradientColor));
            g2.fill(rect);
        }
    }

    public void addTemplate(final String name, ImageIcon icon, mxCell cell)
    {
        mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
        final mxGraphTransferable t = new mxGraphTransferable(
                new Object[] { cell }, bounds);

        // Scales the image if it's too large for the library
        if (icon != null)
        {
            if (icon.getIconWidth() > 32 || icon.getIconHeight() > 32)
            {
                icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32,
                        0));
            }
        }

        final JLabel entry = new JLabel(icon);
        entry.setPreferredSize(new Dimension(50, 50));
        entry.setBackground(EditorPalette.this.getBackground().brighter());
        entry.setFont(new Font(entry.getFont().getFamily(), Font.PLAIN, 10));

        entry.setVerticalTextPosition(JLabel.BOTTOM);
        entry.setHorizontalTextPosition(JLabel.CENTER);
        entry.setIconTextGap(0);

        entry.setToolTipText(name);
        entry.setText(name);

        entry.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setSelectionEntry(entry, t);
            }


        });

        // Install the handler for dragging nodes into a graph
        DragGestureListener dragGestureListener = new DragGestureListener()
        {
            /**
             *
             */
            public void dragGestureRecognized(DragGestureEvent e)
            {
                e.startDrag(null, mxSwingConstants.EMPTY_IMAGE, new Point(), t, null);
            }

        };

        DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(entry, DnDConstants.ACTION_COPY, dragGestureListener);

        getContentPane().add(entry);
    }
}
