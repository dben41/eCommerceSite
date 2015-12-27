package helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import classes.Book;
import classes.Customer;
import classes.Feedback;


public class HelperMethods {
	
	/**
	 * Creates the HTML header text for all pages.
	 */
	public String buildHtmlPageHeader(String title) {				
		return "<%@ page language='java' contentType='text/html; charset=ISO-8859-1' pageEncoding='ISO-8859-1'%>"+
			"<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>"+
			"<html>"+
					"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>"+
					"<link rel='icon' href='source/web.ico' type='image/x-icon'>"+
					"<link rel='stylesheet' type='text/css' href='style/css/bootstrap.css'>"+			
					"<link rel='stylesheet' type='text/css' href='style/SlidePushMenus/css/default.css' />"+
					"<link rel='stylesheet' type='text/css' href='style/SlidePushMenus/css/component.css' />"+
					"<script src='style/SlidePushMenus/js/modernizr.custom.js'></script>"+
					" <!-- CSS code from Bootply.com editor --> "+     
			        "<style type='text/css'>"+  
			           " .carousel-inner .active.left { left: -20%; }"+  
			          "  .carousel-inner .next        { left:  20%; }"+  
			           " .carousel-control.left,.carousel-control.right {background-image:none;}"+  
			          "  .col-lg-3 {width: 25%;}"+  
			      "  </style>"+  
					"<title>"+title+"</title>"+
				"</head>" +
				 "<body class='cbp-spmenu-push'>";
	}
	
	/**
	 * Creates the HTML footer text for all pages.
	 */
	public String buildHtmlPageFooter() {
		return 
			"<script src=\"style/js/jquery.js\"></script>"+
			"<!-- Sticky footer from http://foundation.zurb.com/forum/posts/629-sticky-footer-->"+
			"<script type=\"text/javascript\">"+
				"$(window).bind(\"load\", function () {"+
				    "var footer = $(\"#footer\");"+
				    "var pos = footer.position();"+
				    "var height = $(window).height();"+
				    "height = height - pos.top;"+
				    "height = height - footer.height();"+
				    "if (height > 0) {"+
				        "footer.css({"+
				            "'margin-top': height + 'px'"+
				        "});"+
				    "}"+
				"});"+
			"</script>"+
			"<script src='style/js/bootstrap.js'></script>"+
			"</body>"+
			"<footer>"+
				"<div class='container' role='contentinfo'>"+
					"<div class='row'>"+
						"<div class='col-md-12'>"+
					
					"Built and maintained by Daryl Bennett- 2014 - <a style='color:blue;' href='http://www.darylbennett24.com'>Contact Me!</a>"+
						"</div>"+
					"</div>"+
				"</div>"+
			"</footer>"+
		"</html>";
	}
	
	/**
	 * This is the non-dynamic, anti-pattern way to end sessions
	 * @param session
	 */
	public void endSession(HttpSession session)
	{
		if(session.getAttribute("user")!=null)
			session.setAttribute("user", null);
		if(session.getAttribute("book")!=null)
			session.setAttribute("book", null);
	}
	
	public Customer loadUser(HttpSession session)
	{
		//request.getSession().setAttribute("user", user);
		Customer c = null;
		if(session.getAttribute("user")==null)
			session.setAttribute("user", new Customer());
		c=(Customer) session.getAttribute("user");
		return c;
	}
	
	public Book loadBook(HttpSession session)
	{
		//request.getSession().setAttribute("user", user);
		Book b = null;
		if(session.getAttribute("book")==null)
			session.setAttribute("book", new Book());
		b=(Book) session.getAttribute("book");
		return b;
	}
	
	/*
	 * Verify that a user has proper credentials to be visiting that page
	 */
	public Boolean isLoggedIn(Customer user)
	{
		if(!user.getLoggedIn())
		    return false;	
		return true;
	}
	
	/*
	 * Verify that a user has proper credentials to be visiting that page
	 */
	public Boolean isAdmin(Customer user)
	{
		if(!user.getRole().equals("admin"))
		    return false;
		return true;
	}
	
