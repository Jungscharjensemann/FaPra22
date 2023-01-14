package gcat.editor.view.table.model.cell;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import gcat.editor.view.table.renderer.CellParameterTableModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.List;

public class CellComponent extends mxCell {

    private List<MultiMediaType> input, output;

    private String name, classAddress;

    public CellComponent(Object value, mxGeometry geometry, String style, String name) {
        super(value, geometry, style);
        this.name = name;
    }

    enum MultiMediaType {
        TXT, MP4,

        MMFG
    }

    public List<MultiMediaType> getInputMultiMediaType() {
        return input;
    }

    public List<MultiMediaType> getOutputMultiMediaType() {
        return output;
    }

    public CellParameterTableModel getCellTableModel() {
        CellParameterTableModel cellParameterTableModel = new CellParameterTableModel();
        cellParameterTableModel.addRow(new Object[] {"Id", getId()});
        cellParameterTableModel.addRow(new Object[] {"Value", getValue()});
        cellParameterTableModel.addRow(new Object[] {"Edge Count", getEdgeCount()});
        System.out.println(cellParameterTableModel.getDataVector().toString());
        return cellParameterTableModel;
    }

    protected Element createElement() {
        HashMap<String, String> attrs = new HashMap<>();
        attrs.put("Attr1", "Wert1");
        return createElement(name, attrs);
    }

    protected Element createElement(String name, HashMap<String, String> attrs) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
        Element element = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            element = document.createElement(name);
            attrs.forEach(element::setAttribute);
        } catch(ParserConfigurationException e) {
            e.printStackTrace();
        }
        return element;
    }
}
