import model.Score;
import model.Student;

import java.util.List;
import java.util.Locale;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * @author Daiqj
 */
public class SOAPCreator {

    private String fileName;
    private SOAPMessage message;
    private SOAPEnvelope envelope;
    private SOAPBody body;

    private final String JW_PREFIX = "tns";
    private final String NJU_PREFIX = "nju";
    private final String ENV_PREFIX = "env";

    public void setXMLFileName(String xml) {
        fileName = xml;
    }

    public SOAPMessage getScore(String id) {
        try {
            MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            message = factory.createMessage();
            SOAPPart part = message.getSOAPPart();
            envelope = part.getEnvelope();
            body = envelope.getBody();

            //非法ID
            if (null == id || id.length() == 0 || !isValid(id)) {
                createIllegalFault();
            } else {
                XMLParser parser = new XMLParser();
                parser.setXMLFileName(fileName);
                Student student = parser.getStudent(id);

                if (null == student) {
                    //ID不存在
                    createNoneFault();
                } else {
                    //返回正常学生信息
                    getScore(student);
                }
            }

            message.saveChanges();
            return message;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断学号格式是否符合要求：9位数字
     */
    private boolean isValid(String id) {
        if (id.length() != 9) {
            return false;
        }

        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            if (c > '9' || c < '0') {
                return false;
            }
        }

        return true;
    }

    private void createIllegalFault() {
        try {
            SOAPFault fault = body.addFault();
            fault.setFaultCode(fault.createQName("Sender", ENV_PREFIX));
            fault.setFaultString("学号格式不符合要求，应为9位数字", new Locale("zh-CN"));
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    private void createNoneFault() {
        try {
            SOAPFault fault = body.addFault();
            fault.setFaultCode(fault.createQName("Sender", ENV_PREFIX));
            fault.setFaultString("学号不存在", new Locale("zh-CN"));
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    private void getScore(Student student) {
        try {
            SOAPElement element = body.addChildElement(envelope.createName("学生信息", JW_PREFIX, "http://jw.nju.edu.cn/schema"));
            element.setAttribute("xmlns:nju", "http://www.nju.edu.cn/schema");

            element.addChildElement(element.createQName("学号", JW_PREFIX)).addTextNode(student.getId());

            SOAPElement personInfo = element.addChildElement(element.createQName("个人基本信息", NJU_PREFIX));
            personInfo.addChildElement(element.createQName("姓名", NJU_PREFIX)).addTextNode(student.getName());
            personInfo.addChildElement(element.createQName("性别", NJU_PREFIX)).addTextNode(student.getGender());
            personInfo.addChildElement(element.createQName("出生日期", NJU_PREFIX)).addTextNode(student.getBirthday());
            personInfo.addChildElement(element.createQName("身份证号", NJU_PREFIX)).addTextNode(student.getIdNumber());
            personInfo.addChildElement(element.createQName("学校部门", NJU_PREFIX)).addTextNode(student.getDepartment());

            SOAPElement scoreInfo = element.addChildElement(element.createQName("成绩信息", JW_PREFIX));
            List<Score> scores = student.getScores();
            for (Score score : scores) {
                SOAPElement scoreElement = scoreInfo.addChildElement(element.createQName("成绩", JW_PREFIX));
                scoreElement.addChildElement(element.createQName("课程编号", JW_PREFIX)).addTextNode(score.getCourseID());
                scoreElement.addChildElement(element.createQName("成绩性质", JW_PREFIX)).addTextNode(score.getScoreType());
                scoreElement.addChildElement(element.createQName("分数", JW_PREFIX)).addTextNode(score.getScore() + "");
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

}
