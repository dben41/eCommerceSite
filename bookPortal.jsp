<%
/*This page represents a book, and will be dynamically populated
 *
 *Contains a 'getImage' method, getBookInfo, getBookFeedback, getRatingsAboutFeedback, giveFeedback, giveRatingAboutFeedback
 *Contains javascript similar to the resume page as to allow adding of feedbacks
 *
 *Daryl Bennett
 *Spring 2014
 */
%>
<%@ page import="helpers.HelperMethods,classes.navBar,helpers.Db, classes.Customer, classes.Book,java.text.SimpleDateFormat" %>

<%
//create objects
HelperMethods helper = new HelperMethods();
navBar nav           = new navBar();
Db db                = new Db();
Book book		     = new Book();

//set the current page to 
nav.setCurrentPage("home");
//build header info
out.write(helper.buildHtmlPageHeader("Book Browsing")); 
//load session
Customer user =helper.loadUser(session);
//build navbar
out.write(nav.printNav(user));

String formError="";
session.setAttribute("bookBrowsingMessage", formError);
//get parameter from url
Integer isbn;
try {isbn=Integer.parseInt(request.getParameter("isbn"));
} catch (Exception e){ isbn=null ;}

if(user.getLoggedIn())
{
	//like feedback button
	if(request.getParameter("veryUseful")!=null)
	{
		int fid = Integer.parseInt(request.getParameter("veryUseful"));
		int cid = db.getCidFromLogin(user.getLoginId());
		if(!db.judgingOwn(cid, fid))
			db.insertUsefulness(fid, cid, 2);
	}
	
	//like feedback button
	if(request.getParameter("useful")!=null)
	{
		int fid = Integer.parseInt(request.getParameter("useful"));
		int cid = db.getCidFromLogin(user.getLoginId());
		if(!db.judgingOwn(cid, fid))
			db.insertUsefulness(fid, cid, 1);
	}
	
	//like feedback button
	if(request.getParameter("useless")!=null)
	{
		int fid = Integer.parseInt(request.getParameter("useless"));
		int cid = db.getCidFromLogin(user.getLoginId());
		if(!db.judgingOwn(cid, fid))
			db.insertUsefulness(fid, cid, 0);
	}
}
//if the display a special page if there is no user
if(isbn==null || isbn==0 || !db.verifyExists(isbn))
	out.write(helper.customerProfileError()); //better redirect needed
