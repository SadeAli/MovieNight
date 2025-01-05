package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Genre;

public class GenreDAO extends AbstractDAO<Genre> {

	public GenreDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableName() {
		return "genre";
	}

	@Override
	protected Genre mapResultSetToEntity(ResultSet rs) throws SQLException {
		return new Genre(
				rs.getInt("id"),
				rs.getString("name")
		);
	}

}
