package comp9321.assignment2.bookstore.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.mysql.jdbc.Statement;
import comp9321.assignment2.bookstore.beans.CartLog;
import comp9321.assignment2.bookstore.beans.ItemBean;

public class CustomerDAO {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	static final String DB_URL = "jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "root";

	public static void addToMap(HashMap<String, String> item, String key,
			String value) {
		if (value != null) {
			if (!value.trim().isEmpty()) {
				item.put(key, value);
			}
		}
	}

	public static int randInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public static void addImageURL(HashMap<String, String> item) {

		if (!item.containsKey("image_url")) {


			String image_url = "images/book" + Integer.toString(randInt(1, 12))+".jpg";

			item.put("image_url", image_url);

		}
	}

	public static int runInsertQuery(String query) {
		Connection conn = null;
		Statement stmt = null;
		int result = 0;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			String sql = query;
			stmt = (Statement) conn.createStatement();
			result = stmt.executeUpdate(query);	
			System.out.println("RUNNING QUERY: "+ query);

		}
		catch (SQLException se) {
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
		
		return result;
	}

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
						rs.getString("key"), rs.getString("author"));
				// Add the row values
				addToMap(item.ItemList, "title", rs.getString("title")); // title
				addToMap(item.ItemList, "key", rs.getString("key")); // key
				addToMap(item.ItemList, "author", rs.getString("author")); // author
				addToMap(item.ItemList, "editor", rs.getString("editor")); // editor
				addToMap(item.ItemList, "booktitle", rs.getString("booktitle")); // booktitle
				addToMap(item.ItemList, "pages", rs.getString("pages")); // pages
				addToMap(item.ItemList, "year",
						Integer.toString(rs.getInt("year"))); // year
				addToMap(item.ItemList, "address", rs.getString("address")); // address
				addToMap(item.ItemList, "journal", rs.getString("journal")); // journal
				addToMap(item.ItemList, "volume", rs.getString("volume")); // volume
				addToMap(item.ItemList, "url", rs.getString("url")); // url
				addToMap(item.ItemList, "ee", rs.getString("ee")); // ee
				addToMap(item.ItemList, "cdrom", rs.getString("cdrom")); // cdrom
				addToMap(item.ItemList, "cite", rs.getString("cite")); // cite
				addToMap(item.ItemList, "publisher", rs.getString("publisher")); // publisher
				addToMap(item.ItemList, "note", rs.getString("note")); // note
				addToMap(item.ItemList, "crossref", rs.getString("crossref")); // crossref
				addToMap(item.ItemList, "isbn", rs.getString("isbn")); // isbn
				addToMap(item.ItemList, "series", rs.getString("series")); // series
				addToMap(item.ItemList, "school", rs.getString("school")); // school
				addToMap(item.ItemList, "chapter", rs.getString("chapter")); // chapter
				addToMap(item.ItemList, "mdate", rs.getString("mdate")); // mdate
				addToMap(item.ItemList, "publtype", rs.getString("publtype")); // publtype
				addToMap(item.ItemList, "type", rs.getString("type")); // type
				addToMap(item.ItemList, "price",
						Float.toString(rs.getFloat("price"))); // price
				addToMap(item.ItemList, "image_url", rs.getString("image_url")); // image_url
				
				// Add a default image URL if image URL is not defined
				addImageURL(item.ItemList);

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

	public static void executeQuery(String query) {

		Connection conn = null;
		Statement stmt = null;

		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();

			String sql = query;
			stmt.executeUpdate(sql);

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

	}
	
	public static int retrieveQueryCount(String query) {

		Connection conn = null;
		Statement stmt = null;
		int count = 0;

		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();

			String sql = query;
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				count = rs.getInt("value");
			}

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
		return count;

	}
	
	public static CartLog retrieveSavedCart(String query) {

		Connection conn = null;
		Statement stmt = null;
		CartLog cart = new CartLog();

		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();

			String sql = query;
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				cart.setCart_id(rs.getInt("cart_id"));
				cart.setUser_id(rs.getInt("user_id"));
				cart.setItems(rs.getString("items_list"));
				cart.setAction(rs.getString("action"));
				cart.setPrice(rs.getFloat("price"));
			}

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
		return cart;

	}

}
