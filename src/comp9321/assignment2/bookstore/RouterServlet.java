package comp9321.assignment2.bookstore;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.mysql.jdbc.Statement;

import comp9321.assignment2.bookstore.beans.CartItem;
import comp9321.assignment2.bookstore.beans.CartLog;
import comp9321.assignment2.bookstore.beans.ItemBean;
import comp9321.assignment2.bookstore.dao.CustomerDAO;
import comp9321.assignment2.bookstore.dao.UserDAO;
import comp9321.assignment2.bookstore.helpers.BuildGraph;
import comp9321.assignment2.bookstore.helpers.CartLogger;
import comp9321.assignment2.bookstore.helpers.FormBuilder;
import comp9321.assignment2.bookstore.helpers.GraphSearch;
import comp9321.assignment2.bookstore.helpers.QueryBuilder;
import comp9321.assignment2.bookstore.helpers.UploadImageHandler;

/**
 * Servlet implementation class RouterServlet
 */

@MultipartConfig
public class RouterServlet extends HttpServlet {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "root";
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
		HttpSession session = request.getSession();
		System.out.println("Hello from GET method");
		build_index_page(request, response, session);
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
		} else if (action.equals("upload_item")) {
			insert_item(request, response, session);
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
		} else if (action.equals("quick_search")) {
			quick_search(request, response, session);
		} else if (action.equals("checkout")) {
			checkout(request, response, session);
		} else if (action.equals("graph_search")) {
			response.setContentType("text/html;charset=UTF-8");
			RequestDispatcher rd = getServletContext().getRequestDispatcher(
					"/graph.jsp");
			rd.forward(request, response);
		} else if (action.equals("graph_search_form")) {
			generate_form(request, response, session);
		}else if(action.equals("checkout_done")){
			checkout_done(request, response, session);

		}
		 
	}
	
	private void search_activity(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		String id = request.getParameter("user");
		String output = "";
		    try{  
		    Class.forName("com.mysql.jdbc.Driver");  
		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false","root","root");  
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
					if(rs.getInt("activity_type") == 0){
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
		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false","root","root");  
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
		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false","root","root");  
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

	private void generate_form(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		String search_key = request.getParameter("search_key");
		String search_type = request.getParameter("search_type");

		String json_data = new String();

		if (search_type.equals("publication")) {
			json_data = GraphSearch.getJSONString(GraphSearch
					.buildGraphWithKey(search_key.trim()));
		} else if (search_type.equals("author")) {
			json_data = GraphSearch.getJSONString(GraphSearch.buildAuthorGraph(
					search_key.trim(), 0, 0));
		} else if (search_type.equals("venue")) {
			json_data = BuildGraph.getJSONString(BuildGraph
					.buildGraphWithYear(search_key));
		} else if (search_type.equals("year")) {
			json_data = BuildGraph.getJSONString(BuildGraph
					.buildGraphWithYear(search_key));
		}

		request.setAttribute("json_data", json_data);
		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/graph_search.jsp");
		rd.forward(request, response);
	}

	@SuppressWarnings("unchecked")
	private void checkout(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/checkout.jsp");
		rd.forward(request, response);
	}

	

	private void quick_search(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		String title = getRequestParameter(request, "title");
		String author = getRequestParameter(request, "author");
		String type = getRequestParameter(request, "type");
		HashMap<String, String> query = new HashMap<String, String>();
		boolean next = false;

		if (!title.isEmpty())
			query.put("title", title);
		if (!author.isEmpty())
			query.put("author", author);
		query.put("type", type);

		String queryString = QueryBuilder.buildQuery(query, "", "", 12, 0);

		ArrayList<ItemBean> items_list = CustomerDAO.runQuery(queryString);

		int total_results = QueryBuilder.getQueryCount(queryString);

		if (total_results > 12) {
			next = true;
		}

		request.setAttribute("item_list", items_list);
		session.setAttribute("query_string", queryString);
		session.setAttribute("page_count", 0);
		request.setAttribute("previous", false);
		request.setAttribute("next", next);
		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/search_result.jsp");
		rd.forward(request, response);

	}

	private void build_index_page(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		// Initial elements
		ArrayList<ItemBean> item_list = CustomerDAO
				.runQuery("select * from item WHERE RAND()<=0.0006 limit 12");
		request.setAttribute("item_list", item_list);

		FormBuilder form = new FormBuilder();
		HashMap<String, String> form_fields = form.getFormFields();

		session.setAttribute("search_fields", form_fields);

		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/index.jsp");
		rd.forward(request, response);

	}

	private void get_previous_page(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		// get the session attributes
		String query = (String) session.getAttribute("query_string");
		int page_count = (Integer)session.getAttribute("page_count");
		boolean previous = false;

		// set the session attributes
		session.setAttribute("page_count", page_count - 1);

		// get the new query
		String queryString = QueryBuilder.changeQueryOffset(query,
				page_count - 1, 12);

		ArrayList<ItemBean> items_list = CustomerDAO.runQuery(queryString);

		if ((page_count - 1) != 0) {
			previous = true;
		}

		request.setAttribute("item_list", items_list);
		request.setAttribute("next", true);
		request.setAttribute("previous", previous);
		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/search_result.jsp");
		rd.forward(request, response);

	}
	
	private void checkout_done(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		ArrayList<ItemBean> cartItems = new ArrayList<ItemBean>();
		ArrayList<CartItem> checkoutItems = new ArrayList<CartItem>();

		String email = request.getParameter("email");
		String address = request.getParameter("full_address");
		String purchase_card = request.getParameter("CC");

		cartItems = (ArrayList<ItemBean>) session.getAttribute("cart");
		float total_price = 0;
		String items_string = CartLogger.generateItemsList(cartItems);

		for (ItemBean item : cartItems) {
			boolean item_indicator = false;
			for (CartItem cart_item : checkoutItems) {
				if (cart_item.title.equals(item.ItemList.get("title"))) {
					cart_item.quantity += 1;
					item_indicator = true;
				}
			}

			if (!item_indicator) {
				CartItem item_value = new CartItem();
				item_value.title = item.ItemList.get("title");
				item_value.quantity = 1;
				item_value.unit_price = Float.parseFloat(item.ItemList
						.get("price"));
				checkoutItems.add(item_value);
			}
			total_price += Float.parseFloat(item.ItemList.get("price"));
		}

		session.setAttribute("cart", new ArrayList<ItemBean>());
		session.setAttribute("checkout_items", checkoutItems);
		session.setAttribute("total_price", total_price);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("True");

		// Log the checkout

		int user_id = -1;

		if ((null != session.getAttribute("user_id"))) {
			user_id = Integer.parseInt(session.getAttribute("user_id").toString());
		}

		CartLogger
				.logCartValues(user_id, items_string, "purchase", total_price,email,address,purchase_card);

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

			String sql = "SELECT *, count(*) as cnt FROM users WHERE username = '" + uname + "' and "
					+ "password='"+UserDAO.getMD5(password)+"' and"
					+ " acc_status = 1;";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			// STEP 5: Extract data from result set
			while(rs.next()){
				user_id = rs.getString("id");
				i = rs.getInt("cnt");
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
	            session.setAttribute("cart", CartLogger.loadSavedCart(Integer.parseInt(user_id)));
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

		private void logout(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		int user_id = Integer.parseInt(session.getAttribute("user_id").toString());
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
        
        ArrayList<ItemBean> cart = new ArrayList();
        if(session.getAttribute("cart")!=null){
        	cart = (ArrayList<ItemBean>)session.getAttribute("cart");
        	if(cart.size()>0){
        		CartLogger.logCartValues(user_id, CartLogger.generateItemsList(cart), "save", 0, " ", " ", " ");
        	}
        }
        session.invalidate();
        session = request.getSession();
        FormBuilder form = new FormBuilder();
		HashMap<String, String> form_fields = form.getFormFields();
		session.setAttribute("search_fields", form_fields);
		
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
			
			String body = "Hi "+ nickName + ",<br><br>Please click on the following link to complete your dblpStore Registration<br><br>";
			body += "<a href='"+"http://localhost:8080/COMP9321_Ass2/emailConfirm.jsp?accId="+username +"'> Complete Registration</a><br><br>regards,<br>dblpAdmin" ;
			String subject = " Complete your registration";
			UserDAO.sendMail(email, subject  , body);
			response.setContentType("text/html;charset=UTF-8");
	        response.getWriter().write("True");
		}
		else{
			response.setContentType("text/html;charset=UTF-8");
	        response.getWriter().write("False");
		}
	}
	
	
	


	private void get_next_page(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		// get the session attributes
		String query = session.getAttribute("query_string").toString();
		int page_count = (Integer)session.getAttribute("page_count");
		boolean next = false;

		// set the session attributes
		session.setAttribute("page_count", page_count + 1);

		// get the new query
		String queryString = QueryBuilder.changeQueryOffset(query,
				page_count + 1, 12);

		ArrayList<ItemBean> items_list = CustomerDAO.runQuery(queryString);

		int total_results = QueryBuilder.getQueryCount(queryString);

		if (total_results > (12 * (page_count + 2))) {
			next = true;
		}

		request.setAttribute("item_list", items_list);
		request.setAttribute("previous", true);
		request.setAttribute("next", next);
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

	private void insert_item(HttpServletRequest request,
            HttpServletResponse response, HttpSession session) 
            throws ServletException, IOException {
        
        Part filePart = request.getPart("photo");
        String imagePath = null;
        //String path = ""+Paths.get("CoverPhotos").toAbsolutePath();
        //System.out.println("THIS IS THE ABS PATH--> "+path);
        if (filePart != null) {
            if (filePart.getSize() != 0) {
                //Obtains the absolute path of the CoverPhotos folder
                ServletContext context = getServletContext();
                URL fullPath = context.getResource("/images/");
                System.out.println("fullpath: "+fullPath.toString().substring(5));
                //System.out.println("FullPath: "+fullPath);
                // saves and returns the path of the file uploaded in request                
                imagePath = UploadImageHandler.saveImageInFolder(filePart, fullPath.toString().substring(5));
                System.out.println("Image Path: "+imagePath);
                String workingDir = System.getProperty("user.dir");
                System.out.println("Working Directory: "+workingDir);
            }
        }
        
        String item_name = getRequestParameter(request, "item_name");
        String publication = getRequestParameter(request, "publication");
        //Get the id of the logged in user account for this field
        String seller_id = "5";//getRequestParameter(request, "seller_id");
        String authors = getRequestParameter(request, "authors");
        String year = getRequestParameter(request, "year");
        String price = getRequestParameter(request, "price");
        String url = getRequestParameter(request, "url");
        Boolean on_hold = !(getRequestParameter(request, "on_hold")).equalsIgnoreCase("on");
        
        String query = QueryBuilder.buildInsertQuery(item_name, publication, seller_id, authors, url, year, price, on_hold, imagePath);
        //System.out.println(query);
        CustomerDAO.runInsertQuery(query);
        //UploadImageHandler.getImageFromPath(imagePath);
        build_index_page(request, response, session);
    }

	private void search_items(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		boolean next = false;
		FormBuilder form = new FormBuilder();
		HashMap<String, String> form_fields = form.getFormFields();
		HashMap<String, String> query = new HashMap<String, String>();

		for (String key : form_fields.keySet()) {
			String value = (String) request.getParameter(key);
			if (value != null && !value.isEmpty()) {
				query.put(key, value);
			}
		}

		String[] type_list = (String[]) request.getParameterValues("type");
		if (type_list != null) {
			String type = "";
			for (String type_value : type_list) {
				if (type.isEmpty())
					type = type_value;
				else
					type = type + "," + type_value;
			}

			query.put("type", type);
		}

		String price_min = getRequestParameter(request, "price-min");
		String price_max = getRequestParameter(request, "price-max");

		String queryString = QueryBuilder.buildQuery(query, price_min,
				price_max, 12, 0);

		ArrayList<ItemBean> items_list = CustomerDAO.runQuery(queryString);

		int total_results = QueryBuilder.getQueryCount(queryString);

		if (total_results > 12) {
			next = true;
		}

		request.setAttribute("item_list", items_list);
		session.setAttribute("query_string", queryString);
		session.setAttribute("page_count", 0);
		request.setAttribute("previous", false);
		request.setAttribute("next", next);
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

		// Initialize the cart items list
		ArrayList<ItemBean> cart_items = new ArrayList<ItemBean>();

		// Check if the cart exists
		if (session.getAttribute("cart") != null) {
			cart_items = (ArrayList<ItemBean>) session.getAttribute("cart");
		}

		ItemBean removed_item = new ItemBean(0, id, id);
		// Remove the item to the customer cart
		for (ItemBean item : cart_items) {
			if (item.getId() == Integer.parseInt(id)) {
				cart_items.remove(item);
				removed_item = item;
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

		// Log User activity
		int user_id = -1;

		if ((null != session.getAttribute("user_id"))) {
			user_id = Integer.parseInt(session.getAttribute("user_id").toString()) ;
		}

		CartLogger.logUserActivity(user_id, removed_item.getId(), 0);

	}

	@SuppressWarnings("unchecked")
	private void cart_add(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		// Initialize the cart items list
		ArrayList<ItemBean> cart_items = new ArrayList<ItemBean>();
		boolean checkout_button = false;
		String response_content = new String();
		String cart_content = new String();
		String cart_modal = new String();

		ArrayList<ItemBean> current_cart = (ArrayList<ItemBean>) session
				.getAttribute("cart");
		PrintWriter printer = response.getWriter();

		// Check if the cart exists
		if (session.getAttribute("cart") != null) {
			if (current_cart.isEmpty()) {
				checkout_button = true;
			}
			cart_items = (ArrayList<ItemBean>) session.getAttribute("cart");
		} else {
			checkout_button = true;
		}

		// Get the item details to add
		String id = request.getParameter("hashcode");

		// Retrieve the item from the DB
		ItemBean item = CustomerDAO.runQuery(
				"select * from item where id ='" + id + "'").get(0);

		// Add the item to the customer cart
		cart_items.add(item);
		session.setAttribute("cart", cart_items);
		// response.setContentType("text/json");

		if (checkout_button) {
			cart_content = "<div class=\"col-md-12\">"
					+ "<form method=\"post\" action=\"check_out\">"
					+ "<input type=\"hidden\" name=\"action\" value=\"checkout\" /> <input"
					+ " type=\"submit\" class=\"btn btn-success cart pager\""
					+ "value=\"Checkout&nbsp;>\">" + "</form>" + "</div>";
		}

		cart_content += "<div class=\"col-md-12 panel panel-primary\" id=\"cart_item"
				+ item.getId()
				+ "\"><div><h6><a id=\"cartBtn_"
				+ item.getId()
				+ "\" onclick=\"modal_cart_open("
				+ item.getId()
				+ ")\" style=\"cursor:pointer;font-size:14px;\">"
				+ item.ItemList.get("title")
				+ "</a><button class=\"btn btn-warning btn-xs cart\" onclick=\"removeCartItem('cart_item"
				+ item.getId()
				+ "','"
				+ item.ItemList.get("title")
				+ "')\">Remove</button></h6></div></div>";

		cart_modal = generateCartModal(item);

		cart_content = cart_content.replace("\"", "\\\"");
		cart_modal = cart_modal.replace("\"", "\\\"");

		response_content = "{\"cart\":\"" + cart_content + "\",\"modal\":\""
				+ cart_modal + "\"}";

		printer.println(response_content);

		// Log User activity
		int user_id = -1;

		if ((null != session.getAttribute("user_id"))) {
			user_id = Integer.parseInt(session.getAttribute("user_id").toString());
		}

		CartLogger.logUserActivity(user_id, item.getId(), 1);

	}

	private String generateCartModal(ItemBean item) {
		String cart_modal = "<div id=\"cartModal_" + item.getId() + "\""
				+ "class=\"modal col-md-4\">" + "<div class=\"modal-content\">"
				+ "<span class=\"close\"" + "onclick=\"close_cart_modal("
				+ item.getId() + ")\">x&nbsp;</span>"
				+ "<div class=\"panel panel-primary\">"
				+ "<!--Panel Definition-->" + "<div class=\"panel-heading\">"
				+ item.ItemList.get("title") + "</div>"
				+ "<!--Panel heading-->" + "<div class=\"panel-body\">"
				+ "<!--Panel Body-->" + "<table class=\"table table-striped\">";
		for (String key : item.ItemList.keySet()) {
			if (!key.equals("image_url")) {
				cart_modal += "<tr><th>" + key + "</th><td>"
						+ item.ItemList.get(key) + "</td></tr>";
			}
		}
		cart_modal += "</table></div><!--Panel body end--></div><!--Panel Definition end--></div></div>";

		return cart_modal;
	}

}
