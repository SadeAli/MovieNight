package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.InLobby;
import models.Invitation;
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
	
    public List<InLobby> findByLobbyId(int lobbyId) {
    	String query = "SELECT * FROM " + getTableName() + " WHERE lobby_id = ?";
        List<InLobby> results = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
        	stmt.setInt(1, lobbyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("FindAll error: " + e.getMessage());
        }
        return results;
    }
    
    public InLobby findByUserId(int userId) {
    	String query = "SELECT * FROM " + getTableName() + " WHERE user_id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
        	stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            System.err.println("FindAll error: " + e.getMessage());
        }
        return null;
    }
}
