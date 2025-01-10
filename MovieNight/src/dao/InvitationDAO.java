package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	public boolean createInvitation(Invitation invitation) {
	    String insertQuery = "INSERT INTO " + getTableName() + " (sender_id, lobby_id, receiver_id) VALUES (?, ?, ?)";
	    return create(insertQuery, invitation.getSenderId(), invitation.getLobbyId(), invitation.getReceiverId());
	}
	
	public boolean deleteInvitation(int senderId, int receiverId) {
	    String deleteQuery = "DELETE FROM " + getTableName() + " WHERE sender_id = ? AND receiver_id = ?";
	    return delete(deleteQuery, senderId, receiverId);
	}
	
    public List<Invitation> findByReceiver(int receiverId) {
    	String query = "SELECT * FROM " + getTableName() + " WHERE receiver_id = ?";
        List<Invitation> results = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
        	stmt.setInt(1, receiverId);
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
