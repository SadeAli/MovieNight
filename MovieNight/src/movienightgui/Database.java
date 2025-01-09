package movienightgui;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import dao.*;
import models.*;

public class Database implements IDatabase {
	
	private UserDAO userDAO;
	private InvitationDAO invitationDAO;
	private InLobbyDAO inLobbyDAO;
	private MovieDAO movieDAO;
	private SuggestionDAO suggestionDAO;
	private VoteDAO voteDAO;
	private LobbyDAO lobbyDAO;
	
	public Database(Connection connection) {
		this.userDAO = new UserDAO(connection);
		this.invitationDAO = new InvitationDAO(connection);
		this.inLobbyDAO = new InLobbyDAO(connection);
		this.movieDAO = new MovieDAO(connection);
		this.suggestionDAO = new SuggestionDAO(connection);
		this.voteDAO = new VoteDAO(connection);
		this.lobbyDAO = new LobbyDAO(connection);
	}
	
	@Override
	public boolean validateLogin(String username, String password) {
		User user = userDAO.findByUsername(username);
		if (user instanceof User && user.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	@Override
	public HashMap<String, String> getUsersAndPasswords() {
		return null;
		// TODO: Not needed
	}

	@Override
	public ArrayList<String> getUsers() {
		ArrayList<String> usernames = new ArrayList<>();
		for (User user : userDAO.findAll()) {
			usernames.add(user.getUsername());
		}
		return usernames;
	}

	@Override
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
	
	@Override
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
	
	@Override
	public void sendInvitationToUser(String fromUser, String toUser) {
		int senderId = userDAO.findByUsername(fromUser).getId();
		int receiverId = userDAO.findByUsername(toUser).getId();
		
		invitationDAO.createInvitation(new Invitation(senderId, senderId, receiverId));
		// TODO: Putting senderId instead of lobbyId.
	}

	@Override
	public void removeInvitationFromUser(String user, String sender) {
		int senderId = userDAO.findByUsername(sender).getId();
		int receiverId = userDAO.findByUsername(user).getId();
		
		invitationDAO.deleteInvitation(senderId, receiverId);
		// TODO: Is correct?
	}

	@Override
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

	@Override
	public ArrayList<String> getMovieTitles() {
		ArrayList<String> movieTitles = new ArrayList<String>();
		for (Movie movie : movieDAO.findAll()) {
			movieTitles.add(movie.getTitle() + " (" + movie.getId() + ")");
		}
		return movieTitles;
	}
	
	@Override
	public ArrayList<Integer> getMovieIds() {
		ArrayList<Integer> ids = new ArrayList<>();
		for (Movie movie : movieDAO.findAll()) {
			ids.add(movie.getId());
		}
		return ids;
	}

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
	public void addUserToLobby(String ownerUser, String username) {
		int ownerId = userDAO.findByUsername(ownerUser).getId();
		Lobby lobby = lobbyDAO.findById(ownerId);
		User user = userDAO.findByUsername(username);
		System.out.println(ownerId);

		inLobbyDAO.assignUserToLobby(user, lobby);
	}

	@Override
	public void removeUserFromLobby(String ownerUser, String username) {
		int ownerId = userDAO.findByUsername(ownerUser).getId();
		Lobby lobby = lobbyDAO.findById(ownerId);
		User user = userDAO.findByUsername(username);
		inLobbyDAO.removeUserToLobby(user, lobby);
	}

	@Override
	public void deleteLobby(String ownerUser) {
		int ownerId = userDAO.findByUsername(ownerUser).getId();
		lobbyDAO.deleteLobby(ownerId);
		// TODO Keep in mind, ownerId == lobbyId
	}

	@Override
	public void removeSuggestion(String ownerUser, int movieId) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		suggestionDAO.removeSuggestion(lobbyId, movieId);
	}

	@Override
	public String getBelongingLobbyOwner(String user) {
		int userId = userDAO.findByUsername(user).getId();
		int ownerId = inLobbyDAO.findByUserId(userId).getLobbyId();
		return userDAO.findById(ownerId).getUsername();
	}

	@Override
	public void updateVotesUserReady(String ownerUser, ArrayList<String> votedMoviesOfUser) {
		// TODO deprecated probably...
		// But if ready, prevent continuing?
	}

	@Override
	public boolean isLobbyStillVoting(String ownerUser) {
		int ownerId = userDAO.findByUsername(ownerUser).getId();
		return !lobbyDAO.findById(ownerId).isReady();
	}

	@Override
	public int getLobbyReadyCount(String ownerUser) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUsernameExists(String username) {
		return userDAO.findByUsername(username) != null;
	}

	@Override
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

	@Override
	public boolean deleteUser(String username) {
	    User user = userDAO.findByUsername(username);
	    return userDAO.deleteById(user.getId());
	}

	@Override
	public void suggestMovie(String ownerUser, String movieName) {
	    int lobbyId = userDAO.findByUsername(ownerUser).getId();
	    Movie movie = movieDAO.findByTitle(movieName);
	    if (movie != null) {
	        suggestionDAO.addSuggestion(lobbyId, lobbyId, movie.getId());
	    }
	}

	@Override
	public void removeSuggestion(String ownerUser, String movieName) {
	    int lobbyId = userDAO.findByUsername(ownerUser).getId();
	    Movie movie = movieDAO.findByTitle(movieName);
	    if (movie != null) {
	        suggestionDAO.removeSuggestion(lobbyId, lobbyId);
	    }
	}

	@Override
	public void setLobbyReady(String ownerUser) {
		lobbyDAO.setLobbyReady(userDAO.findByUsername(ownerUser).getId());
	}

	@Override
	public void emptyLobby(String ownerUser) {
		int ownerId = userDAO.findByUsername(ownerUser).getId();
		Lobby lobby = lobbyDAO.findById(ownerId);
		inLobbyDAO.removeAllUsers(lobby);
	}

	@Override
	public void emptyInvitations(String sender) {
		int ownerId = userDAO.findByUsername(sender).getId();
		invitationDAO.removeAllInvitations(ownerId);
	}

	@Override
	public void removeSuggestion(String ownerUser, String user, String movieName) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ArrayList<Integer> getSuggestedMovieIds(String ownerUser) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		ArrayList<Integer> suggestions = new ArrayList<>();
		for (Suggestion s : suggestionDAO.findByLobbyId(lobbyId)) {
			suggestions.add(s.getMovieId());
		}
		return suggestions;
	}
	
