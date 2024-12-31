package models;

public class Suggestion {

	int lobbyId;
	int suggestedBy;
	int movieId;
	
	public Suggestion(int lobbyId, int suggestedBy, int movieId)
	{
		this.lobbyId = lobbyId;
		this.suggestedBy = suggestedBy;
		this.movieId = movieId;
	}

	public int getLobbyId() {
		return lobbyId;
	}

	public void setLobbyId(int lobbyId) {
		this.lobbyId = lobbyId;
	}

	public int getSuggestedBy() {
		return suggestedBy;
	}

	public void setSuggestedBy(int suggestedBy) {
		this.suggestedBy = suggestedBy;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	
	
}
