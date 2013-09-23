package model;

import java.util.Date;

public class TransactionDocument {
	private long id;
	private Date created;
	private String document;
	private int numberOfSections;
	private String title;
	
	public long getId() {
		return id;
	}
	
	public void setId(long value){
		id = value;
	}
	
	public Date getCreated(){
		return created;
	}
	
	public void setCreated(Date value){
		created = value;
	}
	
	public String getDocument(){
		return document;
	}
	
	public void setDocument(String value){
		document = value;
	}
	
	public int getNumberOfSections(){
		return numberOfSections;
	}
	
	public void setNumberOfSections(int value){
		numberOfSections = value;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String value){
		title = value;
	}
}
