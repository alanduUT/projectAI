package ut.university.projectAI.caching;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ut.university.projectAI.model.Movie;

public class MoviesCache implements DSGeneralCache<String, Movie>{

	private final Map<String, Movie> cache = new ConcurrentHashMap<>();
		
	@Override
	public void insert(String key, Movie value) {
		cache.putIfAbsent(key, new Movie(value));
	}

	@Override
	public Movie getCached(String key) {
		Movie movie;
		if((movie = cache.get(key)) != null){
			return new Movie(movie);//clone
		}
		return movie;
	}

	@Override
	public void preCache() {}

}
