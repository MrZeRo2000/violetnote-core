package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteParserException;
import org.w3c.dom.Document;

import javax.swing.text.html.parser.Parser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created on 19.01.2016.
 */
public class XMLPassDataWriter {
    private PassData passData;

    public XMLPassDataWriter(PassData passData) {
        this.passData = passData;
    }

    private Document newXMLDocument() throws DataReadWriteException {
        //factory
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            return docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new DataReadWriteParserException(e.getMessage());
        }
    }

    public void write() {

    }
}
