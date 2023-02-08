package gcat.editor.controller;

import de.swa.gmaf.api.GMAFFacade;
import de.swa.gmaf.api.GMAFFacadeSOAPImplService;
import de.swa.gmaf.api.Mmfg;
import de.swa.gmaf.ui.GMAFUIFacade;
import de.swa.gmaf.ui.GMAFUIFacadeImplService;

import javax.swing.*;
import javax.xml.ws.WebServiceException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller zum Exportieren eines
 * Processing Flows in eine laufende
 * GMAF-Instanz. (Future Work)
 */
public class ExportGMAFController implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        /*GMAFFacadeSOAPImplService service = new GMAFFacadeSOAPImplService();
        GMAFFacade facade = service.getGMAFFacadeSOAPImplPort();
        System.out.println("Api-Facade: " + facade.getAuthToken("fp2223"));

        GMAFUIFacadeImplService service1 = new GMAFUIFacadeImplService();
        GMAFUIFacade facade1 = service1.getGMAFUIFacadeImplPort();
        System.out.println("Ui-Facade: " + facade1.getAuthToken("fp2223"));*/

        GMAFFacadeSOAPImplService apiService;
        GMAFUIFacadeImplService uiService;
        try {
            apiService = new GMAFFacadeSOAPImplService();
            uiService = new GMAFUIFacadeImplService();

            GMAFFacade apiFacade = apiService.getGMAFFacadeSOAPImplPort();
            GMAFUIFacade uiFacade = uiService.getGMAFUIFacadeImplPort();

            String auth_token = apiFacade.getAuthToken("fp2223");

            System.out.println("Api-Facade: " + auth_token);
            //System.out.println("Ui-Facade: " + uiFacade.getAuthToken("fp2223"));

            Mmfg mmfg = apiFacade.processAssetFromFile(auth_token, "C:\\Users\\Jens\\IdeaProjects\\FaPra22\\collection\\quokka-froehlich.jpeg");
            mmfg.getAllNodes().forEach(n -> System.out.println(n.getName()));
        } catch(WebServiceException wex) {
            JOptionPane.showMessageDialog(null, wex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }

        //TODO (future work): Exportieren eines Processing Flows
        // in eine laufende GMAF-Instanz.
    }
}
