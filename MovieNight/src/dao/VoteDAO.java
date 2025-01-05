package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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

}
