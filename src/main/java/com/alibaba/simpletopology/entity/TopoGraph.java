package com.alibaba.simpletopology.entity;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.alibaba.simpletopology.batik.DOMUtilities;
import com.alibaba.simpletopology.draw2d.graph.Edge;
import com.alibaba.simpletopology.entity.dubbo.DubboTopoGraph;

/**
 * 拓扑图基类 TODO 拓扑图抽象
 * 
 * @author chris
 */
public abstract class TopoGraph {

    private static final Log                                              LOG              = org.apache.commons.logging.LogFactory
                                                                                                   .getLog(TopoGraph.class);

    protected Map<TopoNode, com.alibaba.simpletopology.draw2d.graph.Node> nodeMap          = new HashMap<TopoNode, com.alibaba.simpletopology.draw2d.graph.Node>();
    protected Map<TopoRelationship, Edge>                                 relationMap      = new HashMap<TopoRelationship, Edge>();

    protected String                                                      title;

    protected String                                                      desc;

    protected int                                                         width;

    protected int                                                         height;

    protected Map<Long, TopoNode>                                         nodes;

    protected List<TopoRelationship>                                      relationships;

    private static final String                                           DOC_TEMPLATE_URI = "dubbo.provider/dubbotopo.svg";

    private static Document                                               docTemp;

    static {
        try {
            InputStream is = DubboTopoGraph.class.getClassLoader().getResourceAsStream(
                    DOC_TEMPLATE_URI);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance(
                    "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl",
                    TopoGraph.class.getClassLoader());
            DocumentBuilder dbuilder = docFactory.newDocumentBuilder();
            docTemp = dbuilder.parse(is);
            is.close();
        } catch (Exception e) {
            LOG.error("parse error", e);
        }
    }

    protected TopoGraph() {
        this.nodes = new HashMap<Long, TopoNode>();
        this.relationships = new ArrayList<TopoRelationship>();
    }

    protected String getTitle() {
        return title;
    }

    protected String getDesc() {
        return desc;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    protected void setDesc(String desc) {
        this.desc = desc;
    }

    public synchronized long addNode(TopoNode node) {
        long id = nodes.size();
        nodes.put(id, node);
        node.setNodeId(id);
        return id;
    }

    public void addRelationship(TopoRelationship relationship) {
        relationships.add(relationship);
    }

    protected void renderData() {
        for (TopoNode node : nodes.values()) {
            try {

                Map<String, Object> meta = node.metaMap;
                for (Map.Entry<String, Object> entry : meta.entrySet()) {
                    Element e = node.bounds.getElementById(entry.getKey());
                    if (e != null) {
                        e.setTextContent((String) entry.getValue());
                    } else {
                        LOG.warn("error node meta  entryKey=" + entry.getKey() + ", entryValue="
                                + entry.getValue());
                    }
                }

            } catch (Exception e) {
                LOG.error(node, e);
            }
        }
    }

    public abstract void coordinateGenerate(Document document);

    public class TopoNode {

        private Long         nodeId;

        private String       title;

        private String       desc;

        private List<String> tipList = new ArrayList<String>();

        private int          width;

        private int          height;

        private int          x;

        private int          y;

        @Override
        public String toString() {
            return "TopoNode [nodeId=" + nodeId + ", title=" + title + ", desc=" + desc
                    + ", width=" + width + ", height=" + height + ", x=" + x + ", y=" + y
                    + ", metaMap=" + metaMap + ", bounds=" + bounds + "]";
        }

        private Map<String, Object> metaMap = new HashMap<String, Object>();

        private Document            bounds;

        public TopoNode(Document bounds) {

            try {
                this.bounds = (Document) bounds.cloneNode(true);
            } catch (Exception e) {
                return;
            }

            String width = this.bounds.getDocumentElement().getAttribute("width");
            String height = this.bounds.getDocumentElement().getAttribute("height");
            if (width != null && height != null) {
                this.width = Integer.parseInt(width);
                this.height = Integer.parseInt(height);
            }
        }

        public void appendTips(String e) {
            tipList.add(e);
        }

        public Long getNodeId() {
            return nodeId;
        }

        public void setNodeId(Long nodeId) {
            this.nodeId = nodeId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        protected String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public Object putMeta(String key, Object value) {
            return metaMap.put(key, value);
        }

        public Object getMeta(String key) {
            return metaMap.get(key);
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public Document getBounds() {
            return bounds;
        }

        public void setBounds(Document bounds) {
            this.bounds = bounds;
        }

    }

    public String toSVG() throws Exception {
        Document doc = null;
        try {
            doc = (Document) docTemp.cloneNode(true);
        } catch (Exception e) {

        }
        return paint(doc);
    }

    private void appendNodeToDocument(Document document, Element bizNode, TopoNode node) {

        Node gg = document.importNode(bizNode, true);
        Element g = document.createElement("g");
        g.setAttribute("id", new Long(node.getNodeId()).toString());
        g.setAttribute("width", new Integer(node.getWidth()).toString());
        g.setAttribute("height", new Integer(node.getHeight()).toString());
        g.appendChild(gg);
        document.getDocumentElement().appendChild(g);
        setNodeCoodinate(node, g);

    }

    private void setNodeCoodinate(TopoNode node, Element cg) {
        cg.setAttribute("transform", cg.getAttribute("transform") + " translate(" + node.getX()
                + "," + node.getY() + ")");
    }

    public String paint(Document document) throws Exception {
        coordinateGenerate(document);

        renderData();
        for (TopoNode node : nodes.values()) {
            Element g = node.getBounds().getDocumentElement();
            boolean flag = false;
            String tips = "";
            for (String tip : node.tipList) {
                if (!flag) {
                    tips += tip;
                    flag = true;
                } else {
                    tips += "%%" + tip;
                }
            }
            if (!tips.trim().isEmpty()) {
                g.setAttribute("node_detail", tips);
            }
            appendNodeToDocument(document, g, node);
        }
        for (TopoRelationship ship : relationships) {
            Document doc = ship.getBounds();
            Element e = (Element) doc.getDocumentElement().getElementsByTagName("line").item(0);
            e.setAttribute("x1", new Integer(ship.getX1()).toString());
            e.setAttribute("y1", new Integer(ship.getY1()).toString());
            e.setAttribute("x2", new Integer(ship.getX2()).toString());
            e.setAttribute("y2", new Integer(ship.getY2()).toString());
            e.setAttribute("source", new Long(ship.getFromId()).toString());
            e.setAttribute("target", new Long(ship.getToId()).toString());
            Node line = document.importNode(e, true);
            document.getDocumentElement().appendChild(line);
        }
        StringWriter sWriter = new StringWriter();
        Writer out = sWriter;
        DOMUtilities.writeDocument(document, sWriter);
        return out.toString();
    }

}
