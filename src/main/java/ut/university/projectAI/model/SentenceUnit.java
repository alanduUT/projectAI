package ut.university.projectAI.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SentenceUnit<T> {
	private final List<Movie> movies;
	private final List<T> words;
	
	public SentenceUnit(List<String> movies, List<T> words){
		List<Movie> tmp = new ArrayList<>();
		for(String movie : new HashSet<>(movies)) {
			tmp.add(new Movie(movie));
		}
		this.movies = tmp;
		this.words = words;
	}
	
	public SentenceUnit(SentenceUnit<?> unit, List<T> words){
		this.movies = unit.getMovies();
		this.words = words;
	}
	
	public List<Movie> getMovies() {
		return movies;
	}
	
	public List<T> getWords() {
		return words;
	}
}
