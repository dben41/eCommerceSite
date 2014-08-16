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
nav.setCurrentPage("login");
//build header info
out.write(helper.buildHtmlPageHeader("login")); 
//load session
Customer user =helper.loadUser(session);
//build navbar
out.write(nav.printNav(user));


//error messages
String loginIdError="";
String passwordError="";
String formError ="";

//if user just came from login
String message;
if(session.getValue("message")==null)
	formError="";
else 
	formError = (String)session.getValue("message");

//check if submit button has been pressed
if(request.getParameter("submit")!=null)
{
	
 //check each individual box and highlight red if not full
 if(request.getParameter("inputLoginId")==null || request.getParameter("inputLoginId")=="") 
	   loginIdError="has-error"; 
 else{ 
	   loginIdError=" ";
	   user.setLoginId(request.getParameter("inputLoginId"));
	 //request.getSession().setAttribute("user", user);
 }
 if(request.getParameter("inputPassword")==null || request.getParameter("inputPassword")=="")
	   passwordError="has-error";
 else{
	   passwordError=" ";
 }
	 //seeing if all the boxes are empty
	 if(request.getParameter("inputPassword")==null || request.getParameter("inputPassword")==""||request.getParameter("inputLoginId")==null || request.getParameter("inputLoginId")=="")
		formError="<div class='alert alert-danger'>Please fill in all the fields!</div>";
	//verify user exists
	else if(!db.verifyUserExists(request.getParameter("inputLoginId")))
		formError="<div class='alert alert-danger'>User "+ request.getParameter("inputLoginId") +" doesn't exist!</div>";	
	//verify credentials
	else if(!db.login(request.getParameter("inputLoginId"),request.getParameter("inputPassword")))
		formError="<div class='alert alert-danger'>Password for user: "+ request.getParameter("inputLoginId") +" was incorrect!</div>";		
	else{
		//login user
		user = db.getCustomerInfo(request.getParameter("inputLoginId"), user);
		user.setLoginId(request.getParameter("inputLoginId"));
		//display success message
		formError = "<div class='alert alert-success'>Log in successful!</div>";
		session.setAttribute("message", formError);	 
		 response.sendRedirect("index.jsp");
	   //maybe redirect to login page?j
		}
	}
 
%>

<!-- body content -->
<div class="container">
<div class="bs-docs-section">
        <div class="row">
          <div class="col-lg-12">
            <div class="page-header">
              <h1 id="forms">Log in</h1>
            </div>
          </div>
        </div>
		<% out.write(formError); %>
        <div class="row">
          <div class="col-lg-6">
            <div class="well bs-component">
              <form class="form-horizontal" method="post" action="">
                <fieldset>
                  <legend>Log in and get started!</legend>
                  <div class="form-group <%out.write(loginIdError);%>">
	                    <label for="inputLoginId" class="col-lg-2 control-label ">Login ID</label>
	                    <div class="col-lg-10">
	                      <input type="text" value="<%out.write(user.getLoginId()); %>" class="form-control" name="inputLoginId" placeholder="Login ID" >
	                    </div>
                  </div> 
                    <div class="form-group <%out.write(passwordError);%>">
	                    <label for="inputPassword" class="col-lg-2 control-label <%out.write(passwordError); %>">Password</label>
	                    <div class="col-lg-10">
	                      <input type="password" class="form-control" name="inputPassword" placeholder="Password">
	                    </div>
                    </div>
                    
                   <div class="form-group">
                    <div class="col-lg-10 col-lg-offset-2">
                     <button type="submit" class="btn btn-primary" name="submit">Submit</button>
                      <button class="btn btn-default">Cancel</button>
                     
                    </div>
                  </div>
                </fieldset>
              </form>    
          </div>
          </div>
          
          <div class="col-lg-6 col-md">		
			 <img src="source/register.png" alt="Registration books" height="70%" width="100%"> 
          </div>
        </div>
  </div>
  </div>




<%
//build html footer
out.write(nav.printAdmin());
out.write(helper.buildHtmlPageFooter());
out.write(helper.buildModal("login"));
%>
