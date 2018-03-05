package ut.university.projectAI.caching;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LemmasCache implements DSGeneralCache<String, String> {
	private final Map<String, String> cache = new ConcurrentHashMap<>();
	
	@Override
	public void insert(String key, String value) {
		cache.putIfAbsent(key, value);
	}
	
	/**
	 * result might be null(if not cached)
	 */
	@Override
	public String getCached(String key) {
		return cache.get(key);
	}

	@Override
	public void preCache() {}
}
