<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl="http://exslt.org/common"
                version="1.0" >
    <xsl:output encoding="utf-8" method="xml" indent="no" omit-xml-declaration="no"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="/catalog">
        <catalogue>
            <xsl:apply-templates select="*" />
        </catalogue>
    </xsl:template>
    <xsl:template match="book">
        <livre>
            <auteur>
                <xsl:value-of select="author"/>
            </auteur>
            <titre>
                <xsl:value-of select="title"/>
            </titre>
            <genre>
                <xsl:variable name="genre" select="genre" />
                <xsl:value-of select="exsl:node-set($genre-map)/map/entry[@key=$genre]"/>
                <!-- BUG XALANJ-2308 Node-set not accessible through function key
                <xsl:for-each select="exsl:node-set($genre-map)">
                    <xsl:value-of select="key('genre-key', $genre)"/>
                </xsl:for-each>-->
            </genre>
            <prix>
                <xsl:value-of select="price"/>
            </prix>
            <date_publication>
                <xsl:value-of select="publish_date"/>
            </date_publication>
            <description>
                <xsl:value-of select="description"/>
            </description>
        </livre>
    </xsl:template>
    <xsl:variable name="genre-map">
        <map>
            <entry key="Computer">Informatique</entry>
            <entry key="Fantasy">Fantastique</entry>
            <entry key="Horror">Horreur</entry>
            <entry key="Romance">Romance</entry>
            <entry key="Science Fiction">Science Fiction</entry>
        </map>
    </xsl:variable>    
    <xsl:key name="genre-key" match="/map/entry" use="@key" />    
</xsl:stylesheet>
