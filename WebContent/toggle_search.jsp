    <%@ page import="java.sql.*, comp9321.assignment2.bookstore.*" %>  
    <%  
    String name = request.getParameter("val");
    String target = request.getParameter("target");
    if(target.trim().contains("usr")){
    	if(name==null||name.trim().equals("")){  
    	    out.print("<p>Please enter name!</p>");  
    	    }else{  
    	    	String output = UserDAO.searchUser(name);
    	    	if(output == null){
    	    		  out.print("<p>No results found</p>");
    	    		  
    	    	}
    	    	else{
    	    		  out.print(output);
    	    		  
    	    	}
    	    }//end of else 
    }
    else if(target.trim().contains("item"))
    {
    	if(name==null||name.trim().equals("")){  
    	    out.print("<p>Please enter name!</p>");  
    	    }else{  
    	    	String output = ItemBean.searchItem(name);
    	    	if(output == null){
    	    		  out.print("<p>No results found</p>");
    	    	}
    	    	else{
    	    		  out.print(output);
    	    	}
    	    }//end of else 
    }
    else if(target.trim().contains("act"))
    {
    	if(name==null||name.trim().equals("")){  
    	    out.print("<p>Please enter Search String!</p>");  
    	    }else{  
    	    	String output = UserDAO.searchUserAct(name);
    	    	if(output == null){
    	    		  out.print("<p>No results found</p>");
    	    	}
    	    	else{
    	    		  out.print(output);
    	    	}
    	    }//end of else 
    }
    %>  