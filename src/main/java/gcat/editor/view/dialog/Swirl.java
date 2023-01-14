package gcat.editor.view.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Swirl extends JPanel {

    public Swirl() {
        setLayout(new BorderLayout());
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/swirl.png")));
        icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32, 0));
        JLabel swirl = new JLabel(icon);
        swirl.setPreferredSize(new Dimension(64, 64));
        swirl.setVerticalTextPosition(JLabel.BOTTOM);
        swirl.setHorizontalTextPosition(JLabel.CENTER);
        swirl.setIconTextGap(10);
        swirl.setText("Drehwurm!");
        add(swirl, BorderLayout.CENTER);
    }
}
