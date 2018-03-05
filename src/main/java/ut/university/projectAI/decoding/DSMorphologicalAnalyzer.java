package ut.university.projectAI.decoding;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ut.university.projectAI.caching.CacheDispenser;
import ut.university.projectAI.caching.DSGeneralCache;
import ut.university.projectAI.model.SentenceUnit;
import ut.university.projectAI.networking.RequestPerformer;
import ut.university.projectAI.networking.RequestPerformerImpl;

/**
 * Takes _text as input and produces an output of possible _[key tokens] (in this application token~word,
 * because morphological analyzer is combined with lexical analyzer)
 */

public class DSMorphologicalAnalyzer implements DSGeneralAnalyzer<String>{
	
	private final SentenceUnit<String> unit;
	private SentenceUnit<String> result;
	private DSGeneralCache<String, String> lemmasCache = CacheDispenser.getLemmasCache();
	private static final String MORPH_URI = "http://morphological.org/?words=";
	private static final String P1 = "<strong>Lemma</strong>:";
	private static final String P2 = "<br /><strong>Part";
	private static final String REGEX = P1 + "[\\w\\s]+" + P2;
	private static final Logger logger = Logger.getLogger(DSMorphologicalAnalyzer.class.getName());
	private String CUR_THREAD;
	
	public DSMorphologicalAnalyzer(SentenceUnit<String> unit) {
		this.unit = unit;
	}
	
	@Override
	public void analyze() {
		CUR_THREAD = "From thread: " + Thread.currentThread().getName();
		logger.log(Level.INFO, "************************************");
		logger.log(Level.INFO, "Start DSMorphologicalAnalyzer. " + CUR_THREAD);
		logger.log(Level.INFO, "************************************");
		morphologicalAnalysis();
	}
	
	@Override
	public boolean hasMore() {
		return result != null;
	}
	
	@Override
	public SentenceUnit<String> getNext() {
		return result;
	}
	
	private void morphologicalAnalysis() {
		List<String> tmp = new ArrayList<>();
		for(String word : unit.getWords()) {
			logger.log(Level.INFO, "************************************");
			logger.log(Level.INFO, "Searching lemma for word: " + word + ". " + CUR_THREAD);
			String lemma;
			if((lemma = lemmasCache.getCached(word)) != null) {
				tmp.add(lemma);
			} else {
				if((lemma = getWordsLemma(word)) == null) {
					lemma = word;
				}
				tmp.add(lemma);
				lemmasCache.insert(word, lemma);
			}
			logger.log(Level.INFO, "Found lemma: " + lemma + ". " + CUR_THREAD);
			logger.log(Level.INFO, "************************************");
		}
		logger.log(Level.INFO, "************************************");
		result = new SentenceUnit<String>(unit, tmp);
		logger.log(Level.INFO, "************************************");
	}
	
	private String getWordsLemma(String word) {
		logger.log(Level.INFO, "Making request for " + word + ". " + CUR_THREAD);
		RequestPerformer rp = RequestPerformerImpl.instance();
		return processResult(rp.performRequest(MORPH_URI + word));
	}
	
	private String processResult(String result){
		String res = null;
		Pattern pattern = Pattern.compile(REGEX);
		Matcher m = pattern.matcher(result);
		if(m.find()) {
			res = m.group(0);
			res = res.substring(P1.length(), res.length() - P2.length()).trim().toLowerCase();
		}
		return res;
	}
}