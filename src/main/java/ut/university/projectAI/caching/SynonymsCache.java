package ut.university.projectAI.caching;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.transform.stream.StreamSource;

import ut.university.projectAI.decoding.DSSynonymFinder;
import ut.university.projectAI.model.SentenceUnit;
import ut.university.projectAI.model.Tokens;

public class SynonymsCache implements DSGeneralCache<String, Set<String>>{
	
	private final Map<Integer, Set<String>> cache = new ConcurrentHashMap<>(); 
	private final Map<String, Integer> indexCache = new ConcurrentHashMap<>();
	private static final Logger logger = Logger.getLogger(SynonymsCache.class.getName());
	
	@Override
	public void insert(String key, Set<String> value) {
		Integer index = null;
		
		if(!cache.containsValue(value)){
			synchronized(cache){
				if(!cache.containsValue(value)){
					do {
						index = ThreadLocalRandom.current().nextInt();
					} while(cache.containsValue(index));
					cache.put(index, value);
				}
			}
		}
		if(index == null){
			for(Entry<Integer, Set<String>> entry : cache.entrySet()) {
				if(value.equals(entry.getValue())){
					index = entry.getKey();
					break;
				}
			}
		}
		indexCache.putIfAbsent(key, index);
	}

	@Override
	public Set<String> getCached(String key) {
		Integer index;
		if((index = indexCache.get(key)) != null) {
			return cache.get(index);
		}
		return null;
	}
	
	//pre-cache token synonyms
	@Override
	public void preCache() {
		logger.log(Level.INFO, "************************************");
		logger.log(Level.INFO, "Pre-caching Tokens synonyms");
		List<String> list = Stream.of(Tokens.values()).flatMap(t -> t.keywords().stream()).collect(Collectors.toList());
		new DSSynonymFinder(new SentenceUnit<String>(Arrays.asList(), list)).analyze();
		logger.log(Level.INFO, "Pre-cached synonyms: " + cache.values());
		logger.log(Level.INFO, "************************************");
	}

}
