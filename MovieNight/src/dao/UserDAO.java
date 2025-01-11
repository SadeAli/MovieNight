package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.User;

public class UserDAO extends AbstractDAO<User> {

	public UserDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableName() {
		return "\"User\"";
	}

	@Override
    protected User mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("fname"),
                rs.getString("lname"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("created_at")
        );
    }
	
	public boolean createUser(User user) {
	    String insertQuery = "INSERT INTO " + getTableName() + " (id, fname, lname, username, password) VALUES (?, ?, ?, ?, ?)";
	    return create(insertQuery, user.getId(), user.getFname(), user.getLname(), user.getUsername(), user.getPassword());
	}
	
    public User findByUsername(String username) {
        String query = "SELECT * FROM " + getTableName() + " WHERE username = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, username);
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
    
    public boolean updateUserPassword(int userId, String password) {
        String updateQuery = "UPDATE " + getTableName() + " SET password = ? WHERE id = ?";
        return update(updateQuery, password, userId);
    }
}
