package service


import collection.JavaConversions._

import org.drools.command.CommandFactory
import wrapper.CCDHelper
import java.io.ByteArrayOutputStream

case class MergedCCD(helper:CCDHelper)

object DroolsCCDMerge {
  def mergeDocs(docs:Seq[CCDHelper]) : Option[CCDHelper] = {
    val rules = DroolsMergeRulesFactory.buildKnowledgeSession
    try {
      // First we need to make a new document, based on the first document we're trying to merge
      // We'll cheat a bit by serializing then deserializing to a new document, since ClinicalDocument doesn't provide clone()
      val cloned = cloneCCD(docs(0))

      rules.insert(new MergedCCD(cloned))
      docs.drop(1).foreach(d => rules.insert(d))
      rules.execute(CommandFactory.newFireAllRules())

      val output = rules.getObjects()
      val merged = output.find(o => o.isInstanceOf[MergedCCD])

      if (merged.isEmpty) None else Some(merged.get.asInstanceOf[MergedCCD].helper)
    }
    finally {
      rules.dispose()
    }
  }

  private def cloneCCD(doc:CCDHelper) : CCDHelper = {
    val out = new ByteArrayOutputStream()
    try {
      doc.writeToStream(out)
      new CCDHelper(out.toString)
    }
    finally {
      out.close()
    }
  }
}
