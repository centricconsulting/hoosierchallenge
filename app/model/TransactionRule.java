package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name="transaction_rules")
public class TransactionRule extends Model {
	@Id
	@Column(name="id")
	long id;
	
	@Column(name="created_at")
	private Date created;
	
	@Column(name="rule_contents")
	private String rule;
	
	@Column(name="transaction_id")
	private long transactionId;
	
	@Column(name="")
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
