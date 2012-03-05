package com.alibaba.simpletopology.test;

import org.junit.Test;

import com.alibaba.simpletopology.entity.TopoGraph.TopoNode;
import com.alibaba.simpletopology.entity.TopoRelationship;
import com.alibaba.simpletopology.entity.dubbo.DubboTopoGraph;
import com.alibaba.simpletopology.entity.dubbo.DubboTopoGraph.DUBBO_NODE_TYPE;

public class DubboProviderTopoTest {

	@Test
	public void test() throws Exception {
		DubboTopoGraph graph = new DubboTopoGraph();
		TopoNode providerNode = graph.newNodeByNodeType(DUBBO_NODE_TYPE.PROVIDER);
		providerNode.putMeta("appNum", "dragoon-monitor");
		long id1 = graph.addNode(providerNode);
		TopoNode conusmerNode = graph.newNodeByNodeType(DUBBO_NODE_TYPE.CONSUMER);
		conusmerNode.putMeta("appNum", "pt_intl_hz_NapoliMQServer");
		long id11 = graph.addNode(conusmerNode);
		long id13 = graph.addNode(graph.newNodeByNodeType(DUBBO_NODE_TYPE.CONSUMER));
		long id14 = graph.addNode(graph.newNodeByNodeType(DUBBO_NODE_TYPE.CONSUMER));
		long id15 = graph.addNode(graph.newNodeByNodeType(DUBBO_NODE_TYPE.CONSUMER));
		long id21 = graph.addNode(graph.newNodeByNodeType(DUBBO_NODE_TYPE.CI));
		long id22 = graph.addNode(graph.newNodeByNodeType(DUBBO_NODE_TYPE.CI));
		
		graph.addRelationship(new TopoRelationship(id1,id21));
		graph.addRelationship(new TopoRelationship(id1,id22));
		graph.addRelationship(new TopoRelationship(id21,id11));
		graph.addRelationship(new TopoRelationship(id21,id13));
		graph.addRelationship(new TopoRelationship(id21,id14));
		graph.addRelationship(new TopoRelationship(id22,id15));
		System.out.println(graph.toSVG());
	}
	
}
