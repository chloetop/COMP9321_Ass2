package comp9321.assignment2.bookstore.helpers;

import java.util.ArrayList;

import comp9321.assignment2.bookstore.beans.Graph;
import comp9321.assignment2.bookstore.beans.GraphEdge;
import comp9321.assignment2.bookstore.beans.GraphNode;
import comp9321.assignment2.bookstore.dao.GraphDAO;

public class GraphSearch {

	public static String getJSONString(Graph graph) {
		String JSONString = new String();
		JSONString = "{\nnodes: [\n";

		for (GraphNode n : graph.getNodes()) {
			JSONString += "{name: \"" + n.getName() + "\"},\n";
		}

		JSONString = JSONString.substring(0, JSONString.length() - 2);
		JSONString += "\n],\nedges: [\n";

		for (GraphEdge e : graph.getEdges()) {
			JSONString += "{source: " + e.getSource_id() + ", target: "
					+ e.getTarget_id() + ", label: \"" + e.getRelation()
					+ "\"},\n";
		}
		JSONString = JSONString.substring(0, JSONString.length() - 1);
		JSONString += "\n]\n};";

		return JSONString;
	}

	public static Graph buildGraph(String key, int root_id, int current_node) {
		GraphNode node = GraphDAO
				.retrieveEntityNodes("select * from entity_store where value = '"
						+ key + "'");

		String query = "SELECT * FROM graph_store where source="
				+ node.getEntity_id();

		ArrayList<GraphEdge> edges = GraphDAO.retrieveGraphEdges(query);

		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
		int node_count = current_node;
		int source_id = root_id;
		node.setGraph_node_id(root_id);
		if (current_node == 0)
			nodes.add(node);

		for (GraphEdge edge : edges) {
			node_count++;
			String query_string = "SELECT * FROM entity_store WHERE entity_id="
					+ edge.getTarget();
			GraphNode n = GraphDAO.retrieveEntityNodes(query_string);
			n.setGraph_node_id(node_count);
			edge.setSource_id(source_id);
			edge.setTarget_id(node_count);
			nodes.add(n);
		}

		Graph graph = new Graph();
		graph.setEdges(edges);
		graph.setNodes(nodes);

		return graph;

	}

	public static Graph buildGraphWithKey(String key) {
		Graph g = buildGraph(key, 0, 0);
		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
		ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();

		for (GraphNode node : g.getNodes()) {
			if (node.getAttribute().equals("item")) {
				if (!node.getName().equals(key)) {
					Graph buffer = buildGraph(node.getName(),
							node.getGraph_node_id(), g.getNodes().size()-1);
					nodes.addAll(buffer.getNodes());
					edges.addAll(buffer.getEdges());
					
				}
			}
		}
		
		g.addNodes(nodes);
		g.addEdges(edges);
		
		return g;
	}

	public static String keySearch(String key) {
		GraphNode node = GraphDAO
				.retrieveEntityNodes("select * from entity_store where value = '"
						+ key + "'");

		String query = "SELECT * FROM graph_store where source="
				+ node.getEntity_id();

		ArrayList<GraphEdge> edges = GraphDAO.retrieveGraphEdges(query);

		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
		int node_count = 0;
		int source_id = 0;
		node.setGraph_node_id(0);
		nodes.add(node);

		for (GraphEdge edge : edges) {
			node_count++;
			String query_string = "SELECT * FROM entity_store WHERE entity_id="
					+ edge.getTarget();
			GraphNode n = GraphDAO.retrieveEntityNodes(query_string);
			n.setGraph_node_id(node_count);
			edge.setSource_id(source_id);
			edge.setTarget_id(node_count);
			nodes.add(n);
		}

		Graph graph = new Graph();
		graph.setEdges(edges);
		graph.setNodes(nodes);

		String JSONString = new String();
		JSONString = "{\nnodes: [\n";

		for (GraphNode n : graph.getNodes()) {
			JSONString += "{name: \"" + n.getName() + "\"},\n";
		}

		JSONString = JSONString.substring(0, JSONString.length() - 2);
		JSONString += "\n],\nedges: [\n";

		for (GraphEdge e : graph.getEdges()) {
			JSONString += "{source: " + e.getSource_id() + ", target: "
					+ e.getTarget_id() + ", label: \"" + e.getRelation()
					+ "\"},\n";
		}
		JSONString = JSONString.substring(0, JSONString.length() - 1);
		JSONString += "\n]\n};";

		return JSONString;
	}

	public static void main(String[] args) {
		// System.out.println(keySearch("journals/acta/FinkelC87"));
		String key = "journals/tkde/ZhengL96";
		//Graph g = buildGraph(key, 0, 0);
		
		Graph g = buildGraphWithKey(key);

		System.out.println(getJSONString(g));
	}

}
