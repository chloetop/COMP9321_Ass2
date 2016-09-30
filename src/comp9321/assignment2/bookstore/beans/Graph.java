package comp9321.assignment2.bookstore.beans;

import java.util.ArrayList;

public class Graph {

	ArrayList<GraphNode> nodes;
	ArrayList<GraphEdge> edges;

	public ArrayList<GraphNode> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<GraphNode> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<GraphEdge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<GraphEdge> edges) {
		this.edges = edges;
	}

}
