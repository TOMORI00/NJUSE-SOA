# readme

使用 CreateXMLbyDOM 类操作 DOM 生成 studentList.xml 文件

代码见src文件夹

xml文档和schema见xml文件夹

学生信息见代码内

另：由于组织成员变动，沟通不畅等缘故，第一次作业提交出现缺少文件（部分schema设计）的情况

已补包含在本次作业提交中，望助教海涵

---

### xml验证和空白文本节点处理方法


- ## 可使用验证

    使用DocumentBuilderFactory
  
    - setValidating启用验证
    - setIgnoringElementContentWhitespace忽略空白字符
    - newDocumentBuilder 创建 DocumentBuilder 

    使用DocumentBuilder
  
    - parse解析XML文件
    - 获得document对象

```java
package com.learningjava;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class  ParserUtil {

    public static Document getDocument(String filePath) {
        Document document = null;
        try {
            //step1: get DocumentBuilderFactory
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            //configure the factory to set validate mode
            boolean dtdValidate = false;
            boolean xsdValidate = false;
            if (filePath.contains("dtd")) {
                dtdValidate = true;
            } else if (filePath.contains("xsd")) {
                xsdValidate = true;
                dbFactory.setNamespaceAware(true);
                final String JAXP_SCHEMA_LANGUAGE =
                        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
                final String W3C_XML_SCHEMA =
                        "http://www.w3.org/2001/XMLSchema";
                dbFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            }
            dbFactory.setValidating(dtdValidate || xsdValidate);
            dbFactory.setIgnoringElementContentWhitespace(dtdValidate || xsdValidate);

            //parse an XML file into a DOM tree 
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            document = builder.parse(new File(filePath));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return document;
    }
}
```

- ## 可手动删除节点

```java
import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class PrintNodeDemo {
	public static void main(String[] args) {
		Document doc = ParserUtil.getDocument("books-no.xml");
		Element rootElement = doc.getDocumentElement();
		
		//before whitespace node removed
		System.out.format("Node Architecture of %s as follow:%n%n","books-no.xml");
		ParserUtil.printElementAndTextNode(1,rootElement);
		
		//remove whitespace node
		System.out.format("%nremoved %d whitespace node.%n",
				ParserUtil.removeWhiteSpaceTextElement(rootElement));
		System.out.format("after removed: %n%n");
		ParserUtil.printElementAndTextNode(1,rootElement);
	}
}
```