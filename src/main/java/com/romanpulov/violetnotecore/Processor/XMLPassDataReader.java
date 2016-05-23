package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassDataItem;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteParserException;
import org.w3c.dom.*;

import java.io.InputStream;

/**
 * Created on 20.01.2016.
 */
public class XMLPassDataReader extends XMLPassDataProcessor{

    public PassData readStream(InputStream stream) throws DataReadWriteException {
        //create passData
        passData = new PassData();
        Document doc;
        try {
            //document
            doc = newDocumentBuilder().parse(stream);
        } catch (Exception e) {
            throw new DataReadWriteParserException(e.getMessage());
        }
        //root
        Element rootElement = doc.getDocumentElement();
        if (rootElement != null) {
            processNode(rootElement, null);
        } else {
            throw new DataReadWriteParserException("Root element not found");
        }
        return passData;
    }

    private void processNode(Node node, PassDataItem parentItem) throws DataReadWriteException {
        PassDataItem newDataItem = null;
        if (!(node.getNodeName().equals(XML_ROOT_TAG_NAME))) {
            newDataItem = NodeCreator.create(node, parentItem);
            passData.addPassDataItem(newDataItem);
        }
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            processNode(nodeList.item(i), newDataItem);
        }
    }

    private static class NodeCreator {
        private static PassDataItem create(Node node, PassDataItem parentItem) throws DataReadWriteException {
            switch (node.getNodeName()) {
                case PassCategory.ATTR_NODE_CATEGORY:
                    return createPassCategory(node, (PassCategory) parentItem);
                case PassNote.ATTR_NODE_NOTE:
                    return createPassNote(node, (PassCategory) parentItem);
                default:
                    throw new DataReadWriteParserException("Unexpected node name: " + node.getNodeName());
            }
        }

        private static String getNodeAttributeContent(NamedNodeMap nodeMap, String attrName) {
            Node namedItem = nodeMap.getNamedItem(attrName);
            if (namedItem != null)
                return namedItem.getTextContent();
            else
                return null;
        }

        private static PassCategory createPassCategory(Node node, PassCategory parentCategory) throws DataReadWriteParserException {
            NamedNodeMap nodeMap = node.getAttributes();
            if (nodeMap != null) {
                String categoryName = getNodeAttributeContent(nodeMap, PassCategory.ATTR_CATEGORY_NAME);
                if (categoryName != null)
                    return new PassCategory(categoryName, parentCategory);
                else
                    throw new DataReadWriteParserException("Category name not found");
            } else
                throw new DataReadWriteParserException("Category node attributes not found");
        }

        private static PassNote createPassNote(Node node, PassCategory parentCategory) throws DataReadWriteParserException {
            NamedNodeMap nodeMap = node.getAttributes();
            if (nodeMap != null) {
                return new PassNote(parentCategory,
                        getNodeAttributeContent(nodeMap, PassNote.ATTR_SYSTEM),
                        getNodeAttributeContent(nodeMap, PassNote.ATTR_USER),
                        getNodeAttributeContent(nodeMap, PassNote.ATTR_PASSWORD),
                        getNodeAttributeContent(nodeMap, PassNote.ATTR_COMMENTS),
                        getNodeAttributeContent(nodeMap, PassNote.ATTR_CUSTOM),
                        getNodeAttributeContent(nodeMap, PassNote.ATTR_INFO)
                        );
            } else
                throw new DataReadWriteParserException("Note node attributes not found");
        }
    }
}