	/**
	 * Prints carousel of popular books
	 */
	public String buyingSuggestionsCarousel(Db db,int isbn) throws Exception  
	{
		HashMap<Integer,String> returnHash = db.getBuyingSuggestionsCarousel(isbn);
		Boolean flag = true;
		int index = 1;
		String returnString = "<!-- HTML code from Bootply.com editor -->"+
				"<div class='col-lg-8 text-center'><h3>Other Users who bought this book are also checking out:</h3></div>"+
				"<div class='col-lg-6 col-md-offset-3'>"+
				"<div class='carousel slide' id='myCarousel'>"+
				  "<div class='carousel-inner'>";
				for(Map.Entry<Integer, String> entry: returnHash.entrySet())
				{
					if(flag)
					{
						returnString +="<div class='item active'>"+
							      "<div class='col-xs-12 col-lg-3'><a href='bookPortal.jsp?isbn="+entry.getKey()+"'><img src='source/genericBook"+index+".png;' class='img-responsive'><p style='color:black;'>"+entry.getValue()+"</p></a>"+
									"</div>"+
									    "</div>";
						flag=false;
						//to alternate the images for demo site
						if(index==1) index=2; else index=1;
					} else
					{
						
						returnString+= 	"<div class='item'>"+
							      "<div class='col-md-12 col-lg-3'><a href='bookPortal.jsp?isbn="+entry.getKey()+"'><img src='source/genericBook"+index+".png;text=Good Book'  class='img-responsive'><p style='color:black;'>"+entry.getValue()+"</p></a></div>"+
								    "</div>";
						if(index==1) index=2; else index=1;
					}
				}
				returnString += 
				  "</div>"+
				"  <a class='left carousel-control' href='#myCarousel' data-slide='prev'><i class='glyphicon glyphicon-chevron-left'></i></a>"+
				  "<a class='right carousel-control' href='#myCarousel' data-slide='next'><i class='glyphicon glyphicon-chevron-right'></i></a>"+
				"</div>"+
				"</div>"+
				        "<div id='push'></div>      "+
				"<script type='text/javascript' src='//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js'></script>"+
				"<script type='text/javascript' src='//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js'></script>"+
				"<!-- JavaScript jQuery code from Bootply.com editor  -->       "+
				"<script type='text/javascript'>      "+
				   "$(document).ready(function() {"+
				        ""+
				            "$('#myCarousel').carousel({"+
				  "interval: 20000"+
				"})"+
				"$('.carousel .item').each(function(){"+
				  "var next = $(this).next();"+
				"  if (!next.length) {"+
				    "next = $(this).siblings(':first');"+
				  "}"+
				"  next.children(':first-child').clone().appendTo($(this));"+
				  "for (var i=0;i<2;i++) {"+
				    "next=next.next();"+
				"    if (!next.length) {"+
				    	"next = $(this).siblings(':first');"+
				  	"}"+
				"    next.children(':first-child').clone().appendTo($(this));"+
				  "}"+
				"});      "+
				    "});"+
				"</script>";

		return returnString;
	}
	
