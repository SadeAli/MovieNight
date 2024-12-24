package movienight;

public class InLobby {

	int lobbyId;
	int userId;
	
	public InLobby(int lobbyId, int userId)
	{
		this.lobbyId = lobbyId;
		this.userId = userId;
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
	
	
}
