# projectAI
This is the final porject for the Machine Learning course. The idea was to develop the desktop software using an enterprise language and a Natural Language Processing technique in order to simplify ones life when searching for some movie information.

# 2018 Note
~~Some API keys are outdated and unfortunately some resources started providing API for funds. Once I find a replacement, I wll think about bringing the project back to life.~~
Managed to update the API keys!

Remember, it is a university project and it really can provide some unexpected behavior sometimes. For example, I implemented a less strict matching mechanims between the synonyms of some word typed by the user (ie cast) and the synonyms of the keyword required (ie actor), this way app can provide more information (like both release date and imdb) than requested (like only imdb score).

# Example usage
Me: Could you please provide an imdb of movie "Her"?  Would be great to know something about "Jumanji".
Robot: Movie "Her" has been released in 10 Jan 2014, imdb score of 8.0.

Could You specify attributes that go with movies: "jumanji"

Me: Lets say, like date of release?
Robot: Movie "Jumanji" has been released in 15 Dec 1995.
