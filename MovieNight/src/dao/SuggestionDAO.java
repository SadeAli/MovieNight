package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Lobby;
import models.Suggestion;
import models.User;

public class SuggestionDAO extends AbstractDAO<Suggestion> {
	public SuggestionDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableName() {
		return "Suggestion";
	}

	@Override
	protected Suggestion mapResultSetToEntity(ResultSet rs) throws SQLException {
		return new Suggestion(
				rs.getInt("lobby_id"),
				rs.getInt("suggested_by"),
				rs.getInt("movie_id")
		);
	}
	
	public boolean addSuggestion(int lobbyId, int userId, int movieId) {
	    String insertQuery = "INSERT INTO " + getTableName() + " (lobby_id, suggested_by, movie_id) VALUES (?, ?, ?)";
	    return create(insertQuery, lobbyId, userId, movieId);
	    // TODO: Do not insert if suggestion already exists.
	}
	
	public boolean removeSuggestion(int lobbyId, int movieId) {
	    String insertQuery = "DELETE FROM " + getTableName() + " where lobby_id = ? and movie_id = ?";
	    return delete(insertQuery, lobbyId, movieId);
	}
	
    public List<Suggestion> findByLobbyId(int lobbyId) {
        String query = "SELECT * FROM " + getTableName() + " WHERE lobby_id = ?";
        List<Suggestion> results = new ArrayList<Suggestion>();
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, lobbyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("FindByLobbyId error: " + e.getMessage());
        }
        return results;
    }
    
    public boolean suggestionExists(int lobbyId, int userId, int movieId) {
        String query = "SELECT COUNT(*) FROM suggestion WHERE lobbyId = ? AND suggestedBy = ? AND movieId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, lobbyId);
            stmt.setInt(2, userId);
            stmt.setInt(3, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Return true if count > 0
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking suggestion existence: " + e.getMessage());
        }
        return false;
    }
}
