package controllers

import java.io.ByteArrayOutputStream

import play.api._
import play.api.libs.Files
import play.api.mvc._

import service.DroolsCCDMerge
import model.Transaction
import util.CCDHelper

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def adhoc = Action {
    Ok(views.html.adhoc())
  }

  def auditTrail = Action {
    val transactions = Transaction.findLatestTransactions()

    val grouped = transactions.groupBy(tx => tx.id)
    val keys = grouped.keys.toSeq.sortBy(-_)
    Ok(views.html.auditTrail(keys.map(grouped.get(_).get).toSeq))
  }

  def mergeDocs = Action { request =>
    val postedFiles = request.body.asMultipartFormData.get.files
    val helpers = postedFiles.map(f => new CCDHelper(Files.readFile(f.ref.file)))

    val merged = DroolsCCDMerge.mergeDocs(helpers)

    Ok(views.html.merged(helpers, merged.get))
  }
  
}