package service

import helper.CCDHelper

object DroolsCCDMerge {
  def mergeDocs(docs:Seq[CCDHelper]) : CCDHelper = {
    val rules = DroolsMergeRulesFactory.buildKnowledgeSession
    null
  }
}
