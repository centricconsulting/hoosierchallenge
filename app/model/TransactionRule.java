package model;

import java.util.Date;

public class TransactionRule {
	private long id;
	private Date created;
	private String rule;
	private Transaction transaction = null;
	
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
	
	public String getRule(){
		return rule;
	}
	
	public void setRule(String value){
		rule = value;
	}
	
	public Transaction getTransaction(){
		return transaction;
	}
	
	public void setTransaction(Transaction value){
		transaction = value;
	}
}