	/**
	 * Prints a carousel of Buying suggestions
	 */
	public String popularBookCarousel()
	{
		return "<div class='col-lg-8 text-center'><h3>Recommended Books</h3></div>"+
				"<div class='col-lg-6 col-md-offset-3'>"+
				"<div class='carousel slide' id='myCarousel'>"+
				  "<div class='carousel-inner'>"+
				    "<div class='item active'>"+
				      "<div class='col-xs-12 col-lg-3'><a href='source/genericBook.png'><img src='source/genericBook1.png;' class='img-responsive'><p style='color:black;'>Title</p></a>"+
				"</div>"+
				    "</div>"+
				"    <div class='item'>"+
				      "<div class='col-md-12 col-lg-3'><a href='bookPortal.jsp?isbn=1'><img src='source/genericBook2.png;text=Good Book'  class='img-responsive'><p style='color:black;'>Title</p></a></div>"+
				    "</div>"+
				"    <div class='item'>"+
				      "<div class='col-md-12 col-lg-3'><a href='bookPortal.jsp?isbn=1'><img src='source/genericBook1.png;text=Good Book;' class='img-responsive'><p style='color:black;'>Title</p></a></div>"+
				    "</div>"+
				"    <div class='item'>"+
				      "<div class='col-md-12 col-lg-3'><a href='bookPortal.jsp?isbn=1'><img src='source/genericBook2.png;text=Good Book;' class='img-responsive'><p style='color:black;'>Title</p></a></div>"+
				    "</div>"+
				"    <div class='item'>"+
				      "<div class='col-md-12 col-lg-3'><a href='bookPortal.jsp?isbn=1'><img src='source/genericBook1.png;text=Good Book' class='img-responsive'><p style='color:black;'>Title</p></a></div>"+
				    "</div>"+
				"    <div class='item'>"+
				      "<div class='col-md-12 col-lg-3'><a href='bookPortal.jsp?isbn=1'><img src='source/genericBook2.png;text=Good Book;' class='img-responsive'><p style='color:black;'>Title</p></a></div>"+
				    "</div>"+
				"    <div class='item'>"+
				      "<div class='col-md-12 col-lg-3'><a href='bookPortal.jsp?isbn=1'><img src='source/genericBook1.png;' class='img-responsive'><p style='color:black;'>Title</p></a></div>"+
				    "</div>"+
				"    <div class='item'>"+
				      "<div class='col-md-12 col-lg-3'><a href='bookPortal.jsp?isbn=1'><img src='source/genericBook2.png;' class='img-responsive'><p style='color:black;'>Title</p></a></div>"+
				    "</div>"+
				  "</div>"+
				"  <a class='left carousel-control' href='#myCarousel' data-slide='prev'><i class='glyphicon glyphicon-chevron-left'></i></a>"+
				  "<a class='right carousel-control' href='#myCarousel' data-slide='next'><i class='glyphicon glyphicon-chevron-right'></i></a>"+
				"</div>"+
				"</div>"+
				"<div id='push'></div>"+
				"<script type='text/javascript' src='//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js'></script>"+
				"<script type='text/javascript' src='//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js'></script>"+
				"<!-- JavaScript jQuery code from Bootply.com editor  -->       "+
				"<script type='text/javascript'>   "+
				"$(document).ready(function() {"+
				            "$('#myCarousel').carousel({"+
				  "interval: 20000"+
				"})"+
				"$('.carousel .item').each(function(){"+
				  "var next = $(this).next();"+
				"  if (!next.length) {"+
				    "next = $(this).siblings(':first');"+
				  "}"+
				"  next.children(':first-child').clone().appendTo($(this));"+
				  "for (var i=0;i<2;i++) {"+
				    "next=next.next();"+
				"    if (!next.length) {"+
				      "next = $(this).siblings(':first');"+
				    "}  "+
				"    next.children(':first-child').clone().appendTo($(this));"+
				  "}"+
				"});     "+
				        "});      "+
				"        </script>";
	}
	
