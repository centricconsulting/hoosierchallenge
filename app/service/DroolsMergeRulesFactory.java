package service;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class DroolsMergeRulesFactory {
  protected static final KnowledgeBase kb;

  static {
    kb = KnowledgeBaseFactory.newKnowledgeBase();
    KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();

    // First we need to add our DSL specification
    builder.add(ResourceFactory.newClassPathResource("merge-rules.dsl", DroolsMergeRulesFactory.class), ResourceType.DSL);
    // Now the DSLR
    builder.add(ResourceFactory.newClassPathResource("merge-rules.dslr", DroolsMergeRulesFactory.class), ResourceType.DSLR);

    if(builder.hasErrors()) {
      throw new RuntimeException("Couldn't build rules" + builder.getErrors());
    }
    kb.addKnowledgePackages(builder.getKnowledgePackages());
  }

  public static StatefulKnowledgeSession buildKnowledgeSession() {
    return kb.newStatefulKnowledgeSession();
  }
}
