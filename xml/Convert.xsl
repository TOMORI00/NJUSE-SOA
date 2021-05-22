<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="1.0"
        xmlns="http://jw.nju.edu.cn/schema"
        xmlns:tns="http://jw.nju.edu.cn/schema"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
>
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:key name="score-key" match="//tns:学生/tns:成绩信息/tns:成绩" use="concat(@课程编号,@成绩性质)"/>

    <xsl:template match="/">
        <tns:课程成绩列表 xsi:schemaLocation="http://jw.nju.edu.cn/schema ScoreList.xsd">
            <xsl:apply-templates select="//tns:学生"/>
        </tns:课程成绩列表>
    </xsl:template>

    <xsl:template match="//tns:学生">
        <xsl:for-each select=".">
            <xsl:for-each select="tns:成绩信息/tns:成绩[count(. | key('score-key',concat(@课程编号,@成绩性质))[1])=1]">
                <tns:课程成绩>
                    <xsl:attribute name="课程编号">
                        <xsl:value-of select="@课程编号"/>
                    </xsl:attribute>
                    <xsl:attribute name="成绩性质">
                        <xsl:value-of select="@成绩性质"/>
                    </xsl:attribute>
                    <xsl:for-each select="key('score-key', concat(@课程编号,@成绩性质))">
                        <xsl:sort select="tns:得分"/>
                        <tns:成绩>
                            <tns:学号>
                                <xsl:value-of select="../../tns:学号"/>
                            </tns:学号>
                            <tns:得分>
                                <xsl:value-of select="tns:得分"/>
                            </tns:得分>
                        </tns:成绩>
                    </xsl:for-each>
                </tns:课程成绩>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
