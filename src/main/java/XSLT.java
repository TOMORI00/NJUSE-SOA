import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Daiqj
 */
public class XSLT {

    public static void main(String[] args) {
        convert("xml/ScoreList.xml", "xml/StudentList.xml", "xml/Convert.xsl");
//        validate("xml/ScoreList.xml", "xml/ScoreList.xsd");
    }

    private static void convert(String dest, String src, String xsl) {
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer(new StreamSource(xsl));
            transformer.transform(
                    new StreamSource(src),
                    new StreamResult(new FileOutputStream(dest))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void validate(String xml, String xsd) {
        String LANG = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory sf = SchemaFactory.newInstance(LANG);
        try {
            Schema schema = sf.newSchema(new File(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
