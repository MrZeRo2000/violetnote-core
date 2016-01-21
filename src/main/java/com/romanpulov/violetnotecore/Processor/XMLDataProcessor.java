package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteParserException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created on 20.01.2016.
 */
public abstract class XMLDataProcessor {

    protected DocumentBuilder newDocumentBuilder() throws DataReadWriteException {
        //factory
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        try {
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new DataReadWriteParserException(e.getMessage());
        }
    }
}