else  //else display a page about special stats
{
	//out.write(helper.bookPortalInfo(isbn, db));
	//out.write(helper.bookPortalFeedback(isbn, db));
	book = helper.getBook(isbn,db);	
	
	//adds to cart
	if(request.getParameter("order")!=null && user.getLoggedIn()) 
	{
		//so doesn't send more than one request
		request.setAttribute("order", null);
		user.addToShoppingCart(isbn, db);
		
		formError="<div class='alert alert-success'>Item placed placed in shopping cart!</div>";
		session.setAttribute("bookBrowsingMessage", formError);	
		out.write(nav.printNav(user)); //reload
		
	}

	if(request.getParameter("order")!= null && !user.getLoggedIn())
	{
		formError="<div class='alert alert-danger'>You must log in to place order!</div>";
		session.setAttribute("bookBrowsingMessage", formError);	
	}
		
	
	//useful
	if(request.getParameter("useful")!=null && user.getLoggedIn())
	{
		int cid = db.getCidFromLogin(user.getLoginId());
	} else if(request.getParameter("notUseful")!=null && user.getLoggedIn()) {
		
	}
	
	//enters feedback
	if(request.getParameter("submit")!=null && user.getLoggedIn())
	{
		//current date
		java.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = new java.util.Date();
		String dateString= dateFormat.format(date);
		
		int rating = Integer.parseInt(request.getParameter("selectRating"));
		String comments = request.getParameter("textArea");
		int cid = db.getCidFromLogin(user.getLoginId());
		//ensure only one feedback has been given
		if(!db.onlyOneSubmission(cid,isbn))
		{
			//now only allow 1 feedback, add ajax to keep track of chars.
			formError = "<div class='alert alert-danger'>You've already submitted feedback about this book!!</div>";
			session.setAttribute("bookBrowsingMessage", formError);	
		} else
		{
			//now only allow 1 feedback, add ajax to keep track of chars.
			db.giveFeedback(cid,isbn,dateString,rating,comments);
			formError = "<div class='alert alert-success'>Your feedback has been posted!</div>";
			session.setAttribute("bookBrowsingMessage", formError);	
		}	
	} else{
		if(request.getParameter("submit")!=null)
		{
			formError = "<div class='alert alert-danger'>You must be logged in to perform that action!!</div>";
			session.setAttribute("bookBrowsingMessage", formError);	
		}
	}
}
%>
<!-- body content -->
<div class="container">
<div class="bs-docs-section">
        <div class="row">
          <div class="col-lg-12">
            <div class="page-header">
              <h1 id="forms">Book Profile</h1>
              <form class="form-horizontal" method="post" action="">
             	 <button  type='submit' name="order" class="btn btn-warning">
             	 	Add to Cart<span class='glyphicon glyphicon-shopping-cart'></span>
             	 </button>
             	     <a href="usefulFeedbacks.jsp?isbn=<%out.write(isbn); %>&quantity=5"><p style='color:blue;'>View useful feedback about this book</p></a>
              </form>
              	<!-- button type='submit' id='submit' name='submit' class='btn btn-primary btn-md'-->
            </div>
          </div>
        </div>
        <% out.write(formError); %>
  						<!-- This is where an image goes-->
				        <div class='row'>
				          <div class='col-lg-12'>
				            				
				            <div class='col-md-4'>
				            <div class='well bs-component'>  
				            <legend><%out.write(book.getTitle()); %></legend>   
				        		 <img src='source/miracle.jpg' alt='Registration books' height='52%' width='70%'>
				        		                                        				
				             	
				      		</div>                       
				          	</div>	
				          	 <div class='col-md-4'>
					             <div class='well bs-component'>
					             	<legend>Book Description</legend>
					             	
					             	<p>Pellentesque habitant morbi lum tortor qu ante. Donlamcorper phan fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus</p> 
					             	<br>
					             </div>
				            </div> 	
				             	 <div class='col-md-4'>
					             <div class='well bs-component'>
					             	<legend>Book Details</legend>
					             	<table class="table table-hover">
					             	<tr>
					             		<th>ISBN:</th>
					             		<td><%out.write(book.getIsbn()); %></td>
					             	</tr>
					             	<tr>
					             		<th>Publisher:</th>
					             		<td><%out.write(book.getPublisher()); %></td>
					             	</tr>
					             	<tr>
					             		<th>Year:</th>
					             		<td><%out.write(book.getYear()); %></td>
					             	</tr>
					             	<tr>
					             		<th>Price: </th>
					             		<td>$<%out.print(book.getPrice()); %></td>
					             	</tr>
					             	<tr>
					             		<th>Format</th>
					             		<td><%out.write(book.getSubject()); %></td>
					             	</tr>
					             	<tr>
					             		<th>Subject:</th>
					             		<td><%out.write(book.getSubject()); %></td>
					             	</tr>
					             	<tr>
					             		<th>Authors:</th>
					             		<% for(String author : book.getAuthors())
					             		        out.write("<td>"+author+"</td>");%>
					             		
					             	</tr>
					             	</table>
					             </div>
				            </div> 
				          </div>   
				        </div>
				        
				        <div class='row'>  
				        	<div class='col-lg-6 col-md'>	
				        		<div class='well bs-component'> 	
							   		<legend>What Others are saying:</legend>
							   		<% out.write(helper.bookPortalFeedback(isbn, db)); %>
							   </div>
				          	</div>
				          	<div class='col-lg-6 col-md'>	
			
				        		<div class='well bs-component'> 	
							   		<form class="form-horizontal" method="post" action="">
						                <fieldset>
						                  <legend>Enter your feedback!</legend>
						                  <div class="form-group ">
							                    <label for="inputLoginId" class="col-lg-2 control-label ">Rating</label>
							                    <div class="col-lg-5">
							                      <select class="form-control" name="selectRating" id="select">
								                        <option>1</option>
								                        <option>2</option>
								                        <option>3</option>
								                        <option>4</option>
								                        <option>5</option>
								                        <option>6</option>
								                        <option>7</option>
								                        <option>8</option>
								                        <option>9</option>
								                        <option>10</option>
							                      </select>
							                    </div>
						                  </div> 
						                    <div class="form-group <%//out.write(passwordError);%>">
							                    <label for="textArea" class="col-lg-2 control-label <%//out.write(passwordError); %>">Comments</label>
							                    <div class="col-lg-10">
							                      <textarea class="form-control" rows="4" id="textArea" name="textArea"></textarea>
							                    </div>
						                    </div>
						                    
						                   <div class="form-group">
						                    <div class="col-lg-10 col-lg-offset-2">
						                     <button type='submit' id='submit' name='submit' class='btn btn-primary btn-md'>
												<span class='glyphicon glyphicon-pencil'></span> Add Feedback
											</button>
						                     <button class="btn btn-default">Cancel</button>
						                     
						                    </div>
						                  </div>
						                </fieldset>
						              </form>    
							   </div>
				          	</div>
				        </div>
				  </div>
				  </div>;

        </div>
  </div>
  </div>
  <!-- This needs to go into here for the carousel to work -->
          <script type='text/javascript' src='//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js'></script>
        <script type='text/javascript' src='//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js'></script>
        <!-- JavaScript jQuery code from Bootply.com editor  -->       
        <script type='text/javascript'>
        
        $(document).ready(function() {
        
            $('#myCarousel').carousel({
  interval: 20000
})

$('.carousel .item').each(function(){
  var next = $(this).next();
  if (!next.length) {
    next = $(this).siblings(':first');
  }
  next.children(':first-child').clone().appendTo($(this));
  
  for (var i=0;i<2;i++) {
    next=next.next();
    if (!next.length) {
      next = $(this).siblings(':first');
    }
    
    next.children(':first-child').clone().appendTo($(this));
  }
});
        
        });
        
        </script>

<%
out.write(helper.buyingSuggestionsCarousel(db, isbn));
out.write(nav.printAdmin());
//build html footer
out.write(helper.buildHtmlPageFooter());
out.write(helper.buildModal("bookPortal"));
%>
