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

            //�Ƿ�ID
            if (null == id || id.length() == 0 || !isValid(id)) {
                createIllegalFault();
            } else {
                XMLParser parser = new XMLParser();
                parser.setXMLFileName(fileName);
                Student student = parser.getStudent(id);

                //ID������
                if (null == student) {
                    createNoneFault();
                }
                //��������ѧ����Ϣ
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
     * �ж�ѧ�Ÿ�ʽ�Ƿ����Ҫ��9λ����
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
            fault.setFaultString("ѧ�Ÿ�ʽ������Ҫ��ӦΪ9λ����", new Locale("zh-CN"));
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    private void createNoneFault() {
        try {
            SOAPFault fault = body.addFault();
            fault.setFaultCode(fault.createQName("Sender", env_prefix));
            fault.setFaultString("ѧ�Ų�����", new Locale("zh-CN"));
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    private void createMessage(Student student) {
        try {
            SOAPElement element = body.addChildElement(envelope.createName("ѧ����Ϣ", jw_prefix, "http://jw.nju.edu.cn/schema"));
            element.setAttribute("xmlns:nju", "http://www.nju.edu.cn/schema");

            element.addChildElement(element.createQName("ѧ��", jw_prefix)).addTextNode(student.getId());

            SOAPElement personInfo = element.addChildElement(element.createQName("���˻�����Ϣ", nju_prefix));
            personInfo.addChildElement(element.createQName("����", nju_prefix)).addTextNode(student.getName());
            personInfo.addChildElement(element.createQName("�Ա�", nju_prefix)).addTextNode(student.getGender());
            personInfo.addChildElement(element.createQName("��������", nju_prefix)).addTextNode(student.getBirthday());
            personInfo.addChildElement(element.createQName("���֤��", nju_prefix)).addTextNode(student.getIdNumber());
            personInfo.addChildElement(element.createQName("ѧУ����", nju_prefix)).addTextNode(student.getDepartment());

            SOAPElement scoreInfo = element.addChildElement(element.createQName("�ɼ���Ϣ", jw_prefix));
            List<Score> scores = student.getScores();
            for (int i = 0; i < scores.size(); i++) {
                Score score = scores.get(i);
                SOAPElement scoreElement = scoreInfo.addChildElement(element.createQName("�ɼ�", jw_prefix));
                scoreElement.addChildElement(element.createQName("�γ̱��", jw_prefix)).addTextNode(score.getCourseID());
                scoreElement.addChildElement(element.createQName("�ɼ�����", jw_prefix)).addTextNode(score.getScoreType());
                scoreElement.addChildElement(element.createQName("����", jw_prefix)).addTextNode(score.getScore() + "");
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }
}
