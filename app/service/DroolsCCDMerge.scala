package service

import org.drools.KnowledgeBase
import org.drools.KnowledgeBaseFactory
import org.drools.builder.{ResourceType, KnowledgeBuilderFactory}
import org.drools.io.ResourceFactory

import helper.CCDHelper

class DroolsCCDMerge private (val kb:KnowledgeBase) {
  def mergeDocs(docs:Seq[CCDHelper]) : CCDHelper = {
    null
  }
}

object DroolsCCDMerge {
  val kb = KnowledgeBaseFactory.newKnowledgeBase
  val builder = KnowledgeBuilderFactory.newKnowledgeBuilder

  // First we need to add our DSL specification
  builder.add(ResourceFactory.newClassPathResource("merge-rules.dsl", getClass), ResourceType.DSL)
  // Now the DSLR
  builder.add(ResourceFactory.newClassPathResource("merge-rules.dslr", getClass), ResourceType.DSLR)

  if(builder.hasErrors) {
    throw new RuntimeException("Couldn't build rules" + builder.getErrors)
  }
  kb.addKnowledgePackages(builder.getKnowledgePackages)

  val merge = new DroolsCCDMerge(kb)

  def newCCDMerge() = {
    merge
  }
}
