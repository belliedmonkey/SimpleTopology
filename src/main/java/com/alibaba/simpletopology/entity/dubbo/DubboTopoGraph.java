package com.alibaba.simpletopology.entity.dubbo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.alibaba.simpletopology.entity.TopoDirectedGraph;

public class DubboTopoGraph extends TopoDirectedGraph {

    private static final String                   CI_TEMPLATE_URI       = "dubbo.provider/ci.svg";

    private static final String                   CONSUMER_TEMPLATE_URI = "dubbo.provider/consumer.svg";

    private static final String                   PROVIDER_TEMPLATE_URI = "dubbo.provider/provider.svg";

    private static Document                       ciDocTemp;

    private static Document                       consumerDocTemp;

    private static Document                       providerDocTemp;

    private static Map<DUBBO_NODE_TYPE, Document> nodeTempMap           = new HashMap<DUBBO_NODE_TYPE, Document>();

    static {
        try {
           
            InputStream ci_uri = DubboTopoGraph.class.getClassLoader().getResourceAsStream(
                    CI_TEMPLATE_URI);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder =  docFactory.newDocumentBuilder();
            ciDocTemp = dbuilder.parse(ci_uri);
            consumerDocTemp = dbuilder.parse(DubboTopoGraph.class.getClassLoader()
                    .getResourceAsStream(CONSUMER_TEMPLATE_URI));
            providerDocTemp = dbuilder.parse( DubboTopoGraph.class.getClassLoader()
                    .getResourceAsStream(PROVIDER_TEMPLATE_URI));
            nodeTempMap.put(DUBBO_NODE_TYPE.PROVIDER, providerDocTemp);
            nodeTempMap.put(DUBBO_NODE_TYPE.CONSUMER, consumerDocTemp);
            nodeTempMap.put(DUBBO_NODE_TYPE.CI, ciDocTemp);

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public enum DUBBO_NODE_TYPE {
        CI,
        PROVIDER,
        CONSUMER
    }

    public TopoNode newNodeByNodeType(DUBBO_NODE_TYPE type) {
        TopoNode node = new TopoNode(nodeTempMap.get(type));
        return node;
    }

}
