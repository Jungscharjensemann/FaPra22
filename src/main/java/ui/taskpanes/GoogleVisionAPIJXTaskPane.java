package ui.taskpanes;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTaskPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GoogleVisionAPIJXTaskPane extends JXTaskPane {

    public GoogleVisionAPIJXTaskPane() {
        super("Google Vision API");
        setAnimated(true);

        List<JButton> buttons = new ArrayList<>();

        JButton b1 = new JButton("DominantColorDetection");
        JButton b2 = new JButton("ImageTextDetection");
        JButton b3 = new JButton("FaceDetection");
        JButton b4 = new JButton("LabelDetection");
        JButton b5 = new JButton("LandmarkDetection");
        JButton b6 = new JButton("LogoDetection");
        JButton b7 = new JButton("MoodDetection");
        JButton b8 = new JButton("ObjectDetection");

        buttons.add(b1);
        buttons.add(b2);
        buttons.add(b3);
        buttons.add(b4);
        buttons.add(b5);
        buttons.add(b6);
        buttons.add(b7);
        buttons.add(b8);

        setLayout(new MigLayout("", "[grow]", "[]".repeat(buttons.size())));

        for(int i = 0; i < buttons.size(); i++) {
            add(buttons.get(i), "cell 0 " + i + ", growx");
        }
    }
}
