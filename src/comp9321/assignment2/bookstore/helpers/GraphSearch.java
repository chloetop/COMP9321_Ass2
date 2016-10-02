package comp9321.assignment2.bookstore.helpers;

import java.util.ArrayList;
import java.util.HashSet;

import comp9321.assignment2.bookstore.beans.Graph;
import comp9321.assignment2.bookstore.beans.GraphEdge;
import comp9321.assignment2.bookstore.beans.GraphNode;
import comp9321.assignment2.bookstore.dao.GraphDAO;

public class GraphSearch {

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
							node.getGraph_node_id(), g.getNodes().size() - 1);
					nodes.addAll(buffer.getNodes());
					edges.addAll(buffer.getEdges());

				}
			}
		}

		g.addNodes(nodes);
		g.addEdges(edges);

		cleanGraph(g);

		return g;
	}

	public static Graph buildAuthorGraph(String author, int root_id,
			int current_node) {

		GraphNode node = GraphDAO
				.retrieveEntityNodes("select * from entity_store where value = '"
						+ author + "'");

		String query = "SELECT * FROM graph_store where target="
				+ node.getEntity_id();

		ArrayList<GraphEdge> edges = GraphDAO.retrieveGraphEdges(query);

		Graph graph = new Graph();
		graph.initializeGraph();

		for (GraphEdge edge_root : edges) {
			String query_check = "SELECT * FROM graph_store where source="
					+ edge_root.getSource();
			ArrayList<GraphEdge> edges_result = GraphDAO
					.retrieveGraphEdges(query_check);

			ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
			int node_count = current_node;
			int source_id = root_id;
			node.setGraph_node_id(root_id);

			GraphNode root_node = GraphDAO
					.retrieveEntityNodes("select * from entity_store where entity_id = '"
							+ edge_root.getSource() + "'");
			nodes.add(root_node);

			for (GraphEdge edge : edges_result) {
				node_count++;
				String query_string = "SELECT * FROM entity_store WHERE entity_id="
						+ edge.getTarget();
				GraphNode n = GraphDAO.retrieveEntityNodes(query_string);
				n.setGraph_node_id(node_count);
				edge.setSource_id(source_id);
				edge.setTarget_id(node_count);
				nodes.add(n);
			}
			graph.addEdges(edges_result);
			graph.addNodes(nodes);
		}

		ArrayList<GraphNode> nodes_next = new ArrayList<GraphNode>();
		ArrayList<GraphEdge> edges_next = new ArrayList<GraphEdge>();

		for (GraphNode nod : graph.getNodes()) {
			if (nod.getAttribute().equals("item")) {
				if (!nod.getName().equals(graph.getNodes().get(0))) {
					Graph buffer = buildGraph(nod.getName(),
							nod.getGraph_node_id(), graph.getNodes().size() - 1);
					nodes_next.addAll(buffer.getNodes());
					edges_next.addAll(buffer.getEdges());

				}
			}
		}

		graph.addNodes(nodes_next);
		graph.addEdges(edges_next);

		cleanGraph(graph);

		return graph;

	}

	public static Graph cleanGraph(Graph graph) {
		ArrayList<String> node_set = new ArrayList<String>();

		ArrayList<GraphNode> node_values = new ArrayList<GraphNode>();

		for (GraphNode node : graph.getNodes()) {
			String name = node.getName();
			int graph_node_index = node.getGraph_node_id();
			int entity_node_index = node.getEntity_id();
			String attribute = node.getAttribute();

			if (node_set.contains(name)) {
				graph.setEdges(cleanEdges(graph.getEdges(), graph_node_index,
						node_set.indexOf(name), entity_node_index, attribute));
				node_values.add(node);
			} else {
				node_set.add(node.getName());
			}
		}

		for (GraphNode node : node_values) {
			graph.removeNode(node);
		}

		return graph;
	}

	private static ArrayList<GraphEdge> cleanEdges(ArrayList<GraphEdge> edges,
			int duplicate_index, int unique_index, int entity_id,
			String attribute) {
		boolean match_source = false;

		// check if replacing a item entity node
		if (attribute.equals("item")) {
			match_source = true;
		}

		// do replace
		for (GraphEdge edge : edges) {
			if (match_source) {
				if (edge.getSource() == entity_id) {
					edge.setSource_id(unique_index);
				}
			} else {
				if (edge.getTarget() == entity_id) {
					edge.setTarget_id(unique_index);
				}

			}
		}
		return edges;
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
		// Graph g = buildGraph(key, 0, 0);

		Graph g = buildAuthorGraph("1996", 0, 0);

		System.out.println(getJSONString(g));
	}

}
