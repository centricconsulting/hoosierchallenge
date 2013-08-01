package util

import collection.JavaConversions._

import java.io.{ByteArrayOutputStream, ByteArrayInputStream, OutputStream, InputStream}

import org.openhealthtools.mdht.uml.cda.{ClinicalDocument, Section}
import org.openhealthtools.mdht.uml.cda.util.CDAUtil

class CCDHelper (d:ClinicalDocument) {
  private val doc = d

  def this(stream:InputStream) {
    this(CDAUtil.load(stream))
  }

  def this(str:String) {
    this(CDAUtil.load(new ByteArrayInputStream(str.getBytes("UTF-8"))))
  }

  def document() : ClinicalDocument = {
    doc
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
  override def toString() : String = {
    val buf = new ByteArrayOutputStream()
    try {
      writeToStream(buf)

      buf.toString
    }
    finally {
      buf.close()
    }
  }
}
