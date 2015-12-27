/**
 * This contains all the necessary info to connect 
 * to the database and to run the queries, and return 
 * correct info to the user interface.
 * 
 * @author Daryl Bennett
 * @date Spring 2014
 */
package helpers;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import classes.Book;
import classes.Customer;
import classes.Feedback;

public class Db {
	//this is to SELECT
	public Connection con;
	public Statement stmt;
	public Statement prepareStatement;
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	public Db() throws Exception{
		try{
		 	String userName = "xxxx"; //the credentials now longer work, since the instance is no longer deployed.
	   		String password = "xxxxx"; 
	        String url = "jdbc:mysql://xxxx"; 
		    Class.forName ("com.mysql.jdbc.Driver").newInstance ();
		    con = DriverManager.getConnection (url, userName, password);
			//stmt = con.createStatement();
			
        } catch(Exception e) {
			System.err.println("Unable to open mysql jdbc connection. The error is as follows,\n");
            		System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	/*
	 * This verifies that the username someone chooses a unique username
	 */
	public Boolean verifyUnique(String login/*, Statement stmt*/)
	{
		
		
		String sql="SELECT COUNT(login) AS count FROM customers WHERE login= '"+login+"'";	
		ResultSet rs=null;		
		try{
			stmt = con.createStatement();
			rs=stmt.executeQuery("SELECT COUNT(login) AS count FROM customers WHERE login= '"+login+"'");
			while (rs.next())
			 {
				if(rs.getInt("count")!=0)
					return false;
			 }
		}catch(Exception e){
			System.out.println("Exception: " + e);
		}
		return true;
	}
	
	/*
	 * This registers a new user
	 */
	public void register(String login, String name, String password, String creditCard, String address, String phone)
	{
		//check login for uniqueness
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement=con.prepareStatement("INSERT INTO customers (name, login, password, creditCard, address,  phone,role) VALUES (?,?,?,?,?,?,?)");
			 preparedStatement.setString(1, name);
		     preparedStatement.setString(2, login);
		     preparedStatement.setString(3,password);
		     preparedStatement.setString(4, creditCard);
		     preparedStatement.setString(5, address);
		     preparedStatement.setString(6, phone);
		     preparedStatement.setString(7, "client"); //client by default
		     preparedStatement.executeUpdate();
	   		return;
		 }catch(Exception e)
		 	{
		 		System.out.println("Problem with registering: "+e);
		 	}
		return;
	}

	
	/*
	 *3.  This is how you get all the info about a user
	 */
	public String customerInfo(int cid) throws Exception
	{
		String result="<h1> User "+this.getLoginFromCid(cid)+"'s stats.</h1>";
		PreparedStatement preparedStatement = null;
		ResultSet rs=null; 
		int counter = 1;
		try{
			
			//get all the personal data
		    preparedStatement = con.prepareStatement("SELECT * FROM customers WHERE cid=?");
		    preparedStatement.setInt(1, cid);
		    //error check
		    rs = preparedStatement.executeQuery();	
//		    if(!rs.next())
//		    	return "'"+ cid +"'"+ " doesn't exist!";
   		 	while (rs.next())
			{
   	   		  result += ""
   	   		  		+ "<table class='table table-stribed table hover'><thead>Info</thead><th>Name:</th><tr><td> "+ rs.getString("name")+"</td></tr><tr><th>Login:</th><td> "+rs.getString("login")+"</td></tr><tr><th> Credit Card:</th><td> " + rs.getString("creditCard")+"</td></tr><tr><th> Address: </th><td>"+rs.getString("address")+ "</td></tr><tr><th> Phone:</th><td> "+rs.getString("phone")+"</td></tr></table>"; 
			}
   		 	
   		    //get the full history of sales from user  (book name, number of copies, date)
   		 	result += "<h3>Purchase History</h3>";
   		 	preparedStatement = con.prepareStatement("SELECT * FROM placeOrder p, book b WHERE p.isbn=b.isbn AND cid=?");
		    preparedStatement.setInt(1, cid);
		    rs = preparedStatement.executeQuery();	
		    result+="<table class='table table-stribed table hover'><th>Book Name:</th><th> Quantity:</th><th>  Date: </th>";
		 	while (rs.next())
			{
	   		  result += "<tr><td>  "+ rs.getString("quantity")+"</td></tr><tr><td> "+rs.getString("quantity")+"</td><td>" + rs.getDate("orderDate")+"</td></tr>"; 
			}
		 	result +="</table>";
		 	
   		    //full history of feedback given by this user     
   		 	result += "<h3>User Feedback About Books</h3>";
   		 	preparedStatement = con.prepareStatement("SELECT title,rating,comments,cdate,f.fid FROM feedback f, book b WHERE f.isbn=b.isbn AND cid=?");
		    preparedStatement.setInt(1, cid);
		    rs = preparedStatement.executeQuery();		   
		 	while (rs.next())
			{
	   		  result += "<table class='table table-stribed table hover'><thead>FID #"+ rs.getInt("fid") +"</thead><th> Book: </th><tr><td> "+ rs.getString("title")+"</td></tr><tr><th>Rating: </th><td>"+rs.getInt("rating")+" </td></tr><tr><th>Comments:</th><td> " + rs.getString("comments")+"</td></tr><tr><th> Date:</th><td> "+rs.getDate("cdate")+"</td></tr></table>"; 
			}
		 	
   		 	//list of all feedback they rank in usefulness
   		 	result += "<h3>User feedback they found useful</h3> \n";
   		 	preparedStatement = con.prepareStatement("SELECT title,r.rating,comments,cdate,name FROM feedback f,ratings r, book b, customers c WHERE b.isbn=f.isbn AND f.fid=r.fid AND c.cid=r.cid AND r.cid=?");
		    preparedStatement.setInt(1, cid);
		    rs = preparedStatement.executeQuery();		   
		 	while (rs.next())  
			{
		 		result += "<table class='table table-stribed table hover'><thead>Feedback</thead><th>Book: </th><tr><td>"+ rs.getString("title")+"</td></tr><tr><th> Rating: </th><td>"+rs.getInt("rating")+" </td></tr><tr><th>Comments:</th><td> " + rs.getString("comments")+"</td></tr><tr><th> Date:</th><td> "+rs.getDate("cdate")+"</td></tr><tr><th> Given By:</th><td> "+rs.getString("name")+"</td></tr></table>"; 
			}
		 	
   		 	//all of their trust of other users
   		 	result += "<h3>Users trusted </h3>";  //TODO insert date
   		 	preparedStatement = con.prepareStatement("SELECT name, cid2,isTrusted FROM trust t, customers c WHERE  c.cid=t.cid2 AND t.cid1=?");
		    preparedStatement.setInt(1, cid);
		    rs = preparedStatement.executeQuery();	
		    result += "<table class='table table-stribed table hover'><thead><em>0-false 1-true</em></thead>";
		 	while (rs.next())
			{
	   		  result += "<tr><th>Name:</th><td> "+ rs.getString("name")+"</td></tr><tr><th> Trusted?: </th><td> "+rs.getInt("isTrusted")+"</td></tr></table>"; 
			}
   		 	rs.close(); //TODO make sure to close all
		    return result;
		} catch(Exception e){
			throw e;
		}
		
	}
	
	/*
	 *3.5  This is how you get all the info about a user with a string login
	 */
	public String customerInfo(String login) throws Exception
	{
		String result="<h1>"+login+"'s stats.</h1>";
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		int cid = this.getCidFromLogin(login);
		int counter = 1;
		try{
			
			//get all the personal data
		    preparedStatement = con.prepareStatement("SELECT * FROM customers WHERE login=?");
		    preparedStatement.setString(1, login);
		    //error check
		    rs = preparedStatement.executeQuery();	
//		    if(!rs.next())
//		    	return "'"+ cid +"'"+ " doesn't exist!";
   		 	while (rs.next())
			{
   	   		  result += ""
   	   		  		+ "<table class='table table-condensed table-striped table-hover'><thead>Info</thead><th>Name:</th><td> "+ rs.getString("name")+"</td></tr><tr><th>Login:</th><td> "+rs.getString("login")+"</td></tr><tr><th> Credit Card:</th><td> " + rs.getString("creditCard")+"</td></tr><tr><th> Address: </th><td>"+rs.getString("address")+ "</td></tr><tr><th> Phone:</th><td> "+rs.getString("phone")+"</td></tr></table>"; 
			}
   		 	
   		    //get the full history of sales from user  (book name, number of copies, date)
   		 	result += "<h3>Purchase History</h3>";
   		 	preparedStatement = con.prepareStatement("SELECT * FROM placeOrder p, book b WHERE p.isbn=b.isbn AND cid=?");
		    preparedStatement.setInt(1, cid);
		    rs = preparedStatement.executeQuery();	
		    result += "<table class='table table-condensed table-striped table-hover'><th>Book Title:</th><th> Quantity:</th><th>  Date: </th>";
		 	while (rs.next())
			{
	   		  result += "<tr><td>  "+ rs.getString("title")+"</td><td> "+rs.getString("quantity")+"</td><td>" + rs.getDate("orderDate")+"</td></tr>"; 
			}
		 	result += "</table>";
		 	counter=1;
   		    //full history of feedback given by this user     
   		 	result += "<h3>User Feedback About Books</h3>"+
   		    "<table class='table table-condensed table-striped table-hover'><th> Book: </th><th>Rating: </th><th>Comments:</th><th> Date:</th>";
   		 	preparedStatement = con.prepareStatement("SELECT title,rating,comments,cdate,f.fid FROM feedback f, book b WHERE f.isbn=b.isbn AND cid=?");
		    preparedStatement.setInt(1, cid);
		    rs = preparedStatement.executeQuery();		   
		 	while (rs.next())
			{
	   		  result += "<tr><td> "+ rs.getString("title")+"</td> <td>"+rs.getInt("rating")+" </td><td> " + rs.getString("comments")+"</td><td> "+rs.getDate("cdate")+"</td></tr>"; 
			}
		 	result += "</table>";
   		 	//list of all feedback they rank in usefulness
   		 	result += "<h3>User feedback they found useful</h3> "
   		 			+ "<table class='table table-condensed table-striped table-hover'><th>Book: </th><th> Rating: </th><th>Comments:</th><th> Date:</th><th> Given By:</th>";
   		 	preparedStatement = con.prepareStatement("SELECT title,r.rating,comments,cdate,name FROM feedback f,ratings r, book b, customers c WHERE b.isbn=f.isbn AND f.fid=r.fid AND c.cid=r.cid AND r.cid=?");
		    preparedStatement.setInt(1, cid);
		    rs = preparedStatement.executeQuery();		   
		 	while (rs.next())  
			{
		 		result += "<tr><td>"+ rs.getString("title")+"</td><td>"+rs.getInt("rating")+" </td><td> " + rs.getString("comments")+"</td><td> "+rs.getDate("cdate")+"</td><td> "+rs.getString("name")+"</td></tr>"; 
			}
		 	result += "</table>";
   		 	//all of their trust of other users
   		 	result += "<h3>Users trusted </h3>";  //TODO insert date
   		 	preparedStatement = con.prepareStatement("SELECT name, cid2,isTrusted FROM trust t, customers c WHERE  c.cid=t.cid2 AND t.cid1=?");
		    preparedStatement.setInt(1, cid);
		    rs = preparedStatement.executeQuery();	
		    result += "<table class='table table-striped table-condensed table-hover'><thead><em>0-false 1-true</em></thead><th>Name:</th><th> Trusted?: </th>";
		 	while (rs.next())
			{
	   		  result += "<tr><td> "+ rs.getString("name")+"</td><td> "+rs.getInt("isTrusted")+"</td></tr>"; 
			}
		 	result += "</table>";
   		 	rs.close(); //TODO make sure to close all
		    return result;
		} catch(Exception e){
			throw e;
		}
		
	}
	
	
	/*
	 *4.  Enters new book
	 */
	public int enterNewBook(int isbn, String title, String publisher, String yearPublished, int numCopies, double price, String format, String subject) throws Exception
	{
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		int quantity=0;
		int aid=0;
		//price= 0.0;
		try{
			//insert into place order
			preparedStatement=con.prepareStatement("INSERT INTO book (isbn,title,publisher,yearPublished, price,format,subject,quantity) VALUES(?,?,?,?,?,?,?,?)");			
			preparedStatement.setInt(1, isbn);
		    preparedStatement.setString(2, title);
		    preparedStatement.setString(3,publisher);
		    preparedStatement.setString(4, yearPublished);
		    preparedStatement.setDouble(5, price);
		    preparedStatement.setString(6,format);
		    preparedStatement.setString(7, subject);
		    preparedStatement.setInt(8,numCopies);
		    preparedStatement.executeUpdate();
		    
		    //now get the total amount of copies
		    preparedStatement=con.prepareStatement("SELECT quantity FROM book WHERE isbn=?");
		    preparedStatement.setInt(1, isbn);
		    rs = preparedStatement.executeQuery();		   
   		 	while (rs.next())
			{
   		 	//return the price
   	   		  quantity= rs.getInt("quantity");
			}
   		 	rs.close();
			return quantity;
		} catch(Exception e){
			throw e;
		}
		
	}

	/*
	 * 6. Feeback 
	 */
	public void giveFeedback(int cid, int isbn, String orderDate, int rating, String comments) throws Exception
	{
		
		//check to see if it exists in database
		PreparedStatement preparedStatement = null;
		//converts date string to java date to mysql date
		java.util.Date date = dateFormat.parse(orderDate);
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		try{
			//insert into place order
			preparedStatement=con.prepareStatement("INSERT INTO feedback (cid,isbn,cdate,rating,comments) VALUES(?,?,?,?,?);");			
			preparedStatement.setInt(1, cid);
		    preparedStatement.setInt(2, isbn);
		    preparedStatement.setDate(3, sqlDate);
		    preparedStatement.setInt(4, rating);
		    preparedStatement.setString(5, comments);
		    preparedStatement.executeUpdate();	    
		} catch(Exception e){
			throw e;
		}		
	}
	
	/**
	 * checks to see if a user has rated a feedback already
	 */
	public boolean usefulnessExists(int fid, int cid)
	{
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		try{
			preparedStatement=con.prepareStatement("SELECT COUNT(cid) AS count FROM ratings r WHERE fid=? AND cid=?");
		    preparedStatement.setInt(1, fid);
		    preparedStatement.setInt(2, cid);
		    rs = preparedStatement.executeQuery();		   
			while (rs.next())
			{
			 	//return the price
		   		  if( rs.getInt("count") == 0)
		   			  return false;
		   		  else 
		   			  return true;	
		   		  
			}    
			rs.close();
		}catch(Exception e){
			
		}
		return false;
	}
	
	/*
	 * 7. Usefulness rating
	 */
	public void insertUsefulness(int fid, int cid, int rating) 
	{
		PreparedStatement preparedStatement = null;
		try{
			//modify old entry
			if(this.usefulnessExists(fid,cid))
			{//update
				preparedStatement=con.prepareStatement("UPDATE ratings Set rating=? WHERE fid=? AND cid=?;");			
				preparedStatement.setInt(1, rating);
			    preparedStatement.setInt(2, fid);
			    preparedStatement.setInt(3, cid);
			    preparedStatement.executeUpdate();	
				
			}else{
				//insert new entry
				preparedStatement=con.prepareStatement("INSERT INTO ratings (cid,fid,rating) VALUES (?,?,?);");			
				preparedStatement.setInt(1, cid);
			    preparedStatement.setInt(2, fid);
			    preparedStatement.setInt(3, rating);
			    preparedStatement.executeUpdate();	 
			}
		  
		    
		} catch(Exception e){	
			System.out.println("Data not entered! fid, cid was incorrect! \n");
		}	
	}
	
	/*
	 * 8. Trust records
	 */
	public int insertTrust(int isTrusted,int cid1, int cid2) throws Exception
	{
		PreparedStatement preparedStatement = null;
		try{
			//insert into place order
			preparedStatement=con.prepareStatement("INSERT INTO trust (cid1,cid2,isTrusted) VALUES (?,?,?);");			
			preparedStatement.setInt(1, cid1);
		    preparedStatement.setInt(2, cid2);
		    preparedStatement.setInt(3, isTrusted);
		    preparedStatement.executeUpdate();	    
		} catch(Exception e){
			System.out.println("Duplicate entry! Your feedback wasn't entered! \n");
		}	
		
		return 0;
	}
	
	/**
	 * Returns a book object just from the isbn
	 * @throws Exception 
	 */
	public Book getBookInfo(int isbn) throws Exception
	{
		Book b = new Book();
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		
		try{
			//gets authors
			preparedStatement=con.prepareStatement("SELECT * FROM book WHERE isbn=?;");			
			preparedStatement.setInt(1, isbn);
		    rs = preparedStatement.executeQuery();	
		    while (rs.next())
			{
   	   		  b.setFormat(rs.getString("format"));
   	   		  b.setIsbn(isbn);
   	   		  b.setPublisher(rs.getString("format"));
   	   		  b.setYear(rs.getString("yearPublished"));
   	   		  b.setTitle(rs.getString("title"));
   	   		  b.setQuantity(rs.getInt("quantity"));
   	   		  b.setPrice(rs.getString("price"));
   	   		  b.setSubject(rs.getString("subject")); 	   		 
			}
		    //gets the author(s)
		    preparedStatement=con.prepareStatement("SELECT authorName FROM writtenBy w, author a WHERE w.aid=a.aid AND isbn=?;");			
			preparedStatement.setInt(1, isbn);
		    rs = preparedStatement.executeQuery();	
		    while (rs.next())
			{
   	   		  b.addAuthor(rs.getString("authorName")); 	   		 
			}
		    rs.close();
		} catch(Exception e){
			throw e;
		}	
		return b;	
	}
	
	/**
	 * Returns all feedbacks as ArrayList Feedback just from the isbn
	 * @throws Exception 
	 */
	public ArrayList<Feedback> getFeedbackInfo(int isbn) throws Exception
	{
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		ArrayList<Feedback> feedbackArray=new ArrayList<Feedback>();
		
		try{
			//gets authors
			preparedStatement=con.prepareStatement("SELECT * FROM feedback WHERE isbn=?;");			
			preparedStatement.setInt(1, isbn);
		    rs = preparedStatement.executeQuery();	
		    while (rs.next())
			{
		      Feedback temp = new Feedback();
		      temp.setComments(rs.getString("comments"));
		      temp.setDate(rs.getDate("cdate").toString());
		      temp.setGiverLogin(getLoginFromCid(rs.getInt("cid")));  
		      temp.setComments(rs.getString("comments"));
		      temp.setRating(rs.getInt("rating"));
		      temp.setFid(rs.getInt("fid"));
		      double usefulness = getUsefulnessFeedback(temp.getFid());
		      temp.setUsefulness(usefulness);
		      feedbackArray.add(temp);	      
			}
		    rs.close();
		} catch(Exception e){
			throw e;
		}	
		return feedbackArray;	
	}
	
	/**
	 * Get usefulness ratings of feedback
	 */
	public double getUsefulnessFeedback(int fid) throws Exception
	{
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		double usefulness = 0;
		try{
			//insert into place order
			preparedStatement=con.prepareStatement("SELECT AVG(rating) AS feedbackUsefulness FROM ratings WHERE fid = ?");			
			preparedStatement.setInt(1, fid);
		    rs = preparedStatement.executeQuery();	
		    while (rs.next())
			{
		    	usefulness= rs.getDouble("feedbackUsefulness");
			}
		    rs.close();
		} catch(Exception e){
			throw e;
		}	
		return usefulness;
	}
	
	/*getFeebacks
	 * gets cid from login
	 */
	public int getCidFromLogin(String login) throws Exception
	{
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		int cid = 0;
		try{
			//insert into place order
			preparedStatement=con.prepareStatement("SELECT cid FROM customers WHERE login=?;");			
			preparedStatement.setString(1, login);
		    rs = preparedStatement.executeQuery();	
		    while (rs.next())
			{
   	   		  cid= rs.getInt("cid");
			}
		    rs.close();
		} catch(Exception e){
			throw e;
		}	
		return cid;
	}
	
	/*
	 * gets trust
	 */
	public int getTrust(String login) throws Exception
	{
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		int trust = 0;
		try{
			//insert into place order
			preparedStatement=con.prepareStatement("SELECT c.name AS name2, SUM(isTrusted) AS trust FROM trust t, customers c, customers d WHERE c.cid=t.cid2 AND c.login=? AND d.cid=t.cid1 GROUP BY t.cid2;");			
			preparedStatement.setString(1, login);
		    rs = preparedStatement.executeQuery();	
		    while (rs.next())
			{
   	   		  trust= rs.getInt("trust");
			}
		    rs.close();
		} catch(Exception e){
			throw e;
		}	
		return trust;
	}



	



