import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;

/**
 * @author Daiqj
 */
public class SAXStarter {

    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new SAXHandler());
            xmlReader.parse("xml/ScoreList.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
