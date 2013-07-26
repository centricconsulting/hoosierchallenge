package service

import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import helper.CCDHelper

import java.io.FileInputStream

class DroolsCCDMergeSpec extends Specification {
  "DroolsCCDMerge" should {
    "Merge two docs with no overlapping problems" in new testDocs {
      // Preconditions
      doc1.findSectionByTitle("Problems").get.getEntries.size() must equalTo(3)
      doc2.findSectionByTitle("Problems").get.getEntries.size() must equalTo(1)

      val merged = DroolsCCDMerge.newCCDMerge().mergeDocs(Seq(doc1, doc2))
      merged.findSectionByTitle("Problems").get.getEntries.size() must equalTo(4)
    }
  }
}

trait testDocs extends Scope {
  val doc1 = new CCDHelper(new FileInputStream("data/HHIC_CCD_1.xml"))
  val doc2 = new CCDHelper(new FileInputStream("data/HHIC_CCD_3.xml"))
}
