package gcat.editor.controller;

import de.swa.gmaf.api.GMAFFacade;
import de.swa.gmaf.api.GMAFFacadeSOAPImplService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExportGMAFController implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        GMAFFacadeSOAPImplService service = new GMAFFacadeSOAPImplService();
        GMAFFacade facade = service.getGMAFFacadeSOAPImplPort();
        facade.getAuthToken("fp2223");

        //TODO (future work): Exportieren eines Processing Flows
        // in eine laufende GMAF-Instanz.
    }
}
