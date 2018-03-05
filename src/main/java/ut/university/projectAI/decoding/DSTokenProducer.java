package ut.university.projectAI.decoding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ut.university.projectAI.caching.CacheDispenser;
import ut.university.projectAI.caching.DSGeneralCache;
import ut.university.projectAI.model.SentenceUnit;
import ut.university.projectAI.model.Tokens;

public class DSTokenProducer implements DSGeneralAnalyzer<Tokens>{
	
	private final SentenceUnit<Set<String>> unit;
	private DSGeneralCache<String, Set<String>> synonymsCache = CacheDispenser.getSynonymsCache();
	private static final Logger logger = Logger.getLogger(DSTokenProducer.class.getName());
	private SentenceUnit<Tokens> result;
	private String CUR_THREAD;
	
	public DSTokenProducer(SentenceUnit<Set<String>> unit) {
		this.unit = unit;
	}

	@Override
	public void analyze() {
		CUR_THREAD = "From thread: " + Thread.currentThread().getName();
		logger.log(Level.INFO, "************************************");
		logger.log(Level.INFO, "Start DSTokenProducer. " + CUR_THREAD);
		logger.log(Level.INFO, "************************************");
		produceTokens();
	}

	@Override
	public boolean hasMore() {
		return result != null; 
	}

	@Override
	public SentenceUnit<Tokens> getNext() {
		return result;
	}
	
	private void produceTokens(){
		Set<Tokens> tokens = new HashSet<>();
		for(Set<String> synonyms : unit.getWords()) {
			logger.log(Level.INFO, "************************************");
			logger.log(Level.INFO, "Particular word with synonyms: " + synonyms + ". " + CUR_THREAD);
			Tokens winnerToken = null;
			double percOfMatch = 0;
			for(Tokens token : Tokens.values()){
				logger.log(Level.INFO, "Checking matching percent with token: " + token + ". " + CUR_THREAD);
				int counter = 0;
				double totalElements = 0;
				for(String keyword : token.keywords()){
					logger.log(Level.INFO, "Checking tokens keyword: " + keyword + ". " + CUR_THREAD);
					Set<String> cached = synonymsCache.getCached(keyword);
					cached.add(keyword);
					logger.log(Level.INFO, "Keyword has following synonyms including keyword itself: " + cached + ". " + CUR_THREAD);
					totalElements += cached.size();
					for(String keywordSynonym : cached){
						for(String synonym : synonyms){
							logger.log(Level.INFO, "Trying to match word synonym: " + synonym + " with token keyword synonym: "+ keywordSynonym + ". " + CUR_THREAD);
							if(keywordSynonym.equals(synonym)){
								counter += 1;
							}
						}
					}
				}
				double res;
				if((res = counter/totalElements) >= percOfMatch && res > 0) {
					winnerToken = token;
					percOfMatch = res; 
				}
			}
			logger.log(Level.INFO, "Word with synonyms: " + synonyms + " got following token: " + winnerToken + ". " + CUR_THREAD);
			if(winnerToken != null) tokens.add(winnerToken);
				
		}
		logger.log(Level.INFO, "Matched words with synonyms: " + unit.getWords() + " to tokens: " + tokens + ". " + CUR_THREAD);
		result = new SentenceUnit<>(unit, new ArrayList<>(tokens));
	}
}
