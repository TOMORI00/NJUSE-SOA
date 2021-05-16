<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns="http://jw.nju.edu.cn/schema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
>
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:key name="score-key" match="学生列表/学生/成绩信息/成绩" use="concat(@课程编号,@成绩性质)"/>

    <xsl:template match="/">
        <课程成绩列表 xsi:schemaLocation="http://jw.nju.edu.cn/schema ScoreList.xsd">
            <xsl:apply-templates select="学生列表/学生"/>
        </课程成绩列表>
    </xsl:template>

    <xsl:template match="学生列表/学生">
        <xsl:for-each select=".">
            <xsl:for-each select="成绩信息/成绩[count(. | key('score-key',concat(@课程编号,@成绩性质))[1])=1]">
                <课程成绩>
                    <xsl:attribute name="课程编号">
                        <xsl:value-of select="@课程编号"/>
                    </xsl:attribute>
                    <xsl:attribute name="成绩性质">
                        <xsl:value-of select="@成绩性质"/>
                    </xsl:attribute>
                    <xsl:for-each select="key('score-key', concat(@课程编号,@成绩性质))">
                        <xsl:sort select="得分"/>
                        <成绩>
                            <学号>
                                <xsl:value-of select="../../学号"/>
                            </学号>
                            <得分>
                                <xsl:value-of select="得分"/>
                            </得分>
                        </成绩>
                    </xsl:for-each>
                </课程成绩>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
