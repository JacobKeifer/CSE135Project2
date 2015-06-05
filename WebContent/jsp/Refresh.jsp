<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.List"
    import="java.util.ArrayList"
    import="helpers.*"%>
<%
//////////////////////NEW////////////////////////////////
//jsp that takes a list of objects that contain info on sales
//and returns an xml string containing the info stored in the sales
//which is used to update the table asynchronously
List<SalesHelper> newSales = AnalyticsHelper.refresh();
String xml = "<root>";
for(SalesHelper s : newSales){
	xml += "<state>";
	xml += s.getStateName();
	xml += "</state>";
	xml += "<product>";
	xml += s.getProductName();
	xml += "</product>";
	xml += "<total>";
	xml += s.getProductTotal();
	xml += "</total>";
}
xml += "</root>";
System.out.println(xml);
%>
<%= xml %>