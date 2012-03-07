package com.alibaba.simpletopology.entity.dubbo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

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
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            InputStream is = DubboTopoGraph.class.getClassLoader().getResourceAsStream(
                    CI_TEMPLATE_URI);
            ciDocTemp = f.createSVGDocument(parser, is);
            consumerDocTemp = f.createSVGDocument(parser, DubboTopoGraph.class.getClassLoader()
                    .getResourceAsStream(CONSUMER_TEMPLATE_URI));
            providerDocTemp = f.createSVGDocument(parser, DubboTopoGraph.class.getClassLoader()
                    .getResourceAsStream(PROVIDER_TEMPLATE_URI));
            nodeTempMap.put(DUBBO_NODE_TYPE.PROVIDER, providerDocTemp);
            nodeTempMap.put(DUBBO_NODE_TYPE.CONSUMER, consumerDocTemp);
            nodeTempMap.put(DUBBO_NODE_TYPE.CI, ciDocTemp);

        } catch (IOException ex) {
            ex.printStackTrace();
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
