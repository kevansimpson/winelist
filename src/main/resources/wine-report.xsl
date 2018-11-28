<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="wl-portrait"
                                       page-height="11.00in" page-width="8.50in" margin="0.50in">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="wl-portrait">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                        <fo:table>
                            <fo:table-column width="2.5in"/>
                            <fo:table-column width="2.5in"/>
                            <fo:table-column width="2.5in"/>
                            <fo:table-body>
                                <xsl:for-each select="/List/Entry[position() mod 3 = 1]">
                                    <fo:table-row>
                                        <xsl:apply-templates select=". | following-sibling::*[3 > position()]"/>
                                    </fo:table-row>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template match="Entry">
        <fo:table-cell padding="1mm" padding-top="0.5mm" padding-bottom="0.5mm">
            <fo:block-container border="solid grey 1pt"
                                height="2.40in">
                <fo:block>Prod: <xsl:value-of select="Producer"/></fo:block>
                <fo:block>Name: <xsl:value-of select="Name"/></fo:block>
                <fo:block>Type: <xsl:value-of select="Type"/></fo:block>
                <fo:block>Year: <xsl:value-of select="Year"/></fo:block>
                <fo:block>Cost: <xsl:value-of select="Price"/></fo:block>
                <fo:block>Qty : <xsl:value-of select="Qty"/></fo:block>
                <fo:block>Bin : <xsl:value-of select="Bin"/></fo:block>
                <fo:block>When: <xsl:value-of select="Ready"/></fo:block>
                <fo:block>Rtg : <xsl:value-of select="Rating"/></fo:block>
            </fo:block-container>
        </fo:table-cell>
    </xsl:template>

</xsl:stylesheet>