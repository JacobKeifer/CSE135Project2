<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
/////////////////////////////////////////NEW////////////////////////////////////////
//page that gets the parameters from the ajax request and uses SignupHelper in order to see
//if the signup was successful or not, response is in string format
                            	String name = null, role = null, state = null;
                            	Integer age = null;
                            	try {
                            		name = request.getParameter("name");
                            	} catch (Exception e) {
                            		name = null;
                            	}
                            	try {
                            		role = request.getParameter("role");
                            	} catch (Exception e) {
                            		role = null;
                            	}
                            	try {
                            		age = Integer.parseInt(request.getParameter("age"));
                            	} catch (Exception e) {
                            		age = null;
                            	}
                            	try {
                            		state = request.getParameter("state");
                            	} catch (Exception e) {
                            		state = null;
                            	}
                            	System.out.println(name);
                            	System.out.println(age);
                            	System.out.println(role);
                            	System.out.println(state);
                            	if (name != null && age != null && role != null && state != null){
                            		%>
                            		
                                    <%= helpers.SignupHelper.signup(name, age, role, state) %>
                            	<%}
                            %>