package comp9321.assignment2.bookstore.beans;

public class GraphEdge {

	int source;
	int target;
	int source_id;// for the graph
	int target_id;// for the graph
	String relation;

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getSource_id() {
		return source_id;
	}

	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}

	public int getTarget_id() {
		return target_id;
	}

	public void setTarget_id(int target_id) {
		this.target_id = target_id;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public String getStringValue(){
		return "Source: "+getSource()+" Target: "+getTarget()+" Relation: "+getRelation();
	}

}
