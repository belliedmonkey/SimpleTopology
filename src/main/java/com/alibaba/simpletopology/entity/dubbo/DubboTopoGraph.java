package com.alibaba.simpletopology.entity.dubbo;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.alibaba.simpletopology.entity.TopoDirectedGraph;

public class DubboTopoGraph extends TopoDirectedGraph {

    private static Log                            LOG                        = LogFactory
                                                                                     .getLog(DubboTopoGraph.class);

    private static final String                   CI_TEMPLATE_URI            = "dubbo.provider/ci.svg";

    private static final String                   CONSUMER_TEMPLATE_URI      = "dubbo.provider/consumer.svg";

    private static final String                   PROVIDER_TEMPLATE_URI      = "dubbo.provider/provider.svg";

    public static final String                    GRAPH_OUTLET_APP_NUM       = "appNum";

    public static final String                    GRAPH_OUTLET_INVOKE_COUNT  = "InvokeCount";

    public static final String                    GRAPH_OUTLET_RUNNING_COUNT = "RunningCount";

    private static Document                       ciDocTemp;

    private static Document                       consumerDocTemp;

    private static Document                       providerDocTemp;

    private static Map<DUBBO_NODE_TYPE, Document> nodeTempMap                = new HashMap<DUBBO_NODE_TYPE, Document>();

    static {
        try {

            InputStream ci_uri = DubboTopoGraph.class.getClassLoader().getResourceAsStream(
                    CI_TEMPLATE_URI);
            InputStream consumer_uri = DubboTopoGraph.class.getClassLoader().getResourceAsStream(
                    CONSUMER_TEMPLATE_URI);
            InputStream provider_uri = DubboTopoGraph.class.getClassLoader().getResourceAsStream(
                    PROVIDER_TEMPLATE_URI);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance(
                    "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl",
                    DubboTopoGraph.class.getClassLoader());
            DocumentBuilder dbuilder = docFactory.newDocumentBuilder();
            InputSource ci_source = new InputSource(ci_uri);
            ci_source.setEncoding("utf-8");
            ciDocTemp = dbuilder.parse(ci_source);
            consumerDocTemp = dbuilder.parse(consumer_uri);
            providerDocTemp = dbuilder.parse(provider_uri);
            nodeTempMap.put(DUBBO_NODE_TYPE.PROVIDER, providerDocTemp);
            nodeTempMap.put(DUBBO_NODE_TYPE.CONSUMER, consumerDocTemp);
            nodeTempMap.put(DUBBO_NODE_TYPE.CI, ciDocTemp);
            ci_uri.close();
            consumer_uri.close();
            provider_uri.close();
        } catch (Exception ex) {
            LOG.error("init svg template error ", ex);
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
