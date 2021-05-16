import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Random;

/**
 * @author Daiqj
 */
public class CreateXMLbyDOM {

    public static void main(String[] args) {
        Document doc;
        Element studentList, student, studentId, basicInfo, name, sex, birthday, id, department, major, scoreList, score, point;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            if (null != doc) {
                studentList = doc.createElement("学生列表");

                for (int i = 0; i < 15; i++) {
                    student = doc.createElement("学生");

                    String sid = "", sname = "", ssex = "", sbirth = "", sidno = "";
                    switch (i) {
                        case 0:
                            sid = "181250016";
                            sname = "陈昱霖";
                            ssex = "男";
                            sbirth = "20000407";
                            sidno = "123456200004071234";
                            break;
                        case 1:
                            sid = "181250199";
                            sname = "郑舒玮";
                            ssex = "男";
                            sbirth = "20000108";
                            sidno = "123456200001081234";
                            break;
                        case 2:
                            sid = "181250052";
                            sname = "黄雨晨";
                            ssex = "男";
                            sbirth = "20001015";
                            sidno = "123456200010151234";
                            break;
                        case 3:
                            sid = "181250023";
                            sname = "戴祺佳";
                            ssex = "男";
                            sbirth = "20001016";
                            sidno = "123456200010161234";
                            break;
                        case 4:
                            sid = "171250672";
                            sname = "李晔华";
                            ssex = "男";
                            sbirth = "19991230";
                            sidno = "123456199912301234";
                            break;
                        case 5:
                            sid = "181250001";
                            sname = "刘一也";
                            ssex = "男";
                            sbirth = "20000101";
                            sidno = "123456200001011234";
                            break;
                        case 6:
                            sid = "181250002";
                            sname = "李秉华";
                            ssex = "男";
                            sbirth = "20000102";
                            sidno = "123456200001021234";
                            break;
                        case 7:
                            sid = "181250003";
                            sname = "浦隽轩";
                            ssex = "男";
                            sbirth = "20000103";
                            sidno = "123456200001031234";
                            break;
                        case 8:
                            sid = "181250004";
                            sname = "王万峰";
                            ssex = "男";
                            sbirth = "20000104";
                            sidno = "123456200001041234";
                            break;
                        case 9:
                            sid = "181250005";
                            sname = "赵志翔";
                            ssex = "男";
                            sbirth = "20000105";
                            sidno = "123456200001051234";
                            break;
                        case 10:
                            sid = "181250006";
                            sname = "张嘉玥";
                            ssex = "男";
                            sbirth = "20000106";
                            sidno = "123456200001061234";
                            break;
                        case 11:
                            sid = "181250007";
                            sname = "曾昭宁";
                            ssex = "男";
                            sbirth = "20000107";
                            sidno = "123456200001071234";
                            break;
                        case 12:
                            sid = "181250008";
                            sname = "潘越";
                            ssex = "男";
                            sbirth = "20001108";
                            sidno = "123456200011081234";
                            break;
                        case 13:
                            sid = "181250009";
                            sname = "郭丰睿";
                            ssex = "男";
                            sbirth = "20000109";
                            sidno = "123456200001091234";
                            break;
                        case 14:
                            sid = "181250010";
                            sname = "陈俊杰";
                            ssex = "男";
                            sbirth = "20000110";
                            sidno = "123456200001101234";
                            break;
                        default:
                            break;
                    }

                    studentId = doc.createElement("学号");

                    studentId.appendChild(doc.createTextNode(sid));
                    student.appendChild(studentId);

                    basicInfo = doc.createElement("个人基本信息");
                    name = doc.createElement("姓名");
                    name.appendChild(doc.createTextNode(sname));
                    basicInfo.appendChild(name);

                    sex = doc.createElement("性别");
                    sex.appendChild(doc.createTextNode(ssex));
                    basicInfo.appendChild(sex);

                    birthday = doc.createElement("出生日期");
                    birthday.appendChild(doc.createTextNode(sbirth));
                    basicInfo.appendChild(birthday);

                    id = doc.createElement("身份证号");
                    id.appendChild(doc.createTextNode(sidno));
                    basicInfo.appendChild(id);

                    department = doc.createElement("学校部门");
                    major = doc.createElement("院系");
                    major.appendChild(doc.createTextNode("软件学院"));
                    department.appendChild(major);

                    basicInfo.appendChild(department);
                    student.appendChild(basicInfo);


                    scoreList = doc.createElement("成绩信息");

                    String[] scoreProperty = {"平时成绩", "期末成绩", "总评成绩"};
                    int[] scores = {60, 60, 70, 80, 90, 60, 60, 70, 80, 90, 60, 60, 70, 80, 90};
                    String[] courseId = {"123456", "123457", "123458", "123459", "123460"};
                    for (int j = 0; j < 5; j++) {

                        for (int k = 0; k < 3; k++) {
                            score = doc.createElement("成绩");
                            score.setAttribute("课程编号", courseId[j]);
                            score.setAttribute("成绩性质", scoreProperty[k]);
                            point = doc.createElement("得分");
                            Random r = new Random();
                            int offset = r.nextInt(20) - 10;
                            point.appendChild(doc.createTextNode(scores[i] + offset + ""));
                            score.appendChild(point);
                            scoreList.appendChild(score);
                        }
                    }

                    student.appendChild(scoreList);
                    studentList.appendChild(student);
                }
                doc.appendChild(studentList);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(new DOMSource(doc), new StreamResult("xml/StudentList.xml"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
