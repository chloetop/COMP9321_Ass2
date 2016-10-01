package comp9321.assignment2.bookstore.helpers;

import java.util.ArrayList;

import comp9321.assignment2.bookstore.beans.Graph;
import comp9321.assignment2.bookstore.beans.GraphEdge;
import comp9321.assignment2.bookstore.beans.GraphNode;
import comp9321.assignment2.bookstore.dao.GraphDAO;

public class BuildGraph {

	public static int containsNode(ArrayList<GraphNode> nodes,
			GraphNode match_node) {
		for (GraphNode node : nodes) {
			if (node.getEntity_id() == match_node.getEntity_id()) {
				return nodes.indexOf(node);
			}
		}

		match_node.setGraph_node_id(nodes.size());
		nodes.add(match_node);

		return nodes.size() - 1;
	}

	// Build the graph for year relation
	public static Graph buildGraphWithYear(String year) {
		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
		ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();
		// Add year to the nodes list
		GraphNode node = GraphDAO
				.retrieveEntityNodes("SELECT * FROM entity_store WHERE value='"
						+ year + "'");
		node.setGraph_node_id(0);
		nodes.add(node);

		for (Integer key : getKeySet(year)) {
			// Get the relations
			ArrayList<GraphEdge> relations = GraphDAO
					.retrieveGraphEdges("SELECT * FROM graph_store WHERE source="
							+ key);

			// add the source/key
			GraphNode n = new GraphNode();
			n = GraphDAO
					.retrieveEntityNodes("SELECT * FROM entity_store WHERE entity_id="
							+ key);
			n.setGraph_node_id(nodes.size());
			nodes.add(n);

			int source_index = nodes.size() - 1;// index for key

			// add the edges
			for (GraphEdge edge : relations) {
				// get the graphNode for the relation
				GraphNode target = GraphDAO
						.retrieveEntityNodes("SELECT * FROM entity_store WHERE entity_id="
								+ edge.getTarget());
				int target_index = containsNode(nodes, target);

				edge.setSource_id(source_index);
				edge.setTarget_id(target_index);

				edges.add(edge);

			}

		}

		Graph graph = new Graph();
		graph.setNodes(nodes);
		graph.setEdges(edges);

		return graph;

	}

	// Returns the entity id value of the value passed
	public static int getEntityId(String value) {
		return GraphDAO.retrieveEntityNodes(
				"SELECT * FROM entity_store WHERE value='" + value + "'")
				.getEntity_id();
	}

	// Returns the keys involved in the search
	public static ArrayList<Integer> getKeySet(String year) {
		int year_entity_id = getEntityId(year);

		String query = "SELECT * FROM graph_store WHERE target="
				+ year_entity_id;

		ArrayList<Integer> keyset = new ArrayList<Integer>();

		for (GraphEdge edge : GraphDAO.retrieveGraphEdges(query)) {
			keyset.add(edge.getSource());
		}

		return keyset;
	}

	private static String returnName(String name) {
		if (name == null) {
			return "n";
		} else if (name.isEmpty()) {
			return "n";
		}

		return name;
	}

	public static String getJSONString(Graph graph) {
		String JSONString = new String();
		JSONString = "{\nnodes: [\n";

		for (GraphNode n : graph.getNodes()) {
			JSONString += "{name: \"" + returnName(n.getName()) + "\"},\n";
		}

		JSONString = JSONString.substring(0, JSONString.length() - 2);
		JSONString += "\n],\nedges: [\n";

		int node_size = graph.getNodes().size();

		for (GraphEdge e : graph.getEdges()) {
			int source = e.getSource_id();
			int target = e.getTarget_id();

			if (source < node_size && target < node_size) {
				JSONString += "{source: " + e.getSource_id() + ", target: "
						+ e.getTarget_id() + ", label: \"" + e.getRelation()
						+ "\"},\n";
			}
		}
		JSONString = JSONString.substring(0, JSONString.length() - 2);
		JSONString += "\n]\n};";
		System.out.println(JSONString);
		return JSONString;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Graph graph = buildGraphWithYear("1996");

		System.out.println(getJSONString(graph));

	}

}
