package ui.taskpanes;

import org.jdesktop.swingx.JXTaskPane;

import javax.swing.*;
import java.awt.*;

public class CustomJXTaskPane extends JXTaskPane {

    public CustomJXTaskPane(String title) {
        super(title);
        setAnimated(false);
        getContentPane().setLayout(new GridBagLayout());
    }

    private void initComponents() {
        JButton btnNewButton = new JButton("New button");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton.gridx = 0;
        gbc_btnNewButton.gridy = 0;
        getContentPane().add(btnNewButton, gbc_btnNewButton);

        JButton btnNewButton_1 = new JButton("New button");
        GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
        gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButton_1.gridx = 1;
        gbc_btnNewButton_1.gridy = 0;
        getContentPane().add(btnNewButton_1, gbc_btnNewButton_1);

        JButton btnNewButton_2 = new JButton("New button");
        GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
        gbc_btnNewButton_2.insets = new Insets(0, 0, 0, 5);
        gbc_btnNewButton_2.gridx = 0;
        gbc_btnNewButton_2.gridy = 1;
        getContentPane().add(btnNewButton_2, gbc_btnNewButton_2);

        JButton btnNewButton_3 = new JButton("New button");
        GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
        gbc_btnNewButton_3.gridx = 1;
        gbc_btnNewButton_3.gridy = 1;
        getContentPane().add(btnNewButton_3, gbc_btnNewButton_3);
    }
}
