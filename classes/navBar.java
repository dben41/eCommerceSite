package classes;
/**
 * This provides a nav bar to the user for each page 
 * and it changes the pages dynamically.
 * @author Daryl
 * @date Spring 2014
 *
 */
public class navBar {
	private String currentPage;
	
	/**
	 * Constructor
	 */
	public void navBar()
	{
		this.currentPage=""; //this is the defalt value
	}
	
	/**
	 * Sets the current page
	 */
	public void setCurrentPage(String page)
	{
		//verify that the param page is valid
		if(page.equals("home")||page.equals("login")||page.equals("registration")||page.equals("logout")||page.equals("admin"))
			this.currentPage=page;
	}
	
	/**
	 * Prints out the current page
	 */
	public String printNav(Customer user)
	{
		if (user==null)
			user=new Customer();
		String outputNav="";
		String currentHome="";
		String currentLogin="";
		String currentRegistration="";
		//check to see what the current page is
		if(this.currentPage.equals("home")) currentHome="class=\"active\""; else currentHome=" ";
		if(this.currentPage.equals("login")) currentLogin="class=\"active\""; else currentLogin=" ";
		if(this.currentPage.equals("registration")) currentRegistration="class=\"active\""; else currentRegistration=" ";
		//return the nav bar
		outputNav += "	<div class='navbar navbar-inverse navbar-fixed-top'> "+
		"<div class='container'>"+
			"<div class='navbar-header'>"+
				"<button type='button' class='navbar-toggle' data-toggle='collapse' data-target='.navbar-collapse'>"+
					"<span class='icon-bar'></span>"+
					"<span class='icon-bar'></span>"+
					"<span class='icon-bar'></span>"+
				"</button>"+
				"<a class=' navbar-brand' href='#'>Bookstore</a>"+
			"</div>"+
			"<div class='navbar-collapse collapse'>"+
				"<ul class='nav navbar-nav'>"+
					"<li "+currentHome+ "><a href='index.jsp'>Home</a></li>"+
					"<li ><a href='browseBooks.jsp'>Browse Books</a></li>"+
					"<li><a href='manageAccount.jsp'>My Account</a></li>"+
										
				"</ul>";
				if(!user.getLoggedIn())
				{
					outputNav +=
				"<ul class='nav navbar-nav navbar-right'>"+		
					//"<li><a href='shoppingCart.jsp'>Shopping Cart <span class=\"badge\">"+user.getShoppingCartList().size()+"</span> </a></li>"+  
					"<li "+currentLogin+"><a href='login.jsp' id='btnAbout'>Login</a></li>"+
					"<li "+currentRegistration+"><a href='registration.jsp'>Registration</a></li>"+
				"</ul>";
				} else {
					outputNav +=
					"<ul class='nav navbar-nav navbar-right'>"+	
							"<li><a href='shoppingCart.jsp'><span class='glyphicon glyphicon-shopping-cart'></span> <span class=\"badge\">"+user.getShoppingCartList().size()+"</span> </a></li>"+
							"<li>Welcome, "+user.getName()+"</li>"+
							"<li><a href='logout.jsp'>Logout</a></li>";
							if(user.getRole().equals("admin"))
							 outputNav+="<li "+currentRegistration+"><button class='btn btn-primary' id='showLeftPush'>Admin</button></li>";
					outputNav +=
					"</ul>";
				}
				outputNav+=
			"</div>"+
		"</div>"+
	"</div>"+
"</div>";
				
				return outputNav;
	}
	
	/**
	 * Prints out the current page
	 */
	public String printAdmin()
	{
		return " <!--the menu--> "+
		"<nav class='cbp-spmenu cbp-spmenu-vertical cbp-spmenu-left' id='cbp-spmenu-s1'>"+
			"<a href='index.jsp'>Celery seakale</a>"+
			"<h3>Admin Menu</h3>"+
			"<a href='NewBook.jsp'>Insert New Book</a>"+
			"<a href='UpdateQuantity.jsp'>Update Quantities </a>"+
			"<a href='UserRecords.jsp'>User Records</a>"+
			"<a href='error.jsp'>Update Users</a>"+
			"<a href='statsDashboard.jsp'>Stats Dashboard</a>"+
			"<a href='importAll.jsp'>Import/Export Data</a>"+
			
		"</nav>"+


		"<!-- Classie - class helper functions by @desandro https://github.com/desandro/classie -->"+
		"<script src='style/SlidePushMenus/js/classie.js'></script>"+
		"<script>"+
			"var menuLeft = document.getElementById( 'cbp-spmenu-s1' ),	"+								
		    "showLeftPush = document.getElementById( 'showLeftPush' ), "+
				"body = document.body;"+

	
			"showLeftPush.onclick = function() {"+
				"classie.toggle( this, 'active' );"+
				"classie.toggle( body, 'cbp-spmenu-push-toright' );"+
				"classie.toggle( menuLeft, 'cbp-spmenu-open' );"+
			"};"+
		"</script> ";
	}

}
