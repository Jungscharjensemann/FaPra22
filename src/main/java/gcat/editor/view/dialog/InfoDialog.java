package gcat.editor.view.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class InfoDialog extends JDialog {

    public InfoDialog() {
        InfoPanel panel = new InfoPanel();
        getContentPane().add(panel);
        setUndecorated(true);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                dispose();
            }
        });
    }

    static class InfoPanel extends JPanel {

        private final Image image;

        private final Color blue = new Color(44, 170, 225);
        private final Color orange = new Color(225, 154, 44);

        private final int width = 740;
        private final int height = 440;

        private final JLabel link;

        private final String url = "http://www.stefan-wagenpfeil.de/";

        public InfoPanel() {
            setLayout(null);
            image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/gcat.png"))).getImage();
            link = new JLabel("Dr. Stefan Wagenpfeil");
            link.setForeground(Color.blue.darker());
            link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            link.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (IOException | URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    link.setText(String.format("<html><a href=\"%s\">%s</a></html>", url, "Dr. Stefan Wagenpfeil"));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    link.setText("Dr. Stefan Wagenpfeil");
                }
            });
            add(link);
            link.setBounds(getInsets().left + 70, getInsets().top + 414, link.getPreferredSize().width, link.getPreferredSize().height);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(blue);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.white);
            g.fillRect(0, 80, width, 5);
            g.drawImage(image, -25, 0, null);
            g.setColor(Color.white);
            g.setFont(new Font("Verdana", Font.PLAIN, 80));
            g.drawString("MAF", 70, 75);
            g.setColor(orange);
            g.drawString("C",10, 190);
            g.setColor(Color.white);
            g.drawString("onfiguration and", 68, 190);
            g.setColor(orange);
            g.drawString("A", 10, 270);
            g.setColor(Color.white);
            g.drawString("dministration", 68, 270);
            g.setColor(orange);
            g.drawString("T", 10, 350);
            g.setColor(Color.white);
            g.drawString("ool", 58, 350);
            g.fillRect(0, 370, width, 60);
            g.setFont(new Font("Verdana", Font.PLAIN, 15));
            g.setColor(Color.black);
            g.drawString("© 2023 Jens Andreß", 10, 389);
            g.setFont(new Font("Verdana", Font.PLAIN, 11));
            g.drawString("Entwickelt im Rahmen des Fachpraktikums WS22/23", 10, 410);
            g.drawString("Betreuer: ", 10, 425);
        }
    }
}
