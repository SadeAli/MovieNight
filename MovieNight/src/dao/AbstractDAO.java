package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T> {
	protected final Connection connection;
	
	public AbstractDAO(Connection connection){
		this.connection = connection;
	}
	
	protected abstract String getTableName();
	protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
	
	// Create (Insert)
    public boolean create(String insertQuery, Object... parameters) {
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            setParameters(stmt, parameters);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Create error: " + e.getMessage());
            return false;
        }
    }

    // Read (Find by ID)
    public T findById(int id) {
        String query = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            System.err.println("FindById error: " + e.getMessage());
        }
        return null;
    }

    // Read (Find All)
    public List<T> findAll() {
        String query = "SELECT * FROM " + getTableName();
        List<T> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("FindAll error: " + e.getMessage());
        }
        return results;
    }

    // Update
    public boolean update(String updateQuery, Object... parameters) {
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            setParameters(stmt, parameters);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update error: " + e.getMessage());
            return false;
        }
    }

    // Delete
    public boolean deleteById(int id) {
        String query = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete error: " + e.getMessage());
            return false;
        }
    }
    
    // Delete with any condition 
    public boolean delete(String deleteQuery, Object... parameters) {
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            setParameters(stmt, parameters);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Destroy error: " + e.getMessage());
            return false;
        }
    }

    // Helper to set parameters for PreparedStatement
    private void setParameters(PreparedStatement stmt, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            stmt.setObject(i + 1, parameters[i]);
        }
    }

	public Connection getConnection() {
		return connection;
	}   
}
