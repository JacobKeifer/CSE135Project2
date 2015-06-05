<%@page
    import="java.util.List"
    import="helpers.*"%>
<div class="panel panel-default">
	<div class="panel-body">
		<div class="bottom-nav">
            <h4> Options </h4>
            <!-- Put your part 2 code here -->
            <form action="analytics" method="post">
<%
String filter = "";
try{
  if (request.getParameter("runQuery") != null){
    filter = (String) request.getParameter("categoryFilter");
  }
}
catch (Exception e){
	
}
%>                  
                <%
                	List<CategoriesDisplay> categoryNames = CategoriesHelper.getCategoryNames();
                %>
                <select name="categoryFilter">
                <% for(CategoriesDisplay s : categoryNames){ %>
                   <option value="<%= s.getId() %>"<%if(filter.equals(s.getId().toString())){%>
                   selected="selected"<%}%>> <%= s.getName() %> </option>
                   <%}%>
                   <option value="-1" <%if(filter.equals("-1") || filter.equals("")){%>
                   selected="selected"<%}%>> All Categories </option>
                </select> <br />
                
                <input type="hidden" value="0" name="offset1">
                <input type="hidden" value="0" name="offset2">
                
            	<input type="submit" name="runQuery" value="Run Query">
            	
            </form>
            
            <!-- NEW -->
            <button onclick="refresh()"> Refresh </button>
            	<script>
            	//function called when refresh is clicked
            	function refresh(){
            		//create a new object for ajax depending on browser
            		var ajaxRequest;
            	    try{
            	    	ajaxRequest = new XMLHttpRequest();
            	    }catch(e){
            	    	try{
            	    		ajaxRequest = new ActiveXObject("MSxml2.XMLHTTP");
            	    	}catch(e){
            	    		try{
            	    			ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
            	    		}catch(e){
            	    			return false;
            	    		}
            	    	}
            	    }
            	    
            	    //every time button is clicked, set all elements in table to have
            	    //color black, updated elements will then be set to red later, but
            	    //this helps reset red elements back to black
            	    var th = document.getElementsByTagName("th");
            	    var td = document.getElementsByTagName("td");
            	    for(var j = 0; j<th.length; j++)
            	       th[j].style.color = "black";
            	    for(var k = 0; k<td.length; k++)
            	       td[k].style.color = "black";
            	    
            	    ajaxRequest.onreadystatechange = function(){
            	    	//called when the ajax request gets a response
            	    	if(ajaxRequest.readyState == 4){
            	    		//response text is xml string so create object to traverse 
            	    		//XMLDOM
            	    		var parser = new DOMParser();
            	    		var string = ajaxRequest.responseText;
            	    		var xmlDoc = parser.parseFromString(string, "text/xml");
            	    		
            	    		var root = xmlDoc.getElementsByTagName("root");
            	    		var states = root[0].getElementsByTagName("state");
            	    		var products = root[0].getElementsByTagName("product");
            	    		var totals = root[0].getElementsByTagName("total");
            	    		var stateHeader;
            	    		var productHeader;
            	    		var sname;
            	    		var pname;
            	    		var cname;
            	    		var total;
            	    		var oldTotal;
            	    		var regex = /\d+/g;
            	    		var sum;
            	    		//go through every sale and use that strings in the xml response
            	    		//to find the id of the element and update the total there
            	    		//and set the color to red
                            for(var i = 0; i<states.length; i++){
                            	sname = states[i].firstChild.nodeValue;
                            	total = totals[i].firstChild.nodeValue;       	
                            	stateHeader = document.getElementById(sname);
                            	oldtotal = stateHeader.innerHTML.match(regex);
                            	stateHeader.style.color = "red";
                            	stateHeader.style.fontWeight = "bold";
                            	sum = parseFloat(oldtotal) + parseFloat(total);
                            	stateHeader.innerHTML = sname + "<br>("+ sum + ")";
                            	
                            	pname = products[i].firstChild.nodeValue;
                            	productHeader = document.getElementById(pname);
                            	if(productHeader!= null) {
                            		oldtotal = productHeader.innerHTML.match(regex)[1];
                            		productHeader.style.color = "red";
                            		productHeader.style.fontWeight = "bold";
                                	sum = parseFloat(oldtotal) + parseFloat(total);
                                	productHeader.innerHTML = pname + "<br>("+ sum + ")";
                                	
                                	cname = document.getElementById(sname+pname);
                                	cname.style.color = "red";
                                	cname.innerHTML = parseFloat(cname.innerHTML) + parseFloat(total);
                            	}
                            }
            	    	}
            	    }       
            	    //make a get request to refresh.jsp
            	    ajaxRequest.open("GET", "refresh", true);
            	    ajaxRequest.send();
            	}
            	</script>
		</div>
	</div>
</div>