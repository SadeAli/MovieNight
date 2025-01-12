package movienightgui;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import dao.*;
import dao.LobbyDAO.VoteResult;
import models.*;

public class Database {
	
	private UserDAO userDAO;
	private InvitationDAO invitationDAO;
	private InLobbyDAO inLobbyDAO;
	private MovieDAO movieDAO;
	private SuggestionDAO suggestionDAO;
	private VoteDAO voteDAO;
	private LobbyDAO lobbyDAO;
	private GenreDAO genreDAO;
	private HasGenreDAO hasGenreDAO;
	
	public Database(Connection connection) {
		this.userDAO = new UserDAO(connection);
		this.invitationDAO = new InvitationDAO(connection);
		this.inLobbyDAO = new InLobbyDAO(connection);
		this.movieDAO = new MovieDAO(connection);
		this.suggestionDAO = new SuggestionDAO(connection);
		this.voteDAO = new VoteDAO(connection);
		this.lobbyDAO = new LobbyDAO(connection);
		this.genreDAO = new GenreDAO(connection);
		this.hasGenreDAO = new HasGenreDAO(connection);
	}
	
	public void removeVotesForMovie(String ownerUser, int movieId) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		voteDAO.removeVotesOfMovie(lobbyId, movieId);
	}
	
	
	public String getSuggestedByUsername(int suggestedMovieId, String ownerUser) {
		ArrayList<Suggestion> suggestions = (ArrayList<Suggestion>) suggestionDAO.findByLobbyId(userDAO.findByUsername(ownerUser).getId());
		for (Suggestion s : suggestions) {
			if (s.getMovieId() == suggestedMovieId) {
				return userDAO.findById(s.getSuggestedBy()).getUsername();
			}
		}
		return null;
	}
	
	
	public boolean validateLogin(String username, String password) {
		return userDAO.getUserByCredentials(username, password);
	}

	
	public HashMap<String, String> getUsersAndPasswords() {
		return null;
		// TODO: Not needed
	}

	
	public ArrayList<String> getUsers() {
		return (ArrayList<String>) userDAO.findAllUsername();
	}
	
	public ArrayList<String> getInvitiationsForUser(String username) {
		int receiverId = userDAO.findByUsername(username).getId();
		List<Invitation> invitations = invitationDAO.findByReceiver(receiverId);
		ArrayList<String> invitationSenders = new ArrayList<String>();
		
		for (Invitation invitation : invitations) {
			int senderId = invitation.getSenderId();
			invitationSenders.add(userDAO.findById(senderId).getUsername());
		}
		return invitationSenders;
	}
	
	
	public ArrayList<String> getInvitationsOfUser(String username) {
		int senderId = userDAO.findByUsername(username).getId();
		List<Invitation> invitations = invitationDAO.findBySender(senderId);
		ArrayList<String> invitationReceivers = new ArrayList<String>();
		
		for (Invitation invitation : invitations) {
			int receiverId = invitation.getReceiverId();
			invitationReceivers.add(userDAO.findById(receiverId).getUsername());
		}
		return invitationReceivers;
	}
	
	
	public void sendInvitationToUser(String fromUser, String toUser) {
		int senderId = userDAO.findByUsername(fromUser).getId();
		int receiverId = userDAO.findByUsername(toUser).getId();
		
		invitationDAO.createInvitation(new Invitation(senderId, senderId, receiverId));
		// TODO: Putting senderId instead of lobbyId.
	}

	
	public void removeInvitationFromUser(String user, String sender) {
		int senderId = userDAO.findByUsername(sender).getId();
		int receiverId = userDAO.findByUsername(user).getId();
		
		invitationDAO.deleteInvitation(senderId, receiverId);
		// TODO: Is correct?
	}

	
	public ArrayList<String> getUsersAtLobby(String ownerUser) {
		ArrayList<String> usernames = new ArrayList<String>();
		int lobbyOwnerId = userDAO.findByUsername(ownerUser).getId();
		for (InLobby inLobby : inLobbyDAO.findByLobbyId(lobbyOwnerId)) {
			int userId = inLobby.getUserId();
			String username = userDAO.findById(userId).getUsername();
			usernames.add(username);
		}
		return usernames;
	}

	
	public ArrayList<String> getMovieTitles() {
		ArrayList<String> movieTitles = new ArrayList<String>();
		for (Movie movie : movieDAO.findAll()) {
			movieTitles.add(movie.getTitle() + " (" + movie.getId() + ")");
		}
		return movieTitles;
	}
	
	
	public String getMovieTitle(int movieId) {
		Movie movie = movieDAO.findById(movieId);
		return movie.getTitle() + " (" + movie.getId() + ")";
	}
	
	
	public ArrayList<Integer> getMovieIds() {
		ArrayList<Integer> ids = new ArrayList<>();
		for (Movie movie : movieDAO.findAll()) {
			ids.add(movie.getId());
		}
		return ids;
	}

	
	public void suggestMovie(String ownerUser, String user, int movieId) {
		
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		int userId = userDAO.findByUsername(user).getId();

	    // Check if the suggestion already exists
	    if (!suggestionDAO.suggestionExists(lobbyId, userId, movieId)) {
	        suggestionDAO.addSuggestion(lobbyId, userId, movieId);
	    } else {
	        System.out.println("Suggestion already exists.");
	    }
	}

	
	public ArrayList<String> getSuggestions(String ownerUser) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		System.out.println(suggestionDAO.findByLobbyId(lobbyId));
		ArrayList<String> suggestions = new ArrayList<String>();
		for (Suggestion suggestion : suggestionDAO.findByLobbyId(lobbyId)) {
			int movieId = suggestion.getMovieId();
			int userId = suggestion.getSuggestedBy();
			String movieTitle = movieDAO.findById(movieId).getTitle();
			String username = userDAO.findById(userId).getUsername();
			suggestions.add(movieTitle + " (" + movieId + ")");
			System.out.println(movieTitle + " (" + movieId + ")");
		}
		return suggestions;
	}
	
	
    public HashMap<String, Integer> getVotes(String ownerUser) {
    	return null;
    	// TODO deprecated
    }

	
	public HashMap<Integer, Integer> getVotes2(String ownerUser) {
		
		// Movie name by vote count.
		HashMap<Integer, Integer> votes = new HashMap<>();
		for (Movie movie : movieDAO.findAll()) {
			votes.put(movie.getId(), 0);
		}
		
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		for (User user : userDAO.findAll()) {
			int userId = user.getId();
			ArrayList<Vote> userVotes = (ArrayList<Vote>) voteDAO.findVotesOfUser(lobbyId, userId);
			for (Vote vote : userVotes) {
				int movieId = movieDAO.findById(vote.getMovieId()).getId();
				int voteCount = votes.get(movieId);
				votes.put(movieId, voteCount + 1);
			}
		}
		return votes;
	}
	
	
	public ArrayList<Integer> getVoteMovieIdsOfUser(String ownerUser, String username) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		int userId = userDAO.findByUsername(username).getId();
		
		ArrayList<Integer> votedMovieIds = new ArrayList<Integer>();
		for (Vote v : voteDAO.findVotesOfUser(lobbyId, userId)) {
			votedMovieIds.add(v.getMovieId());
		}
		return votedMovieIds;
	}

	
	public void createLobby(String ownerUser) {
	    int ownerId = userDAO.findByUsername(ownerUser).getId();

	    // Check if a lobby already exists for this owner
	    if (!lobbyDAO.lobbyExists(ownerId)) {
	    	System.out.println("creating new lobby!");
	        lobbyDAO.createLobby(ownerId, ownerId);
	    } else {
	        System.out.println("Lobby already exists for user: " + ownerUser);
	    }
	}

	
	public void addUserToLobby(String ownerUser, String username) {
		int ownerId = userDAO.findByUsername(ownerUser).getId();
		Lobby lobby = lobbyDAO.findById(ownerId);
		User user = userDAO.findByUsername(username);
		System.out.println(ownerId);

		inLobbyDAO.assignUserToLobby(user, lobby);
	}

	
	public void removeUserFromLobby(String ownerUser, String username) {
		int ownerId = userDAO.findByUsername(ownerUser).getId();
		Lobby lobby = lobbyDAO.findById(ownerId);
		User user = userDAO.findByUsername(username);
		inLobbyDAO.removeUserToLobby(user, lobby);
	}

	
	public void deleteLobby(String ownerUser) {
		int ownerId = userDAO.findByUsername(ownerUser).getId();
		lobbyDAO.deleteLobby(ownerId);
		// TODO Keep in mind, ownerId == lobbyId
	}

	
	public void removeSuggestion(String ownerUser, int movieId) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		suggestionDAO.removeSuggestion(lobbyId, movieId);
	}

	
	public String getBelongingLobbyOwner(String user) {
		try {
			int userId = userDAO.findByUsername(user).getId();
			int ownerId = inLobbyDAO.findByUserId(userId).getLobbyId();
			return userDAO.findById(ownerId).getUsername();
		} catch (NullPointerException e) {
			return null;
		}
	}

	
	public void updateVotesUserReady(String ownerUser, ArrayList<String> votedMoviesOfUser) {
		// TODO deprecated probably...
		// But if ready, prevent continuing?
	}

	
	public boolean isLobbyStillVoting(String ownerUser) {
		int ownerId = userDAO.findByUsername(ownerUser).getId();
		return !lobbyDAO.findById(ownerId).isReady();
	}

	
	public int getLobbyReadyCount(String ownerUser) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public boolean isUsernameExists(String username) {
		return userDAO.findByUsername(username) != null;
	}

	
	public int addUser(String username, String password, int age) {
	    // Check if the username is blank
	    if (username == null || username.trim().isEmpty()) {
	        return 1; // Username is blank
	    }

	    // Check if the username already exists
	    if (userDAO.findByUsername(username) != null) {
	        return 2; // Username already exists
	    }

	    // Check if the password is blank
	    if (password == null || password.trim().isEmpty()) {
	        return 3; // Password is blank
	    }

	    // Check if the age constraint is violated (e.g., minimum age requirement)
	    if (age < 18) { // Assuming 18 is the minimum age
	        return 4; // Age constraint violated
	    }

	    // If all checks pass, create and add the user to the database
	    Random rn = new Random();
	    int userId = rn.nextInt(100000);
	    User newUser = new User(userId, "", "", username, password, "");
	    userDAO.createUser(newUser);

	    return 0; // Success
	}

	
	public boolean deleteUser(String username) {
	    User user = userDAO.findByUsername(username);
	    return userDAO.deleteById(user.getId());
	}

	
	public void suggestMovie(String ownerUser, String movieName) {
	    int lobbyId = userDAO.findByUsername(ownerUser).getId();
	    Movie movie = movieDAO.findByTitle(movieName);
	    if (movie != null) {
	        suggestionDAO.addSuggestion(lobbyId, lobbyId, movie.getId());
	    }
	}

	
	public void removeSuggestion(String ownerUser, String movieName) {
	    int lobbyId = userDAO.findByUsername(ownerUser).getId();
	    Movie movie = movieDAO.findByTitle(movieName);
	    if (movie != null) {
	        suggestionDAO.removeSuggestion(lobbyId, lobbyId);
	    }
	}

	
	public void setLobbyReady(String ownerUser) {
		lobbyDAO.setLobbyReady(userDAO.findByUsername(ownerUser).getId());
	}

	
	public void emptyLobby(String ownerUser) {
		int ownerId = userDAO.findByUsername(ownerUser).getId();
		Lobby lobby = lobbyDAO.findById(ownerId);
		inLobbyDAO.removeAllUsers(lobby);
	}

	
	public void emptyInvitations(String sender) {
		int ownerId = userDAO.findByUsername(sender).getId();
		invitationDAO.removeAllInvitations(ownerId);
	}

	
	public void removeSuggestion(String ownerUser, String user, String movieName) {
		// TODO Auto-generated method stub
		
	}
	
	
	public ArrayList<Integer> getSuggestedMovieIds(String ownerUser) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		ArrayList<Integer> suggestions = new ArrayList<>();
		for (Suggestion s : suggestionDAO.findByLobbyId(lobbyId)) {
			suggestions.add(s.getMovieId());
		}
		return suggestions;
	}
	
	
	public String getSuggestionTitle(String ownerUser, int movieId, String suggestedBy) {
		int voteCount = getVotes2(ownerUser).get(movieId);
		Movie m = movieDAO.findById(movieId);
		return String.format("%s (%d) (s: %s) %d", 
				m.getTitle(), m.getId(), suggestedBy, voteCount);
	}
	
	
	public ArrayList<String> getSuggestionTitles(String ownerUser) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		ArrayList<String> suggestionTitles = new ArrayList<>();
		for (Suggestion s : suggestionDAO.findByLobbyId(lobbyId)) {
			String suggestedBy = userDAO.findById(s.getSuggestedBy()).getUsername();
			suggestionTitles.add(getSuggestionTitle(ownerUser, s.getMovieId(), suggestedBy));
		}
		return suggestionTitles;
	}

	
	public ArrayList<String> getMovies() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void voteMovie(String user, String ownerUser, int movieId) {
		voteDAO.addVote(
				userDAO.findByUsername(ownerUser).getId(), 
				userDAO.findByUsername(user).getId(), movieId);
	}
	
	
	public void removeVote(String user, String ownerUser, int movieId) {
		voteDAO.removeVote(
				userDAO.findByUsername(ownerUser).getId(), 
				userDAO.findByUsername(user).getId(), movieId);
	}
	
	
	public void emptySuggestions(String ownerUser) {
		suggestionDAO.removeAllSuggestions(userDAO.findByUsername(ownerUser).getId());
	}

	
	public void emptySuggestions(int lobbyId) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void emptyVotes(String ownerUser) {
		voteDAO.removeAllVotes(userDAO.findByUsername(ownerUser).getId());
	}
	
	public void updatePassword(String username, String newPassword) {
		int userId = userDAO.findByUsername(username).getId();
		userDAO.updateUserPassword(userId, newPassword);
	}
	
	public ArrayList<String> getGenres() {
		ArrayList<String> genres = new ArrayList<>();
		for (Genre g : genreDAO.findAll()) {
			genres.add(g.getName());
		}
		return genres;
	}
	
	public ArrayList<Integer> findMovieIdsByGenres(ArrayList<String> genres) {
		ArrayList<Integer> genreIds = new ArrayList<>();
		for (String genreName : genres) {
			Genre g = genreDAO.getGenre(genreName);
			if (g != null) {
				genreIds.add(g.getId());
			}
		}
		int[] genreIdsArray = genreIds.stream().mapToInt(Integer::intValue).toArray();
		ArrayList<Movie> movies = (ArrayList<Movie>) movieDAO.findMoviesByGenres(genreIdsArray);
		ArrayList<Integer> movieIds = new ArrayList<>();
		for (Movie m : movies) {
			movieIds.add(m.getId());
		}
		System.out.println(movieIds);
		return movieIds;
	}
	
	public String getMovieGenresLabel(int movieId) {
		String label = "";
		for (HasGenre g : hasGenreDAO.getMovieGenres(movieId)) {
			label += genreDAO.findById(g.getGenreId()).getName() + ", ";
		}
		return label;
	}
	
	public String getDescription(int movieId) {
		return movieDAO.findById(movieId).getDescription();
	}
	
	public VoteResult[] getWinnerMovies(String ownerUser) {
		System.out.println("aaa " + ownerUser + " " + userDAO.findByUsername(ownerUser));
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		return lobbyDAO.getWinningMoviesByVotes(lobbyId);
	}
}
