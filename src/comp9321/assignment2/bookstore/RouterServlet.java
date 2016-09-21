package comp9321.assignment2.bookstore;

import java.io.IOException;
import java.io.PrintWriter;
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
		System.out.println("Hello from GET method");
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
		}

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
		System.out.println("Remove Cart: " + id);

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
		System.out.println(id);

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
