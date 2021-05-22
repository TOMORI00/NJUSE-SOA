import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author Daiqj
 */
public class SAXStarter {

    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new SAXHandler());
            xmlReader.parse("xml/ScoreList.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
