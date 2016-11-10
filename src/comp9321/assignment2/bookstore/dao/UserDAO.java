/**
 * 
 */
package comp9321.assignment2.bookstore.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.*;
import javax.mail.*; 
import javax.mail.internet.*;

import com.mysql.jdbc.Statement;

/**
 * @author svajiraya
 *
 */
public class UserDAO {
	
	public static String getMD5(String data) throws NoSuchAlgorithmException
    { 
		MessageDigest messageDigest=MessageDigest.getInstance("MD5");

        messageDigest.update(data.getBytes());
        byte[] digest=messageDigest.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(Integer.toHexString((int) (b & 0xff)));
        }
        return sb.toString();
    }
	
	public static void sendMail(String email, String sub,String body){
		String host = "outlook.office365.com";
		String user = "z5081713@ad.unsw.edu.au";
		String pass = "Init@123456";
		String to = email;
		String from = "s.vajiraya@unsw.edu.au";
		String subject = sub;
		String messageText = body;
		boolean sessionDebug = false;

		Properties props = System.getProperties();
		props.put("mail.host", host);
		/* props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true"); */
		/* props.put("mail.smtp.port", 26); */
		// Uncomment 5 SMTPS-related lines below and comment out 2 SMTP-related lines above and 1 below if you prefer to use SSL
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smts.auth", "true");
		props.put("mail.smtp.port", 587);
		props.put("mail.smtp.starttls.enable","true");
		/* props.put("mail.smtps.ssl.trust", host); */
		Session mailSession = Session.getDefaultInstance(props, null);
		mailSession.setDebug(sessionDebug);
		Message msg = new MimeMessage(mailSession);
		try{
			msg.setContent(messageText, "text/html; charset=utf-8");
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = {new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			Transport transport = mailSession.getTransport("smtp");
			// Transport transport = mailSession.getTransport("smtps");
			transport.connect(host, user, pass);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static String searchUser(String name){
		String output = "";
		try{  
		    Class.forName("com.mysql.jdbc.Driver");  
		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false","root","root");  
		    PreparedStatement ps=con.prepareStatement("select * from users where username like '"+name+"%' || email like '"+name+"%' || fname like '"+name+"%' || lname like '"+name+"%'");  
		    ResultSet rs=ps.executeQuery();  
		      
		    if(!rs.isBeforeFirst()) {      
		    output= null;   
		    }else{  
		    output += "<center><table width='100%' class='table table-bordered table-striped table-text-center'>";  
		    output += "<tr><th>User ID</th><th>Username</th><th>Email</th><th>Account Status</th></tr>";  
		    String button= null;
		    while(rs.next()){
		    	if(Integer.valueOf(rs.getString("acc_status")) == Integer.valueOf(1)){
		    		button = "<a href=\"#\" onclick=\"toggleUsrStatus("+rs.getString("id")+", 0);return false;\" class=\"btn btn-danger\" id='toggle_user' role=\"button\">Disable User</a>";
		    	}
		    	else{
		    		button = "<a href=\"#\" onclick=\"toggleUsrStatus("+rs.getString("id")+", 1);return false;\" class=\"btn btn-success\" id='toggle_user' role=\"button\">Enable User</a>";
		    	}
		    	output += "<tr><td>"+rs.getString("id")+"</td><td>"+rs.getString("username")+"</td><td>"+rs.getString("email")+"</td> <td>"+button+"</td></tr>";  
		    }  
		    output += "</table><center>";
		    
		    }//end of else for rs.isBeforeFirst		    
		    con.close(); 		    
		    }catch(Exception e){e.printStackTrace();}
		return output;
	}
	
	public static String searchUserAct(String name){
		String output = "";
		try{  
		    Class.forName("com.mysql.jdbc.Driver");  
		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false","root","root");  
		    PreparedStatement ps=con.prepareStatement("select * from users where username like '"+name+"%' || email like '"+name+"%' || fname like '"+name+"%' || lname like '"+name+"%'");  
		    ResultSet rs=ps.executeQuery();  
		      
		    if(!rs.isBeforeFirst()) {      
		    output= null;   
		    }else{  
		    output += "<center><table width='100%' class='table table-bordered table-striped table-text-center'>";  
		    output += "<tr><th>User ID</th><th>Username</th><th>Email</th><th>Show Activity</th></tr>";  
		    String button= "";
		    while(rs.next()){
		    	button = "<a href=\"#\" onclick=\"showActivity("+rs.getString("id")+");return false;\" class=\"btn btn-danger\" id='toggle_user' role=\"button\">Fetch User Activity</a>";
		    	output += "<tr><td>"+rs.getString("id")+"</td><td>"+rs.getString("username")+"</td><td>"+rs.getString("email")+"</td> <td>"+button+"</td></tr>";  
		    }  
		    output += "</table><center>";
		    
		    }//end of else for rs.isBeforeFirst		    
		    con.close(); 		    
		    }catch(Exception e){e.printStackTrace();}
		return output;
	}
	
	public static boolean createUser(String username, String nickName, 
			String fname, String lname, String email, Integer yob, String full_address, BigInteger CC, String password, Integer type){
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		final String DB_URL = "jdbc:mysql://localhost/bookstore?autoReconnect=true&useSSL=false";

		// Database credentials
		final String USER = "root";
		final String PASS = "root";
		
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();

			String query = "INSERT INTO `users` (`id`, `username`, `nickname`, `fname`, `lname`, `email`, `yob`, `full_address`, `cc_no`, `password`, `type`, `acc_status`, `admin`) VALUES (NULL, '"+ username +"', '"+nickName+"', '"+fname+"', '"+lname+"', '"+email+"', '"+yob+"', '"+full_address+"', '"+CC+"', '"+password+"', '"+type+"', '1', '0');";
			String sql = query ;
			int rs = stmt.executeUpdate(sql);
			// STEP 5: Extract data from result set
			
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
			return false;
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			return false;
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		
		return true;
	}

}
