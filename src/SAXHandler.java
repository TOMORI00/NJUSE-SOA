import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Daiqj
 */
public class SAXHandler extends DefaultHandler {

    boolean failed = false;
    boolean processing = false;
    boolean reading = false;
    BufferedWriter writer = null;
    String fileName = "xml/FailedList.xml";
    String temp = "";
    final String SCORE = "tns:成绩";
    final String POINT = "tns:得分";

    @Override
    public void startDocument() throws SAXException {
        File file = new File(fileName);
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        } catch (IOException e) {
            e.printStackTrace();
            throw new SAXException(e);
        }
    }

    /**
     * 钩子函数-文档结束
     * @throws SAXException SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SAXException(e);
        }
    }

    /**
     * 钩子函数-元素开始
     * @param uri URI
     * @param localName 本地名称
     * @param qName 元素名
     * @param attributes 元素属性
     * @throws SAXException SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (SCORE.equals(qName)) {
            processing = true;
            temp += "<" + qName + ">";
        } else if (POINT.equals(qName)) {
            reading = true;
            temp += "<" + qName + ">";
        } else if (processing) {
            temp += "<" + qName + ">";
        } else {
            try {
                writer.write("<" + qName);
                for (int i = 0; i < attributes.getLength(); i++) {
                    String attrName = attributes.getQName(i);
                    String attrValue = attributes.getValue(i);
                    writer.write(" " + attrName + "=\"" + attrValue + "\"");
                }
                writer.write(">\n");
            } catch (IOException e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }
    }

    /**
     * 钩子函数-元素结束
     * @param uri URI
     * @param localName 本地名称
     * @param qName 元素名称
     * @throws SAXException SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (SCORE.equals(qName)) {
            temp += "</" + qName + ">\n";
            if (failed) {
                try {
                    writer.write(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            processing = false;
            temp = "";
        } else if (POINT.equals(qName)) {
            reading = false;
            temp += "</" + qName + ">";
        } else if (processing) {
            temp += "</" + qName + ">";
        } else {
            try {
                writer.write("</" + qName + ">\n");
            } catch (IOException e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }
    }

    /**
     *
     * @param ch 字符串
     * @param start 开始
     * @param length 长度
     * @throws SAXException SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String str = new String(ch, start, length).trim();
        if (reading) {
            int score = Integer.parseInt(str);
            if (score < 60) {
                failed = true;
            } else {
                failed = false;
            }
            temp += str;
        } else if (processing) {
            temp += str;
        } else {
            if (!"".equals(str)) {
                System.out.println(str);
            }
        }
    }

}
