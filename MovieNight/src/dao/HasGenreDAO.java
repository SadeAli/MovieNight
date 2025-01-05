package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.HasGenre;

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
}
