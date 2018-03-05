package ut.university.projectAI.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ut.university.projectAI.caching.CacheDispenser;
import ut.university.projectAI.caching.DSGeneralCache;
import ut.university.projectAI.decoding.DSAnalyzer;
import ut.university.projectAI.decoding.DSAnalyzerImpl;
import ut.university.projectAI.model.Frame;
import ut.university.projectAI.model.Movie;
import ut.university.projectAI.model.SentenceUnit;
import ut.university.projectAI.model.Tokens;

public class MainProcessor {
	
	private static final Frame frame = new Frame();
	
	public String getResponse(String question){
		DSAnalyzer analyzer = new DSAnalyzerImpl(question);
		try {
			List<SentenceUnit<Tokens>> sentUnits = analyzer.analyze();
			List<SentenceUnit<Tokens>> correctUnits = new ArrayList<>();
			List<SentenceUnit<Tokens>> falseMoviesUnits = new ArrayList<>();
			List<SentenceUnit<Tokens>> falseTokenUnits = new ArrayList<>();
			for(SentenceUnit<Tokens> sentUnit : sentUnits){
				if(sentUnit.getMovies().isEmpty() && !sentUnit.getWords().isEmpty()){
					falseTokenUnits.add(sentUnit);
				}else if(sentUnit.getWords().isEmpty() && !sentUnit.getMovies().isEmpty()){
					falseMoviesUnits.add(sentUnit);
				}else if(!sentUnit.getWords().isEmpty() && !sentUnit.getMovies().isEmpty()){
					correctUnits.add(sentUnit);
				}
			}
			String response = startMovieProducing(correctUnits);
			response += handleWrongOnes(falseMoviesUnits, falseTokenUnits);
			return response;
		} catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return "I am confused!";
	}
	
	private String startMovieProducing(List<SentenceUnit<Tokens>> correctUnits) throws Exception{
		List<Thread> threads = new ArrayList<>();
		List<MovieInfoProducer> movInfoProducers = new ArrayList<>();
		for(SentenceUnit<Tokens> sentUnit : correctUnits){
			MovieInfoProducer mip = new MovieInfoProducer(sentUnit); 
			Thread t = new Thread(mip::fillMovies);
			threads.add(t);
			movInfoProducers.add(mip);
			t.start();
		}
		for(Thread t : threads){
			t.join();
		}
		String response = "";
		for(MovieInfoProducer mip : movInfoProducers){
			response += generateResponse(mip.getFilledMovies());
			for(Movie mov : mip.getNotFoundMovies()){
				response += "Did not find anything about movie " + mov.getCacheName() + "!\n";
			}
			response += "\n";
		}
		return response;
	}
	
	private String handleWrongOnes(List<SentenceUnit<Tokens>> emptyMovies, List<SentenceUnit<Tokens>> emptyTokens) throws Exception {
		String response = "";
		if(frame.hasFrames()){
			boolean found = false;
			SentenceUnit<Tokens> unit = emptyMovies.isEmpty() ? null : emptyMovies.get(0);  
			if(unit != null) {
				if(frame.isSuitable(unit)){
					SentenceUnit<Tokens> combinedUnit = frame.combine(unit);
					response += startMovieProducing(Arrays.asList(combinedUnit));
					emptyMovies.remove(0);
					found = true;
				}
			} 
			if(!found){
				unit = emptyTokens.isEmpty() ? null : emptyTokens.get(0);
				if(unit != null){
					if(frame.isSuitable(unit)){
						SentenceUnit<Tokens> combinedUnit = frame.combine(unit);
						response += startMovieProducing(Arrays.asList(combinedUnit));
						emptyTokens.remove(0);
						found = true;
					}
				}
			}
			response += "\n";
		}
		frame.addToFrame(emptyMovies);
		frame.addToFrame(emptyTokens);
		SentenceUnit<Tokens> frameUnit = frame.getFirst();
		if(frameUnit != null){
			if(frameUnit.getMovies().isEmpty()){
				response += "Could You specify movies that go with attributes: " + String.join(",", frameUnit.getWords().stream().map(t -> t.jsonKey().toLowerCase()).collect(Collectors.toList())) + "\n";
			} else {
				response += "Could You specify attributes that go with movies: " + String.join(",", frameUnit.getMovies().stream().map(m -> "\"" + m.getCacheName() + "\"").collect(Collectors.toList())) + "\n";
			}
		}
		return response;
	}
	//This could be done with reflection.
	private String generateResponse(List<Movie> movies){
		String resp = "";
		for(Movie movie : movies) {
			resp += "Movie \"" + movie.getTitle() + "\" has ";
			if(movie.getReleaseDate() != null) {
				resp += "been released in " + movie.getReleaseDate() + ", ";
			}
			if(movie.getPlot() != null) {
				resp += "following plot \"" + movie.getPlot() + "\", ";
			}
			if(movie.getMetascore() != null) {
				resp += "metascore of " + movie.getMetascore() + ", ";
			}
			if(movie.getLanguage() != null) {
				resp += "language(s): " + movie.getLanguage() + ", ";
			}
			if(movie.getImdb() != null) {
				resp += "imdb score of "  + movie.getImdb() + ", ";
			}
			if(movie.getDirector() != null) {
				resp += "director " + movie.getDirector() + ", ";
			}
			if(movie.getActors() != null) {
				resp += "actors: " + movie.getActors() + ", ";
			}
			resp = resp.replaceFirst(", $", ".");
			resp += "\n";
		}
		return resp;
	}

}
