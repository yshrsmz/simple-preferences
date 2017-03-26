package net.yslibrary.simplepreferences.processor.writer;

import net.yslibrary.simplepreferences.processor.KeyAnnotatedField;
import net.yslibrary.simplepreferences.processor.PreferenceAnnotatedClass;
import net.yslibrary.simplepreferences.processor.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Write the resources to a String resource file.
 *
 * @author Niko Strijbol
 */
public class PreferenceResourceWriter {

    private final List<PreferenceAnnotatedClass> annotatedClasses;

    private Document xmlDocument;

    public PreferenceResourceWriter(List<PreferenceAnnotatedClass> annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
    }

    public void write() throws ParserConfigurationException {

        // Make the document
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = docFactory.newDocumentBuilder();

        Document document = builder.newDocument();
        Element resources = document.createElement("resources");

        document.appendChild(resources);

        for(PreferenceAnnotatedClass annotatedClass: annotatedClasses) {

            for (KeyAnnotatedField field : annotatedClass.keys) {
                Element preference = document.createElement("string");

                String keyName;
                if (annotatedClass.useDefaultPreferences()) {
                    keyName = "";
                } else {
                    keyName = annotatedClass.preferenceName + "_";
                }

                preference.setAttribute("name", annotatedClass.resourcePrefix + keyName + Utils.lowerCamelToLowerSnake(field.name));
                preference.setAttribute("translatable", "false");
                preference.setTextContent(field.preferenceKey);

                resources.appendChild(preference);

                System.out.println("Added key: " + keyName);
            }
        }

        xmlDocument = document;
    }

    /**
     * Write the xml to a file.
     *
     * @param file The file to write the XML to.
     *
     * @throws TransformerException
     * @throws IOException
     */
    public void toFile(File file) throws TransformerException, IOException {
        Objects.nonNull(xmlDocument);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(xmlDocument);

        try(FileWriter writer = new FileWriter(file)) {
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
        }
    }
}