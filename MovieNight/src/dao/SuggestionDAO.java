package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Suggestion;

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

}
