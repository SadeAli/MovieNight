package utils;

import java.sql.Statement;
import java.time.LocalDate;

import dao.MovieDAO;
import dao.UserDAO;
import dao.GenreDAO;
import dao.HasGenreDAO;
import dao.InvitationDAO;
import dao.LobbyDAO;
import models.Genre;
import models.HasGenre;
import models.Lobby;
import models.Movie;
import models.User;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class DatabaseInitializer {
	public static void initialize(Connection connection) {
		try(Statement stmt = connection.createStatement()){
			String createTables = """
				CREATE SEQUENCE user_id_seq START WITH 1 INCREMENT BY 1;
				CREATE TABLE IF NOT EXISTS "User"(
                    id INT DEFAULT nextval('user_id_seq') PRIMARY KEY,
                    fname VARCHAR(50),
                    lname VARCHAR(50),
                    username VARCHAR(50) UNIQUE,
                    password VARCHAR(50),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    
                    age INT CHECK (age >= 18)
                );
				
				CREATE TABLE IF NOT EXISTS Movie(
					id SERIAL PRIMARY KEY,
					title VARCHAR(100),
					description TEXT,
					trailerPath VARCHAR(200)
				);
				
				CREATE TABLE IF NOT EXISTS Genre (
					id SERIAL PRIMARY KEY,
					name VARCHAR(255)	
				);
				
				CREATE TABLE IF NOT EXISTS HasGenre (
					movie_id INTEGER REFERENCES Movie(id),
					genre_id INTEGER REFERENCES Genre(id),
					PRIMARY KEY (movie_id, genre_id)
				);
				
				CREATE TABLE IF NOT EXISTS Actor (
					id SERIAL PRIMARY KEY,
					name VARCHAR(255)
				);
				
				CREATE TABLE IF NOT EXISTS HasActor (
					movie_id INTEGER REFERENCES Movie(id),
					actor_id INTEGER REFERENCES Actor(id),
					role VARCHAR(255),
					PRIMARY KEY (movie_id, actor_id)
				);
				
				CREATE TABLE IF NOT EXISTS Lobby(
					id SERIAL PRIMARY KEY,
					owner_id INTEGER REFERENCES "User"(id),
					is_ready BOOLEAN DEFAULT FALSE,
					date DATE
				);
				
				CREATE TABLE IF NOT EXISTS InLobby (
					lobby_id INTEGER REFERENCES Lobby(id),
					user_id INTEGER REFERENCES "User"(id),
					PRIMARY KEY (lobby_id, user_id)
				);
				
				CREATE TABLE IF NOT EXISTS Suggestion(
					lobby_id INTEGER REFERENCES Lobby(id),
					suggested_by INTEGER REFERENCES "User"(id),
					movie_id INTEGER REFERENCES Movie(id),
					PRIMARY KEY (lobby_id, movie_id),
					FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE RESTRICT
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
				
				
				CREATE OR REPLACE FUNCTION get_winning_movies_by_votes(param_lobby_id INT)
				RETURNS TABLE(movie_id INT, movie_title TEXT, vote_count INT) AS $$
				DECLARE
				    movie_record RECORD;
				    movie_vote_count INT;
				    movie_cursor CURSOR FOR 
				        SELECT m.id, m.title
				        FROM movie m
				        WHERE EXISTS (
				            SELECT 1
				            FROM vote v
				            WHERE v.movie_id = m.id AND v.lobby_id = param_lobby_id
				        );
				BEGIN
				    -- Create a temporary table to hold the results
				    CREATE TEMP TABLE movie_votes_temp (
				        temp_movie_id INT,
				        temp_movie_title TEXT,
				        temp_vote_count INT
				    ) ON COMMIT DROP;

				    -- Loop over movies in the given lobby
				    OPEN movie_cursor;
				    LOOP
				        FETCH NEXT FROM movie_cursor INTO movie_record;
				        EXIT WHEN NOT FOUND;
				
				        -- Count votes for the current movie
				        SELECT COUNT(*) INTO movie_vote_count
				        FROM vote

				        WHERE vote.movie_id = movie_record.id AND vote.lobby_id = param_lobby_id;
				
				        -- Insert the movie and its vote count into the temporary table
				        INSERT INTO movie_votes_temp (temp_movie_id, temp_movie_title, temp_vote_count)
				        VALUES (movie_record.id, movie_record.title, movie_vote_count);
				    END LOOP;
				    CLOSE movie_cursor;
				
				    -- Return the results from the temporary table, ensuring the movie has at least 1 vote
				    RETURN QUERY
				    SELECT temp_movie_id AS movie_id, temp_movie_title AS movie_title, temp_vote_count AS vote_count
				    FROM movie_votes_temp
				    WHERE temp_vote_count > 0;
				
				    RETURN;
				END;
				$$ LANGUAGE plpgsql;

				CREATE OR REPLACE FUNCTION get_movies_by_all_genres(given_genre_ids INT[])
				RETURNS TABLE(id INT, title VARCHAR(100), description TEXT, trailerPath VARCHAR(200)) AS $$
				BEGIN
				    RETURN QUERY
				    SELECT m.id, m.title, m.description, m.trailerPath
				    FROM movie m
				    WHERE NOT EXISTS (
				        -- Ensure that there are no genres in the given list that do not intersect with the movie's genres
				        SELECT unnest(given_genre_ids)
				        EXCEPT
				        SELECT hg.genre_id
				        FROM hasgenre hg
				        WHERE hg.movie_id = m.id
				    )
				    AND (
				        -- Ensure the movie has at least one genre from the given list
				        SELECT COUNT(*) 
				        FROM (
				            SELECT unnest(given_genre_ids)
				            INTERSECT
				            SELECT hg.genre_id
				            FROM hasgenre hg
				            WHERE hg.movie_id = m.id
				        ) AS matched_genres
				    ) > 0;
				END;
				$$ LANGUAGE plpgsql;
				
				CREATE OR REPLACE FUNCTION prevent_unsuggest_if_voted()
				RETURNS TRIGGER AS $$
				BEGIN
				    -- Check if there are votes for the movie in the current lobby
				    IF EXISTS (
				        SELECT 1
				        FROM vote
				        WHERE movie_id = OLD.movie_id
				          AND lobby_id = OLD.lobby_id
				    ) THEN
				        -- Raise an error to prevent deletion
				        RAISE EXCEPTION 'Cannot unsuggest this movie as it has already been voted on.';
				    END IF;
				
				    -- Allow the delete if no votes exist
				    RETURN OLD;
				END;
				$$ LANGUAGE plpgsql;
				
				CREATE TRIGGER prevent_unsuggest_if_voted_trigger
				BEFORE DELETE ON suggestion
				FOR EACH ROW
				EXECUTE FUNCTION prevent_unsuggest_if_voted();
				
				CREATE OR REPLACE FUNCTION delete_lobby_when_empty()
				RETURNS TRIGGER AS $$
				BEGIN
				    -- Check if there are no users left in the lobby
				    IF NOT EXISTS (
				        SELECT 1
				        FROM inlobby
				        WHERE lobby_id = OLD.lobby_id
				    ) THEN
				        -- Delete votes and suggestions if no users are left
				        DELETE FROM vote
				        WHERE lobby_id = OLD.lobby_id;				        
				        DELETE FROM suggestion
				        WHERE lobby_id = OLD.lobby_id;
				        
				        -- Then delete the lobby
				        DELETE FROM lobby
				        WHERE id = OLD.lobby_id;				        
				    END IF;
				
				    -- Return the OLD row (not used, but required for the trigger)
				    RETURN OLD;
				END;
				$$ LANGUAGE plpgsql;

				CREATE TRIGGER delete_lobby_if_empty
				AFTER DELETE ON inlobby
				FOR EACH ROW
				EXECUTE FUNCTION delete_lobby_when_empty();
				
				CREATE VIEW user_identifiers AS
				SELECT id, username
				FROM "User";
			""";
			stmt.execute(createTables);
			System.out.println("Tables created successfully!.");
		} catch (Exception e) {
			System.err.println("Hata: " + e.getMessage());
		}
	}
	
	public static void reset(Connection connection) {
		
		try(Statement stmt = connection.createStatement()){
			
			String removeTables = """
				DROP SCHEMA public CASCADE;
				CREATE SCHEMA public;
			""";
			stmt.execute(removeTables);
			System.out.println("Tables removed successfully!.");
			
			initialize(connection);
			
		} catch (Exception e) {
			System.err.println("Hata: " + e.getMessage());
		}
	}
	
	public static void createDefaults(Connection connection) {
		String creationDate = LocalDate.now().toString();
		User[] users = {
				new User(0, "ali", "kemal", "alkem", "123456", creationDate),
				new User(1, "mahmut", "demir", "mate", "123456", creationDate),
				new User(2, "mehmet", "yalçın", "pro46", "123456", creationDate),
				new User(3, "kemal", "aksakal", "MovieLover11", "123456", creationDate),
				new User(4, "mustafa", "türker", "a", "123456", creationDate),
				new User(5, "ilayda", "karagöz", "ilayda", "123456", creationDate),
				new User(6, "semra", "budak", "sebu", "123456", creationDate),
				new User(7, "ata", "budak", "abu", "123456", creationDate),
				new User(8, "buse", "deniz", "bubu", "123456", creationDate),
				new User(9, "ayşe", "kaya", "aykaya", "123456", creationDate),
				new User(10, "u0", "0", "test0", "123456", creationDate),
				new User(11, "u1", "1", "test1", "123456", creationDate),
				new User(12, "u2", "2", "test2", "123456", creationDate),
				new User(13, "u3", "3", "test3", "123456", creationDate),
		};

		UserDAO userDAO = new UserDAO(connection);
		for (User u : users) {
			userDAO.createUserWithID(u);
		}

		Lobby defaultLobby = new Lobby(0, 0, false, new Date(System.currentTimeMillis()));
		LobbyDAO lobbyDAO = new LobbyDAO(connection);
		lobbyDAO.createLobby(defaultLobby);

		InvitationDAO invitationDAO = new InvitationDAO(connection);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[1]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[2]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[3]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[4]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[5]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[6]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[7]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[8]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[9]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[10]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[11]);
		invitationDAO.sendInvitation(users[0], defaultLobby, users[12]);

		// TODO: add a trailers
		String trailerPath = null;

		// NOTE: at least 10 movies added
		Movie[] movies = {
				new Movie(0, "Kung Fu Panda", "To everyone's surprise, including his own, Po, an overweight, clumsy panda, is chosen as protector of the Valley of Peace. His suitability will soon be tested as the valley's arch-enemy is on his way.", trailerPath),
				new Movie(1, "Kung Fu Panda 2", "Po and his friends fight to stop a peacock villain from conquering China with a deadly new weapon, but first the Dragon Warrior must come to terms with his past.", trailerPath),
				new Movie(2, "Cars", "On the way to the biggest race of his life, a hotshot rookie race car gets stranded in a rundown town and learns that winning isn't everything in life.", trailerPath),
				new Movie(3, "Cars 2", "Star race car Lightning McQueen and his pal Mater head overseas to compete in the World Grand Prix race. But the road to the championship becomes rocky as Mater gets caught up in an intriguing adventure of his own: international espionage.", trailerPath),
				new Movie(4, "Interstellar", "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, along with a team of researchers, to find a new planet for humans.", trailerPath),
				new Movie(5, "The Lord of the Rings: The Fellowship of the Ring", "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.", trailerPath),
				new Movie(6, "Fight Club", "An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.", trailerPath),
				new Movie(7, "The Godfather", "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.", trailerPath),
				new Movie(8, "Up", "78-year-old Carl Fredricksen travels to South America in his house equipped with balloons, inadvertently taking a young stowaway.", trailerPath),
				new Movie(9, "Reservoir Dogs", "When a simple jewelry heist goes horribly wrong, the surviving criminals begin to suspect that one of them is a police informant.", trailerPath)
		};
		
		MovieDAO movieDAO = new MovieDAO(connection);
		for (Movie m : movies) {
			movieDAO.createMovieWithID(m);
		}

		// NOTE: at least 10 genres added
		Genre[] genres = {
			new Genre(0, "animation"),
			new Genre(1, "comedy"),
			new Genre(2, "drama"),
			new Genre(3, "action"),
			new Genre(4, "sci-fi"),
			new Genre(5, "adventure"),
			new Genre(6, "crime"),
			new Genre(7, "family"),
			new Genre(8, "thriller"),
			new Genre(9, "spy"),
			new Genre(10, "fantasy"),
		};

		GenreDAO genreDAO = new GenreDAO(connection);
		for (Genre g : genres) {
			genreDAO.createGenre(g);
		}

		HasGenreDAO hasGenreDAO = new HasGenreDAO(connection);

		// kung fu panda : animation, adventure, action, family, comedy
		hasGenreDAO.assignGenreToMovie(movies[0], genreDAO.getGenre("animation"));
		hasGenreDAO.assignGenreToMovie(movies[0], genreDAO.getGenre("comedy"));
		hasGenreDAO.assignGenreToMovie(movies[0], genreDAO.getGenre("adventure"));
		hasGenreDAO.assignGenreToMovie(movies[0], genreDAO.getGenre("family"));
		hasGenreDAO.assignGenreToMovie(movies[0], genreDAO.getGenre("action"));

		// kung fu panda 2 : animation, adventure, action, family, comedy 
		hasGenreDAO.assignGenreToMovie(movies[1], genreDAO.getGenre("animation"));
		hasGenreDAO.assignGenreToMovie(movies[1], genreDAO.getGenre("comedy"));
		hasGenreDAO.assignGenreToMovie(movies[1], genreDAO.getGenre("adventure"));
		hasGenreDAO.assignGenreToMovie(movies[1], genreDAO.getGenre("family"));
		hasGenreDAO.assignGenreToMovie(movies[1], genreDAO.getGenre("action"));

		//cars : animation adventure, comedy family
		hasGenreDAO.assignGenreToMovie(movies[2], genreDAO.getGenre("animation"));
		hasGenreDAO.assignGenreToMovie(movies[2], genreDAO.getGenre("adventure"));
		hasGenreDAO.assignGenreToMovie(movies[2], genreDAO.getGenre("comedy"));
		hasGenreDAO.assignGenreToMovie(movies[2], genreDAO.getGenre("family"));

		// cars 2 : action spy adventure comedy animation crime family sci-fi
		hasGenreDAO.assignGenreToMovie(movies[3], genreDAO.getGenre("action"));
		hasGenreDAO.assignGenreToMovie(movies[3], genreDAO.getGenre("spy"));
		hasGenreDAO.assignGenreToMovie(movies[3], genreDAO.getGenre("adventure"));
		hasGenreDAO.assignGenreToMovie(movies[3], genreDAO.getGenre("comedy"));
		hasGenreDAO.assignGenreToMovie(movies[3], genreDAO.getGenre("animation"));
		hasGenreDAO.assignGenreToMovie(movies[3], genreDAO.getGenre("crime"));
		hasGenreDAO.assignGenreToMovie(movies[3], genreDAO.getGenre("family"));
		hasGenreDAO.assignGenreToMovie(movies[3], genreDAO.getGenre("sci-fi"));

		hasGenreDAO.assignGenreToMovie(movies[4], genreDAO.getGenre("sci-fi"));
		hasGenreDAO.assignGenreToMovie(movies[4], genreDAO.getGenre("adventure"));
		hasGenreDAO.assignGenreToMovie(movies[4], genreDAO.getGenre("drama"));

		// lord of the rings : adventure drama fantasy
		hasGenreDAO.assignGenreToMovie(movies[5], genreDAO.getGenre("adventure"));
		hasGenreDAO.assignGenreToMovie(movies[5], genreDAO.getGenre("drama"));
		hasGenreDAO.assignGenreToMovie(movies[5], genreDAO.getGenre("fantasy"));

		// Fight Club : drama
		hasGenreDAO.assignGenreToMovie(movies[6], genreDAO.getGenre("drama"));

		// God Father : drama crime
		hasGenreDAO.assignGenreToMovie(movies[7], genreDAO.getGenre("drama"));
		hasGenreDAO.assignGenreToMovie(movies[7], genreDAO.getGenre("crime"));

		// up : animation adventure comedy drama family
		hasGenreDAO.assignGenreToMovie(movies[8], genreDAO.getGenre("animation"));
		hasGenreDAO.assignGenreToMovie(movies[8], genreDAO.getGenre("comedy"));
		hasGenreDAO.assignGenreToMovie(movies[8], genreDAO.getGenre("drama"));
		hasGenreDAO.assignGenreToMovie(movies[8], genreDAO.getGenre("family"));

		// reservoir dogs : crime
		hasGenreDAO.assignGenreToMovie(movies[9], genreDAO.getGenre("crime"));
	}
	
	public static void main(String[] args) {
		
		try {
			Connection connection = DatabaseConnection.connect();
			System.out.println("Reseting DB...");
			reset(connection);
			System.out.println("Create test defaults...");
			createDefaults(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
