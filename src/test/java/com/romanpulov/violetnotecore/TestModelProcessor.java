package com.romanpulov.violetnotecore;

import static org.junit.jupiter.api.Assertions.*;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.PinsDataReader;
import com.romanpulov.violetnotecore.Processor.PinsDataWriter;
import com.romanpulov.violetnotecore.Processor.XMLPassDataReader;
import com.romanpulov.violetnotecore.Processor.XMLPassDataWriter;
import org.junit.jupiter.api.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestModelProcessor {
    private final static Logger logger = Logger.getLogger(TestModelProcessor.class.getName());

    private static final String TEST_CSV_LF_FILE_NAME = TestConfiguration.resolveTestFileName("pins_example.csv");
    private static final String TEST_CSV_LF_OUR_FILE_NAME = TestConfiguration.resolveTestFileName("pins_example_out.csv");
    private static final String TEST_CSV_FILE_NAME = TestConfiguration.resolveTestFileName("pins_example_nolf.csv");
    private static final String TEST_OUT_XML_FILE_NAME = TestConfiguration.resolveTestFileName("out_xml_test.xml");
    private static final String TEST_OUT_VNF_FILE_NAME = TestConfiguration.resolveTestFileName("out_vnf_test.xml");
    private static final String TEST_PASSWORD = "Pass1";

    @AfterAll
    public static void afterAll() {
        TestUtils.deleteFileIfExists(TEST_OUT_XML_FILE_NAME);
        TestUtils.deleteFileIfExists(TEST_OUT_VNF_FILE_NAME);
    }

    @Test
    @Disabled
    public void pinsLoadTest() throws Exception {
        PinsDataReader pinsReader = new PinsDataReader();
        try {
            PassData pd = pinsReader.readStream(new FileInputStream(TEST_CSV_FILE_NAME));

            System.out.println("PassCategory:");
            for (PassCategory c : pd.getPassCategoryList()) {
                System.out.println(c.toString());
            }

            System.out.println("PassNote:");
            for (PassNote n : pd.getPassNoteList()) {
                System.out.println(n.toString());
            }

        } catch (DataReadWriteException e) {
            fail("DataReadWriteException:" + e.getMessage());
            logger.log(Level.WARNING, "DataReadWriteException:" + e.getMessage(), e);
        }
    }

    @Test
    @Order(1)
    public void pinsSaveTest() throws Exception {
        PinsDataReader pinsReader = new PinsDataReader();

        try (
                FileInputStream fileInputStream = new FileInputStream(TEST_CSV_LF_FILE_NAME);
                FileOutputStream fileOutputStream = new FileOutputStream(TEST_CSV_LF_OUR_FILE_NAME)
                ) {
            PassData pd = pinsReader.readStream(fileInputStream);
            PinsDataWriter pinsWriter = new PinsDataWriter();
            pinsWriter.writeStream(fileOutputStream, pd);

            PassData pd1 = (new PinsDataReader()).readStream(new FileInputStream(TEST_CSV_LF_OUR_FILE_NAME));

            //compare
            assertTrue(pd.getPassNoteList().containsAll(pd1.getPassNoteList()));
            assertTrue(pd1.getPassNoteList().containsAll(pd.getPassNoteList()));
        }
    }

    @Test
    @Disabled
    public void xmlCreateTest() throws Exception {
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
        TestUtils.deleteFileIfExists(TEST_OUT_XML_FILE_NAME);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult resultFile = new StreamResult(new File(TEST_OUT_XML_FILE_NAME));

        // Output to console for testing
        StreamResult resultConsole = new StreamResult(System.out);

        transformer.transform(source, resultFile);
        transformer.transform(source, resultConsole);
    }

    @Test
    @Order(2)
    public void xmlPassDataWriterTest() throws  Exception{
        // load something
        PinsDataReader pinsReader = new PinsDataReader();
        PassData pd = null;
        try {
            pd = pinsReader.readStream(new FileInputStream(TEST_CSV_FILE_NAME));
        } catch (DataReadWriteException e) {
            fail("PinsDataReader DataReadWriteException:" + e.getMessage());
            logger.log(Level.WARNING, "PinsDataReader DataReadWriteException:" + e.getMessage(), e);
        }
        //write PassData

        // write the content into xml file
        TestUtils.deleteFileIfExists(TEST_OUT_XML_FILE_NAME);

        try (
                FileOutputStream fileOutputStream = new FileOutputStream(TEST_OUT_XML_FILE_NAME);
                OutputStream stream = new BufferedOutputStream(fileOutputStream)
                ) {
            XMLPassDataWriter writer = new XMLPassDataWriter(pd);
            try {
                writer.writeStream(stream);
            } catch (DataReadWriteException e) {
                fail("XMLPassDataWriter DataReadWriteException:" + e.getMessage());
                logger.log(Level.WARNING, "XMLPassDataWriter DataReadWriteException:" + e.getMessage(), e);
            }
        }
    }

    private void processNode(Node node) {
        System.out.println("Node name = " + node.getNodeName());
        NamedNodeMap nodeMap = node.getAttributes();
        for (int i=0; i<nodeMap.getLength(); i++) {
            System.out.println("Attr Node name = " + nodeMap.item(i).getNodeName());
            System.out.println("Attr Text Content = " + nodeMap.item(i).getTextContent());
        }
        Node userNode = nodeMap.getNamedItem("user");
        if (userNode != null)
            System.out.println("Named attribute user:" + userNode.getTextContent());

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            processNode(nodeList.item(i));
        }
    }

    @Test
    @Disabled
    public void xmlReadTest() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new FileInputStream(TEST_OUT_XML_FILE_NAME));
        Element element = doc.getDocumentElement();
        if (element != null) {
            processNode(element);
        }
    }

    @Test
    @Disabled
    public void xmlPassDataReaderTest() throws Exception{
        XMLPassDataReader reader = new XMLPassDataReader();
        PassData passData= reader.readStream(new FileInputStream(TEST_OUT_XML_FILE_NAME));
        System.out.println("Categories:" + passData.getPassCategoryList().size() + ", notes:" + passData.getPassNoteList().size());
    }

    @Test
    @Order(3)
    public void PassCategoryEqualsTest() {
        // category without parent
        PassCategory c1 = new PassCategory("cat1");
        PassCategory c2 = new PassCategory("cat1");
        List<PassCategory> list1 = List.of(c1);
        List<PassCategory> list2 = List.of(c2);
        assertEquals(c1, c2);
        assertTrue(list1.containsAll(list2));
        //category with parent
        PassCategory p1 = new PassCategory("parentcat1");
        PassCategory p2 = new PassCategory("parentcat2");
        c1.setParentCategory(p1);
        assertNotEquals(c1, c2);
        c2.setParentCategory(p1);
        assertEquals(c1, c2);
        c2.setParentCategory(p2);
        assertNotEquals(c1, c2);
    }

    @Test
    public void PassNoteContainsTest() {
        PassNote n1 = new PassNote(null, null, "user1", "password1", null, null, null);
        PassNote n2 = new PassNote(null, null, "user1", "password1", null, null, null);
        assertEquals(n1, n2);
    }

    private PassData getPINSPassData()  throws Exception {
        PassData pd = null;
        try {
            pd =  new PinsDataReader().readStream(new FileInputStream(TEST_CSV_FILE_NAME));
        } catch (DataReadWriteException e) {
            fail("PinsDataReader DataReadWriteException:" + e.getMessage());
            logger.log(Level.WARNING, "PinsDataReader DataReadWriteException:" + e.getMessage(), e);
        }
        return pd;
    }

    @Test
    @Order(4)
    public void XMLReadWriteEquivalenceTest() throws Exception {
        // get something to PassData
        PassData pd = getPINSPassData();

        //write to test output file
        XMLPassDataWriter writer = new XMLPassDataWriter(pd);
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(TEST_OUT_XML_FILE_NAME)
                ) {
            writer.writeStream(fileOutputStream);
        } catch (DataReadWriteException e) {
            fail("XMLPassDataWriter DataReadWriteException:" + e.getMessage());
            logger.log(Level.WARNING, "XMLPassDataWriter DataReadWriteException:" + e.getMessage(), e);
        }

        //read from test output file to another PassData
        XMLPassDataReader reader = new XMLPassDataReader();
        PassData pd1= reader.readStream(new FileInputStream(TEST_OUT_XML_FILE_NAME));

        //compare
        assertTrue(pd.getPassCategoryList().containsAll(pd1.getPassCategoryList()));
        assertTrue(pd1.getPassCategoryList().containsAll(pd.getPassCategoryList()));
        assertTrue(pd.getPassNoteList().containsAll(pd1.getPassNoteList()));
        assertTrue(pd1.getPassNoteList().containsAll(pd.getPassNoteList()));
    }

    @Test
    @Order(5)
    public void CryptXMLReadWriteEquivalenceTest() throws Exception {
        // get something to PassData
        PassData pd = getPINSPassData();

        //write to crypt output file
        XMLPassDataWriter writer = new XMLPassDataWriter(pd);
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(TEST_OUT_VNF_FILE_NAME);
                OutputStream output = (new AESCryptService()).generateCryptOutputStream(fileOutputStream, TEST_PASSWORD)
        ) {
            writer.writeStream(output);
        }  catch (DataReadWriteException e) {
            fail("XMLPassDataWriter DataReadWriteException:" + e.getMessage());
            logger.log(Level.WARNING, "XMLPassDataWriter DataReadWriteException:" + e.getMessage(), e);
        }

        try (
                FileInputStream fileInputStream = new FileInputStream(TEST_OUT_VNF_FILE_NAME);
                InputStream input = (new AESCryptService()).generateCryptInputStream(fileInputStream, TEST_PASSWORD)
                ) {

            //read from crypt output file to another PassData
            XMLPassDataReader reader = new XMLPassDataReader();
            PassData pd1= reader.readStream(input);

            //compare
            assertTrue(pd.getPassCategoryList().containsAll(pd1.getPassCategoryList()));
            assertTrue(pd1.getPassCategoryList().containsAll(pd.getPassCategoryList()));
            assertTrue(pd.getPassNoteList().containsAll(pd1.getPassNoteList()));
            assertTrue(pd1.getPassNoteList().containsAll(pd.getPassNoteList()));
        }
    }

    @Test
    public void TestPassNoteGetNoteAttr() {
        PassNote n1 = new PassNote(null, "System1", "user1", "password1", null, null, null);
        Map<String, String> a1 = n1.getNoteAttr();
        int i = 1;
        for (String s: a1.keySet()) {
            switch (i++) {
                case 1:
                    assertEquals(PassNote.ATTR_SYSTEM, s);
                    break;
                case 2:
                    assertEquals(PassNote.ATTR_USER, s);
                    break;
                case 3:
                    assertEquals(PassNote.ATTR_PASSWORD, s);
                    break;
                case 4:
                    assertEquals(PassNote.ATTR_COMMENTS, s);
                    break;
                case 5:
                    assertEquals(PassNote.ATTR_CUSTOM, s);
                    break;
                case 6:
                    assertEquals(PassNote.ATTR_INFO, s);
                    break;
            }
        }
    }
}
