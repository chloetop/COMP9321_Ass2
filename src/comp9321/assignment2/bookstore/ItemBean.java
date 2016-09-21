package comp9321.assignment2.bookstore;

import java.util.ArrayList;

public class ItemBean {
	int id;
	String item_name;
	ArrayList<String> authors;
	String publication;
	int year;
	String URL;
	int seller_id;
	float price;
	
	ItemBean(int id_value, String name, String authors_list, String pub_name, int year_value, String URL_Value, int seller_id, float price_value){
		setId(id_value);
		setItem_name(name);
		setAuthors(authors_list);
		setPublication(pub_name);
		setYear(year_value);
		setURL(URL_Value);
		setSeller_id(seller_id);
		setPrice(price_value);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public ArrayList<String> getAuthors() {
		return authors;
	}

	public void setAuthors(String authors_value) {
		//String[] authorList = authors_value.split("|");

		authors = new ArrayList<String>();

		for (String author_value : authors_value.split("\\|")) {
			authors.add(author_value);

		}
	}

	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}


}
