package gcat.main;

import gcat.editor.view.EditorMainFrame;
import gcat.editor.view.splash.SplashScreen;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditorMainFrame::new);
    }
}
