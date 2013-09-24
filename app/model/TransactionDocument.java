package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name="transaction_documents")
public class TransactionDocument extends Model {
	@Id
	@Column(name="id")
	private long id;
	
	@Column(name="transaction_id")
	private long transaction_id;
	
	@Column(name="created_at")
	private Date created;
	
	@Column(name="document")
	private String document;
	
	@Column(name="number_sections")
	private int numberOfSections;
	
	@Column(name="title")
	private String title;
	
	public static Finder<Long,TransactionDocument> find = new Finder<Long,TransactionDocument>(long.class, TransactionDocument.class); 
	
	public long getId() {
		return id;
	}
	
	public void setId(long value){
		id = value;
	}
	
	public long getTransactionId(){
		return transaction_id;
	}
	
	public void setTransactionId(long value){
		transaction_id = value;
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
