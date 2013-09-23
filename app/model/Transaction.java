package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;
//import play.api.*;
import play.db.*;



public class Transaction {
	private long id;
	private Date created;
	
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
	
	public ArrayList<TransactionDocument> findLatestTransactions() throws SQLException {
		DataSource ds = DB.getDataSource();
		Connection conn = ds.getConnection();
		Statement stmt = null;
		ArrayList<TransactionDocument> transactionDocs = new ArrayList<TransactionDocument>();
		String sql = "select transaction_id, document, t.created_at, number_sections, title" +
					" from transaction_documents d" +
					" join transactions t on t.id=d.transaction_id " +
					" order by transaction_id desc limit 20";
		
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				TransactionDocument transactionDoc = new TransactionDocument();
				transactionDoc.setId(rs.getLong("transaction_id"));
				transactionDoc.setDocument(rs.getString("document"));
				transactionDoc.setCreated(rs.getDate("created_at"));
				transactionDoc.setNumberOfSections(rs.getInt("number_sections"));
				transactionDoc.setTitle(rs.getString("title"));
				transactionDocs.add(transactionDoc);
			}
			
		}catch(SQLException e){
			throw e;
		}finally{
			if(stmt != null)
				stmt.close();
		}
		
		return transactionDocs;
	}
}
