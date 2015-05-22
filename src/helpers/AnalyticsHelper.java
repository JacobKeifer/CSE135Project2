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
	
//alpha customer
private static String s1 = "Select t1.uName, t1.pName, t1.total, utotal, ptotal "+
"from "+
"((Select uName, pName, sum(total) as total from "+
"(Select uName, pName, COALESCE(quantity*price,0) as total "+
"from "+
"(Select * from "+
"(select name as uName, id as uid from users order by uName limit 2 offset ?) as t1 "+
"cross join "+
"(select name as pName, id as pid from products order by pName limit 2 offset ?) as t2) as t3 "+
"left join sales on t3.uid = sales.uid and t3.pid = sales.pid) as t4 "+
"group by uName, pName) "+
"order by uName, pName) as t1, "+
"(select name as uname, id as uid, COALESCE(utotal,0) as utotal from "+
"(select name, id from users) as t1 "+
"left join "+
"(select uid, sum(quantity*price) as uTotal "+
"from sales "+
"group by uid) as t2 "+
"on t1.id=t2.uid) as t2, "+
"(select product as pname, t2.pid, COALESCE(ptotal,0) as ptotal from "+
"(select pid, sum(quantity*price) as pTotal "+
"from sales "+
"group by pid) as t1 "+
"right join "+
"(select id as pid, name as product from products) as t2 "+
"on t1.pid=t2.pid) as t3 "+
"where t1.uname = t2.uname and t1.pname=t3.pname "+
"order by uname, pname";

//alpha state
private static String s2 = "Select t1.state, t1.product, t1.sumtotal, t2.stotal, t3.ptotal from "+
"(Select state,product, sum(total) as sumTotal from "+
"(Select state, product, COALESCE(quantity*price,0) as total "+
"From "+
"(Select t1.name as State, t1.id as sid, t2.name as Product, t2.id as pid from "+
"(Select * from states limit 10 offset ?) as t1 "+
"cross join "+
"(select * from products limit 10 offset ?) as t2) as t3 left join "+
"(Select users.state as sid, pid, quantity, price "+
"from sales, users where users.id=sales.uid) as t4 "+
"on t3.sid = t4.sid and t3.pid = t4.pid order by state, product) as t5 "+
"group by state, product "+
"order by state) as t1, "+
"(select name as state, id as sid, COALESCE(stotal,0) as stotal from "+
"(select * from states) as t1 "+
"left join "+
"(Select state as sid, sum(utotal) as stotal from "+
"(select uid, sum(quantity*price) as uTotal "+
"from sales "+
"group by uid) as g1 "+
"left join "+
"(select * from users) g2 "+
"on g1.uid = g2.id "+
"group by sid) as t2 "+
"on t1.id=t2.sid) as t2, "+
"(select product, t2.pid, COALESCE(ptotal,0) as ptotal from "+
"(select pid, sum(quantity*price) as pTotal "+
"from sales "+
"group by pid) as t1 "+
"right join "+
"(select id as pid, name as product from products) as t2 "+
"on t1.pid=t2.pid) as t3 "+
"where t1.state=t2.state and t1.product = t3.product "+
"order by state, product";

//top k customer
private static String s3 = "select uname, product, sum(total) as total, utotal, ptotal from "+
"(Select uname, product, COALESCE(quantity*price,0) as total, utotal, ptotal from "+
"(select * from "+
"(select name as uname, id as uid, COALESCE(utotal,0) as utotal from "+
"(select name, id from users) as t1 "+
"left join "+
"(select uid, sum(quantity*price) as uTotal "+
"from sales "+
"group by uid) as t2 "+
"on t1.id=t2.uid order by utotal desc limit 6 offset ?) as t1 "+
"cross join "+
"(select product, t2.pid, COALESCE(ptotal,0) as ptotal from "+
"(select pid, sum(quantity*price) as pTotal "+
"from sales "+
"group by pid) as t1 "+
"right join "+
"(select id as pid, name as product from products) as t2 "+
"on t1.pid=t2.pid order by ptotal desc limit 6 offset ?) as t2) as t1 "+
"left join "+
"sales as t2 "+
"on t1.uid = t2.uid and t1.pid = t2.pid) as t1 "+
"group by uname, product, utotal, ptotal "+
"order by utotal desc, uname, ptotal desc, product";