	/**
	 * Get some of the user info to display on their profile page
	 * @throws Exception 
	 */
	public String customerProfileInfo(String login, Db db, Customer user) throws Exception
	{	
		int cid= db.getCidFromLogin(login);  //db.insertTrust(isTrusted, cid1, cid2);
		int myCid = db.getCidFromLogin(user.getLoginId());
		return
		"<!-- body content -->"+
				"<div class='container'>"+
				"<div class='bs-docs-section'>"+
				"        <div class='row'>"+
				"          <div class='col-lg-12'>"+
				"            <div class='page-header'>"+				
				"              <h1 id='forms'>"+login+"</h1>"+
				"              Trust Level: "+ db.getTrust(login)+				
				"            </div>"+
				"          </div>"+
				"        </div><%out.write(errorMessage);%>"+
				"		<!-- This is where an image goes-->"+
				"        <div class='row'>"+
				"          <div class='col-lg-12'>"+
				"            <div class='well bs-component'>  "+				
				"            <div class='col-md-4'>"+
				"        		 <img src='source/default_user.jpg' alt='Registration books' height='25%' width='40%'>"+
				"      		</div>                       "+
				"                  <legend>"+db.getCustomerName(login)+"</legend>          "+
				"	             <div class='offset-9'>"+ 
				"                           <form class=\"form-horizontal\" method=\"post\" action=\"\">                  "+
				"			             	<button type='submit' id=\"trust\" name='trust' class='btn btn-default btn-sm'>"+
				"								<span class='glyphicon glyphicon-thumbs-up'></span> Trust"+
				"							</button>"+
				"							<button type='submit' id=\"distrust\" name='distrust' class='btn btn-default btn-sm'>"+
				"								 <span class='glyphicon glyphicon-thumbs-down'></span> Distrust"+
				"							</button></form>"+
				//this handles the trust and distrust buttons
				/*				"<script src='//ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js'></script>"+
								"<script type='text/javascript'>"+
								"  $(document).ready(function() {  "+
								        "$('#trust').on('click', function() {"+
								            "isTrusted();"+
								        "});"+
								"        $('#distrust').on('click', function() {"+
								          "isUntrusted();"+
								        "});"+
								  "});"+
								"  function isTrusted() {"+
									db.insertTrust(1, myCid, cid)+
								"      alert('Button Click Working!!');"+
								  "}"+
								"  "+
								  "function isUntrusted() {"+
							//	  db.insertTrust(-1, myCid, cid)+
								"      alert('Button Click Not Working!!');"+
								  "}"+
								"</script>"+*/
				//return a short bio about the user  
				"	             </div>"+
				"          	</div>"+				
				"          </div>   "+
				"        </div>"+
				"        "+
				"        <div class='row'>  "+
				"        	<div class='col-lg-6 col-md'>	"+
				"        		<div class='well bs-component'> 	"+
				"			   		<legend>Customer Info</legend>"+
				"			   		<p>Some Info about the customer.</p>"+
				"			   </div>"+
				"          	</div>"+
				"          	<div class='col-lg-6 col-md'>	"+
				//shows a carousel of likes of the user
				"        		<div class='well bs-component'> 	"+
				"			   		<legend>Recent Purchases</legend>"+
				"			   		<p>Little Carousel of all books purchased</p>"+
				"			   </div>"+
				"          	</div>"+
				"        </div>"+
				"  </div>"+
				"  </div>";

	}
	
	/**
	 * Displays an error if no user is specified
	 * @return
	 */
	public String customerProfileError()
	{
		return "<p>Please enter a user</p>";
	}
	
	/**
	 * builds modals
	 * @return
	 */
	public String buildModal(String page)
	{
		Modals modal = new Modals();
		return modal.printModal(page, "");		
	}
	
	/**
	 * Gets a string representation of a book
	 * @return
	 * @throws Exception 
	 */
	public String bookPortalInfo(int isbn, Db db) throws Exception
	{
		Book b = db.getBookInfo(isbn);
		return b.toString(); //TODO i need to get 
	}
	

	
	public Book getBook(int isbn, Db db) throws Exception
	{ 
		return db.getBookInfo(isbn); 
	}
	
	/**
	 * Display all the feedbacks   //feedbacks could display themselves
	 * @param isbn
	 * @param db
	 * @return
	 * @throws Exception
	 */
	public String bookPortalFeedback(int isbn, Db db) throws Exception
	{
		ArrayList<Feedback> temp = db.getFeedbackInfo(isbn);
		String returnString="";
		int i = 1;
		for (Feedback feedback : temp)
		{
			returnString+=feedback.toString(db, i);
			i++;
		}
		return returnString;
	}
	
	
	
	//getSomeUserInfo, addTrustRecord, getTrustRecord,removeTrustRecord
	/**
	 * Adds a trust records about a customer, might need to contact DB
	 */
	public String addTrustRecord()
	{
		return "";
	}
	
	/**
	 * retrieves all the trustRecords about a customer
	 */
	public String getTrustRecord(String login)
	{
		return "";
	}
	
	/**
	 * Prints out a search bar
	 * Although this is cool, it isn't necessary
	 */
	public String searchBar()
	{
		return "";
	}
	
	/**
	 * 
	 */
	public String bookBrowsingResults()
	{
		return "";
	}

}
