package comp9321.assignment2.bookstore.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemBean {
	int id;
	String key;
	ArrayList<String> authors;
	public HashMap<String, String> ItemList;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ArrayList<String> getAuthors() {
		return authors;
	}

	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}

	public HashMap<String, String> getItemList() {
		return ItemList;
	}

	public void setItemList(HashMap<String, String> itemList) {
		ItemList = itemList;
	}

	

	public ItemBean(int id_value, String key_value, String authors) {
		id = id_value;
		key = key_value;
		setAuthors(authors);
		ItemList = new HashMap<String,String>();
	}

	public void setAuthors(String authors_value) {
		// String[] authorList = authors_value.split("|");

		authors = new ArrayList<String>();

		for (String author_value : authors_value.split("\\,")) {
			authors.add(author_value);

		}
	}

}
