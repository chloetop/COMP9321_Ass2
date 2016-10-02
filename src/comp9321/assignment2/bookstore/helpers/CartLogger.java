package comp9321.assignment2.bookstore.helpers;

import java.util.ArrayList;

import comp9321.assignment2.bookstore.beans.CartLog;
import comp9321.assignment2.bookstore.beans.ItemBean;
import comp9321.assignment2.bookstore.dao.CustomerDAO;

public class CartLogger {

	public static void logUserActivity(int user_id, int item_id,
			int activity_type) {

		String query = "INSERT INTO user_activity(user_id,item_id,activity_type) VALUES("
				+ user_id + "," + item_id + "," + activity_type + ")";

		CustomerDAO.executeQuery(query);

	}

	public static void logCartValues(int user_id, String items, String action,
			float price, String email, String address, String purchase_card) {
		String query = new String();
		if (price > 0) {
			query = "INSERT INTO cart_log(user_id,items_list,action,price,email,address,purchase_card) VALUES("
					+ user_id
					+ ",'"
					+ items
					+ "','"
					+ action
					+ "',"
					+ price
					+ ",'"
					+ email
					+ "','"
					+ address
					+ "','"
					+ purchase_card
					+ "')";
		} else {
			query = "INSERT INTO cart_log(user_id,items_list,action) VALUES("
					+ user_id + ",'" + items + "','" + action + "')";
		}

		CustomerDAO.executeQuery(query);

	}

	public static String generateItemsList(ArrayList<ItemBean> items) {
		String list = new String();

		for (ItemBean item : items) {
			list += item.getId() + ",";
		}

		list = list.substring(0, list.length() - 1);

		return list;
	}

	public static ArrayList<ItemBean> loadSavedCart(int user_id) {
		ArrayList<ItemBean> items = new ArrayList<ItemBean>();

		CartLog entry = CustomerDAO
				.retrieveSavedCart("SELECT * FROM cart_log WHERE user_id="
						+ user_id + " AND action='save'");

		String query = "SELECT * FROM item WHERE id IN(" + entry.getItems()
				+ ")";

		items = CustomerDAO.runQuery(query);

		return items;
	}

	public static void main(String[] args) {
		ArrayList<ItemBean> items = CustomerDAO
				.runQuery("select * from item where author like '%Mark%' LIMIT 5");

		System.out.println(generateItemsList(items));

		ArrayList<ItemBean> items2 = CustomerDAO
				.runQuery("select * from item where id IN(21,183,282,295,301)");
		System.out.println(generateItemsList(items2));
	}

}
