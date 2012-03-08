package com.alibaba.simpletopology.entity;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.alibaba.simpletopology.entity.dubbo.DubboTopoGraph;

public class TopoRelationship {

    private String              title;

    private long                fromId;

    private long                toId;

    private int                 x1;

    private int                 x2;

    private int                 y1;

    private int                 y2;

    private int                 lineType;

    private Document            bounds;

    private static Document     graphTemplate;

    private static final String DOC_TEMPLATE_URI = "dubbo.provider/relationship.svg";
    static {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbuilder;
        try {
            dbuilder = docFactory.newDocumentBuilder();

            InputStream is = DubboTopoGraph.class.getClassLoader().getResourceAsStream(
                    DOC_TEMPLATE_URI);

            graphTemplate = dbuilder.parse(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Document getBounds() {
        return bounds;
    }

    public void setBounds(Document bounds) {
        this.bounds = bounds;
    }

    public TopoRelationship(long id1, long id11) {
        this.bounds = (Document) graphTemplate.cloneNode(true);
        this.fromId = id1;
        this.toId = id11;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public int getLineType() {
        return lineType;
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
    }

    @Override
    public String toString() {
        return "TopoRelationship [title=" + title + ", fromId=" + fromId + ", toId=" + toId
                + ", lineType=" + lineType + "]";
    }

}
