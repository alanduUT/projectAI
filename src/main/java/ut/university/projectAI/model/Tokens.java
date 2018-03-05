package ut.university.projectAI.model;

import java.util.Arrays;
import java.util.List;

public enum Tokens {
	/**
	 * http://www.omdbapi.com/ - a very good API, to use. Token values are connected with this API. 
	 *
	 */
	DIRECTOR(Arrays.asList("director"), "Director"),
	ACTORS(Arrays.asList("actor"), "Actors"),
	RELEASE_DATE(Arrays.asList("release", "date", "time"), "Released"),
	PLOT(Arrays.asList("plot", "story"), "Plot"),
	LANGUAGE(Arrays.asList("language"), "Language"),
	METASCORE(Arrays.asList("metascore"), "Metascore"),
	IMDB(Arrays.asList("imdb"), "imdbRating");
	
	private final List<String> mainKeywords;
	private final String jsonKey;
	
	
	Tokens(List<String> mainKeywords, String jsonKey){
		this.mainKeywords = mainKeywords;
		this.jsonKey = jsonKey;
	}
	
	public List<String> keywords(){
		return mainKeywords;
	}
	
	public String jsonKey() {
		return jsonKey;
	}
}
