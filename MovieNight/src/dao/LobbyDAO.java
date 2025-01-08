package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Lobby;

public class LobbyDAO extends AbstractDAO<Lobby> {
	public LobbyDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableName() {
		return "Lobby";
	}

	@Override
	protected Lobby mapResultSetToEntity(ResultSet rs) throws SQLException {
		return new Lobby(
				rs.getInt("id"),
				rs.getInt("owner_id"),
				rs.getBoolean("is_ready"),
				rs.getDate("date")
		);
	}
	
	public boolean createLobby(int lobbyId, int ownerId) {
	    String insertQuery = "INSERT INTO " + getTableName() + " (id, owner_id, is_ready, date) VALUES (?, ?, FALSE, now())";
	    return create(insertQuery, lobbyId, ownerId);
	}
	
	public boolean deleteLobby(int lobbyId) {
	    String deleteQuery = "DELETE FROM " + getTableName() + " WHERE id = ?";
	    return delete(deleteQuery, lobbyId);
	}
	
	public boolean lobbyExists(int ownerId) {
	    String query = "SELECT COUNT(*) FROM lobby WHERE owner_id = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, ownerId);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1) > 0; // Return true if count > 0
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error checking lobby existence: " + e.getMessage());
	    }
	    return false;
	}
	
	public boolean setLobbyReady(int lobbyId) {
		String updateQuery = "UPDATE " + getTableName() + " SET is_ready = TRUE WHERE id = ?";
		return update(updateQuery, lobbyId);
	}
}
