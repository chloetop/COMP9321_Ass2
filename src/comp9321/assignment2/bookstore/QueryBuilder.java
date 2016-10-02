package comp9321.assignment2.bookstore;

public class QueryBuilder {

	private static String encloseModulo(String attribute) {
		return "%" + attribute + "%";
	}

	public static String buildQuery(String item_name, String publication,
			String seller_id, String author_line, String year,
			String price_min, String price_max, int limit, int page_count) {
		String query = new String();
		boolean add_and = false;

		query = "SELECT * FROM item WHERE";

		if (item_name != null && !item_name.isEmpty()) {
			query += " title like '" + encloseModulo(item_name) + "'";
			add_and = true;
		}
		if (publication != null && !publication.isEmpty()) {
			if (add_and)
				query += " AND ";
			query += " publisher like '" + encloseModulo(publication) + "'";
			add_and = true;
		}
		if (seller_id != null && !seller_id.isEmpty()) {
			if (add_and)
				query += " AND ";
			query += " seller_id like '" + encloseModulo(seller_id) + "'";
			add_and = true;
		}
		if (year != null && !year.isEmpty()) {
			if (add_and)
				query += " AND ";
			query += " year = " + year;
			add_and = true;
		}
		if (author_line != null && !author_line.isEmpty()) {
			if (add_and)
				query += " AND ";
			for (String author : author_line.split("\\,")) {
				query += " author like '" + encloseModulo(author) + "' OR";
			}
			query = query.substring(0, query.length() - 2);
			add_and = true;
		}
		if (!price_max.isEmpty() && !price_min.isEmpty()) {
			if (add_and)
				query += " AND ";
			query += " price between " + price_min + " AND " + price_max;
			add_and = true;
		}

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

	public static void main(String[] args) {

		String item_name = "book";
		String publication = "AB";
		String seller_id = new String();
		String author_line = "Mark";
		String year = "2015";
		String price_min = new String();
		String price_max = new String();
		int page_count = 0;
		String query = buildQuery(item_name, publication, seller_id,
				author_line, year, price_min, price_max, 12, 1);
		//System.out.println(query);
		//System.out.println(changeQueryOffset(query, 3, 12));
	}
}
