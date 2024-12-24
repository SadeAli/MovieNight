package movienight;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

	public static void initialize(Connection connection) {
		
		try(Statement stmt = connection.createStatement()){
			
			String createTables = """
				CREATE TABLE IF NOT EXISTS "User"(
                    id SERIAL PRIMARY KEY,
                    fname VARCHAR(50),
                    lname VARCHAR(50),
                    username VARCHAR(50) UNIQUE,
                    password VARCHAR(50),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
				
				CREATE TABLE IF NOT EXISTS Movie(
					id SERIAL PRIMARY KEY,
					title VARCHAR(100),
					description TEXT,
					trailerPath VARCHAR(200)
				);
				
				CREATE TABLE Genre (
					id SERIAL PRIMARY KEY,
					name VARCHAR(255)	
				);
				
				CREATE TABLE HasGenre (
					movie_id INTEGER REFERENCES Movie(id),
					genre_id INTEGER REFERENCES Genre(id),
					PRIMARY KEY (movie_id, genre_id)
				);
				
				CREATE TABLE Actor (
					id SERIAL PRIMARY KEY,
					name VARCHAR(255)
				);
				
				CREATE TABLE HasActor (
					movie_id INTEGER REFERENCES Movie(id),
					actor_id INTEGER REFERENCES Actor(id),
					role VARCHAR(255),
					PRIMARY KEY (movie_id, actor_id)
				);
				
				CREATE TABLE IF NOT EXISTS Lobby(
					id SERIAL PRIMARY KEY,
					owner_id INTEGER REFERENCES "User"(id),
					date DATE
				);
				
				CREATE TABLE InLobby (
					lobby_id INTEGER REFERENCES Lobby(id),
					user_id INTEGER REFERENCES "User"(id),
					PRIMARY KEY (lobby_id, user_id)
				);
				
				CREATE TABLE IF NOT EXISTS Suggestion(
					lobby_id INTEGER REFERENCES Lobby(id),
					suggested_by INTEGER REFERENCES "User"(id),
					movie_id INTEGER REFERENCES Movie(id),
					PRIMARY KEY (lobby_id, movie_id)
				);
				
				CREATE TABLE IF NOT EXISTS Vote(
					lobby_id INTEGER REFERENCES Lobby(id),
					user_id INTEGER REFERENCES "User"(id),
					movie_id INTEGER REFERENCES Movie(id),
					PRIMARY KEY (lobby_id, user_id, movie_id)
				);
				
				CREATE TABLE IF NOT EXISTS Invitation (
					sender_id INTEGER REFERENCES "User"(id),
					lobby_id INTEGER REFERENCES Lobby(id),
					receiver_id INTEGER REFERENCES "User"(id),
					PRIMARY KEY (sender_id, receiver_id, lobby_id)
				);
			""";
			stmt.execute(createTables);
			System.out.println("Tables created successfully!.");
		} catch (Exception e) {
			System.err.println("Hata: " + e.getMessage());
		}
	}
}
