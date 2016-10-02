package comp9321.assignment2.bookstore.beans;

public class GraphNode {
	String name;
	int entity_id;
	int graph_node_id;
	String attribute;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(int entity_id) {
		this.entity_id = entity_id;
	}

	public int getGraph_node_id() {
		return graph_node_id;
	}

	public void setGraph_node_id(int graph_node_id) {
		this.graph_node_id = graph_node_id;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getStringValue() {
		return "Name:" + getName() + " EntityID: " + getEntity_id()
				+ " Graph Node ID: " + getGraph_node_id() + " Attribute: "
				+ getAttribute();
	}

}
