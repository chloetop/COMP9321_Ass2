package comp9321.assignment2.bookstore;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import comp9321.assignment2.bookstore.beans.CartItem;
import comp9321.assignment2.bookstore.beans.ItemBean;
import comp9321.assignment2.bookstore.dao.CustomerDAO;
import comp9321.assignment2.bookstore.helpers.BuildGraph;
import comp9321.assignment2.bookstore.helpers.CartLogger;
import comp9321.assignment2.bookstore.helpers.FormBuilder;
import comp9321.assignment2.bookstore.helpers.GraphSearch;
import comp9321.assignment2.bookstore.helpers.QueryBuilder;

/**
 * Servlet implementation class RouterServlet
 */

public class RouterServlet extends HttpServlet {
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
		} else if (action.equals("advanced_search")) {
			search_items(request, response, session);
		} else if (action.equals("next")) {
			get_next_page(request, response, session);
		} else if (action.equals("previous")) {
			get_previous_page(request, response, session);
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
			System.out.println("Reaches graph search");
			generate_form(request, response, session);
		}

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
		ArrayList<ItemBean> cartItems = new ArrayList<ItemBean>();
		ArrayList<CartItem> checkoutItems = new ArrayList<CartItem>();

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

		request.setAttribute("checkout_items", checkoutItems);
		request.setAttribute("total_price", total_price);
		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/checkout.jsp");
		rd.forward(request, response);

		// Log the checkout

		int user_id = -1;

		if ((null != session.getAttribute("user_id"))) {
			user_id = (int) session.getAttribute("user_id");
		}

		CartLogger
				.logCartValues(user_id, items_string, "purchase", total_price);

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
		int page_count = (int) session.getAttribute("page_count");
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

	private void get_next_page(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		// get the session attributes
		String query = (String) session.getAttribute("query_string");
		int page_count = (int) session.getAttribute("page_count");
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
		System.out.println("Remove Cart: " + id);

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
			user_id = (int) session.getAttribute("user_id");
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
		System.out.println(id);

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
			user_id = (int) session.getAttribute("user_id");
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
