package controllers

import java.io.ByteArrayOutputStream

import play.api._
import play.api.libs.Files
import play.api.mvc._

import service.DroolsCCDMerge
import model.Transaction
import wrapper.CCDHelper

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def adhoc = Action {
    Ok(views.html.adhoc())
  }

  def auditTrail = Action {
    val transactions = Transaction.findLatestTransactions()

    Ok(views.html.auditTrail(transactions))
  }

  def mergeDocs = Action { request =>
    val postedFiles = request.body.asMultipartFormData.get.files
    val helpers = postedFiles.map(f => new CCDHelper(Files.readFile(f.ref.file)))

    val merged = DroolsCCDMerge.mergeDocs(helpers)

    Ok(views.html.merged(helpers, merged.get))
  }
  
}