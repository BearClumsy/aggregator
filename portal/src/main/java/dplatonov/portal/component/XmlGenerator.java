package dplatonov.portal.component;

import dplatonov.portal.entity.ScannerConfigCutoff;
import dplatonov.portal.entity.ScannerConfigs;
import dplatonov.portal.entity.ScannerPreview;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class XmlGenerator {

  public void generateXml(List<ScannerPreview> previews, String fileName,
      ScannerConfigCutoff scannerConfigCutoff, ScannerConfigs scannerConfigs)
      throws ParserConfigurationException, TransformerException {
    Document document = createXml(previews, scannerConfigCutoff, scannerConfigs);

    writeXml(document, fileName);
  }

  private Document createXml(List<ScannerPreview> previews,
      ScannerConfigCutoff scannerConfigCutoff, ScannerConfigs scannerConfigs)
      throws ParserConfigurationException {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

    // root elements
    Document doc = docBuilder.newDocument();
    Element rootElement = doc.createElement("report");
    doc.appendChild(rootElement);

    previews.forEach(preview -> {
      Element scanner = doc.createElement("scanner");
      rootElement.appendChild(scanner);
      rootElement.setAttribute("id", preview.getScannerId().toString());

      Element name = doc.createElement("name");
      name.setTextContent(scannerConfigs.getName());
      scanner.appendChild(name);

      Element url = doc.createElement("url");
      url.setTextContent(scannerConfigs.getName());
      scanner.appendChild(url);

      StringBuilder sb = new StringBuilder();
      scannerConfigs.getScannerSteps().forEach(step -> {
        String tag = step.getTag();
        String action = step.getAction();
        sb.append("<");
        sb.append(tag);
        sb.append(">");
        sb.append(action);
        sb.append(";");
      });
      Element steps = doc.createElement("steps");
      steps.setTextContent(sb.toString());
      scanner.appendChild(steps);

      Element startDate = doc.createElement("startDate");
      startDate.setTextContent(scannerConfigCutoff.getStartDate().toString());
      scanner.appendChild(startDate);

      Element finishDate = doc.createElement("finishDate");
      finishDate.setTextContent(scannerConfigCutoff.getStopDate().toString());
      scanner.appendChild(finishDate);

      Element value = doc.createElement("value");
      value.setTextContent(preview.getValue());
      scanner.appendChild(value);
    });

    // print XML to system console
    return doc;
  }

  private void writeXml(Document doc, String fileName)
      throws TransformerException {

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();

    // pretty print
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

    try(FileWriter sw = new FileWriter(fileName)) {
      transformer.transform(new DOMSource(doc), new StreamResult(sw));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
