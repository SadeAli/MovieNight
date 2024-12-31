package models;

public class Invitation {
	int senderId;
	int lobbyId;
	int receiverId;
	
	public Invitation(int senderId, int lobbyId, int receiverId)
	{
		this.senderId = senderId;
		this.lobbyId = lobbyId;
		this.receiverId = receiverId;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getLobbyId() {
		return lobbyId;
	}

	public void setLobbyId(int lobbyId) {
		this.lobbyId = lobbyId;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}
	
	
}
