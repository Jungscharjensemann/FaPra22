package gcat.main;

import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.splash.SplashScreen;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditorMainFrame::new);
    }
}
