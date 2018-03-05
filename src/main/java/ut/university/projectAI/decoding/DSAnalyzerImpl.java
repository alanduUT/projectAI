package ut.university.projectAI.decoding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ut.university.projectAI.model.SentenceUnit;
import ut.university.projectAI.model.Tokens;

public class DSAnalyzerImpl implements DSAnalyzer{
	private final String text;
	
	public DSAnalyzerImpl(String text){
		this.text = text;
	}
	
	public List<SentenceUnit<Tokens>> analyze() throws Exception {
		DSGeneralAnalyzer<String> textProcessor = new DSTextProcessor(text);
		textProcessor.analyze();
		List<Thread> threads = new ArrayList<>();
		
		List<DSGeneralAnalyzer<String>> morpAnalyzers = new ArrayList<>();
		while(textProcessor.hasMore()){
			SentenceUnit<String> sentUnit = textProcessor.getNext();
			DSGeneralAnalyzer<String> morpAnalyzer = new DSMorphologicalAnalyzer(sentUnit);
			morpAnalyzers.add(morpAnalyzer);
			Thread t = new Thread(morpAnalyzer::analyze);
			threads.add(t);
			t.start();
		}
		for(Thread t : threads) {
			t.join();
		}
		threads.clear();
		
		List<DSGeneralAnalyzer<Set<String>>> synFinders = new ArrayList<>();
		for(DSGeneralAnalyzer<String> moAn : morpAnalyzers) {
			if(moAn.hasMore()) {
				SentenceUnit<String> sentUnit = moAn.getNext();
				DSGeneralAnalyzer<Set<String>> synFinder = new DSSynonymFinder(sentUnit);
				synFinders.add(synFinder);
				Thread t = new Thread(synFinder::analyze);
				threads.add(t);
				t.start();
			}
		}
		morpAnalyzers.clear();
		for(Thread t : threads) {
			t.join();
		}
		threads.clear();
		
		List<DSGeneralAnalyzer<Tokens>> tokenProducers = new ArrayList<>();
		for(DSGeneralAnalyzer<Set<String>> syFi : synFinders) {
			if(syFi.hasMore()){
				SentenceUnit<Set<String>> sentUnit = syFi.getNext();
				DSGeneralAnalyzer<Tokens> tokenProducer = new DSTokenProducer(sentUnit);
				tokenProducers.add(tokenProducer);
				Thread t = new Thread(tokenProducer::analyze);
				threads.add(t);
				t.start();
			}
		}
		synFinders.clear();
		for(Thread t : threads) {
			t.join();
		}
		threads.clear();
		List<SentenceUnit<Tokens>> sentUnits = new ArrayList<>();
		for(DSGeneralAnalyzer<Tokens> toPr : tokenProducers){
			if(toPr.hasMore()){
				SentenceUnit<Tokens> sentUnit = toPr.getNext();
				sentUnits.add(sentUnit);
			}
		}
		tokenProducers.clear();
		threads.clear();
		return sentUnits;
	}
}
