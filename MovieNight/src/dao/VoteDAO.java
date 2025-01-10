package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Movie;
import models.Suggestion;
import models.Vote;

public class VoteDAO extends AbstractDAO<Vote> {
	public VoteDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableName() {
		return "Vote";
	}

	@Override
	protected Vote mapResultSetToEntity(ResultSet rs) throws SQLException {
		return new Vote(
				rs.getInt("lobby_id"),
				rs.getInt("user_id"),
				rs.getInt("movie_id")
		);
	}
	
    public List<Vote> findVotesOfUser(int lobbyId, int userId) {
        String query = "SELECT * FROM " + getTableName() + " WHERE lobby_id = ? and user_id = ?";
        List<Vote> results = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, lobbyId);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("FindByLobbyId error: " + e.getMessage());
        }
        return results;
    }
    
    public int getMostVotedMovieIdInLobby(int lobbyId) {
        String query = """
            SELECT movie_id, COUNT(*) AS vote_count
            FROM votes
            WHERE lobby_id = ?
            GROUP BY movie_id
            HAVING COUNT(*) = (
                SELECT MAX(vote_count) 
                FROM (
                    SELECT COUNT(*) AS vote_count 
                    FROM votes
                    WHERE lobby_id = ?
                    GROUP BY movie_id
                ) AS vote_counts
            );
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, lobbyId);  
            stmt.setInt(2, lobbyId); 
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("movie_id");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching most voted movie ID in lobby: " + e.getMessage());
        }
        return -1;
    }
}