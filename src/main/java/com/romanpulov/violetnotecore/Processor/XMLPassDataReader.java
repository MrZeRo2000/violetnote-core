package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

/**
 * Created on 20.01.2016.
 */
public class XMLPassDataReader extends XMLPassDataProcessor{

    public XMLPassDataReader(PassData passData) {
        super(passData);
    }

    public PassData readStream(InputStream stream) throws DataReadWriteException {
        //document
        Document doc = newXMLDocument();
        //root
        Element rootElement = doc.getDocumentElement();
        if (rootElement != null) {
            processNode(rootElement);
        } else {
            throw new DataReadWriteParserException("Root element not found");
        }
        return passData;
    }

    private void processNode(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            processNode(nodeList.item(i));
        }
    }
}
