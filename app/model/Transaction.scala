package model

import anorm._
import play.api.Play._
import play.api.db._

import java.util.Date

case class Transaction(id:Int, created:Date)
case class TransactionDocument(id:Int, created:Date, document:Array[Byte], transaction:Transaction=null)
case class TransactionRule(id:Int, created:Date, rule:String, transaction:Transaction=null)

object Transaction {
  def findLatestTransactions() : Seq[Transaction] = {
    DB.withConnection{ implicit connection =>
      SQL("select * from transactions order by id desc limit 20").apply().map( row =>
        Transaction(row[Int]("id"), row[Date]("created_at"))
      ).toSeq
    }
  }
}
