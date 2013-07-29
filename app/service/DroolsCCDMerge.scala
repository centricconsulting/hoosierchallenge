package service

import helper.CCDHelper

import collection.JavaConversions._

import org.drools.command.CommandFactory

case class MergedCCD(helper:CCDHelper)

object DroolsCCDMerge {
  def mergeDocs(docs:Seq[CCDHelper]) : Option[CCDHelper] = {
    val rules = DroolsMergeRulesFactory.buildKnowledgeSession
    try {
      // First we need to make a new document, based on the first document we're trying to merge
      val master = docs(0)
      val toMerge = docs.drop(1)

      rules.insert(new MergedCCD(master))
      toMerge.foreach(d => rules.insert(d))
      rules.execute(CommandFactory.newFireAllRules())

      val output = rules.getObjects()
      val merged = output.find(o => o.isInstanceOf[MergedCCD])

      if (merged.isEmpty) None else Some(merged.get.asInstanceOf[MergedCCD].helper)
    }
    finally {
      rules.dispose()
    }
  }
}
