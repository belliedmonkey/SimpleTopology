package com.alibaba.simpletopology.entity;

import java.awt.Dimension;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;

public class TopoDirectedGraph extends TopoGraph {

    private DirectedGraph dg = new DirectedGraph();

    @SuppressWarnings("unchecked")
    public void coordinateGenerate(Dimension dimension) {
        for (TopoNode node : nodes.values()) {
            org.eclipse.draw2d.graph.Node n = new org.eclipse.draw2d.graph.Node(node);
            n.width = node.getWidth();
            n.height = node.getHeight();
            n.setPadding(new Insets(10, 8, 10, 12));
            nodeMap.put(node, n);
            dg.nodes.add(n);

        }
        for (TopoRelationship ship : relationships) {
            org.eclipse.draw2d.graph.Node source = nodeMap.get(nodes.get(ship.getFromId()));
            org.eclipse.draw2d.graph.Node target = nodeMap.get(nodes.get(ship.getToId()));
            Edge e = new Edge(ship, source, target);
            e.weight = 1;
            dg.edges.add(e);
            relationMap.put(ship, e);
        }
        new DirectedGraphLayout().visit(dg);
        for (TopoNode node : nodes.values()) {
            org.eclipse.draw2d.graph.Node n = nodeMap.get(node);
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
    }
}
