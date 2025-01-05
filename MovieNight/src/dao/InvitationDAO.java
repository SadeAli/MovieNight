package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Invitation;

public class InvitationDAO extends AbstractDAO<Invitation> {
	public InvitationDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableName() {
		return "Invitation";
	}

	@Override
	protected Invitation mapResultSetToEntity(ResultSet rs) throws SQLException {
		return new Invitation(
				rs.getInt("sender_id"),
				rs.getInt("lobby_id"),
				rs.getInt("receviver_id")
		);
	}

}
