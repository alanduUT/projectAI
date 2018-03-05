package ut.university.projectAI.caching;

import java.util.Set;

import ut.university.projectAI.model.Movie;

public class CacheDispenser {
	
	private static final DSGeneralCache<String, String> lemmasCache = new LemmasCache();
	private static final DSGeneralCache<String, Set<String>> synonymsCache = new SynonymsCache();
	private static final DSGeneralCache<String, Movie> moviesCache = new MoviesCache();
	
	static {
		synonymsCache.preCache();
	}
	
	public static DSGeneralCache<String, String> getLemmasCache() {
		return lemmasCache;
	}
	
	public static DSGeneralCache<String, Set<String>> getSynonymsCache() {
		return synonymsCache;
	}
	
	public static DSGeneralCache<String, Movie> getMoviesCache() {
		return moviesCache;
	}
}