//top k state 
private static String s4 = "select t1.state, product, COALESCE(total,0) as total, stotal, ptotal from "+
		"(select state, sid, product, pid, stotal, ptotal from "+
		"(select name as state, id as sid, COALESCE(stotal,0) as stotal from "+
		"(select * from states) as t1 "+
		"left join "+
		"(Select state as sid, sum(utotal) as stotal from "+
		"(select uid, sum(quantity*price) as uTotal "+
		"from sales "+
		"group by uid) as g1 "+
		"left join "+
		"(select * from users) g2 "+
		"on g1.uid = g2.id "+
		"group by sid) as t2 "+
		"on t1.id=t2.sid order by stotal desc limit 10 offset ?) as t1 "+
		"cross join "+
		"(select product, t2.pid, COALESCE(ptotal,0) as ptotal from "+
		"(select pid, sum(quantity*price) as pTotal "+
		"from sales "+
		"group by pid) as t1 "+
		"right join "+
		"(select id as pid, name as product from products) as t2 "+
		"on t1.pid=t2.pid order by ptotal desc limit 6 offset ?) as t2) as t1 "+
		"full join "+
		"(select state, pid, sum(total) as total from "+
		"(Select users.state, sales.pid, quantity*price as total "+
		"from users, sales "+
		"where users.id=sales.uid) as t1 "+
		"group by state, pid) as t2 "+
		"on t1.sid = t2.state and t1.pid=t2.pid "+
		"where t1.state is not null "+
		"order by stotal desc, state, ptotal desc, product";	

//specific category alpha customer
private static String s5 = "Select t1.uName, t1.pName, t1.total, utotal, ptotal "+
		"from "+
		"((Select uName, pName, sum(total) as total from "+
		"(Select uName, pName, COALESCE(quantity*price,0) as total "+
		"from "+
		"(Select * from "+
		"(select name as uName, id as uid from users order by uName limit 10 offset ?) as t1 "+
		"cross join "+
		"(select name as pName, id as pid from products where cid = ? order by pName limit 10 offset ?) as t2) as t3 "+
		"left join sales on t3.uid = sales.uid and t3.pid = sales.pid) as t4 "+
		"group by uName, pName) "+
		"order by uName, pName) as t1, "+
		"(select name as uname, id as uid, COALESCE(utotal,0) as utotal from "+
		"(select name, id from users) as t1 "+
		"left join "+
		"(select uid, sum(quantity*price) as uTotal "+
		"from sales "+
		"group by uid) as t2 "+
		"on t1.id=t2.uid) as t2, "+
		"(select product as pname, t2.pid, COALESCE(ptotal,0) as ptotal from "+
		"(select pid, sum(quantity*price) as pTotal "+
		"from sales "+
		"group by pid) as t1 "+
		"right join "+
		"(select id as pid, name as product from products) as t2 "+
		"on t1.pid=t2.pid) as t3 "+
		"where t1.uname = t2.uname and t1.pname=t3.pname "+
		"order by uname, pname";

