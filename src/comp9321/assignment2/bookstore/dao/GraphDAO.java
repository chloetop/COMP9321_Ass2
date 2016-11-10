package comp9321.assignment2.bookstore.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;
import comp9321.assignment2.bookstore.beans.Graph;
import comp9321.assignment2.bookstore.beans.GraphEdge;
import comp9321.assignment2.bookstore.beans.GraphNode;

public class GraphDAO {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "root";


	public static GraphNode retrieveEntityNodes(String query) {
		Connection conn = null;
		Statement stmt = null;

		GraphNode node = new GraphNode();

		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();

			String sql = query;
			ResultSet rs = stmt.executeQuery(sql);
			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				node.setEntity_id(rs.getInt("entity_id"));
				node.setAttribute(rs.getString("entity_attribute"));
				node.setName(rs.getString("value"));

			}
			rs.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try

		return node;

	}

	public static ArrayList<GraphEdge> retrieveGraphEdges(String query) {

		Connection conn = null;
		Statement stmt = null;

		ArrayList<GraphEdge> edgeList = new ArrayList<GraphEdge>();

		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();

			String sql = query;
			ResultSet rs = stmt.executeQuery(sql);
			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				GraphEdge edge = new GraphEdge();
				edge.setSource(rs.getInt("source"));
				edge.setRelation(rs.getString("attribute"));
				edge.setTarget(rs.getInt("target"));
				edgeList.add(edge);

			}
			rs.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try

		return edgeList;

	}

	public static ArrayList<GraphNode> retrtieveEntityNodesList(String query){
		Connection conn = null;
		Statement stmt = null;

		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();

		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();

			String sql = query;
			ResultSet rs = stmt.executeQuery(sql);
			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				GraphNode node = new GraphNode();
				node.setEntity_id(rs.getInt("entity_id"));
				node.setAttribute(rs.getString("entity_attribute"));
				node.setName(rs.getString("value"));
				nodes.add(node);

			}
			rs.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try

		return nodes;
	}

	public static void main(String[] args) {
		
	}

}
