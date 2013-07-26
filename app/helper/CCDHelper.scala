package helper

import collection.JavaConversions._

import java.io.{OutputStream, InputStream}

import org.openhealthtools.mdht.uml.cda.Section
import org.openhealthtools.mdht.uml.cda.util.CDAUtil

class CCDHelper(stream:InputStream) {
  private val doc = CDAUtil.load(stream)

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
  def writeToStream(stream:OutputStream) = {
    CDAUtil.save(doc, stream)
  }
}
