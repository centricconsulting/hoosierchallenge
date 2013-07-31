package test.service

import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import service.DroolsCCDMerge

import java.io.FileInputStream
import wrapper.CCDHelper

class DroolsCCDMergeSpec extends Specification {
  "DroolsCCDMerge" should {
    "Just try to merge some sections - bogus" in new testDocs {
      // Preconditions
      doc1.findAllSections().size must equalTo(12)
      doc2.findAllSections().size must equalTo(10)

      val merged = DroolsCCDMerge.mergeDocs(Seq(doc1, doc2))
      merged.isEmpty must equalTo(false)
      merged.get.document().getAllSections.size() must equalTo(13)
    }

    "Merge two docs with no overlapping problems" in new testDocs {
      // Preconditions
      doc1.findSectionByTitle("Problems").get.getEntries.size() must equalTo(3)
      doc2.findSectionByTitle("Problems").get.getEntries.size() must equalTo(1)

      val merged = DroolsCCDMerge.mergeDocs(Seq(doc1, doc2))
      merged.isEmpty must equalTo(false)
      merged.get.findSectionByTitle("Problems").get.getEntries.size() must equalTo(4)
    }
  }
}

trait testDocs extends Scope {
  val doc1 = new CCDHelper(new FileInputStream("data/HHIC_CCD_1.xml"))
  val doc2 = new CCDHelper(new FileInputStream("data/HHIC_CCD_2.xml"))
}
