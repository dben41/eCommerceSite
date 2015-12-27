spackage helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This function is to read in some CSV files that contain fake data
 * for this project. 
 * 
 * I'm parsing each table as a separate csv file, and each file type is
 * put in a separate function, they are all called in the constructor.
 * 
 * @author Daryl
 * @date Spring 2014
 *
 *
 *03/31/2014 - customers now handles roles
 */
public class ParseCsv {
	String fileToParse = "";
	BufferedReader fileReader = null;
	final String DELIMITER = ",";
	int authors, book, customers, feedback, placeOrder, ratings, trust, writtenBy;
	//database
	Db db= null;
	//constructor
	public ParseCsv()
	{
		//create database object to insert and delete
		try {db= new Db();} catch (Exception e) {e.printStackTrace();}
	}
	
	/*
	 * This function imports all of the data from all of the csv file into the database.
	 * The order of when the fxns are called is ESSENTIAL because of foreign key constraints.
	 * 
	 */
	public void importAll() throws Exception
	{
		//delete all the data to avoid duplicate keys
		this.clearDatabase();
		//import authors
		this.importAuthors();
		System.out.println("Authors imported successfully! Records imported: " +authors);
		//import books
		this.importBooks();
		System.out.println("Books imported successfully! Records imported: " + book );
		//import customers
		this.importCustomers();
		System.out.println("Customers imported successfully!  Records imported: " + customers );
	    //import orders
		this.importOrders();
		//import feedback
		this.importFeedback();
		System.out.println("Feedback imported successfully! Records imported: " + feedback);
		//import writtenBy
		this.importWrittenBy();
		System.out.println("writtenBy imported successfully!  Records imported: " + writtenBy);
		//import trust
		this.importTrust();
		System.out.println("Trust imported successfully!  Records imported: " + trust);
    	//	import ratings
		this.importRatings();
		System.out.println("Ratings imported successfully!  Records imported: " + ratings);
		//import placeOrder
		this.importPlaceOrder();
		System.out.println("placeOrder imported successfully!  Records imported: " + placeOrder);
		System.out.println("All files imported!");
	}
	
	
	/*
	 * Imports the 'placeOrder' CSV file data and inserts it into database
	 */
	public void importPlaceOrder() {
		try{
			String line = "";
			int flag = 0;
			//file to be parsed
			fileToParse = "csvFiles/placeOrder.csv";
			
			//Create the file Reader
			fileReader = new BufferedReader(new FileReader(fileToParse));
			
			//Read the file line by line
			while ((line = fileReader.readLine())!=null)
			{
				//get all tokens on all the available line
				String[] tokens = line.split(DELIMITER);
				if(flag>0)
				 {
					//get cid
					String cidString = tokens[1];
					int cid = Integer.parseInt(cidString);
					//get isbn
					String isbnString = tokens[2];
					int isbn = Integer.parseInt(isbnString);	
					//get order date
					String orderDate = tokens[3];
					//get quantity
					String quantityString = tokens[4];
					int quantity = Integer.parseInt(quantityString);
					
					//insert into the database
					db.placeOrder(cid, isbn, quantity, orderDate);
					//print all tokens
					//System.out.println(token);
				}
				flag++;
			}
			placeOrder=flag-1;
		} catch (Exception e){
			e.printStackTrace();
		}
		finally
		{
			try{
				fileReader.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * Imports the 'ratings' CSV file data and inserts it into database
	 */
	private void importRatings() {
		try{
			String line = "";
			
			//file to be parsed
			fileToParse = "csvFiles/ratings.csv";
			
			//Create the file Reader
			fileReader = new BufferedReader(new FileReader(fileToParse));
			int flag = 0;
			//Read the file line by line
			while ((line = fileReader.readLine())!=null)
			{
				//get all tokens on all the available line
				String[] tokens = line.split(DELIMITER);
				 if(flag>0) //normal i cid	fid	rating
				{
					//get cid
					String cidString = tokens[0];
					int cid = Integer.parseInt(cidString);
					//get fid
					String fidString = tokens[1];
					int fid = Integer.parseInt(fidString);	
					//get rating
					String ratingsString = tokens[2];
					int ratings = Integer.parseInt(ratingsString);

					//insert into the database
					db.insertUsefulness(cid, fid, ratings);
					//print all tokens
					//System.out.println(token);
				}
				 flag++;
			}
			ratings=flag-1;
			
		} catch (Exception e){
			e.printStackTrace();
		}
		finally
		{
			try{
				fileReader.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	/*
	 * Imports the 'trust' CSV file data and inserts it into database   cid1	cid2	isTrusted
     *
	 */
	private void importTrust() {
		try{
			String line = "";
			
			//file to be parsed
			fileToParse = "csvFiles/trust.csv";
			
			//Create the file Reader
			fileReader = new BufferedReader(new FileReader(fileToParse));
			int flag = 0;
			//Read the file line by line
			while ((line = fileReader.readLine())!=null)
			{
				//get all tokens on all the available line
				String[] tokens = line.split(DELIMITER);
				
				if(flag>0)
				{
					//get customer 1
					String cid1String = tokens[0];
					int cid1 = Integer.parseInt(cid1String);
					//get customer 2
					String cid2String = tokens[1];
					int cid2 = Integer.parseInt(cid2String);				
					//get isTrusted
					String isTrustedString = tokens[2];
					int isTrusted = Integer.parseInt(isTrustedString);

					//insert into the database
					db.insertTrust(isTrusted, cid1, cid2);
					//db.insertUsefulness(cid1, cid2, isTrusted);
					//print all tokens
					//System.out.println(token);
				}
				flag++;
			}
			trust=flag-1;
			
		} catch (Exception e){
			e.printStackTrace();
		}
		finally
		{
			try{
				fileReader.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}		
	}

	/*
	 * Imports the 'writtenBy' CSV file data and inserts it into database
	 */
	private void importWrittenBy() {
		try{
			String line = "";
			int flag = 0;
			//file to be parsed
			fileToParse = "csvFiles/writtenBy.csv";
			
			//Create the file Reader
			fileReader = new BufferedReader(new FileReader(fileToParse));
			
			//Read the file line by line
			while ((line = fileReader.readLine())!=null)
			{
				//get all tokens on all the available line
				String[] tokens = line.split(DELIMITER);
				if(flag>0)
				{
					//get aid, author id
					String aidString = tokens[0];
					int aid = Integer.parseInt(aidString);	
					//get book isbn
					String isbnString = tokens[1];
					int isbn = Integer.parseInt(isbnString);				

					//insert into the database
					db.enterWrittenByCsv(aid, isbn);
				
				}
				flag++;	
			}
			writtenBy=flag-1;
			
		} catch (Exception e){
			e.printStackTrace();
		}
		finally
		{
			try{
				fileReader.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}		
		
	}

	/*
	 * Imports the 'feedback' CSV file data and inserts it into database
	 * fid	comments	cdate	isbn	cid	rating
	 */
	private void importFeedback() {
		try{
			String line = "";
			
			//file to be parsed
			fileToParse = "csvFiles/feedback.csv";
			
			//Create the file Reader
			fileReader = new BufferedReader(new FileReader(fileToParse));
			int flag = 0;
			//Read the file line by line
			while ((line = fileReader.readLine())!=null)
			{
				
				//get all tokens on all the available line
				String[] tokens = line.split(DELIMITER);
				 if(flag>0)
				 {
					 String fid = tokens[0];
					//get comments
					String comments = tokens[1];
					//get date	
					String date = tokens[2];
					//get isbn
					String isbnString = tokens[3];
					int isbn = Integer.parseInt(isbnString);
					//get cid
					String cidString = tokens[4];
					int cid = Integer.parseInt(cidString);
					//get rating
					String ratingsString = tokens[5];
					int ratings = Integer.parseInt(ratingsString);

					//insert into the database
					db.giveFeedback(cid, isbn, date, ratings, comments);  
					//print all tokens
					//System.out.println(token);
				 }
				 flag++;	
				}
			feedback=flag-1;
		} catch (Exception e){
			e.printStackTrace();
		}
		finally
		{
			try{
				fileReader.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	/*
	 * Imports the 'orders' CSV file data and inserts it into database
	 * I may not need this function.
	 */
	private void importOrders() {
	}

	/*
	 * Import into customers
	 */
	void importCustomers() {
		try{
			String line = "";
			
			
			//file to be parsed
			fileToParse = "csvFiles/customers.csv";
			int flag = 0;
			//Create the file Reader
			fileReader = new BufferedReader(new FileReader(fileToParse));
			
			//Read the file line by line
			while ((line = fileReader.readLine())!=null)
			{
				//get all tokens on all the available line
				String[] tokens = line.split(DELIMITER);
				if(flag>0)
				 {
					//get name
					String name = tokens[1];
					//get login
					String login = tokens[2];
					//get password	
					String password = tokens[3];
					//get creditCard
					String creditCard = tokens[4];
					//get address
					String address = tokens[5];
					//get phone
					String phone = tokens[6];
					//get phone
					String role = tokens[7];
		
					//insert into the database
					db.register(login, name, password, creditCard, address, phone);  
				 }
				flag++;	
			}
			customers=flag-1;
		} catch (Exception e){
			e.printStackTrace();
		}
		finally
		{
			try{
				fileReader.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	/*
	 * Imports the 'books' CSV file data and inserts it into database
	 * isbn	title	publisher	yearPublished	price	format	subject	quantity
	 */
	private void importBooks() {
		try{
			String line = "";
			int flag = 0;
			
			//file to be parsed
			fileToParse = "csvFiles/book.csv";
			
			//Create the file Reader
			fileReader = new BufferedReader(new FileReader(fileToParse));
			
			//Read the file line by line
			while ((line = fileReader.readLine())!=null)
			{
				//get all tokens on all the available line
				String[] tokens = line.split(DELIMITER);
				 if(flag>0)
				 {
					//get isbn
					String isbnString = tokens[0];
					int isbn = Integer.parseInt(isbnString);
					//get title
					String title = tokens[1];
					//get publisher	
					String publisher = tokens[2];
					//get yearPublished
					String yearPublished = tokens[3];
					//get price
					String priceString = tokens[4];
					double price = Double.parseDouble(priceString);
					//get format
					String format = tokens[5];
					//subject
					String subject = tokens[6];
					//quantity
					String quantityString = tokens[7];
					int quantity = Integer.parseInt(quantityString);
		
					//insert into the database
					db.enterNewBook(isbn, title, publisher, yearPublished, quantity, price, format, subject);							
				 }
				 flag++;		
			}
			book=flag-1;
		} catch (Exception e){
			e.printStackTrace();
		}
		finally
		{
			try{
				fileReader.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	/*
	 * Imports the 'authors' CSV file data and inserts it into database
	 * I may not need to use this... this may be encapsulated elsewhere
	 */
	private void importAuthors() {	
		try{
			String line = "";
			int flag = 0;
			//file to be parsed
			fileToParse = "csvFiles/author.csv";
			
			//Create the file Reader
			fileReader = new BufferedReader(new FileReader(fileToParse));
			
			//Read the file line by line
			while ((line = fileReader.readLine())!=null)
			{
				//get all tokens on all the available line
				String[] tokens = line.split(DELIMITER);
				if(flag>0)
				{
					//get aid, author id
					String aidString = tokens[0];
					int aid = Integer.parseInt(aidString);	
					//get book isbn
					String authorName = tokens[1];
									

					//insert into the database
					db.enterAuthorCsv(aid, authorName);
				
				}
				flag++;	
			}
			authors=flag-1;
			
		} catch (Exception e){
			e.printStackTrace();
		}
		finally
		{
			try{
				fileReader.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}		
	}

	/*
	 * This function is to delete all the old data,
	 * this makes sense when predicting certain tests.
	 */
	public void clearDatabase() throws Exception
	{
		db.deleteAll();
	}

	/**
	 *  This test tests the number of records imported
	 * @param option
	 *     -int authors, book, customers, feedback, placeOrder, ratings, trust, writtenBy
	 * @return quantity of records imported
	 */
	public int recordsImported(String option) 
	{
		int returnInt;
		switch(option)
		{
			case "authors":
				returnInt=authors;
				break;
			case "book":
				returnInt=book;
				break;
			case "customers":
				returnInt=customers;
				break;
			case "feedback":
				returnInt=feedback;
				break;
			case "placeOrder":
				returnInt=placeOrder;
				break;
			case "ratings":
				returnInt=ratings;
				break;
			case "trust":
				returnInt=trust;
				break;
			case "writtenBy":
				returnInt=writtenBy;
				break;
			default:
				System.out.println("Error! invalid tag name!");
				return 0;
		
		}
		return returnInt;
	}
}
