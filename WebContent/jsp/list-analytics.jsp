<%@page
    import="java.util.List"
    import="java.util.ArrayList"
    import="helpers.*"%>
    
<%
/////////////////Whole page is pretty much new/////////////////////////////////////////////
//Elements in table are assigned id's that make them distinguishable and searchable which
//helps for updating the table in the ajax call
try{
	int filter = Integer.parseInt((String) request.getParameter("categoryFilter"));
    %>
<%
    List<ProductTotalHelper> productData = AnalyticsHelper.listProductHeaders(filter);
    List<CombinedHelper> cellValues = AnalyticsHelper.getCellValues(filter);
	%>    
	<table style="border:1px"
	  class="table table-striped"
	  align="center">
	     
	  <thead>
	    <tr align="center">
	    <th width=20%> States </th>
<%
      int pcount = 0;
      for(ProductTotalHelper p : productData){
	  %>
	  
	  <th id="<%= p.getProductName() %>"> <%= p.getProductName() %> <br> (<%= p.getTotal() %>) </th>
	  
	  <% 
	  pcount++;
	  }
	  %>
	  </tr>
	  
	  <%
	  int counter = 0;
	  for(CombinedHelper c : cellValues){
		  if(counter == 0){
	  %>
	  <tr>
	  <td id="<%= c.getState() %>"> <b> <%= c.getState() %> </b><br> <b> (<%=c.getStateTotal() %>) </b> </td>
<%
		  }
		  %>
	  <td id="<%= c.getState()+c.getProduct() %>"> <%= c.getTotal() %> </td>
<%
      if(counter == (pcount-1)){
    	  counter = 0;
      %>
      </tr>
<%    	  
      }
      else{
    	  counter++;
      }
	  }
%>
<% 	
}
catch (Exception e){
  out.println("Filter your search");	
}
%>