//specific category alpha state
private static String s6 = "Select t1.state, t1.product, t1.sumtotal, t2.stotal, t3.ptotal from "+
		"(Select state,product, sum(total) as sumTotal from "+
		"(Select state, product, COALESCE(quantity*price,0) as total "+
		"From "+
		"(Select t1.name as State, t1.id as sid, t2.name as Product, t2.id as pid from "+
		"(Select * from states limit 10 offset ?) as t1 "+
		"cross join "+
		"(select * from products where cid = ? limit 10 offset ?) as t2) as t3 left join "+
		"(Select users.state as sid, pid, quantity, price "+
		"from sales, users where users.id=sales.uid) as t4 "+
		"on t3.sid = t4.sid and t3.pid = t4.pid order by state, product) as t5 "+
		"group by state, product "+
		"order by state) as t1, "+
		"(select name as state, id as sid, COALESCE(stotal,0) as stotal from "+
		"(select * from states) as t1 "+
		"left join "+
		"(Select state as sid, sum(utotal) as stotal from "+
		"(select uid, sum(quantity*price) as uTotal "+
		"from sales "+
		"group by uid) as g1 "+
		"left join "+
		"(select * from users) g2 "+
		"on g1.uid = g2.id "+
		"group by sid) as t2 "+
		"on t1.id=t2.sid) as t2, "+
		"(select product, t2.pid, COALESCE(ptotal,0) as ptotal from "+
		"(select pid, sum(quantity*price) as pTotal "+
		"from sales "+
		"group by pid) as t1 "+
		"right join "+
		"(select id as pid, name as product from products) as t2 "+
		"on t1.pid=t2.pid) as t3 "+
		"where t1.state=t2.state and t1.product = t3.product "+
		"order by state, product";	

//specific category top customer
private static String s7 = "select uname, product, sum(total) as total, utotal, ptotal from "+
		"(Select uname, product, COALESCE(quantity*price,0) as total, utotal, ptotal from "+
		"(select * from "+
		"(select name as uname, id as uid, COALESCE(utotal,0) as utotal from "+
		"(select name, id from users) as t1 "+
		"left join "+
		"(select uid, sum(quantity*price) as uTotal "+
		"from sales "+
		"group by uid) as t2 "+
		"on t1.id=t2.uid order by utotal desc limit 6 offset ?) as t1 "+
		"cross join "+
		"(select product, t2.pid, COALESCE(ptotal,0) as ptotal from "+
		"(select pid, sum(quantity*price) as pTotal "+
		"from sales "+
		"group by pid) as t1 "+
		"right join "+
		"(select id as pid, name as product from products where cid=?) as t2 "+
		"on t1.pid=t2.pid order by ptotal desc limit 6 offset ?) as t2) as t1 "+
		"left join "+
		"sales as t2 "+
		"on t1.uid = t2.uid and t1.pid = t2.pid) as t1 "+
		"group by uname, product, utotal, ptotal "+
		"order by utotal desc, uname, ptotal desc, product";

