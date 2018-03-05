package ut.university.projectAI.decoding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ut.university.projectAI.caching.CacheDispenser;
import ut.university.projectAI.caching.DSGeneralCache;
import ut.university.projectAI.model.SentenceUnit;
import ut.university.projectAI.networking.RequestPerformer;
import ut.university.projectAI.networking.RequestPerformerImpl;

import org.json.JSONArray;
import org.json.JSONObject;

public class DSSynonymFinder implements DSGeneralAnalyzer<Set<String>> {
	
	private final SentenceUnit<String> unit;
	private SentenceUnit<Set<String>> result;
	private DSGeneralCache<String, Set<String>> synonymsCache = CacheDispenser.getSynonymsCache();
	private static final Logger logger = Logger.getLogger(DSSynonymFinder.class.getName());
	private static final String THESAURUS_API_KEY = "5c4684a65e8ab0b05ff5feebab78a22f";
	private static final String THESAURUS_URI = "http://words.bighugelabs.com/api/2/";
	private String CUR_THREAD;
	
	
	public DSSynonymFinder(SentenceUnit<String> unit) {
		this.unit = unit;
	}
	
	@Override
	public void analyze() {
		CUR_THREAD = "From thread: " + Thread.currentThread().getName();
		logger.log(Level.INFO, "************************************");
		logger.log(Level.INFO, "Start DSSynonymFinder. " + CUR_THREAD);
		logger.log(Level.INFO, "************************************");
		findSynonyms();
	}

	@Override
	public boolean hasMore() {
		return result != null;
	}

	@Override
	public SentenceUnit<Set<String>> getNext() {
		return result;
	}
	
	private void findSynonyms() {
		List<Set<String>> tmp = new ArrayList<>();
		for(String word : unit.getWords()) {
			logger.log(Level.INFO, "************************************");
			logger.log(Level.INFO, "Searching synonyms for word: " + word + ". " + CUR_THREAD);
			Set<String> synonyms;
			if((synonyms = synonymsCache.getCached(word)) != null) {
				tmp.add(synonyms);
			}else{
				if((synonyms = getWordsSynonyms(word)) == null) {
					synonyms = new HashSet<>(Arrays.asList(word));
				}
				tmp.add(synonyms);
				synonymsCache.insert(word, synonyms);
			}
			logger.log(Level.INFO, "Found synonyms: " + synonyms + ". " + CUR_THREAD);
			logger.log(Level.INFO, "************************************");
		}
		result = new SentenceUnit<Set<String>>(unit, tmp);
	}
	
	private Set<String> getWordsSynonyms(String word){
		logger.log(Level.INFO, "Making request for " + word + ". " + CUR_THREAD);
		RequestPerformer rp = RequestPerformerImpl.instance();
		return processResult(rp.performRequest(THESAURUS_URI + THESAURUS_API_KEY + "/" + word + "/json"));
	}
	
	private Set<String> processResult(String result){
		if(!result.isEmpty()){
			try{
				JSONObject json = new JSONObject(result);
				json = json.getJSONObject("noun");
				JSONArray jsonAr = json.getJSONArray("syn");
				Set<String> tmp = new HashSet<>();
				for(Object obj : jsonAr.toList()){
					String str = obj.toString();
					if(!str.contains(" ")){
						tmp.add(str.toLowerCase());
					}
				}
				return tmp;
			}catch(Exception skip) {
				//we are interested only in nouns
			}
		}
		return null;
		
	}

}
