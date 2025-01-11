package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Lobby;
import models.Movie;

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
	
	public class VoteResult {
	    public int movieID;
	    public String movieTitle;  // Should be a String for the movie title
	    public int voteCount;
	}

	public VoteResult[] getWinningMoviesByVotes(int lobbyID) {
	    List<VoteResult> results = new ArrayList<>();  // Use a list to dynamically collect results

	    String query = "SELECT get_winning_movies_by_votes(?)";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, lobbyID);
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                VoteResult result = new VoteResult();
	                result.movieID = rs.getInt("movie_id");  // Assuming movie_id is in the result set
	                result.movieTitle = rs.getString("movie_title");  // Assuming movie_title is in the result set
	                result.voteCount = rs.getInt("vote_count");  // Assuming vote_count is in the result set
	                results.add(result);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error checking lobby existence: " + e.getMessage());
	        return null;
	    }

	    return results.toArray(new VoteResult[0]);  // Convert the list to an array and return it
	}
	
	public boolean setLobbyReady(int lobbyId) {
		String updateQuery = "UPDATE " + getTableName() + " SET is_ready = TRUE WHERE id = ?";
		return update(updateQuery, lobbyId);
	}

	public boolean createLobby(Lobby lobby) {
		String insertQuery = "INSERT INTO " + getTableName() + " (id, owner_id, date) VALUES (?, ?, ?)";
	    return create(insertQuery, lobby.getId(), lobby.getOwnerId(), lobby.getDate());
	}
}
