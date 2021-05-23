import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

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
            response.setContentType("application/soap+xml;charset=utf-8");
        } else {
            response.setContentType("application/xml;charset=utf-8");
        }
        
        SearchScore creator = new SearchScore();
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
        System.out.println("Do Post");
        var soap = request.getReader().lines().collect(Collectors.joining());
        JSONObject jsonObject = XML.toJSONObject(soap);
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
        scoreService.updateScore(id, courseId, scoreProperty, newScore, listPath);
        request.setAttribute("id", id);
        doGet(request, response);
    }

}