//specific category top state
private static String s8 = "select t1.state, product, COALESCE(total,0) as total, stotal, ptotal from "+
		"(select state, sid, product, pid, stotal, ptotal from "+
		"(select name as state, id as sid, COALESCE(stotal,0) as stotal from "+
		"(select * from states) as t1 "+
		"left join "+
		"(Select state as sid, sum(utotal) as stotal from "+
		"(select uid, sum(quantity*price) as uTotal "+
		"from sales "+
		"group by uid) as g1 "+
		"left join "+
		"(select * from users) g2 "+
		"on g1.uid = g2.id "+
		"group by sid) as t2 "+
		"on t1.id=t2.sid order by stotal desc limit 10 offset ?) as t1 "+
		"cross join "+
		"(select product, t2.pid, COALESCE(ptotal,0) as ptotal from "+
		"(select pid, sum(quantity*price) as pTotal "+
		"from sales "+
		"group by pid) as t1 "+
		"right join "+
		"(select id as pid, name as product from products where cid = ?) as t2 "+
		"on t1.pid=t2.pid order by ptotal desc limit 6 offset ?) as t2) as t1 "+
		"full join "+
		"(select state, pid, sum(total) as total from "+
		"(Select users.state, sales.pid, quantity*price as total "+
		"from users, sales "+
		"where users.id=sales.uid) as t1 "+
		"group by state, pid) as t2 "+
		"on t1.sid = t2.state and t1.pid=t2.pid "+
		"where t1.state is not null "+
		"order by stotal desc, state, ptotal desc, product";	
	
  public static List<UsersWithSales> listUsers(String ordering, int category, int offset1, int offset2){
	  Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    
	    String userName;
	    String productName;
	    Float totalSales;
	    Float uTotal;
	    Float pTotal;
	    
	    List<UsersWithSales> users = new ArrayList<UsersWithSales>();
	    try {
	      try {
	        conn = HelperUtils.connect(); 
	      } catch (Exception e) {
	    	  System.err.println("Internal Server error. This shouldn't happen");
	      }
          String query = "";
          if(category == -1){
	          if(ordering.equals("alphabetical")){
	        	  query = s1;
	          }
	          
	          else{
	        	  query = s3;
	          }
          }
          
          else{
        	  if(ordering.equals("alphabetical")){
	        	  query = s5;
	          }
	          
	          else{
	        	  query = s7;
	          }
          }
          stmt = conn.prepareStatement(query);
          if(category != -1){
        	  stmt.setInt(1, offset1);
        	  stmt.setInt(2, category);
        	  stmt.setInt(3, offset2);
          }
          
          else{
        	  stmt.setInt(1,  offset1);
        	  stmt.setInt(2, offset2);
          }
	      rs = stmt.executeQuery();
	      while(rs.next()){
	        userName = rs.getString(1);
	        productName = rs.getString(2);
	        totalSales = rs.getFloat(3);
	        uTotal = rs.getFloat(4);
	        pTotal = rs.getFloat(5);
	        users.add(new UsersWithSales(userName, productName, totalSales, uTotal, pTotal));
	      }
	      return users;
	    } catch (Exception e) {
	    	System.err.println("Some error happened!" + e.getMessage());
	    	return new ArrayList<UsersWithSales>();
	    } finally {
	    	try {
	    		stmt.close();
	    		conn.close();
	    	} catch (SQLException e) {
	    		System.err.println("HELP!!");
	    	}
	    }
  }
  
  public static List<StateWithSales> listStates(String ordering, int category, int offset1, int offset2){
	  Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    
	    String stateName;
	    String productName;
	    Float totalSales;
	    Float sTotal;
	    Float pTotal;
	    
	    List<StateWithSales> users = new ArrayList<StateWithSales>();
	    try {
	      try {
	        conn = HelperUtils.connect(); 
	      } catch (Exception e) {
	    	  System.err.println("Internal Server error. This shouldn't happen");
	      }
	      
	      String query = "";
	      System.out.println(category);
	      if(category == -1){
	          if(ordering.equals("alphabetical")){
	        	  query = s2;
	          }
	          
	          else{
	        	  query = s4;
	          }
          }
          
          else{
        	  if(ordering.equals("alphabetical")){
	        	  query = s6;
	          }
	          
	          else{
	        	  query = s8;
	          }
          }
	      stmt = conn.prepareStatement(query);
	      if(category != -1){
	    	  stmt.setInt(1, offset1);
        	  stmt.setInt(2, category);
        	  stmt.setInt(3, offset2);
	      }
	      
	      else{
	    	  stmt.setInt(1, offset1);
	    	  stmt.setInt(2, offset2);
	      }
	      rs = stmt.executeQuery();
	      while(rs.next()){
	        stateName = rs.getString(1);
	        productName = rs.getString(2);
	        totalSales = rs.getFloat(3);
	        sTotal = rs.getFloat(4);
	        pTotal = rs.getFloat(5);
	        users.add(new StateWithSales(stateName, productName, totalSales, sTotal, pTotal));
	      }
	      return users;
	    } catch (Exception e) {
	    	System.err.println("Some error happened!" + e.getMessage());
	    	return new ArrayList<StateWithSales>();
	    } finally {
	    	try {
	    		stmt.close();
	    		conn.close();
	    	} catch (SQLException e) {
	    		System.err.println("HELP!!");
	    	}
	    }
  }
}
