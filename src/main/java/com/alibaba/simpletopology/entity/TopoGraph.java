package com.alibaba.simpletopology.entity;

import java.awt.Color;
import java.awt.Dimension;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.alibaba.simpletopology.draw2d.graph.Edge;
import com.alibaba.simpletopology.entity.dubbo.DubboTopoGraph;

/** 拓扑图基类
 * 
 * TODO 拓扑图抽象
 * @author chris
 *
 */
public abstract class TopoGraph {
    
    
    protected Map<TopoNode, com.alibaba.simpletopology.draw2d.graph.Node> nodeMap     = new HashMap<TopoNode, com.alibaba.simpletopology.draw2d.graph.Node>();
    protected Map<TopoRelationship, Edge>                  relationMap = new HashMap<TopoRelationship, Edge>();

    protected String                 title;

    protected String                 desc;

    protected int                    width;

    protected int                    height;

    protected Map<Long, TopoNode>    nodes;

    protected List<TopoRelationship> relationships;

    private static final String      DOC_TEMPLATE_URI = "dubbo.provider/dubbotopo.svg";

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

    public long addNode(TopoNode node) {
        long id = nodes.size();
        nodes.put(id, node);
        return id;
    }

    public void addRelationship(TopoRelationship relationship) {
        relationships.add(relationship);
    }

    protected void renderData() {
        for (TopoNode node : nodes.values()) {
            Map<String, Object> meta = node.metaMap;
            for (Map.Entry<String, Object> entry : meta.entrySet()) {
                Element e = node.bounds.getElementById(entry.getKey());
                if (e != null) {
                    e.setTextContent((String) entry.getValue());
                } else {
                    //TODO 记录日志
                }
            }
        }
    }
 

    public abstract void coordinateGenerate(Dimension dimension);

    

    public class TopoNode {

        private Long                nodeId;

        private String              title;

        private String              desc;

        private int                 width;

        private int                 height;

        private int                 x;

        private int                 y;

        private Map<String, Object> metaMap = new HashMap<String, Object>();

        private Document            bounds;

        public TopoNode(Document bounds) {
            this.bounds = (Document) bounds.cloneNode(true);
            this.width = Integer.parseInt(this.bounds.getDocumentElement().getAttribute("width"));
            this.height = Integer.parseInt(this.bounds.getDocumentElement().getAttribute("height"));
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
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        InputStream is = DubboTopoGraph.class.getClassLoader().getResourceAsStream(DOC_TEMPLATE_URI);
        Document doc = f.createSVGDocument(parser, is);
        return paint(doc);
    }

    private void appendNodeToDocument(Document document, Element bizNode, TopoNode node) {
        Node gg = document.importNode(bizNode, true);
        Element g = document.createElement("g");
        g.appendChild(gg);
        document.getDocumentElement().appendChild(g);
        setNodeCoodinate(node, g);

    }

    private void setNodeCoodinate(TopoNode node, Element cg) {
        cg.setAttribute("transform", cg.getAttribute("transform") + " translate(" + node.getX()
                + "," + node.getY() + ")");
    }

    public String paint(Document document) throws Exception {
        coordinateGenerate(new Dimension(1440, 900));
        renderData();
        for (TopoNode node : nodes.values()) {
            Element g = node.getBounds().getDocumentElement();
            appendNodeToDocument(document, g, node);
        }

        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document lineDocument = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(lineDocument);

        for (TopoRelationship relationship : relationships) {

            svgGenerator.setPaint(Color.black);
            svgGenerator.drawLine(relationship.getX1(), relationship.getY1(), relationship.getX2(),
                    relationship.getY2());
        }
        Element root = svgGenerator.getRoot();
        Node line = document.importNode(root, true);
        document.getDocumentElement().appendChild(line);
        StringWriter sWriter = new StringWriter();
        Writer out = sWriter;
        DOMUtilities.writeDocument(document, sWriter);
        return out.toString();
    }

}
