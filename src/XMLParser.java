import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

	private String fileName;
	
	public void setXMLFileName(String xml) {
		fileName = xml;
	}

	public Student getStudent(String id) {
		Student student = new Student();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fileName);
			Element students = document.getDocumentElement();
			NodeList nodes = students.getElementsByTagName("tns:学号");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String studentID = node.getTextContent();
				
				if (studentID.equals(id)) {
					student.setId(id);
					
					Element element = (Element)node.getParentNode();
					String name = element.getElementsByTagName("nju:姓名").item(0).getTextContent();
					String gender = element.getElementsByTagName("nju:性别").item(0).getTextContent();
					String birthday = element.getElementsByTagName("nju:出生日期").item(0).getTextContent();
					String idNumber = element.getElementsByTagName("nju:身份证号").item(0).getTextContent();
					String department = element.getElementsByTagName("nju:学校部门").item(0).getTextContent();


					student.setName(name);
					student.setGender(gender);
					student.setBirthday(birthday);
					student.setDepartment(department);
					student.setIdNumber(idNumber);

					NodeList scoreInfo = ((Element)element.getElementsByTagName("tns:成绩信息").item(0)).getElementsByTagName("tns:成绩");
					List<Score> scoreList = new ArrayList<Score>();
					System.out.println(scoreInfo.getLength());
					for (int j = 0; j < scoreInfo.getLength(); j++) {
						Element info = (Element) scoreInfo.item(j);
						String courseType = info.getAttribute("成绩性质");
						String courseID = info.getAttribute("课程编号");
						NodeList scores = info.getElementsByTagName("tns:得分");
						Score score = new Score(courseID, courseType, Double.parseDouble(scores.item(0).getTextContent()));
						scoreList.add(score);
					}
					student.setScores(scoreList);
					
					return student;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
