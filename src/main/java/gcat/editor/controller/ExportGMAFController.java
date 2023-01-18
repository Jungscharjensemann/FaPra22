package gcat.editor.controller;

import de.swa.gmaf.api.*;
import de.swa.gmaf.ui.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExportGMAFController implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        GMAFFacadeSOAPImplService service = new GMAFFacadeSOAPImplService();
        GMAFFacade facade = service.getGMAFFacadeSOAPImplPort();
        System.out.println("Api-Facade: " + facade.getAuthToken("fp2223"));

        GMAFUIFacadeImplService service1 = new GMAFUIFacadeImplService();
        GMAFUIFacade facade1 = service1.getGMAFUIFacadeImplPort();
        System.out.println("Ui-Facade: " + facade1.getAuthToken("fp2223"));

        //TODO (future work): Exportieren eines Processing Flows
        // in eine laufende GMAF-Instanz.
    }
}
