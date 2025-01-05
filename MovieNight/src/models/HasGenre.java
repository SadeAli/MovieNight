package models;

public class HasGenre {
	int movieId;
	int genreId;

	public HasGenre(int movieId, int genreId)
	{
		this.movieId = movieId;
		this.genreId = genreId;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getGenreId() {
		return genreId;
	}

	public void setGenreId(int genreId) {
		this.genreId = genreId;
	}
	
	
}
