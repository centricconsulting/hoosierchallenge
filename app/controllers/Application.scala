package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import service.DroolsCCDMerge
import helper.CCDHelper
import java.io.ByteArrayOutputStream

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def mergeDocs = Action { request =>
    val postedFiles = request.body.asMultipartFormData.get.files
    val helpers = postedFiles.map{f => System.out.println(f.ref.toString); new CCDHelper(f.ref.toString)}

    val merged = DroolsCCDMerge.mergeDocs(helpers)
    val outStream = new ByteArrayOutputStream()
    merged.get.writeToStream(outStream)

    Ok(views.html.merged(outStream.toString))
  }
  
}