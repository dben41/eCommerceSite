<%
/*
 *This displays an image, some of the customer info
 *and uses javascript to add trust records.  getSomeUserInfo, addTrustRecord, getTrustRecord,removeTrustRecord
 *
 *Daryl Bennett
 *Spring 2014
 */
%>
<%@ page import="helpers.HelperMethods,classes.navBar,helpers.Db, classes.Customer" %>

<%
//create objects
HelperMethods helper = new HelperMethods();
navBar nav           = new navBar();
Db db                = new Db();

//set the current page to 
nav.setCurrentPage("home");
//build header info
out.write(helper.buildHtmlPageHeader("Profile")); 
//load session
Customer user =helper.loadUser(session);
//build navbar
out.write(nav.printNav(user));
String errorMessage="";
//get parameter from url
String login = request.getParameter("login");

//if the display a special page if there is no user
if(login==null || login=="" || !db.verifyUserExists(login))
	response.sendRedirect("error1.jsp");
else  //else display a page about special stats
{
	out.write(helper.customerProfileInfo(login,db,user));
	//helper.getTrustRecord(login);SELECT c.name AS name2, SUM(isTrusted) AS trust FROM trust t, customers c, customers d WHERE c.cid=t.cid2 AND c.login='user2' AND d.cid=t.cid1 GROUP BY t.cid2
}

//check if submit button has been pressed
if(request.getParameter("trust")!=null)
{	
	if(user.getLoggedIn()==false)
		errorMessage="<div class='alert alert-danger'>You must be logged in to perform that action!</div>";
	else 
	{
		if(user.getLoginId().equals(login))
			errorMessage="<div class='alert alert-danger'>You trust yourself?! More like you practice shameless self-promotion.</div>!";
		else
		{
			//verifies that only one trust rating has been given
			db.handleTrust(1, db.getCidFromLogin(user.getLoginId()), db.getCidFromLogin(login));
			request.setAttribute("trust", null);
			response.sendRedirect("customerProfile.jsp?login="+login);
		}
	}	
}
//check if submit button has been pressed
if(request.getParameter("distrust")!=null)
{	
	if(request.getParameter("true")==null || request.getParameter("inputQuantity")=="")
	{
		if(user.getLoggedIn()==false)
			errorMessage="<div class='alert alert-danger'>You must be logged in to perform that action!</div>";
		else 
		{
			if(user.getLoginId().equals(login))
				errorMessage="<div class='alert alert-danger'>You trust yourself?! More like you practice shameless self-promotion.</div>!";
			else
			{
				//verifies that only one trust rating has been given
				db.handleTrust(-1, db.getCidFromLogin(user.getLoginId()), db.getCidFromLogin(login));
				request.setAttribute("trust", null);
				response.sendRedirect("customerProfile.jsp?login="+login);
			}
		}	
	}	
}

out.write(errorMessage);
%>


<%
//build html footer
out.write(nav.printAdmin());
out.write(helper.buildHtmlPageFooter());
out.write(helper.buildModal("customerProfile"));
%>
