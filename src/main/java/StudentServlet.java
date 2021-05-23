import except.NoMatchFoundException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.*;

/**
 * @author Daiqj
 */
@WebServlet
public class StudentServlet extends HttpServlet {

    private String listPath;

    @Override
    public void init() throws ServletException {
        super.init();
        listPath = getServletContext().getRealPath("StudentList.xml");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");

        // Fixme Hacky way to reuse code
        String attrId = (String) request.getAttribute("id");
        if (attrId != null) {
            id = attrId;
        } else {
            response.setContentType("application/xml;charset=utf-8");
        }

        SOAPCreator creator = new SOAPCreator();
        creator.setXMLFileName(listPath);
        SOAPMessage message = creator.getScore(id);
        try {
            message.writeTo(response.getOutputStream());
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/soap+xml;charset=utf-8");

        var soap = request.getReader().lines().collect(Collectors.joining());
        var jsonObject = XML.toJSONObject(soap);
        var args = jsonObject
            .getJSONObject("SOAP-ENV:Envelope")
            .getJSONObject("SOAP-ENV:Body")
            .getJSONObject("Func");
        var id = String.valueOf(args.getInt("id"));
        var courseId = String.valueOf(args.getInt("courseId"));
        var scoreProperty = args.getString("scoreProperty");
        var newScore = args.getInt("newScore");

        System.out.println(id);
        System.out.println(courseId);
        System.out.println(scoreProperty);
        System.out.println(newScore);

        var scoreService = new ScoreService();
        if (newScore < 0 || newScore > 100) {
            myHandle(response, "该分数不在合理区间内！");
            return;
        }
        if (courseId.length() != 6) {
            myHandle(response, "课程编号长度错误！");
            return;
        }
        if (!scoreProperty.endsWith("成绩")) {
            myHandle(response, "不存在的成绩类型！");
            return;
        }
        try {
            scoreService.updateScore(id, courseId, scoreProperty, newScore, listPath);
        } catch (NoMatchFoundException e) {
            myHandle(response, "未找到满足描述的成绩记录！");
            return;
        }

        request.setAttribute("id", id);
        doGet(request, response);
    }

    private void myHandle(HttpServletResponse response, String desc) {
        System.out.println("Exception Handle!");
        MessageFactory factory;
        try {
            factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            var message = factory.createMessage();
            SOAPPart part = message.getSOAPPart();
            var envelope = part.getEnvelope();
            var body = envelope.getBody();
            SOAPFault fault = body.addFault();
            fault.setFaultCode(fault.createQName("Sender", "env"));
            fault.setFaultString(desc, new Locale("zh-CN"));
            message.writeTo(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
