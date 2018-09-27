package org.jrd3.engine.core.utils;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * XML utility functions.
 *
 * @author Ray1184
 * @version 1.0
 */
public class XMLUtils {

    /**
     * Gets XML doc element.
     *
     * @param path The XML path.
     * @return The doc element.
     * @throws JRD3Exception
     */
    public static Element getDocElement(String path) throws JRD3Exception {

        File fXmlFile = new File(path.replaceAll("%20", " "));
        if (!fXmlFile.exists()) {
            throw new JRD3Exception("XML File not found at: " + path);
        }

        //InputStream inputStream= new FileInputStream(path);
        //Reader reader = new InputStreamReader(inputStream,"UTF-8");
        //InputSource is = new InputSource(reader);
        //is.setEncoding("UTF-8");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
        } catch (ParserConfigurationException e) {
            throw new JRD3Exception(e);
        } catch (SAXException e2) {
            throw new JRD3Exception(e2);
        } catch (IOException e3) {
            throw new JRD3Exception(e3);
        }
        doc.getDocumentElement().normalize();
        return doc.getDocumentElement();

    }
}
