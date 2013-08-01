package test

import _root_.util.CCDHelper
import org.specs2.mutable._
import org.specs2.specification.Scope

import java.io.{FileInputStream}


class CCDHelperSpec extends Specification {
  "CCDHelper" should {
    "Read title" in new docs {
      doc.title() must startWith("Editha Tester")
    }
    "Read patient name" in new docs {
      doc.patientName() must equalTo("Editha Tester")
    }
    "Count number of sections" in new docs {
      doc.findAllSections().size must equalTo(12)
    }
  }
}

trait docs extends Scope {
  val doc = new CCDHelper(new FileInputStream("data/HHIC_CCD_1.xml"))
}
