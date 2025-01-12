package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Genre;
import models.HasGenre;
import models.InLobby;
import models.Movie;

public class HasGenreDAO extends AbstractDAO<HasGenre> {

	public HasGenreDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableName() {
		return "HasGenre";
	}

	@Override
	protected HasGenre mapResultSetToEntity(ResultSet rs) throws SQLException {
		return new HasGenre(
				rs.getInt("movie_id"),
				rs.getInt("genre_id")
		);
	}

	public boolean assignGenreToMovie(Movie m, Genre g) {
	    String insertQuery = "INSERT INTO " + getTableName() + " (movie_id, genre_id) VALUES (?, ?)";
	    return create(insertQuery, m.getId(), g.getId());
	}
	
	public List<HasGenre> getMovieGenres(int movieId) {
    	String query = "SELECT * FROM " + getTableName() + " WHERE movie_id = ?";
        List<HasGenre> results = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
        	stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("FindAll error: " + e.getMessage());
        }
        return results;
    }
}
