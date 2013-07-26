package helper

import collection.JavaConversions._

import java.io.InputStream
import org.openhealthtools.mdht.uml.cda.Section

class CCDHelper(stream:InputStream) {
  private val doc = org.openhealthtools.mdht.uml.cda.util.CDAUtil.load(stream)

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
}
