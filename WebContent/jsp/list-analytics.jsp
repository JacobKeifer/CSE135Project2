<%@page
    import="java.util.List"
    import="java.util.ArrayList"
    import="helpers.*"%>
    
<%
try{
	String customer = (String) request.getParameter("rowSelector");
	String order = (String) request.getParameter("order");
	int filter = Integer.parseInt((String) request.getParameter("categoryFilter"));
	int offset1 = Integer.parseInt((String) request.getParameter("offset1"));
	int offset2 = Integer.parseInt((String) request.getParameter("offset2"));
    %>
    <form action="analytics" method="post">
		<input type="submit" value="Next 20 <%if(customer.equals("customer")) { %>Customers<%} else {%>States <%}%>">
		<input type="hidden" value="<%= customer %>" name="rowSelector">
		<input type="hidden" value="<%= order %>" name="order">
		<input type="hidden" value="<%= filter %>" name="categoryFilter">
		<input type="hidden" value="<%= offset1 + 20 %>" name="offset1">
		<input type="hidden" value="<%= offset2 %>" name="offset2">
	</form>
	
	<form action="analytics" method="post">
		<input type="submit" value="Next 10 Products">
		<input type="hidden" value="<%= customer %>" name="rowSelector">
		<input type="hidden" value="<%= order %>" name="order">
		<input type="hidden" value="<%= filter %>" name="categoryFilter">
		<input type="hidden" value="<%= offset1 %>" name="offset1">
		<input type="hidden" value="<%= offset2+10 %>" name="offset2">
	</form>  
	
	<%if(offset1 != 0){ %>
	<form action="analytics" method="post">
		<input type="submit" value="Previous 20 <%if(customer.equals("customer")) { %>Customers<%} else {%> States <%}%>">
		<input type="hidden" value="<%= customer %>" name="rowSelector">
		<input type="hidden" value="<%= order %>" name="order">
		<input type="hidden" value="<%= filter %>" name="categoryFilter">
		<input type="hidden" value="<%= offset1 - 20 %>" name="offset1">
		<input type="hidden" value="<%= offset2 %>" name="offset2">
	</form> <%} %>
	
	<%if(offset2 != 0) {%>
	<form action="analytics" method="post">
		<input type="submit" value="Previous 10 Products">
		<input type="hidden" value="<%= customer %>" name="rowSelector">
		<input type="hidden" value="<%= order %>" name="order">
		<input type="hidden" value="<%= filter %>" name="categoryFilter">
		<input type="hidden" value="<%= offset1 %>" name="offset1">
		<input type="hidden" value="<%= offset2-10 %>" name="offset2">
	</form> <%} %>
<%
if(customer.equals("customer")){
List<UsersWithSales> usersData = AnalyticsHelper.listUsers(order, filter, offset1, offset2);
%>
        
<table style="border:1px"
  class="table table-striped"
  align="center">
     
  <thead>
    <tr align="center">
    <th width=20%> Users </th>
    <%
       String uName = usersData.get(0).getName();
       int counter = 0;
       while(uName.equals(usersData.get(counter).getName())){
    %>
    <th> <%= usersData.get(counter).getProductName() %> <br>
    (<%= usersData.get(counter).getProductTotal() %>)
    </th>
    <% 
        counter++;
        }
       %>
    </tr>
    
    
  </thead>
<%   
int i = 0;
for(UsersWithSales data : usersData) {
	if(i == 0){
	   %>
	   <tr>
	   <td><b>
	   <%= data.getName() %> <br>
	   (<%= data.getUserTotal() %>)</b>
	   </td>
<%
	}
	%>
	<td>
	<%= data.getSales() %>
	</td>
<%
   if(i == (counter-1)){
	   i = 0;
	   %>
	   </tr>
	  <%
   }

   else{
	   i++;
   }

}
} //if customer is selected

else{
	List<StateWithSales> stateData = AnalyticsHelper.listStates(order, filter, offset1, offset2);
	%>    
	<table style="border:1px"
	  class="table table-striped"
	  align="center">
	     
	  <thead>
	    <tr align="center">
	    <th width=20%> States </th>
	    <%
	       String uName = stateData.get(0).getName();
	       int counter = 0;
	       while(uName.equals(stateData.get(counter).getName())){
	    %>
	    <th> <%= stateData.get(counter).getProductName() %> <br>
	    (<%= stateData.get(counter).getProductTotal() %>)
	    </th>
	    <% 
	        counter++;
	        }
	       %>
	    </tr>
	    
	    
	  </thead>
	<%   
	int i = 0;
	for(StateWithSales data : stateData) {
		if(i == 0){
		   %>
		   <tr>
		   <td><b>
		   <%= data.getName() %> <br>
		   (<%= data.getStateTotal() %>)</b>
		   </td>
	<%
		}
		%>
		<td>
		<%= data.getSales() %>
		</td>
	<%
	   if(i == (counter-1)){
		   i = 0;
		   %>
		   </tr>
		  <%
	   }

	   else{
		   i++;
	   }

	}
} //else state selected
%>
<% 	
}
catch (Exception e){
  out.println("Filter your search");	
}
%>