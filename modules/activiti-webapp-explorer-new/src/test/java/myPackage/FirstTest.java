package myPackage;

import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.Test;

public class FirstTest {

	@Test
	public void RuleTest1() {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("rule/first.drl", FirstTest.class), ResourceType.DRL);
		Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
		KnowledgeBase kbase = kbuilder.newKnowledgeBase();
		kbase.addKnowledgePackages(pkgs);
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		Person person = new Person();
		person.setName("limeng32");
		ksession.insert(person);
		ksession.fireAllRules();
		Person person2 = new Person();
		person2.setName("limeng321");
		ksession.insert(person2);
		ksession.fireAllRules();

		Assert.assertEquals(2, ksession.getFactCount());

		ksession.dispose();
	}

}
