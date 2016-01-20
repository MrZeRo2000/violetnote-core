package com.romanpulov.violetnotecore;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.PinsDataReader;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import java.io.File;


public class Test1 {

    @Test
    public void method1() {
        assertEquals(1, 1);
    }

    public void pinsLoadTest() {
        String fileName = "data\\pins_example.csv";
        PinsDataReader loader = new PinsDataReader();
        try {
            loader.loadFromFile(fileName);

            System.out.println("PassCategory:");
            for (PassCategory c : loader.getPassCategoryList()) {
                System.out.println(c.toString());
            }

            System.out.println("PassNote:");
            for (PassNote n : loader.getPassNoteList()) {
                System.out.println(n.toString());
            }

        } catch (DataReadWriteException e) {
            fail("DataReadWriteException:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void xmlCreateTest() throws Exception {
        String fileName = "data\\out_xml_test.xml";

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();

        //document
        Document doc = docBuilder.newDocument();

        //root
        Element rootElement = doc.createElement("root");
        doc.appendChild(rootElement);

        //node types

        //child category
        Element categoryElement1 = doc.createElement("category");
        categoryElement1.setAttribute("name", "category1 name");
        rootElement.appendChild(categoryElement1);
        Element categoryElement2 = doc.createElement("category");
        categoryElement2.setAttribute("name", "category2 name");
        rootElement.appendChild(categoryElement2);
        Element categoryElement3 = doc.createElement("category");
        categoryElement3.setAttribute("name", "category3 name");
        categoryElement2.appendChild(categoryElement3);


        //element 1 will have nodes
        Element nodeElement1 = doc.createElement("node");
        nodeElement1.setAttribute("system", "email");
        nodeElement1.setAttribute("user", "user1");
        nodeElement1.setAttribute("password", "password1");
        categoryElement1.appendChild(nodeElement1);

        //element 3 will have nodes
        Element nodeElement3 = doc.createElement("node");
        nodeElement3.setAttribute("system", "email 3");
        nodeElement3.setAttribute("user", "user3");
        nodeElement3.setAttribute("password", "password13");
        categoryElement3.appendChild(nodeElement3);

        // write the content into xml file
        File f = new File(fileName);
        if (f.exists())
            f.delete();



        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult resultFile = new StreamResult(f);

        // Output to console for testing
        StreamResult resultConsole = new StreamResult(System.out);

        transformer.transform(source, resultFile);
        transformer.transform(source, resultConsole);
    }
}
