package model

import anorm._
import play.api.Play._
import play.api.db._
import java.util.Date
import util.{CCDHelper, EncodingUtil}
import java.io.InputStream

case class Transaction(id:Long, created:Date)
case class TransactionDocument(documentId:Long, id:Long, created:Date, document:String, numberOfSections:Int, title:String, isOutput:Boolean) // document => Base64 representation of gzipped doc
case class TransactionRule(id:Long, created:Date, rule:String, transaction:Transaction=null)

object Transaction {
  def getTransactionDocumentAsStream(transactionDocumentId : Long) : Option[String] = {
    DB.withConnection{ implicit connection =>
      val docs = SQL("select document from transaction_documents d where d.id={id}")
      .on("id" -> transactionDocumentId)
      .apply().map( row =>
        EncodingUtil.inflateText(row[String]("document"))
      ).toList
      
      docs(0)
    }
  }
  
  
  def findLatestTransactions() : Seq[TransactionDocument] = {
    DB.withConnection{ implicit connection =>
      SQL(
        "select d.id, transaction_id, document, t.created_at, number_sections, title, is_output from transaction_documents d join transactions t on t.id=d.transaction_id order by transaction_id desc limit 20"
      )
      .apply().map( row =>
        // TODO - what to do about Option?
        TransactionDocument(row[Long]("id"), row[Long]("transaction_id"), row[Date]("created_at"), EncodingUtil.inflateText(row[String]("document")).get, row[Int]("number_sections"), row[String]("title"), row[Boolean]("is_output"))
      ).toList
    }
  }

  def saveTransaction(docs:Seq[CCDHelper], firedRules:List[(String, String)], mergedCCD : Option[CCDHelper]) = {
    // TODO - wrap in a transaction
    DB.withConnection{ implicit connection =>
      val txId : Option[Long] = SQL("insert into transactions (created_at) values({now})").on("now" -> new Date()).executeInsert()

      if(txId.isDefined) {
        // Save all documents associated with this merge
        val bytes = docs.map{ d =>
          (d, EncodingUtil.compressText(d.toString()))
        }
        bytes.foreach{ case(d, s) =>
          if(s.isDefined) {
            SQL("insert into transaction_documents (transaction_id, number_sections, title, document) values({txid}, {sections}, {title}, {bytes})")
              .on("txid" -> txId.get)
              .on("sections" -> d.findAllSections().size)
              .on("title" -> d.title())
              .on("bytes" -> s)
              .executeInsert()
          }
        }
        
        // save the merged CCD
        if (mergedCCD.isDefined) {
          SQL("insert into transaction_documents (transaction_id, number_sections, title, document, is_output) values({txid}, {sections}, {title}, {bytes}, true)")
              .on("txid" -> txId.get)
              .on("sections" -> mergedCCD.get.findAllSections.size)
              .on("title" -> mergedCCD.get.title())
              .on("bytes" -> EncodingUtil.compressText(mergedCCD.get.toString()))
              .executeInsert()
        }
        
        // Save all rules that were fired
        firedRules.foreach{ case(ruleVersion, docTitle) =>
	        SQL("insert into transaction_rules (transaction_id, rule_version, document_title) values({txid}, {rule_version}, {document_title})")
              .on("txid" -> txId.get)
              .on("rule_version" -> ruleVersion)
              .on("document_title" -> docTitle)
              .executeInsert()
        }
      }
    }
  }
}
