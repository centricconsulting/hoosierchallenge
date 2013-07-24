package test

import org.specs2.mutable._
import org.specs2.specification.Scope
import java.io.FileInputStream

class ParseCCD extends Specification {
  "CCD Parser" should {
    "Read title" in new docs {
      doc.getTitle.getText must startWith("Editha Tester")
    }
    "Read patient first name" in new docs {
      doc.getRecordTargets().get(0).getPatientRole.getPatient.getNames.get(0).getGivens.get(0).getText must equalTo("Editha")
    }
    "Read patient last name" in new docs {
      doc.getRecordTargets().get(0).getPatientRole.getPatient.getNames.get(0).getFamilies.get(0).getText must equalTo("Tester")
    }
  }
}

trait docs extends Scope {
  val doc = org.openhealthtools.mdht.uml.cda.util.CDAUtil.load(new FileInputStream("data/HHIC_CCD_1.xml"))
}
