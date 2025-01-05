package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.InLobby;
import models.Lobby;
import models.User;

public class InLobbyDAO extends AbstractDAO<InLobby> {
	public InLobbyDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableName() {
		return "InLobby";
	}

	@Override
	protected InLobby mapResultSetToEntity(ResultSet rs) throws SQLException {
			return new InLobby(
					rs.getInt("lobby_id"),
					rs.getInt("user_id")
			);
	}
	
	public boolean assignUserToLobby(User u, Lobby l) {
	    String insertQuery = "INSERT INTO " + getTableName() + " (lobby_id, user_id) VALUES (?, ?)";
	    return create(insertQuery, u.getId(), l.getId());
	}
	
	public boolean removeUserToLobby(User u, Lobby l) {
	    String insertQuery = "DELETE FROM " + getTableName() + " where lobby_id = ? and user_id = ?";
	    return delete(insertQuery, u.getId(), l.getId());
	}
}
