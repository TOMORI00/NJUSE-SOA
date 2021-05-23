import javax.xml.soap.*;

public class SOAPClient {

    public static void main(String[] args) {
        String soapEndpointUrl = "http://localhost:8080/Homework/StudentServlet";
        sendSoapRequest(soapEndpointUrl);
    }

    private static void sendSoapRequest(String soapEndpointUrl) {
        try {
            SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = factory.createConnection();

            var message = createMessage();
            SOAPMessage response = connection.call(
                message,
                soapEndpointUrl
            );

            System.out.println("Response:");
            response.writeTo(System.out);
            System.out.println("\n");

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SOAPMessage createMessage() throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        createEnvelope(message);
        message.saveChanges();

        System.out.println("Request:");
        message.writeTo(System.out);
        System.out.println("\n");

        return message;
    }

    private static void createEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart part = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = part.getEnvelope();
        SOAPBody body = envelope.getBody();

        SOAPElement root = body.addChildElement("Func");
        root.addChildElement("id").addTextNode("181250016");
        root.addChildElement("courseId").addTextNode("123456");
        root.addChildElement("scoreProperty").addTextNode("平时分");
        root.addChildElement("newScore").addTextNode("101");
    }

}
