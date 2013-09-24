package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.Ebean;
//import javax.sql.DataSource;
//import play.api.*;
//import play.db.*;
import play.db.ebean.*;
import util.CCDHelper;
import util.EncodingUtil;


@Entity
@Table(name="transactions")
public class Transaction extends Model {
	@Id
	@Column(name="id")
	long id;
	
	@Column(name="created_at")
	Date created;
	
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
	
	public List<TransactionDocument> findLatestTransactions() {
		List<TransactionDocument> allDocs = TransactionDocument.find.all();
		List<TransactionDocument> latestDocs = new ArrayList<TransactionDocument>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -168);
		Date threshold = cal.getTime();
		
		for(TransactionDocument doc:allDocs){
			if(doc.getCreated().after(threshold)){
				latestDocs.add(doc);
			}
		}
		return latestDocs;
	}
	
	public void saveTransactions(List<CCDHelper> docs) throws IOException{
		for(CCDHelper doc: docs){
			Transaction trans = new Transaction();
			trans.setCreated(new Date());
			Ebean.save(trans);
			
			String docBytes = EncodingUtil.compressText(docs.toString());
			
			TransactionDocument transDoc = new TransactionDocument();
			transDoc.setDocument(docBytes);
			transDoc.setCreated(new Date());
			transDoc.setTransactionId(trans.getId());
			transDoc.setTitle(doc.getTitle());
			transDoc.setNumberOfSections(doc.findAllSections().size());
		}
	}
}
