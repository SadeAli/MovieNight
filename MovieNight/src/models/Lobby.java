package models;

import java.sql.Date;

public class Lobby {
	int id;
	int ownerId;
	boolean isReady;
	Date date;
	
	public Lobby(int id, int ownerId, boolean isReady, Date date)
	{
		this.id = id;
		this.ownerId = ownerId;
		this.isReady = isReady;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}
}
