package controllers

import java.io.ByteArrayOutputStream

import play.api._
import play.api.libs.Files
import play.api.mvc._

import service.DroolsCCDMerge
import model.Transaction
import util.CCDHelper
import securesocial.core.{Identity, Authorization}

object Application extends Controller with securesocial.core.SecureSocial {

  def index = SecuredAction { implicit request =>
    Ok(views.html.index())
  }

  def adhoc = SecuredAction { implicit request =>
    Ok(views.html.adhoc())
  }

  def downloadTransactionDocument(id : Long, title : String) = SecuredAction { implicit request =>
    import org.apache.commons.io.IOUtils
    val documents = Transaction.getTransactionDocumentAsStream(id)
    documents.map { is =>
      Ok(IOUtils.toByteArray(is))
        .withHeaders(
            CONTENT_TYPE -> "application/octet-stream",
            CONTENT_DISPOSITION -> "attachment; filename=document.xml".replaceAll("document", title)
        )
    }.getOrElse(NotFound("File not found!"))
  }

  def auditTrail = SecuredAction { implicit request =>
    val transactions = Transaction.findLatestTransactions()

    val grouped = transactions.groupBy(tx => tx.id)
    val keys = grouped.keys.toSeq.sortBy(-_)
    Ok(views.html.auditTrail(keys.map(grouped.get(_).get).toSeq))
  }

  def mergeDocs = SecuredAction { implicit request =>
    val postedFiles = request.body.asMultipartFormData.get.files
    val helpers = postedFiles.map(f => new CCDHelper(Files.readFile(f.ref.file)))

    val merged = DroolsCCDMerge.mergeDocs(helpers)

    Ok(views.html.merged(helpers, merged.get))
  }
  
}