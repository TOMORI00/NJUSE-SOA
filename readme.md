# readme

# 第四次作业 - 第八组

## XSLT part

使用 XSLT.java 和 Convert.xsl 通过 XSLT 方法提取学生名册中的学生成绩，生成成绩表xml
受 XSLT1.0 的限制，使用 Muenchian grouping 
（用 concat 组成用于分组的 key， 用 count(.|key(...)[1])=1找出各组中首节点）
来组合课程编号与成绩性质相同的记录，通过 xsl:sort 将组内记录按成绩升序排序
产物见ScoreList.xml

## SAX part

使用 SAXStarter.java 和 SAXHandler.java 通过SAX方法顺序遍历文档读取并将目标成绩（小于60分成绩）写入不及格成绩表xml

产物见FiledList.xml

## 思考题

如果在XSLT中的属性模板中指定元素节点将会发生什么？

XSLT 的模板可以通过 match 属性指定将要匹配的元素（XPath），
结合 apply-template 可以避免默认的匹配整个子节点的行为，
仅对指定的节点做匹配转换。
