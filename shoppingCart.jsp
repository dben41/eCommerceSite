<%
/*
 *This page allows a user to see all of the books that they have selected with check boxes,
 *this state is kept with an order object and a session
 *
 *Daryl Bennett
 *Spring 2014
 */
%>
<%@ page import="helpers.HelperMethods,classes.navBar, classes.Book, helpers.Db, classes.Customer,java.text.SimpleDateFormat" %>

<%
//create objects
HelperMethods helper = new HelperMethods();
navBar nav           = new navBar();
Db db                = new Db();

//set the current page to 
nav.setCurrentPage("home");
//build header info
out.write(helper.buildHtmlPageHeader("Shopping Cart")); 
//load session
Customer user =helper.loadUser(session);
//build navbar
out.write(nav.printNav(user));
//customer id
int cid = db.getCidFromLogin(user.getLoginId());
//current date
java.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
java.util.Date date = new java.util.Date();
String orderDate= dateFormat.format(date);

String formError="";
if(session.getValue("shoppingCartMessage")==null)
	formError="";
else 
	formError = (String)session.getValue("shoppingCartMessage");
//when user clicks submit
if(request.getParameter("submit")!=null)
{
	String[] books = request.getParameterValues("book");
	if(books != null)
	{
		int quantity = 1; //hardcode this, use other form for special orders of multiple copies
		formError="<div class='alert alert-success'>Successfully Ordered books: ";
			for(int i = 0; i<books.length;i++)
			{
				int isbn = Integer.parseInt(books[i]);
				//confirm order
				//javascript alert
				//place order				
				db.placeOrder(cid, isbn, quantity, orderDate);
				//show user the books they've ordered
				formError += books[i]+", ";
				//delete the books from the shopping cart that they've selected
				user.removeFromShoppingCart(isbn);
				
			}
			formError+="</div>";
			session.setAttribute("shoppingCartMessage", formError);	 
			out.write(nav.printNav(user)); //reload
		}
} else out.println("<p>Your shopping cart is empty.</p>");

//when a user clicks remove
if(request.getParameter("remove")!=null)
{
	String[] books = request.getParameterValues("book");
	if(books != null)
	{
		int quantity = 1; //hardcode this, use other form for special orders of multiple copies
		formError="<div class='alert alert-success'>Successfully Removed books: ";
			for(int i = 0; i<books.length;i++)
			{
				int isbn = Integer.parseInt(books[i]);
				//confirm order
				//javascript alert
				//show user the books they've ordered
				formError += books[i]+", ";
				//delete the books from the shopping cart that they've selected
				user.removeFromShoppingCart(isbn);
				
			}
			formError+="</div>";
			session.setAttribute("shoppingCartMessage", formError);	 
			out.write(nav.printNav(user)); //reload
		}
}


%>
<!-- body content -->
<div class="container">
          <div class="col-lg-12">
            <div class="page-header">
              <h1 id="forms">Your Shopping Cart</h1>
            </div>
          </div>
          
        </div>
        <% out.write(formError); %>
  	   <div class="row">
          <div class="col-lg-6">
            <div class="well bs-component">
  	
  	<form class='form-horizontal' method='post' action=''>
  	 <fieldset>
        <legend>Select and order!</legend>
  	<% 
  	int i = 0;
  	//print out all the items in the shopping card of the user, with check boxes 
  	for(Book book : user.getShoppingCartList())
	{
  		
  		String outputString=""+
        "<input type='checkBox' name='book' value='"+book.getIsbn()+"'>"+book.getTitle()+"</input><br>"+
        "";
        out.write(outputString);
  		i++;
	}

  	%>
  	<br>
	<input class="btn btn-primary" name='submit' type='submit'/>
	<input class="btn btn-danger" name='remove' type='submit' value='Remove'/>
	</fieldset>
	</form>
	
  </div>
  <a href="multipleOrder.jsp"><p style='color:blue;'>Place a big order</p></a>
  </div>
  </div>

<%
//build html footer
out.write(nav.printAdmin());
out.write(helper.buildHtmlPageFooter());
out.write(helper.buildModal("shoppingCart"));
%>
