package helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class AnalyticsHelper {
  //method that uses the stateheaders precomputed table in order to get the ordering of
  //the states along with their sales total
  /////////////////////////New///////////////////////////////////////////////////////
  public static List<StateWithSales> listStateHeaders(int category){
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    //list of objects that contain information for displaying a state header
	    List<StateWithSales> stateHeaders = new ArrayList<StateWithSales>();
	    try{
	       try{
	          conn = HelperUtils.connect();
	       }
	       catch(Exception e){
	    	   return stateHeaders;
	       }
	       //order the states by total and get the first 50
	       String s = "Select name, total from stateHeaders order by total desc limit 50";
	       stmt = conn.prepareStatement(s);
	       rs = stmt.executeQuery();
	       while(rs.next()){
	    	   stateHeaders.add(new StateWithSales(rs.getString(1), rs.getInt(2)));
	       }
	       return stateHeaders;
	    }
	    catch(Exception e){
	    	return stateHeaders;
	    }
	    
  }
  
  //method to get the top 50 products in order to display them in the headers of the table
  //also updates the precomputed tables for run query
  ///////////////////////////////New////////////////////////////////////////////////////
  public static List<ProductTotalHelper> listProductHeaders(int category){
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    
	    //list of objects that contain information to display the product headers in the 
	    //table
	    List<ProductTotalHelper> productHeaders = new ArrayList<ProductTotalHelper>();
	    try{
	       try{
	          conn = HelperUtils.connect();
	       }
	       catch(Exception e){
	    	   return productHeaders;
	       }      
	       String s = "";
	       
	       //code that deletes from precomputed table 'combined', we want to recompute it
	       s = "Delete from combined";
	       stmt = conn.prepareStatement(s);
	       stmt.executeUpdate();
	       
	       //code that finds the id of the last sale in the sales table that is present
	       //in the precomputed table.  All sales after this id need to be added in the 
	       //precomputed table
	       s = "Select last from lastSale where id=2";
	       stmt = conn.prepareStatement(s);
	       rs = stmt.executeQuery();

           rs.next();
           int lastsale = rs.getInt(1);

           String s2 = "Select states.name, products.name, (sales.quantity * sales.price) as total, sales.id"
           		+ " from sales, users, states, products"
           		+ " where sales.id > ?"
           		+ " and sales.uid = users.id"
           		+ " and users.state = states.id"
           		+ " and sales.pid = products.id";
           
           stmt = conn.prepareStatement(s2);
           stmt.setInt(1, lastsale);

           rs = stmt.executeQuery();
           List<SalesHelper> sh = new ArrayList<SalesHelper>();

           //stores all of the information of the newest sales in list of objects
	       while(rs.next()){
	          sh.add(new SalesHelper(rs.getString(1), rs.getString(2), rs.getInt(3)));
	          lastsale = rs.getInt(4);
	       }
	       
	       //set the id of the last sale that is present in the precomputed table
	       String s3 = "Update lastsale Set last=?";
	       stmt = conn.prepareStatement(s3);
	       stmt.setInt(1,lastsale);
	       stmt.executeUpdate();
	       
	       //use the new sales to update the precomputed table
	       for(SalesHelper shelper : sh){
	    	   //update stateheaders table
	    	   s = "Update stateheaders set total=total+? where name=?";
	    	   stmt = conn.prepareStatement(s);
	    	   stmt.setFloat(1,  shelper.getProductTotal());
	    	   stmt.setString(2, shelper.getStateName());
	    	   stmt.executeUpdate();
	    	   
	    	   //update productheaders table
	    	   s = "Update productheaders set total=total+? where name=?";
	    	   stmt = conn.prepareStatement(s);
	    	   stmt.setFloat(1,  shelper.getProductTotal());
	    	   stmt.setString(2, shelper.getProductName());
	    	   stmt.executeUpdate();
	    	   
	    	   //update cellvalues table, first need to check whether this state/product  
	    	   //combination is already present in the table, if true then perform an update,
	    	   //if false then insert the sale as a new row
	    	   s = "Select count(*) from cellvalues where state=? and product=?";
	    	   stmt = conn.prepareStatement(s);
	    	   stmt.setString(1,  shelper.getStateName());
	    	   stmt.setString(2, shelper.getProductName());
	    	   rs = stmt.executeQuery();
	    	   rs.next();
	    	   if(rs.getInt(1) > 0){
	    		   s = "Update cellvalues set total=total+? where state=? and product=?";
	    		   stmt = conn.prepareStatement(s);
	    		   stmt.setFloat(1, shelper.getProductTotal());
		    	   stmt.setString(2,  shelper.getStateName());
		    	   stmt.setString(3, shelper.getProductName());
	    	   }
	    	   else{
	    		   s = "Insert into cellvalues(state, product, total) values(?, ?, ?)";
	    		   stmt = conn.prepareStatement(s);
	    		   stmt.setFloat(3, shelper.getProductTotal());
		    	   stmt.setString(1,  shelper.getStateName());
		    	   stmt.setString(2, shelper.getProductName());
	    	   }
	    	   stmt.executeUpdate();
	       }
	       
	       //if all categories is selected
	       if(category == -1){
	    	   s = "insert into combined(state, product, total, sTotal, pTotal, category) "+
	    			   "(select t1.state, t1.product, coalesce(t2.total, 0) as total, t1.stotal, t1.ptotal, t1.category from "+
	    					   "(select * from "+
	    					   "(select name as state, total as stotal from stateHeaders order by stotal desc) as t1 "+
	    					   "cross join "+
	    					   "(select name as product, total as ptotal, category from productHeaders order by pTotal desc limit 50) as t2) as t1 "+
	    					   "left join "+
	    					   "(select state, product, total from cellValues) as t2 "+
	    					   "on t1.state = t2.state and t1.product = t2.product "+
	    					   "order by stotal desc, state, ptotal desc, product)";
	    	   stmt = conn.prepareStatement(s);
	       }
	       //if a specific category is selected
	       else{
	    	   s = "insert into combined(state, product, total, sTotal, pTotal, category) "+
	    			   "(select t1.state, t1.product, coalesce(t2.total, 0) as total, t1.stotal, t1.ptotal, t1.category from "+
	    					   "(select * from "+
	    					   "(select name as state, total as stotal from stateHeaders order by stotal desc) as t1 "+
	    					   "cross join "+
	    					   "(select name as product, total as ptotal, category from productHeaders where category = ? order by pTotal desc limit 50) as t2) as t1 "+
	    					   "left join "+
	    					   "(select state, product, total from cellValues) as t2 "+
	    					   "on t1.state = t2.state and t1.product = t2.product "+
	    					   "order by stotal desc, state, ptotal desc, product)";
	    	   stmt = conn.prepareStatement(s);
	    	   stmt.setInt(1, category);
	       }
	       //create the new combined table, this precomputed table is used to directly get values
	       //for each cell in the table
	       stmt.executeUpdate();
	       
	       //get the 50 products that will be displayed along with their totals
	       if(category == -1){
	          s = "Select name, total from productHeaders order by total desc limit 50";
	          stmt = conn.prepareStatement(s);
	       }
	       else{
	    	   s = "Select name, total from productHeaders where category = ? order by total desc limit 50";
	    	   stmt = conn.prepareStatement(s);
	    	   stmt.setInt(1, category);
	       }
	       rs = stmt.executeQuery();
	       while(rs.next()){
	    	   productHeaders.add(new ProductTotalHelper(rs.getString(1), rs.getInt(2)));
	       }
	       return productHeaders;
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    	return productHeaders;
	    }
	    
}
  
  //method that pulls values direclty from 'combined' table and stores them in objects
  //for use of displaying each cell in the table
  /////////////////////////////NEW///////////////////////////////////////////////////
  public static List<CombinedHelper> getCellValues(int category){
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    List<CombinedHelper> ch = new ArrayList<CombinedHelper>();
	    try{
	       try{
	          conn = HelperUtils.connect();
	       }
	       catch(Exception e){
	    	   return ch;
	       }
	       String s = "";
	       //if all categories is selected
	       if(category == -1){
	          s = "Select state, total, stotal, ptotal, id, product from combined";
	          stmt = conn.prepareStatement(s);
	       }
	       //if specific category is selected
	       else{
	    	 s = "Select state, total, stotal, ptotal, id, product from combined where category=?"; 
	    	 stmt = conn.prepareStatement(s);
	    	 stmt.setInt(1, category);
	       }
	       //selects all values in the 'combined' table
	       rs = stmt.executeQuery();
	       while(rs.next()){
	          ch.add(new CombinedHelper(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6)));
	       }
	       return ch;
	    }
	    catch(Exception e){
	    	return ch;
	    }
	    
}
  //method used when refresh button is clicked, finds the newest sales and returns objects
  //representing those sales
  ////////////////////////////////NEW//////////////////////////////////////////////////
  public static List<SalesHelper> refresh(){
	  Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    List<SalesHelper> sh = new ArrayList<SalesHelper>();
	    try{
	       try{
	          conn = HelperUtils.connect();
	       }
	       catch(Exception e){
	    	   return sh;
	       }

	       //similar to code in listProductHeaders for getting the newest sales
	       String s = "Select last from lastSale where id=1";
	       stmt = conn.prepareStatement(s);
	       rs = stmt.executeQuery();

           rs.next();
           int lastsale = rs.getInt(1);
 	 	  System.out.println(lastsale);

           String s2 = "Select states.name, products.name, (sales.quantity * sales.price) as total, sales.id"
           		+ " from sales, users, states, products"
           		+ " where sales.id > ?"
           		+ " and sales.uid = users.id"
           		+ " and users.state = states.id"
           		+ " and sales.pid = products.id";
           
           stmt = conn.prepareStatement(s2);
           stmt.setInt(1, lastsale);

           rs = stmt.executeQuery();
           

	       while(rs.next()){
	          sh.add(new SalesHelper(rs.getString(1), rs.getString(2), rs.getInt(3)));
	          lastsale = rs.getInt(4);
	       }
	       
	       String s3 = "Update lastsale Set last=? where id=1";
	       stmt = conn.prepareStatement(s3);
	       stmt.setInt(1, lastsale);

	       stmt.executeUpdate();

	       return sh;
	    }
	    catch(Exception e){
	    	System.out.println("in catch" + e);
	    	return sh;
	    }
	    
  }
  
}
