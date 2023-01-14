package main;

import de.swa.gmaf.api.GMAFFacade;
import de.swa.gmaf.api.GMAFFacadeSOAPImplService;
import de.swa.gmaf.api.GraphCode;
import de.swa.gmaf.api.Mmfg;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*GMAFFacade facade = new GMAFFacadeSOAPImplService().getPort(GMAFFacade.class);
        String auth_token = facade.getAuthToken("");

        String url = "C:\\Users\\Jens\\IdeaProjects\\FaPra22\\collection\\impsum.txt";

        Mmfg test = facade.processAssetFromFile(auth_token, url);

        List<String> plugins = new ArrayList<>();
        plugins.add("de.swa.gmaf.plugin.text.BagOfWordsDetection");
        facade.setProcessingPlugins(auth_token, plugins);

        System.out.println(test);*/

        SwingUtilities.invokeLater(MainFrame::new);
    }
}
