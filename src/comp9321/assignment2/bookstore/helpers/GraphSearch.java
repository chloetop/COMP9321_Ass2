package comp9321.assignment2.bookstore.helpers;

import java.util.ArrayList;

import comp9321.assignment2.bookstore.beans.Graph;
import comp9321.assignment2.bookstore.beans.GraphEdge;
import comp9321.assignment2.bookstore.beans.GraphNode;
import comp9321.assignment2.bookstore.dao.GraphDAO;

public class GraphSearch {
	
	public static String keySearch(String key) {
		GraphNode node = GraphDAO.retrieveEntityNodes("select * from entity_store where value = '"
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
		System.out.println(keySearch("journals/acta/FinkelC87"));
	}

}
