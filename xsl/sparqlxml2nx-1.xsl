<?xml version="1.0" encoding="iso-8859-1"?>

<!--

XSLT script to format SPARQL Query Results XML Format into nx format, adapted by Benedikt Kaempgen 
using:

XSLT script to format SPARQL Query Results XML Format into xhtml

Copyright © 2004, 2005 World Wide Web Consortium, (Massachusetts
Institute of Technology, European Research Consortium for
Informatics and Mathematics, Keio University). All Rights
Reserved. This work is distributed under the W3C® Software
License [1] in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.

[1] http://www.w3.org/Consortium/Legal/2002/copyright-software-20021231

$Id: result-to-html.xsl,v 1.1 2008/01/15 02:25:58 eric Exp $

-->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/1999/xhtml"
  xmlns:res="http://www.w3.org/2005/sparql-results#"
  exclude-result-prefixes="res xsl">
  
  <xsl:output method="text" />

  <xsl:template name="header">

    <div>
      <h2>Header</h2>
      <xsl:for-each select="res:head/res:link"> 
	<p>Link to <xsl:value-of select="@href"/></p>
      </xsl:for-each>
    </div>
  </xsl:template>

  <xsl:template name="boolean-result">

    <div>
      <h2>Boolean Result</h2>
      <p>Value <xsl:value-of select="res:boolean"/></p>
    </div>
  </xsl:template>


  <xsl:template name="vb-result">

	  <xsl:for-each select="res:head/res:variable">?<xsl:value-of select="@name"/><xsl:text> </xsl:text></xsl:for-each><!-- do not forget the . and newline --><xsl:text>.&#10;</xsl:text>
	  <xsl:for-each select="res:results/res:result"><xsl:apply-templates select="."/></xsl:for-each>

  </xsl:template>

  <!-- If we have one result -->
  <xsl:template match="res:result">
    <xsl:variable name="current" select="."/>
    <!-- for each result -->
    <xsl:for-each select="//res:head/res:variable">
      <xsl:variable name="name" select="@name"/>
	<xsl:choose>

	  <xsl:when test="$current/res:binding[@name=$name]">
	    <!-- apply template for the correct value type (bnode, uri, literal) -->
	    <xsl:apply-templates select="$current/res:binding[@name=$name]"/>
	  </xsl:when>
	  <xsl:otherwise>
	  	<!-- no binding available for this variable in this solution -->
	  	<xsl:text disable-output-escaping="yes">&lt;</xsl:text>null<xsl:text disable-output-escaping="yes">&gt;</xsl:text><xsl:text> </xsl:text>
	  </xsl:otherwise>
	  
	</xsl:choose>
    </xsl:for-each>
    <!-- do not forget the . --><xsl:text>.&#10;</xsl:text>
    
  </xsl:template>

  <xsl:template match="res:bnode">
    <xsl:text>_:</xsl:text>
    <xsl:value-of select="text()"/><xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="res:uri">
    <xsl:variable name="uri" select="text()"/>
	<xsl:text disable-output-escaping="yes">&lt;</xsl:text><xsl:value-of select="$uri"/><xsl:text disable-output-escaping="yes">&gt;</xsl:text><xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="res:literal">
    <xsl:choose>

      <xsl:when test="@datatype">
	<!-- datatyped literal value -->
	<xsl:text>"</xsl:text><xsl:value-of select="text()"/><xsl:text>"</xsl:text>^^<xsl:text disable-output-escaping="yes">&lt;</xsl:text><xsl:value-of select="@datatype"/><xsl:text disable-output-escaping="yes">&gt;</xsl:text><xsl:text> </xsl:text>
      </xsl:when>
      
      <xsl:when test="@xml:lang">
	<!-- lang-string -->
	<xsl:text>"</xsl:text><xsl:value-of select="text()"/><xsl:text>"</xsl:text>@<xsl:value-of select="@xml:lang"/><xsl:text> </xsl:text>
      </xsl:when>

      <xsl:when test="string-length(text()) != 0">
	<!-- normal string: present and not empty -->
	<xsl:text>"</xsl:text><xsl:value-of select="text()"/><xsl:text>"</xsl:text><xsl:text> </xsl:text>
      </xsl:when>
      
      <xsl:when test="string-length(text()) = 0">
	<!-- present and empty -->
      <xsl:text>""</xsl:text><xsl:text> </xsl:text>
      </xsl:when>
    </xsl:choose>

  </xsl:template>

  <!-- check what kind of xml output -->
  <xsl:template match="res:sparql">

	<xsl:if test="res:head/res:link">
	  <xsl:call-template name="header"/>
	</xsl:if>

	<xsl:choose>
	  <xsl:when test="res:boolean">
	    <xsl:call-template name="boolean-result" />

	  </xsl:when>
      
      <!-- The kind of output that we are looking for -->
	  <xsl:when test="res:results">
	    <xsl:call-template name="vb-result" />
	  </xsl:when>

	</xsl:choose>

  </xsl:template>
</xsl:stylesheet>
