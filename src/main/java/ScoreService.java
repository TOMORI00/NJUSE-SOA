import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class ScoreService {

    public void updateScore(String id, String courseId, String scoreProperty, int newScore, String listPath) {
        var factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(listPath);
            var students = document.getElementsByTagName("tns:学生");
            for (int i = 0; i < students.getLength(); i++) {
                var student = (Element) students.item(i);
                var theId = student.getElementsByTagName("tns:学号").item(0).getTextContent();
                if (!id.equals(theId)) continue;
                var scoreInfo = (Element) student.getElementsByTagName("tns:成绩信息").item(0);
                var scores = scoreInfo.getElementsByTagName("tns:成绩");
                for (int j = 0; j < scores.getLength(); j++) {
                    var score = (Element) scores.item(j);
                    var attrs = score.getAttributes();
                    var theProperty = attrs.getNamedItem("成绩性质").getTextContent();
                    var theCourseId = attrs.getNamedItem("课程编号").getTextContent();
                    if (scoreProperty.equals(theProperty) && courseId.equals(theCourseId)) {
                        var mark = score.getElementsByTagName("tns:得分").item(0);
                        System.out.println("Old: " + mark.getTextContent());
                        System.out.println("New: " + newScore);
                        mark.setTextContent(String.valueOf(newScore));
                        break;
                    }
                }
                break;
            }
            var transformerFactory = TransformerFactory.newInstance();
            var transformer = transformerFactory.newTransformer();
            var source = new DOMSource(document);
            transformer.transform(source, new StreamResult(new File(listPath)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed");
        }
    }

}
