package helper

import collection.JavaConversions._

import java.io.{OutputStream, InputStream}

import org.openhealthtools.mdht.uml.cda.{ClinicalDocument, Section}
import org.openhealthtools.mdht.uml.cda.util.CDAUtil

class CCDHelper (d:ClinicalDocument) {
  private val doc = d

  def this(stream:InputStream) {
    this(CDAUtil.load(stream))
  }

  def title() : String = {
    doc.getTitle.getText
  }
  def patientName() : String = {
    val name = doc.getRecordTargets().get(0).getPatientRole.getPatient.getNames.get(0)
    name.getGivens.get(0).getText + " " + name.getFamilies.get(0).getText
  }
  def findAllSections() : Seq[Section] = {
    doc.getAllSections
  }
  def findSectionByTitle(title:String) : Option[Section] = {
    findAllSections().find{s => title == (if(Option(s.getTitle).isEmpty) "" else s.getTitle.getText)}
  }
  def writeToStream(stream:OutputStream) = {
    CDAUtil.save(doc, stream)
  }
}
