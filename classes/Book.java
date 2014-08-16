package classes;

import java.util.ArrayList;

public class Book {
	private int isbn = 0;
	private String title = "";
	private String publisher = "";
	private String year="";
	private double price = 0;
	private String format = "";
	private String subject = "";
	private int quantity = 0;
	private ArrayList<String> author = new ArrayList<String>();
	
	/**
	 * This is for debugging purposes
	 * @return String
	 */
	public String toString()
	{
		String returnString= ""+
				"<h1>Book Info</h1>"+
				"<ul>"+
					"<li>ISBN: "+isbn+"</li>"+
				"	<li>Title:"+title+"</li>"+
					"<li>Publisher:"+publisher+"</li>"+
				"	<li>Year:"+year+"</li>"+
					"<li>Price:"+price+"</li>"+
				"	<li>Format:"+format+"</li>"+
					"<li>Subject:"+subject+"</li>"+
				"	<li>Quantity:"+quantity+"</li>"+
					"<li>Authors:</li>"+
				"	<ul>";
				for (String s: author)
				{
					returnString += "<li>"+ s + "</li>";
				}
				 returnString+="</ul>"+
				"</ul>";
		//add authors
			
		return returnString;
	}
	
	public int getIsbn() {
		return isbn;
	}
	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = Integer.parseInt(isbn);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = Double.parseDouble(price);
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		
		this.quantity = Integer.parseInt(quantity);;
	}
	public ArrayList<String> getAuthors() {
		return author;
	}
	public void addAuthor(String author) {
		 this.author.add(author);
	}
	public void setQuantity(int quantity) {
		this.quantity =quantity;
		
	}  
	

}
