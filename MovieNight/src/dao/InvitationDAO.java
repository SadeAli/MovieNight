package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Invitation;
import models.Lobby;
import models.User;

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

	public boolean sendInvitation(User sender, Lobby lobby, User receiver) {
		String insertQuery = "INSERT INTO " + getTableName() + " (sender_id, lobby_id, receiver_id) VALUES (?, ?, ?)";
	    return create(insertQuery, sender.getId(), lobby.getId(), receiver.getId());
	}
}
