package com.alibaba.simpletopology.entity;

import java.awt.Dimension;

import org.w3c.dom.Document;

import com.alibaba.simpletopology.draw2d.PositionConstants;
import com.alibaba.simpletopology.draw2d.geometry.Insets;
import com.alibaba.simpletopology.draw2d.geometry.Point;
import com.alibaba.simpletopology.draw2d.geometry.PointList;
import com.alibaba.simpletopology.draw2d.graph.DirectedGraph;
import com.alibaba.simpletopology.draw2d.graph.DirectedGraphLayout;
import com.alibaba.simpletopology.draw2d.graph.Edge;



public class TopoDirectedGraph extends TopoGraph {

    private DirectedGraph dg = new DirectedGraph();

    @Override
    @SuppressWarnings("unchecked")
    public void coordinateGenerate(Document document) {
        dg.setDirection(PositionConstants.RIGHT);
        for (TopoNode node : nodes.values()) {
            com.alibaba.simpletopology.draw2d.graph.Node n = new com.alibaba.simpletopology.draw2d.graph.Node(node);
            n.width = node.getWidth();
            n.height = node.getHeight();
            n.setPadding(new Insets(10, 8, 10, 12));
            nodeMap.put(node, n);
            dg.nodes.add(n);

        }
        for (TopoRelationship ship : relationships) {
            com.alibaba.simpletopology.draw2d.graph.Node source = nodeMap.get(nodes.get(ship.getFromId()));
            com.alibaba.simpletopology.draw2d.graph.Node target = nodeMap.get(nodes.get(ship.getToId()));
            Edge e = new Edge(ship, source, target);
            e.weight = 1;
            dg.edges.add(e);
            relationMap.put(ship, e);
        }
        new DirectedGraphLayout().visit(dg);
        
        for (TopoNode node : nodes.values()) {
            com.alibaba.simpletopology.draw2d.graph.Node n = nodeMap.get(node);
            node.setX(n.x);
            node.setY(n.y);
            node.setWidth(n.width);
            node.setHeight(n.height);
        }
        for (TopoRelationship ship : relationships) {
            Edge edge = relationMap.get(ship);
            PointList pl = edge.getPoints();
            Point first = pl.getFirstPoint();
            Point last = pl.getLastPoint();
            ship.setX1(first.x);
            ship.setY1(first.y);
            ship.setX2(last.x);
            ship.setY2(last.y);
        }
        document.getDocumentElement().setAttribute("width", ""+dg.size.width);
        document.getDocumentElement().setAttribute("height", ""+dg.size.height);
    }

    public DirectedGraph getDg() {
        return dg;
    }

    public void setDg(DirectedGraph dg) {
        this.dg = dg;
    }
    
    
}
