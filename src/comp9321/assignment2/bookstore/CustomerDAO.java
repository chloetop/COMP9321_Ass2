package comp9321.assignment2.bookstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class CustomerDAO {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/books_9321?autoReconnect=true&useSSL=false";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "root";

	public static ArrayList<ItemBean> runQuery(String query) {
		Connection conn = null;
		Statement stmt = null;

		ArrayList<ItemBean> items_list = new ArrayList<ItemBean>();

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
				ItemBean item = new ItemBean(rs.getInt("id"),
						rs.getString("item_name"), rs.getString("Authors"),
						rs.getString("publication"), rs.getInt("year"),
						rs.getString("url"), rs.getInt("seller_id"),
						rs.getFloat("price"));
				items_list.add(item);
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

		return items_list;

	}

	public static void main(String[] args) {
		ArrayList<ItemBean> items_list = runQuery("select * from item");
		
		for (ItemBean item:items_list){
			System.out.println("ID: "+item.getId());
			System.out.println("Name: "+item.getItem_name());
			System.out.println("Authors: "+item.getAuthors().get(0)+", "+item.getAuthors().get(1));
			System.out.println("Pub: "+item.getPublication());
			System.out.println("Year: "+item.getYear());
			System.out.println("URL: "+item.getURL());
			System.out.println("SellerID: "+item.getSeller_id());
			System.out.println("Price: "+item.getPrice()+"\n");
		}
		

	}// end main

}
