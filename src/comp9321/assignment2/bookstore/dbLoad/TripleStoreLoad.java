package comp9321.assignment2.bookstore.dbLoad;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import comp9321.assignment2.bookstore.beans.GraphNode;
import comp9321.assignment2.bookstore.beans.ItemBean;
import comp9321.assignment2.bookstore.dao.CustomerDAO;
import comp9321.assignment2.bookstore.dao.GraphDAO;

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
				if (checkValue(year)) {
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
							+ getEntityId(key)
							+ " AND target="
							+ getEntityId(author_value))) {
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

	public static int randInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public static void publishedInLoad() {
		if (checkValue("SIGMOD")) {
			String insert_query = "INSERT INTO entity_store(entity_attribute,value) VALUES('publication','SIGMOD')";
			CustomerDAO.executeQuery(insert_query);
		}

		if (checkValue("CAiSE")) {
			String insert_query = "INSERT INTO entity_store(entity_attribute,value) VALUES('publication','CAiSE')";
			CustomerDAO.executeQuery(insert_query);
		}

		int sigmod_id = getEntityId("SIGMOD");
		int caise_id = getEntityId("CAiSE");

		int publication_array[] = new int[] { sigmod_id, caise_id };

		int total_count = CustomerDAO
				.retrieveQueryCount("select count(*) as value from entity_store where entity_attribute='item'");

		for (int i = 0; i < total_count / 10; i++) {
			ArrayList<GraphNode> nodes = GraphDAO
					.retrtieveEntityNodesList("SELECT * FROM entity_store WHERE entity_attribute='item' LIMIT 10 OFFSET "
							+ i);

			for (GraphNode node : nodes) {
				int pub_ent = publication_array[randInt(0, 1)];
				if (checkGraphStore("SELECT COUNT(*) AS value FROM graph_store WHERE source="
						+ node.getEntity_id() + " AND attribute='publishedIn'")) {
					String insert_query = "INSERT INTO graph_store(source,attribute,target) VALUES('"
							+ node.getEntity_id()
							+ "','publishedIn','"
							+ pub_ent + "')";
					System.out.println(insert_query);
					CustomerDAO.executeQuery(insert_query);
				}
			}

		}
	}

	public static void insertItemToGraph(ItemBean item) {

		// AUTHORED BY RELATION
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
					+ getEntityId(item.getKey())
					+ " AND target="
					+ getEntityId(author_value))) {
				String insert_query = "INSERT INTO graph_store(source,attribute,target) VALUES('"
						+ getEntityId(item.getKey())
						+ "','authoredBy','"
						+ getEntityId(author_value) + "')";
				System.out.println(insert_query);
				CustomerDAO.executeQuery(insert_query);
			}

		}

		// AUTHORED BY RELATION END

		// PUBLISHED ON RELATION
		String year = item.ItemList.get("year");
		if (checkValue(year)) {
			String insert_query = "INSERT INTO entity_store(entity_attribute,value) VALUES('year','"
					+ year + "')";

			System.out.println(insert_query);
			CustomerDAO.executeQuery(insert_query);
		}

		// Edge Store - publishedOn
		if (checkGraphStore("SELECT COUNT(*) AS value FROM graph_store WHERE source="
				+ getEntityId(item.getKey()) + " AND target=" + getEntityId(year))) {
			String insert_query = "INSERT INTO graph_store(source,attribute,target) VALUES('"
					+ getEntityId(item.getKey())
					+ "','publishedOn','"
					+ getEntityId(year) + "')";
			System.out.println(insert_query);
			CustomerDAO.executeQuery(insert_query);
		}
		
		// PUBLISHED ON RELATION END
		
		//PUBLISHED IN RELATION
		String publication = item.getKey().split("\\/")[1];
		
		if (checkValue(publication)) {
			String insert_query = "INSERT INTO entity_store(entity_attribute,value) VALUES('publication','"+publication+"')";
			CustomerDAO.executeQuery(insert_query);
		}
		String key = item.getKey();
		if (checkGraphStore("SELECT COUNT(*) AS value FROM graph_store WHERE source="
				+ getEntityId(key) + " AND attribute='publishedIn'")) {
			String insert_query = "INSERT INTO graph_store(source,attribute,target) VALUES('"
					+ getEntityId(key)
					+ "','publishedIn','"
					+ getEntityId(publication) + "')";
			System.out.println(insert_query);
			CustomerDAO.executeQuery(insert_query);
		}
		//PUBLISHED IN RELATION END

	}

	public static void checkCiting() {

		int total_count = CustomerDAO
				.retrieveQueryCount("select count(*) as value from item");

		int iterations = total_count / 12;

		for (int i = 0; i < iterations; i++) {
			String query = "SELECT * FROM item LIMIT 12 OFFSET " + (i * 12);
			ArrayList<ItemBean> items = CustomerDAO.runQuery(query);

			for (ItemBean item : items) {
				String cite = item.ItemList.get("cite");
				if (cite != null) {
					cite = cite.replaceAll("\\.", "");
					cite = cite.trim();

					for (String citing : cite.split("\\,")) {
						if (citing != null) {
							if (!citing.isEmpty()) {
								int valid = CustomerDAO
										.retrieveQueryCount("SELECT COUNT(*) AS value FROM item WHERE item.key='"
												+ citing + "'");
								if (valid > 0) {
									System.out.println(item.getKey());
									String key = item.getKey();

									// Items
									if (checkValue(key)) {
										String insert_query = "INSERT INTO entity_store(entity_attribute,value) VALUES('item','"
												+ key + "')";

										System.out.println(insert_query);
										CustomerDAO.executeQuery(insert_query);
									}
									if (checkValue(citing)) {
										String insert_query = "INSERT INTO entity_store(entity_attribute,value) VALUES('item','"
												+ citing + "')";

										System.out.println(insert_query);
										CustomerDAO.executeQuery(insert_query);
									}

									// Cited by relation
									if (checkGraphStore("SELECT COUNT(*) AS value FROM graph_store WHERE source="
											+ getEntityId(key)
											+ " AND target='"
											+ citing + "'")) {
										String insert_query = "INSERT INTO graph_store(source,attribute,target) VALUES('"
												+ getEntityId(key)
												+ "','citedBy','"
												+ getEntityId(citing)
												+ "')";
										System.out.println(insert_query);
										CustomerDAO.executeQuery(insert_query);
									}

									/* #### VALUE INSERT FOR THE ITEM ##### */
									insertItemToGraph(item);
									
									/* #### VALUE INSERT FOR CITING ####*/
									ItemBean cited_item = CustomerDAO.runQuery("SELECT * FROM item WHERE item.key='"+citing+"'").get(0);
									insertItemToGraph(cited_item);

								}
							}
						}
					}
				}
			}

		}

	}

	public static void main(String[] args) {
		// authorLoad();

		// publishedInLoad();

		checkCiting();
	}
}
