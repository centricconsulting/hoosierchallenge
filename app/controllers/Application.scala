package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import service.DroolsCCDMerge

object Application extends Controller {

  val mergeForm = Form(
    tuple(
      "ccd1" -> Byte[],
      "ccd2" -> Byte[]
    )
  )

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def mergeDocs = Action {
    mergeForm.bindFromRequest().get

    val merged = DroolsCCDMerge.mergeDocs()
    Ok(views.html.merged())
  }
  
}