	@Override
	public String getSuggestionTitle(String ownerUser, int movieId, String suggestedBy) {
		int voteCount = getVotes2(ownerUser).get(movieId);
		Movie m = movieDAO.findById(movieId);
		return String.format("%s (%d) (s: %s) %d", 
				m.getTitle(), m.getId(), suggestedBy, voteCount);
	}
	
	@Override
	public ArrayList<String> getSuggestionTitles(String ownerUser) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		ArrayList<String> suggestionTitles = new ArrayList<>();
		for (Suggestion s : suggestionDAO.findByLobbyId(lobbyId)) {
			String suggestedBy = userDAO.findById(s.getSuggestedBy()).getUsername();
			suggestionTitles.add(getSuggestionTitle(ownerUser, s.getMovieId(), suggestedBy));
		}
		return suggestionTitles;
	}

	@Override
	public ArrayList<String> getMovies() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void voteMovie(String user, String ownerUser, int movieId) {
		voteDAO.addVote(
				userDAO.findByUsername(user).getId(), 
				userDAO.findByUsername(user).getId(), movieId);
	}
	
	@Override
	public void removeVote(String user, String ownerUser, int movieId) {
		voteDAO.removeVote(
				userDAO.findByUsername(user).getId(), 
				userDAO.findByUsername(user).getId(), movieId);
	}
}
