package lab7.CNFConverter;

import java.util.Iterator;
import java.util.Set;

import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.CNFTransformer;

public class UseCNFConverter {
	public static void main(String[] args) {
		//Create Parser: to verify syntax
		PEParser parser = new PEParser();
		//Create Transformer: to convert to CNF sentences
		CNFTransformer transformer = new CNFTransformer();
		//Create Gatherer: to break into CNF clause
		CNFClauseGatherer gatherer = new CNFClauseGatherer();

		//PARSE
		Sentence pl_sentence = (Sentence) parser.parse("((A AND B) => C)");
		//TRANSFORM
		Sentence cnf_sentence = transformer.transform(pl_sentence);

		//GATHER
		Set<Sentence> clauses = gatherer.getClausesFrom(cnf_sentence);

		//Print the whole sentence
		System.out.println("CNF Sentence:");
		System.out.println(cnf_sentence.toString());

		//Print each clause
		System.out.println("CNF Clauses:");
		Iterator<Sentence> iter = clauses.iterator();
		while (iter.hasNext()) {
			Sentence sentence = iter.next();
			System.out.println(sentence.toString());
		}
	}
}
