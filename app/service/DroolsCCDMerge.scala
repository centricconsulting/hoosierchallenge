package service

import collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.io.ByteArrayOutputStream
import org.drools.command.CommandFactory
import org.drools.event.rule._
import model.Transaction
import util.CCDHelper
import org.drools.common.DefaultFactHandle
import org.drools.runtime.rule.FactHandle

case class MergedCCD(helper:CCDHelper)

class RuleFireListener extends AgendaEventListener {
  var fired = List[(String, String)]()

  def afterActivationFired(evt: AfterActivationFiredEvent)  {
    val factHandles = evt.getActivation.getFactHandles
    println("Fact count: " + evt.getKnowledgeRuntime().getFactCount());
    val doc = factHandles.find(f => f.isInstanceOf[DefaultFactHandle])
    
    doc.foreach((f : FactHandle) => 
    	fired = (evt.getActivation.getRule.getMetaData.get("RuleVersion").toString(), f.asInstanceOf[DefaultFactHandle].getObject().asInstanceOf[CCDHelper].title()) :: fired
    )
  }

  def activationCreated(p1: ActivationCreatedEvent) {}
  def activationCancelled(p1: ActivationCancelledEvent) {}
  def beforeActivationFired(p1: BeforeActivationFiredEvent) {}
  def agendaGroupPopped(p1: AgendaGroupPoppedEvent) {}
  def agendaGroupPushed(p1: AgendaGroupPushedEvent) {}
  def beforeRuleFlowGroupActivated(p1: RuleFlowGroupActivatedEvent) {}
  def afterRuleFlowGroupActivated(p1: RuleFlowGroupActivatedEvent) {}
  def beforeRuleFlowGroupDeactivated(p1: RuleFlowGroupDeactivatedEvent) {}
  def afterRuleFlowGroupDeactivated(p1: RuleFlowGroupDeactivatedEvent) {}
}

object DroolsCCDMerge {
  def mergeDocs(docs:Seq[CCDHelper]) : Option[CCDHelper] = {
    val rules = DroolsMergeRulesFactory.buildKnowledgeSession
    try {
      // First we need to make a new document, based on the first document we're trying to merge
      // We'll cheat a bit by serializing then deserializing to a new document, since ClinicalDocument doesn't provide clone()
      val cloned = cloneCCD(docs(0))

      rules.insert(new MergedCCD(cloned))
      docs.drop(1).foreach(d => rules.insert(d))

      // Add listener to capture which rules fired
      val listener = new RuleFireListener()
      rules.addEventListener(listener)
      //rules.addEventListener(new DebugAgendaEventListener)

      rules.execute(CommandFactory.newFireAllRules())

      val output = rules.getObjects()
      val merged = output.find(o => o.isInstanceOf[MergedCCD])

      val result = if (merged.isEmpty) None else Some(merged.get.asInstanceOf[MergedCCD].helper)

      // Let's save the results of this merge, but do it asynchronously
      val f = Future{ Transaction.saveTransaction(docs, listener.fired, result) }
      
      f onSuccess {
        case t => sendEmail("Documents were merged successfully!")
      }
      
      f onFailure {
        case t => sendEmail(t.getMessage)
      }

      result
    }
    finally {
      rules.dispose()
    }
  }
  
  private def sendEmail(message : String) = {
    val f = Future { 
       AmazonEmailService.sendEmail(message)
    }
    
    f onSuccess {
      case t => println("Email sent!")
    }
    
    f onFailure {
      case t => 
        println("Failed to send email!")
        println(t.getMessage())
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
