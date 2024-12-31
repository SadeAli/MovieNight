package models;

import java.time.LocalDate;

public class Lobby {
	int id;
	int ownerId;
	LocalDate date;
	
	public Lobby(int id, int ownerId, LocalDate date)
	{
		this.id = id;
		this.ownerId = ownerId;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
}
