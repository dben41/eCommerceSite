<%
//New users can register here
//Daryl Bennett
//Spring 2014
%>

<%@ page import="helpers.HelperMethods,classes.navBar,helpers.Db, classes.Customer" %>

<%
//create objects
HelperMethods helper = new HelperMethods();
navBar nav           = new navBar();
Db db                = new Db();

//set the current page to 
nav.setCurrentPage("logout");
//build header info
out.write(helper.buildHtmlPageHeader("logout")); 
//reset
helper.endSession(session);
Customer user =null;
//build navbar
out.write(nav.printNav(user));


//error messages
String loginIdError="";
String passwordError="";
String formError ="";
//redirect to the main page
formError = "<div class='alert alert-success'>Log out successful!</div>";
session.setAttribute("message", formError); 
response.sendRedirect("index.jsp");

%>



<%
//build html footer
out.write(helper.buildHtmlPageFooter());
%>
