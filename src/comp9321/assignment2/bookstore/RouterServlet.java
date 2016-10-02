package comp9321.assignment2.bookstore;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLStreamException;

import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class RouterServlet
 */

public class RouterServlet extends HttpServlet {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "pvce@2016";
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public RouterServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("Hello from GET method");
		ArrayList<ItemBean> item_list = CustomerDAO
				.runQuery("select * from item limit 12");
		request.setAttribute("item_list", item_list);
		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");
		HttpSession session = request.getSession();

		if (action.equals("add_cart")) {
			cart_add(request, response, session);
		} else if (action.equals("remove_cart")) {
			cart_remove(request, response, session);
		} else if (action.equals("advanced_search")) {
			search_items(request, response, session);
		} else if (action.equals("next")) {
			get_next_page(request, response, session);
		} else if (action.equals("previous")) {
			get_previous_page(request, response, session);
		} else if (action.equals("login")) {
			login(request, response, session);
		} else if (action.equals("logout")) {
			logout(request, response, session);
		} else if (action.equals("signup")) {
			signup(request, response, session);
		} else if (action.equals("toggle_user")) {
			toggle_user(request, response, session);
		} else if (action.equals("toggle_item")) {
			toggle_item(request, response, session);
		} else if (action.equals("search_results_userAct_usr")) {
			search_activity(request, response, session);
		}
		 
	}
	
	private void search_activity(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		String id = request.getParameter("user");
		String output = "";
		    try{  
		    Class.forName("com.mysql.jdbc.Driver");  
		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false","root","pvce@2016");  
		    PreparedStatement ps=con.prepareStatement("select * from user_activity t1 INNER join item t3 on t1.item_id=t3.id WHERE t1.user_id = "+id+" order by t1.timestamp DESC ");   
		    String act_type = "";
		    //System.out.println("select * from user_activity t1 INNER join item t3 on t1.item_id=t3.id WHERE t1.user_id = "+id+" order by t1.timestamp DESC ");
			ResultSet rs = ps.executeQuery();
			
			// STEP 5: Extract data from result set
			if(!rs.next()){
				output = "Empty Result set";
			}else{
				rs.beforeFirst();
				output += "<center><table width='100%' class='table table-bordered table-striped table-text-center'>";  
			    output += "<tr><th>Activity ID</th><th>Item Name</th><th>Activity Type</th><th>Timestamp</th></tr>"; 
				while(rs.next()){
					if(rs.getString("activity_type") == "0"){
				    	act_type = "Removed from Cart";
				    }
					else{
						act_type = "Added to Cart";
					}
					String data = "Title: "+ rs.getString("title")+ "\\n\\n"+"Author: "+rs.getString("author")+ "\\n\\n"+"Year: "+rs.getString("year")+ "\\n\\n"+"Price: A$ "+rs.getString("price");
					String link = "<a onclick=\"alert('"+data+"');\">"+rs.getString("title")+"</a>";
			    	output += "<tr><td>"+rs.getString("activity_id")+"</td><td>"+link+"</td><td>"+act_type+"</td> <td>"+rs.getTimestamp("timestamp")+"</td></tr>";  
				}
				output += "</table><center>";
			}
//			//System.out.println(output);
			response.setContentType("text/html;charset=UTF-8");
	        response.getWriter().write(output);
		    con.close();  
		    }catch(Exception e){e.printStackTrace();}  
	}
	
	private void toggle_user(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		String id = request.getParameter("user");
		String action = request.getParameter("toggle_action");
		    try{  
		    Class.forName("com.mysql.jdbc.Driver");  
		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false","root","pvce@2016");  
		    PreparedStatement ps=con.prepareStatement("UPDATE users SET acc_status = '"+action+"' WHERE id = '"+id+"'");   
		    Integer i = ps.executeUpdate();
		    if(i>0){
//		    	//System.out.println("Value of i: "+ i);
		    	response.setContentType("text/html;charset=UTF-8");
		        response.getWriter().write("True");
		    }
		    con.close();  
		    }catch(Exception e){e.printStackTrace();}  
	}
	
	private void toggle_item(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		String id = request.getParameter("item");
		String action = request.getParameter("toggle_action");
		    try{  
		    Class.forName("com.mysql.jdbc.Driver");  
		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false","root","pvce@2016");  
		    PreparedStatement ps=con.prepareStatement("UPDATE item SET enabled_status = "+action+" WHERE id = "+id);  
//		    //System.out.println("UPDATE itemisx SET enabled_status = "+action+" WHERE id = "+id);
		    Integer i = ps.executeUpdate();
		    if(i>0){
//		    	//System.out.println("Value of i: "+ i);
		    	response.setContentType("text/html;charset=UTF-8");
		        response.getWriter().write("True");
		    }
		    con.close();  
		    }catch(Exception e){e.printStackTrace();}  
	}

	private void get_previous_page(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		// get the session attributes
		String query = (String) session.getAttribute("query_string");
		int page_count = (int) session.getAttribute("page_count");

		// set the session attributes
		session.setAttribute("page_count", page_count - 1);

		// get the new query
		String queryString = QueryBuilder.changeQueryOffset(query,
				page_count - 1, 12);

		ArrayList<ItemBean> items_list = CustomerDAO.runQuery(queryString);

		request.setAttribute("item_list", items_list);
		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/search_result.jsp");
		rd.forward(request, response);

	}
	
	private void logout(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		session.setAttribute("user_name",null);
        session.setAttribute("user_id",null);
        session.setAttribute("full_name",null);
        session.setAttribute("email",null);
        session.setAttribute("admin_status",null);
        session.setAttribute("type",null);
        session.setAttribute("yob",null);
        session.setAttribute("fname",null);
		response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("True");
		
	}
	
	private void signup(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		String username = null, nickName = null,fname = null, lname = null,email = null,full_address = null, password = null;
		Integer yob = null,type = null;
		BigInteger CC = null;
		username = getRequestParameter(request,"username");
		nickName = getRequestParameter(request,"nickName");
		fname = getRequestParameter(request,"fname");
		lname = getRequestParameter(request,"lname");
		email = getRequestParameter(request,"email");
		full_address = getRequestParameter(request,"full_address");
		yob = Integer.valueOf(getRequestParameter(request,"yob"));
		try {
			password = UserDAO.getMD5(getRequestParameter(request,"password"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		type = Integer.valueOf(getRequestParameter(request,"type"));
		CC = BigInteger.valueOf(Long.valueOf(getRequestParameter(request,"CC")));
		if(UserDAO.createUser(username,nickName,fname,  lname,  email,  yob,  full_address,  CC,  password,  type)){
			response.setContentType("text/html;charset=UTF-8");
	        response.getWriter().write("True");
		}
		else{
			response.setContentType("text/html;charset=UTF-8");
	        response.getWriter().write("False");
		}
	}
	
	
	private void login(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		String uname = getRequestParameter(request, "user");
		String password = getRequestParameter(request, "password");
		String user_id = null, full_name = null, email = null,fname= null,yob= null, full_address=null, cc=null;
		Integer admin_status = null, type = null;
//		//System.out.println("Username: " + uname + ". Password: " + password);
		Connection conn = null;
		Statement stmt = null;
		Integer i= 0;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();

			String sql = "SELECT *, count(*) AS cnt FROM users WHERE username = '" + uname + "' and "
					+ "password='"+UserDAO.getMD5(password)+"' and"
					+ " acc_status = 1;";
			ResultSet rs = stmt.executeQuery(sql);
			// STEP 5: Extract data from result set
			while(rs.next()){
				i = rs.getInt("cnt");
				user_id = rs.getString("id");
				fname = rs.getString("fname");
				full_name = rs.getString("fname") + " " + rs.getString("lname");
				email = rs.getString("email");
				admin_status = rs.getInt("admin");
				type = rs.getInt("type");
				yob = rs.getString("yob");
				full_address = rs.getString("full_address");
				cc = rs.getString("cc_no");
			}
			if(i != 1) {
				//System.out.println("User Not Found");
			}
			else{
				//System.out.println("User Found");
				response.setContentType("text/html;charset=UTF-8");
	            response.getWriter().write("True");
	            session.setAttribute("user_name",uname);
	            session.setAttribute("user_id",user_id);
	            session.setAttribute("full_name",full_name);
	            session.setAttribute("email",email);
	            session.setAttribute("admin_status",admin_status.toString());
	            session.setAttribute("type",type.toString());
	            session.setAttribute("yob",yob);
	            session.setAttribute("fname",fname);
	            session.setAttribute("full_address",full_address);
	            session.setAttribute("cc",cc);
	            //System.out.println("Session Variable Contents: ");
	            //System.out.println("Username: " + session.getAttribute("user_name"));;
	            //System.out.println("User ID: " + session.getAttribute("user_id"));
	            //System.out.println("Full Name: " + session.getAttribute("full_name"));
	            //System.out.println("Email: " + session.getAttribute("email"));
	            //System.out.println("admin_status: " + session.getAttribute("admin_status"));
	            //System.out.println("type: " + session.getAttribute("type"));
			}
			rs.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			//System.out.println("MD5 Exception");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void get_next_page(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		// get the session attributes
		String query = (String) session.getAttribute("query_string");
		int page_count = (int) session.getAttribute("page_count");

		// set the session attributes
		session.setAttribute("page_count", page_count + 1);
		
		// get the new query
		String queryString = QueryBuilder.changeQueryOffset(query,
				page_count + 1, 12);

		ArrayList<ItemBean> items_list = CustomerDAO.runQuery(queryString);

		request.setAttribute("item_list", items_list);
		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/search_result.jsp");
		rd.forward(request, response);

	}

	private String getRequestParameter(HttpServletRequest request,
			String parameter) {
		String value = request.getParameter(parameter);

		if (value != null) {
			return value.trim();
		} else {
			return new String();
		}
	}

	private void search_items(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		String item_name = getRequestParameter(request, "item_name");
		String publication = getRequestParameter(request, "publication");
		String seller_id = getRequestParameter(request, "seller_id");
		String authors = getRequestParameter(request, "authors");
		String year = getRequestParameter(request, "year");
		String price_min = getRequestParameter(request, "price-min");
		String price_max = getRequestParameter(request, "price-max");

		String queryString = QueryBuilder.buildQuery(item_name, publication,
				seller_id, authors, year, price_min, price_max, 12, 0);

		ArrayList<ItemBean> items_list = CustomerDAO.runQuery(queryString);

		request.setAttribute("item_list", items_list);
		session.setAttribute("query_string", queryString);
		session.setAttribute("page_count", 0);
		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/search_result.jsp");
		rd.forward(request, response);

	}

	@SuppressWarnings("unchecked")
	private void cart_remove(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {

		PrintWriter printer = response.getWriter();

		// Get the item details to remove
		String id = request.getParameter("hashcode").substring(9);
		//System.out.println("Remove Cart: " + id);

		// Initialize the cart items list
		ArrayList<ItemBean> cart_items = new ArrayList<ItemBean>();

		// Check if the cart exists
		if (session.getAttribute("cart") != null) {
			cart_items = (ArrayList<ItemBean>) session.getAttribute("cart");
		}

		// Remove the item to the customer cart
		for (ItemBean item : cart_items) {
			if (item.getId() == Integer.parseInt(id)) {
				cart_items.remove(item);
				break;
			}
		}
		session.setAttribute("cart", cart_items);

		// Set the response value
		String response_content = new String();
		if (cart_items.size() == 0) {
			response.setContentType("text/html");
			response_content = "<div class=\"col-md-12 panel panel-primary\" id=\"empty_cart\"><div><h6>Shopping Cart is Empty!</h6></div></div>";
		} else {
			response.setContentType("text/plain");
			response_content = "";
		}
		printer.println(response_content);
	}

	@SuppressWarnings("unchecked")
	private void cart_add(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		// Initialize the cart items list
		ArrayList<ItemBean> cart_items = new ArrayList<ItemBean>();

		PrintWriter printer = response.getWriter();

		// Check if the cart exists
		if (session.getAttribute("cart") != null) {
			cart_items = (ArrayList<ItemBean>) session.getAttribute("cart");
		}

		// Get the item details to add
		String id = request.getParameter("hashcode");
		//System.out.println(id);

		// Retrieve the item from the DB
		ItemBean item = CustomerDAO.runQuery(
				"select * from item where id ='" + id + "'").get(0);

		// Add the item to the customer cart
		cart_items.add(item);
		session.setAttribute("cart", cart_items);

		String response_content = "<div class=\"col-md-12 panel panel-primary\" id=\"cart_item"
				+ item.getId()
				+ "\"><div><h6>"
				+ item.getItem_name()
				+ "<button class=\"btn btn-warning btn-xs cart\" onclick=\"removeCartItem('cart_item"
				+ item.getId()
				+ "','"
				+ item.getItem_name()
				+ "')\">Remove</button></h6></div></div>";
		printer.println(response_content);

	}

}
