package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteIOException;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.text.html.parser.Parser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * Writes PassData to Result
     * @param result
     * @throws DataReadWriteException
     */
    public void writeResult(Result result) throws DataReadWriteException {
        //document
        Document doc = newXMLDocument();
        //root
        Element rootElement = doc.createElement("root");
        doc.appendChild(rootElement);
        //compose
        composeDocument(doc, rootElement);
        //write
        writeDocument(doc, result);
    }

    private void composeDocument(Document doc, Element rootElement) {
        Map<PassCategory, Element> categoryElementMap = new HashMap<>();
        Element categoryElement;
        for (PassNote passNote : passData.getPassNoteList()) {
            PassCategory category = passNote.getPassCategory();
            categoryElement = categoryElementMap.get(category);
            if (categoryElement == null) {
                categoryElement = composeCategoryElement(doc, rootElement, category, categoryElementMap);
            }
            Element noteElement = doc.createElement(PassNote.XML_TAG_NAME);
            noteElement.setAttribute(PassNote.XML_TAG_ATTR_SYSTEM, passNote.getSystem());
            noteElement.setAttribute(PassNote.XML_TAG_ATTR_USER, passNote.getUser());
            noteElement.setAttribute(PassNote.XML_TAG_ATTR_PASSWORD, passNote.getPassword());
            noteElement.setAttribute(PassNote.XML_TAG_ATTR_COMMENTS, passNote.getComments());
            noteElement.setAttribute(PassNote.XML_TAG_ATTR_CUSTOM, passNote.getCustom());
            noteElement.setAttribute(PassNote.XML_TAG_ATTR_INFO, passNote.getInfo());
            categoryElement.appendChild(noteElement);
        }
    }

    private Element composeCategoryElement(Document doc, Element rootElement, PassCategory category, Map<PassCategory, Element> categoryElementMap) {
        //create new element
        Element categoryElement = doc.createElement(PassCategory.XML_TAG_NAME);
        categoryElement.setAttribute(PassCategory.XML_ATTR_CATEGORY_NAME, category.getCategoryName());
        categoryElementMap.put(category, categoryElement);
        if (category.getParentCategory() == null)
            rootElement.appendChild(categoryElement);
        else {
            Element parentElement = categoryElementMap.get(category.getParentCategory());
            if (parentElement == null) {
                parentElement = composeCategoryElement(doc, rootElement, category.getParentCategory(), categoryElementMap);
                parentElement.appendChild(categoryElement);
            }
        }
        return categoryElement;
    }

    private void writeDocument(Document doc, Result result) throws DataReadWriteException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new DataReadWriteIOException(e.getMessage());
        }
    }
}
