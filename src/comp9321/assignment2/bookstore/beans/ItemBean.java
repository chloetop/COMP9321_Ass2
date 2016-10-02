package comp9321.assignment2.bookstore.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemBean {
	int id;
	String item_name;
	String key;
	ArrayList<String> authors;
	String publication;
	int year;
	String URL;
	int seller_id;
	float price;
	public HashMap<String, String> ItemList;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItem_name() {
		return item_name;}
	
	public String getKey() {
		return key;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;}
	
	public void setKey(String key) {
		this.key = key;
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
	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}

//	public String getPublication() {
//		return publication;
	public HashMap<String, String> getItemList() {
		return ItemList;
	}

	public void setPublication(String publication) {
		this.publication = publication;}
	
	public void setItemList(HashMap<String, String> itemList) {
		ItemList = itemList;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	public ItemBean(int id_value, String key_value, String authors) {
		id = id_value;
		key = key_value;
		setAuthors(authors);
		ItemList = new HashMap<String,String>();
	}

	public int getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}
	

	public void setURL(String uRL) {
		URL = uRL;
	}
	
	public static String searchItem(String name){
		String output = "";
		try{  
		    Class.forName("com.mysql.jdbc.Driver");  
		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false","root","pvce@2016");
		    PreparedStatement ps=con.prepareStatement("SELECT * FROM item WHERE MATCH ( title, author, editor, booktitle, address, journal, publisher, note, isbn, school, chapter, publtype, type) AGAINST ('"+name+"' IN NATURAL LANGUAGE MODE) LIMIT 1000;");  
		    ResultSet rs=ps.executeQuery();  
		      
		    if(!rs.isBeforeFirst()) {      
		    output= null;   
		    }else{  
		    output += "<center><table width='100%' class='table table-bordered table-striped table-text-center'>";  
		    output += "<tr><th>Item ID</th><th>Item Name</th><th>Author</th><th>Account Status</th></tr>";  
		    String button= null;
		    while(rs.next()){
		    	if(Integer.valueOf(rs.getString("enabled_status")) == Integer.valueOf(1)){
		    		button = "<a href=\"#\" onclick=\"toggleItemStatus("+rs.getString("id")+", 0);return false;\" class=\"btn btn-danger\" id='toggle_item' role=\"button\">Disable Item</a>";
		    	}
		    	else{
		    		button = "<a href=\"#\" onclick=\"toggleItemStatus("+rs.getString("id")+", 1);return false;\" class=\"btn btn-success\" id='toggle_item' role=\"button\">Enable Item</a>";
		    	}
		    	output += "<tr><td>"+rs.getString("id")+"</td><td>"+rs.getString("title")+"</td><td>"+rs.getString("author")+"</td> <td>"+button+"</td></tr>";  
		    }  
		    output += "</table><center>";
		    
		    }//end of else for rs.isBeforeFirst		    
		    con.close(); 		    
		    }catch(Exception e){e.printStackTrace();}
		return output;
	}


}
