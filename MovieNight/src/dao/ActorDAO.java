package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Actor;

public class ActorDAO extends AbstractDAO<Actor> {

	public ActorDAO(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableName() {
		return "\"Actor\"";
	}

	@Override
	protected Actor mapResultSetToEntity(ResultSet rs) throws SQLException {
		return new Actor(
				rs.getInt("id"),
				rs.getString("fname"),
				rs.getString("lname")
		);
	}
	
	public boolean createActor(Actor actor) {
	    String insertQuery = "INSERT INTO " + getTableName() + " (id, fname, lname) VALUES (?, ?, ?, ?, ?)";
	    return create(insertQuery,actor.getId(), actor.getFname(), actor.getLname());
	}
	
    public boolean updateActorDetails(int actorId, String fname, String lname) {
        String updateQuery = "UPDATE " + getTableName() + " SET fname = ?, lname = ? WHERE id = ?";
        return update(updateQuery, fname, lname, actorId);
    }
}
