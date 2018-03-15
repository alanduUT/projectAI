package ut.university.projectAI.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import ut.university.projectAI.caching.CacheDispenser;
import ut.university.projectAI.caching.DSGeneralCache;
import ut.university.projectAI.decoding.DSMorphologicalAnalyzer;
import ut.university.projectAI.model.Movie;
import ut.university.projectAI.model.SentenceUnit;
import ut.university.projectAI.model.Tokens;
import ut.university.projectAI.networking.RequestPerformer;
import ut.university.projectAI.networking.RequestPerformerImpl;

public class MovieInfoProducer {
	
	private final SentenceUnit<Tokens> unit;
	private final DSGeneralCache<String, Movie> moviesCache = CacheDispenser.getMoviesCache();
	private static final String OMDBAPI_URI = "http://www.omdbapi.com/?t=";
	private static final String OMDBAPI_KEY = "&apikey=b0a61ee";
	private List<Movie> notFoundMovies = new ArrayList<>();
	private List<Movie> filledMovies = new ArrayList<>();
	private static final Logger logger = Logger.getLogger(MovieInfoProducer.class.getName());
	private String CUR_THREAD;
	
	public MovieInfoProducer(SentenceUnit<Tokens> unit) {
		this.unit = unit;
	}
	
	public void fillMovies() {
		CUR_THREAD = "From thread: " + Thread.currentThread().getName();
		logger.log(Level.INFO, "************************************");
		logger.log(Level.INFO, "Start MovieInfoProducer. " + CUR_THREAD);
		logger.log(Level.INFO, "************************************");
		for(Movie mov : unit.getMovies()){
			logger.log(Level.INFO, "************************************");
			logger.log(Level.INFO, "Searching information for movie " + mov.getCacheName() + ". " + CUR_THREAD);
			String movieCacheName = mov.getCacheName();
			if((mov = moviesCache.getCached(movieCacheName)) != null){
				filledMovies.add(mov);
				logger.log(Level.INFO, "Got from cache movie" + mov + ". " + CUR_THREAD);
				removeUnnecessaryAttributes(mov, unit.getWords());
			} else {
				mov = fillAndCacheMovie(new Movie(movieCacheName));
				if(mov != null){
					logger.log(Level.INFO, "After request got movie and cached: " + mov + ". " + CUR_THREAD);
					filledMovies.add(mov);
					removeUnnecessaryAttributes(mov, unit.getWords());
				}
			}
		}
	}
	
	public List<Movie> getFilledMovies(){
		logger.log(Level.INFO, "Filled movies: " + filledMovies + ". " + CUR_THREAD);
		return filledMovies;
	}
	
	public List<Movie> getNotFoundMovies() {
		return notFoundMovies;
	}
	
	private void removeUnnecessaryAttributes(Movie mov, List<Tokens> requiredTokens) {
		for(Tokens token : Tokens.values()){
			if(!requiredTokens.contains(token)){
				switch(token){
				case RELEASE_DATE:
					mov.setReleaseDate(null);
					break;
				case PLOT:
					mov.setPlot(null);
					break;
				case METASCORE:
					mov.setMetascore(null);
					break;
				case LANGUAGE:
					mov.setLanguage(null);
					break;
				case IMDB:
					mov.setImdb(null);
					break;
				case DIRECTOR:
					mov.setDirector(null);
					break;
				case ACTORS:
					mov.setActors(null);
					break;
				}
			}
		}
	}
	
	private Movie fillAndCacheMovie(Movie mov){
		RequestPerformer rp = RequestPerformerImpl.instance();
		return processResponse(rp.performRequest(OMDBAPI_URI + String.join("+", mov.getCacheName().split("\\s")) + OMDBAPI_KEY), mov);
	}
	
	private Movie processResponse(String response, Movie mov){
		logger.log(Level.INFO, "************************************");
		logger.log(Level.INFO, "Making request for " + mov.getCacheName() + ". " + CUR_THREAD);
		logger.log(Level.INFO, "************************************");
		if(!response.isEmpty()){
			logger.log(Level.INFO, "Got response " + response);
			try{
				JSONObject json = new JSONObject(response);
				mov.setTitle(json.getString("Title"));
				mov.setReleaseDate(json.getString(Tokens.RELEASE_DATE.jsonKey()));
				mov.setPlot(json.getString(Tokens.PLOT.jsonKey()));
				mov.setMetascore(json.getString(Tokens.METASCORE.jsonKey()));
				mov.setLanguage(json.getString(Tokens.LANGUAGE.jsonKey()));
				mov.setImdb(json.getString(Tokens.IMDB.jsonKey()));
				mov.setDirector(json.getString(Tokens.DIRECTOR.jsonKey()));
				mov.setActors(json.getString(Tokens.ACTORS.jsonKey()));
				moviesCache.insert(mov.getCacheName(), mov);
				return mov;
			}catch(Exception skip) {
				skip.printStackTrace();
			}
		}
		//Movie is wrong
		notFoundMovies.add(mov);
		return null;
	}
	
	

}
