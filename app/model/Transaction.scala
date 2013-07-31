package model

import anorm._
import play.api.Play._
import play.api.db._

import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.zip.GZIPOutputStream

import wrapper.CCDHelper

case class Transaction(id:Long, created:Date)
case class TransactionDocument(id:Long, created:Date, document:Array[Byte], transaction:Transaction=null)
case class TransactionRule(id:Long, created:Date, rule:String, transaction:Transaction=null)

case class LatestTransaction(id:Long, created:Date, numDocs:Long)

object Transaction {
  def findLatestTransactions() : Seq[LatestTransaction] = {
    DB.withConnection{ implicit connection =>
      SQL(
        """select t.id, t.created_at, count(d.id) as cnt from transactions t
        left outer join transaction_documents d on t.id=d.transaction_id
        group by t.id order by id desc limit 20"""
      )
      .apply().map( row =>
        LatestTransaction(row[Long]("id"), row[Date]("created_at"), row[Long]("cnt"))
      ).toList
    }
  }

  def saveTransaction(docs:Seq[CCDHelper]) = {
    // TODO - wrap in a transaction
    DB.withConnection{ implicit connection =>
      val txId : Option[Long] = SQL("insert into transactions (created_at) values({now})").on("now" -> new Date()).executeInsert()

      if(txId.isDefined) {
        // Save all documents associated with this merge
        val bytes : Seq[Array[Byte]] = docs.map{ d =>
          // TODO - close these streams
          val str = new ByteArrayOutputStream()
          val gzip = new GZIPOutputStream(str)
          gzip.write(d.toString().getBytes("UTF-8"))
          str.toByteArray
        }
        bytes.foreach(s =>
          SQL("insert into transaction_documents (transaction_id, document) values({txid}, {bytes})")
            .on("txid" -> txId.get)
            .on("bytes" -> s)
            .executeInsert()
        )
      }
    }
    // Save all rules that were fired - how to do that?
    // TODO
  }
}
