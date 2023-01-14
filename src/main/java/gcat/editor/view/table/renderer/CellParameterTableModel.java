package gcat.editor.view.table.renderer;

import javax.swing.table.DefaultTableModel;

public class CellParameterTableModel extends DefaultTableModel {

    public CellParameterTableModel() {
        setColumnIdentifiers(new String[] {"Parameter", "Wert"});
    }
}
