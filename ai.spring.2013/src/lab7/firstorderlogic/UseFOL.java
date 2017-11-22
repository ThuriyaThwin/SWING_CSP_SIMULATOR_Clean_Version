package lab7.firstorderlogic;

import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.kb.FOLKnowledgeBase;

public class UseFOL {

	public static void main(String[] args) {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("An");
		domain.addConstant("Quang");

		domain.addPredicate("pmh");
		domain.addPredicate("rich");
		domain.addPredicate("muchmoney");
		domain.addPredicate("govstaff");

		FOLKnowledgeBase myKB = new FOLKnowledgeBase(domain);
		myKB.tell("(pmh(x) => rich(x))");
		myKB.tell("(rich(x) => muchmoney(x))");
		myKB.tell("(govstaff(x) => (NOT muchmoney(x)) )");
		myKB.tell("(pmh(Quang))");
		myKB.tell("(govstaff(An))");

		InferenceResult ir = myKB.ask("(pmh(An))");
		System.out.println("IS TRUE = " + ir.isTrue());
	}

}
