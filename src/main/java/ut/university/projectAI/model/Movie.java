package ut.university.projectAI.model;

public class Movie {

	private final String cacheName;
	private String title;
	private String director;
	private String actors;
	private String releaseDate;
	private String plot;
	private String language;
	private String metascore;
	private String imdb;
	
	public Movie(String cacheName) {
		this.cacheName = cacheName;
	}
	
	public Movie(Movie movie){
		this.cacheName = movie.cacheName;
		this.title = movie.title;
		this.director = movie.director;
		this.actors = movie.actors;
		this.releaseDate = movie.releaseDate;
		this.plot = movie.plot;
		this.language = movie.language;
		this.metascore = movie.metascore;
		this.imdb = movie.imdb;
	}
	
	
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setMetascore(String metascore) {
		this.metascore = metascore;
	}

	public void setImdb(String imdb) {
		this.imdb = imdb;
	}

	public String getTitle(){
		return title;
	}
	
	public String getCacheName() {
		return cacheName;
	}

	public String getDirector() {
		return director;
	}

	public String getActors() {
		return actors;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getPlot() {
		return plot;
	}

	public String getLanguage() {
		return language;
	}
	
	public String getMetascore() {
		return metascore;
	}

	public String getImdb() {
		return imdb;
	}
	
	
	@Override
	public String toString() {
		return "Movie [title=" + title + ", director=" + director + ", actors=" + actors + ", releaseDate="
				+ releaseDate + ", plot=" + plot + ", language=" + language + ", metascore=" + metascore + ", imdb="
				+ imdb + "]";
	}

}
