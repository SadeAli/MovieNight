package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

	public boolean createGenre(Genre g) {
		String insertQuery = "INSERT INTO " + getTableName() + " (id, name) VALUES (?, ?)";
	    return create(insertQuery, g.getId(), g.getName());
	}

	public Genre getGenre(String name) {
		String query = "SELECT * FROM " + getTableName() + " WHERE name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			return mapResultSetToEntity(rs);
		} catch (SQLException e) {
            System.err.println("FindAll error: " + e.getMessage());
        }
		return null;
	}
}
