package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Genre;
import models.HasGenre;
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
}
