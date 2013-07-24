package test

import org.specs2.mutable._
import java.io.FileInputStream

class ParseCCD extends Specification {
  "CCD Parser" should {
    "Read title" in {
      org.openhealthtools.mdht.uml.cda.util.CDAUtil.load(new FileInputStream("data/HHIC_CCD_1.xml")).getTitle.getText must startWith("Editha Tester")
    }
  }
}
