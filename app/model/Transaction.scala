package model

import anorm._
import play.api.Play._
import play.api.db._

import java.util.Date

import wrapper.CCDHelper
import util.EncodingUtil

case class Transaction(id:Long, created:Date)
case class TransactionDocument(id:Long, created:Date, document:String, numberOfSections:Int, title:String) // document => Base64 representation of gzipped doc
case class TransactionRule(id:Long, created:Date, rule:String, transaction:Transaction=null)

object Transaction {
  def findLatestTransactions() : Seq[TransactionDocument] = {
    DB.withConnection{ implicit connection =>
      SQL(
        "select transaction_id, document, t.created_at, number_sections, title from transaction_documents d join transactions t on t.id=d.transaction_id order by transaction_id desc limit 20"
      )
      .apply().map( row =>
        // TODO - what to do about Option?
        TransactionDocument(row[Long]("transaction_id"), row[Date]("created_at"), EncodingUtil.inflateText(row[String]("document")).get, row[Int]("number_sections"), row[String]("title"))
      ).toList
    }
  }

  def saveTransaction(docs:Seq[CCDHelper]) = {
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
      }
    }
    // Save all rules that were fired - how to do that?
    // TODO
  }
}
