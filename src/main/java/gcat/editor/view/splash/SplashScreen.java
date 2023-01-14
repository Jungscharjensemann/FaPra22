package gcat.editor.view.splash;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SplashScreen extends JDialog {

    public SplashScreen() {
        setUndecorated(true);
        setLayout(new MigLayout("debug", "[center]"));

        add(new JLabel("Test"), "cell 0 0, growx");
        setVisible(true);
    }
}
