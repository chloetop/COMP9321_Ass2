package comp9321.assignment2.bookstore.helpers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import comp9321.assignment2.bookstore.dao.CustomerDAO;

public class QueryBuilder {

	private static String encloseModulo(String attribute) {
		return "%" + attribute + "%";
	}

	public static int getQueryCount(String query) {
		int rows = 0;

		String query_count = query.replace("SELECT * ",
				"SELECT count(*) as value ");

		query_count = query_count
				.substring(0, query_count.indexOf(" LIMIT 12"));

		rows = CustomerDAO.retrieveQueryCount(query_count);

		return rows;
	}

	private static String buildQueryParameters(HashMap<String, String> query) {
		String whereClause = new String();

		for (String key : query.keySet()) {
			if (!whereClause.isEmpty())
				whereClause += " AND ";
			String value = query.get(key);
			String predicate = "(";
			for (String value_string : value.split("\\,")) {
				if(!key.equals("year")){
					predicate += " item." + key + " like '"
						+ encloseModulo(value_string) + "' OR";}
				else{
					predicate += " " + key + "="
							+value_string+" OR";
				}
			}
			predicate = predicate.substring(0,
					predicate.length() - 2);
			whereClause += predicate+")";

		}

		return whereClause;
	}

	public static String buildInsertQuery(String item_name, String publication,
			String seller_id, String author_line, String url, String year,
			String price, Boolean on_hold, String imagePath) {
		boolean add_and = false;
		String attributes = "";
		String values = "";
		Date date = new Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		
		if (item_name != null && !item_name.isEmpty()) {
			attributes += "title";
			values += "'"+item_name+"'";
			add_and = true;
		}
		if (publication != null && !publication.isEmpty()) {
			if (add_and) {
				attributes += ", ";
				values += ", ";
			}
			attributes += "type";
			values += "'"+publication+"'";
			add_and = true;
		}
		if (seller_id != null && !seller_id.isEmpty()) {
			if (add_and) {
				attributes += ", ";
				values += ", ";
			}
			attributes += "seller_id";
			values += Integer.parseInt(seller_id);
			add_and = true;
		}
		if (year != null && !year.isEmpty()) {
			if (add_and) {
				attributes += ", ";
				values += ", ";
			}
			attributes += "year";
			values += Integer.parseInt(year);
			add_and = true;
		}
		if (author_line != null && !author_line.isEmpty()) {
			if (add_and) {
				attributes += ", ";
				values += ", ";
			}
			attributes += "author";
			values += "'"+author_line+"'";
			add_and = true;
		}
		if (url != null && !price.isEmpty()) {
			if (add_and) {
				attributes += ", ";
				values += ", ";
			}
			attributes += "url";
			values += "'"+url+"'";
			add_and = true;
		}
		if (price != null && !price.isEmpty()) {
			if (add_and) {
				attributes += ", ";
				values += ", ";
			}
			attributes += "price";
			values += Integer.parseInt(price);
		}
		if (on_hold != null) {
			if (add_and) {
				attributes += ", ";
				values += ", ";
			}
			attributes += "enabled_status";
			values += on_hold;
		}
		if (imagePath != null) {
			if (add_and) {
				attributes += ", ";
				values += ", ";
			}
			attributes += "image_url";
			values += "'"+imagePath+"'";
		}
		//add unique key
		if (add_and) {
			attributes += ", ";
			values += ", ";
		}
		attributes += "item.key";
		values += "'"+timeStamp+"'";
		
		String query = "INSERT INTO item ("+attributes+") VALUES ("+values+")";

		return query;
	}
	
	public static String buildQuery(HashMap<String,String> query_values,
			String price_min, String price_max, int limit, int page_count) {
		String query = new String();
		boolean add_and = false;

		query = "SELECT * FROM item WHERE";
		
		String wherePredicate = buildQueryParameters(query_values);
		
		if(!wherePredicate.isEmpty())
			add_and = true;
		
		query += wherePredicate;

		
		if (!price_max.isEmpty() && !price_min.isEmpty()) {
			if (add_and)
				query += " AND ";
			query += " price between " + price_min + " AND " + price_max;
			add_and = true;
		}
		
		query += " AND enabled_status=1";

		// Add the limit clause
		query += " LIMIT " + limit;

		// Add the offset if present
		if (page_count > 0) {
			query += " OFFSET " + page_count * limit;
		}
		
		return query;
	}

	public static String changeQueryOffset(String query, int page_count,
			int limit) {
		String query_value = new String();
		if (!query.contains("OFFSET"))
			query_value = query + " OFFSET " + page_count * limit;
		else {
			query_value = query.substring(0, query.indexOf("OFFSET "));
			query_value = query_value + " OFFSET " + page_count * limit;
		}
		return query_value;
	}
	
	public static void insertUserActivity(){
		
	}

	public static void main(String[] args) {

		// String item_name = "book";
		// String publication = "AB";
		// String seller_id = new String();
		// String author_line = "Mark";
		// String year = "2015";
		// String price_min = new String();
		// String price_max = new String();
		// int page_count = 0;
		// String query = buildQuery(item_name, publication, seller_id,
		// author_line, year, price_min, price_max, 12, 1);
		// System.out.println(query);
		// System.out.println(changeQueryOffset(query, 3, 12));

		int rows = getQueryCount("SELECT * FROM item WHERE price between 200 AND 250 LIMIT 12");

		System.out.println(rows);
	}
	
}
