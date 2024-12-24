package movienight;

public class Vote {
	int lobbyId;
	int userId;
	int movieId;
	
	public Vote(int lobbyId, int userId, int movieId)
	{
		this.lobbyId = lobbyId;
		this.userId = userId;
		this.movieId = movieId;
	}

	public int getLobbyId() {
		return lobbyId;
	}

	public void setLobbyId(int lobbyId) {
		this.lobbyId = lobbyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	
	
}
