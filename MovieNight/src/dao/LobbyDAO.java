package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Lobby;

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
				rs.getDate("date")
		);
	}
}
