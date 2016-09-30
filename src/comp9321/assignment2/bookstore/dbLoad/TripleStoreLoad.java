package comp9321.assignment2.bookstore.dbLoad;

import java.util.ArrayList;

import comp9321.assignment2.bookstore.beans.ItemBean;
import comp9321.assignment2.bookstore.dao.CustomerDAO;

public class TripleStoreLoad {

	private static boolean checkValue(String value) {
		String query = "SELECT COUNT(*) AS value FROM entity_store where value='"
				+ value + "'";

		int row_count = CustomerDAO.retrieveQueryCount(query);

		if (row_count >= 1)
			return false;
		else
			return true;
	}

	private static boolean checkGraphStore(String query) {
		int row_count = CustomerDAO.retrieveQueryCount(query);

		if (row_count >= 1)
			return false;
		else
			return true;
	}

	private static int getEntityId(String value) {
		String query = "SELECT entity_id as value FROM entity_store WHERE value='"
				+ value + "'";

		return CustomerDAO.retrieveQueryCount(query);
	}

	public static void authorLoad() {
		int total_count = CustomerDAO
				.retrieveQueryCount("select count(*) as value from item");

		int iterations = total_count / 12;

		for (int i = 0; i < iterations; i++) {
			String query = "SELECT * FROM item LIMIT 12 OFFSET " + (i * 12);
			ArrayList<ItemBean> items = CustomerDAO.runQuery(query);

			for (ItemBean item : items) {

				String key = item.getKey();
				String year = item.ItemList.get("year");

				if (checkValue(key)) {
					String insert_query = "INSERT INTO entity_store(entity_attribute,value) VALUES('item','"
							+ key + "')";

					System.out.println(insert_query);
					CustomerDAO.executeQuery(insert_query);
				}
				if (checkValue(year)){
					String insert_query = "INSERT INTO entity_store(entity_attribute,value) VALUES('year','"
							+ year + "')";

					System.out.println(insert_query);
					CustomerDAO.executeQuery(insert_query);
				}
				
				// Edge Store - publishedOn
				if (checkGraphStore("SELECT COUNT(*) AS value FROM graph_store WHERE source="
						+ getEntityId(key) + " AND target=" + getEntityId(year))) {
					String insert_query = "INSERT INTO graph_store(source,attribute,target) VALUES('"
							+ getEntityId(key)
							+ "','publishedOn','"
							+ getEntityId(year) + "')";
					System.out.println(insert_query);
					CustomerDAO.executeQuery(insert_query);
				}

				for (String author : item.getAuthors()) {
					// Insert Author
					String author_value = author.trim().replaceAll("\\'", "");
					// Entity Store
					if (checkValue(author_value)) {
						String insert_query = "INSERT INTO entity_store(entity_attribute,value) VALUES('author','"
								+ author_value + "')";

						System.out.println(insert_query);
						CustomerDAO.executeQuery(insert_query);
					}
					// Edge Store - authoredBy
					if (checkGraphStore("SELECT COUNT(*) AS value FROM graph_store WHERE source="
							+ getEntityId(key) + " AND target=" + getEntityId(author_value))) {
						String insert_query = "INSERT INTO graph_store(source,attribute,target) VALUES('"
								+ getEntityId(key)
								+ "','authoredBy','"
								+ getEntityId(author_value) + "')";
						System.out.println(insert_query);
						CustomerDAO.executeQuery(insert_query);
					}

				}
			}

		}

	}

	public static void main(String[] args) {
		authorLoad();
	}
}
