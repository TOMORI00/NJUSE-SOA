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
public class SOAPDriver {

    private String fileName;
    private SOAPMessage message;
    private SOAPEnvelope envelope;
    private SOAPBody body;

    private String jw_prefix = "tns";
    private String nju_prefix = "nju";
    private String env_prefix = "env";

    public void setXMLFileName(String xml) {
        fileName = xml;
    }

    public SOAPMessage createMessage(String id) {
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

                //ID不存在
                if (null == student) {
                    createNoneFault();
                }
                //返回正常学生信息
                else {
                    createMessage(student);
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
            fault.setFaultCode(fault.createQName("Sender", env_prefix));
            fault.setFaultString("学号格式不符合要求，应为9位数字", new Locale("zh-CN"));
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    private void createNoneFault() {
        try {
            SOAPFault fault = body.addFault();
            fault.setFaultCode(fault.createQName("Sender", env_prefix));
            fault.setFaultString("学号不存在", new Locale("zh-CN"));
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    private void createMessage(Student student) {
        try {
            SOAPElement element = body.addChildElement(envelope.createName("学生信息", jw_prefix, "http://jw.nju.edu.cn/schema"));
            element.setAttribute("xmlns:nju", "http://www.nju.edu.cn/schema");

            element.addChildElement(element.createQName("学号", jw_prefix)).addTextNode(student.getId());

            SOAPElement personInfo = element.addChildElement(element.createQName("个人基本信息", nju_prefix));
            personInfo.addChildElement(element.createQName("姓名", nju_prefix)).addTextNode(student.getName());
            personInfo.addChildElement(element.createQName("性别", nju_prefix)).addTextNode(student.getGender());
            personInfo.addChildElement(element.createQName("出生日期", nju_prefix)).addTextNode(student.getBirthday());
            personInfo.addChildElement(element.createQName("身份证号", nju_prefix)).addTextNode(student.getIdNumber());
            personInfo.addChildElement(element.createQName("学校部门", nju_prefix)).addTextNode(student.getDepartment());

            SOAPElement scoreInfo = element.addChildElement(element.createQName("成绩信息", jw_prefix));
            List<Score> scores = student.getScores();
            for (int i = 0; i < scores.size(); i++) {
                Score score = scores.get(i);
                SOAPElement scoreElement = scoreInfo.addChildElement(element.createQName("成绩", jw_prefix));
                scoreElement.addChildElement(element.createQName("课程编号", jw_prefix)).addTextNode(score.getCourseID());
                scoreElement.addChildElement(element.createQName("成绩性质", jw_prefix)).addTextNode(score.getScoreType());
                scoreElement.addChildElement(element.createQName("分数", jw_prefix)).addTextNode(score.getScore() + "");
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }
}
