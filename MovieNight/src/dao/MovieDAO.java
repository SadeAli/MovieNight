package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Movie;

public class MovieDAO extends AbstractDAO<Movie> {

	public MovieDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableName() {
		return "Movie";
	}

	@Override
	protected Movie mapResultSetToEntity(ResultSet rs) throws SQLException {
		return new Movie(
				rs.getInt("id"),
				rs.getString("title"),
				rs.getString("description"),
				rs.getString("trailerpath")
		);
	}
	
	public boolean createMovieWithID(Movie movie) {
	    String insertQuery = "INSERT INTO " + getTableName() + " (id, title, description, trailerPath) VALUES (?, ?, ?, ?)";
	    return create(insertQuery, movie.getId(), movie.getTitle(), movie.getDescription(), movie.getTrailerPath());
	}
	
	public boolean createMovie(Movie movie) {
	    String insertQuery = "INSERT INTO " + getTableName() + " (title, description, trailerPath) VALUES (?, ?, ?)";
	    return create(insertQuery, movie.getTitle(), movie.getDescription(), movie.getTrailerPath());
	}
	
    public Movie findByTitle(String title) {
        String query = "SELECT * FROM " + getTableName() + " WHERE title = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            System.err.println("FindByUsername error: " + e.getMessage());
        }
        return null;
    }
    
    public boolean updateUserDetails(int userId, String fname, String lname) {
        String updateQuery = "UPDATE " + getTableName() + " SET fname = ?, lname = ? WHERE id = ?";
        return update(updateQuery, fname, lname, userId);
    }
}
