package movienightgui;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.*;
import models.*;

public class Database implements IDatabase {
	
	private UserDAO userDAO;
	private InvitationDAO invitationDAO;
	private InLobbyDAO inLobbyDAO;
	private MovieDAO movieDAO;
	private SuggestionDAO suggestionDAO;
	
	public Database(Connection connection) {
		this.userDAO = new UserDAO(connection);
		this.invitationDAO = new InvitationDAO(connection);
		this.inLobbyDAO = new InLobbyDAO(connection);
		this.movieDAO = new MovieDAO(connection);
		this.suggestionDAO = new SuggestionDAO(connection);
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
	public ArrayList<String> getMovies() {
		ArrayList<String> movieTitles = new ArrayList<String>();
		for (Movie movie : movieDAO.findAll()) {
			movieTitles.add(movie.getTitle());
		}
		return movieTitles;
	}

	@Override
	public void suggestMovie(String ownerUser, String user, int movieId) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		int userId = userDAO.findByUsername(user).getId();
		suggestionDAO.addSuggestion(lobbyId, userId, movieId);
		// TODO: Do not insert if suggestion already exists.
	}

	@Override
	public ArrayList<String> getSuggestions(String ownerUser) {
		int lobbyId = userDAO.findByUsername(ownerUser).getId();
		ArrayList<String> suggestions = new ArrayList<String>();
		for (Suggestion suggestion : suggestionDAO.findByLobbyId(lobbyId)) {
			int movieId = suggestion.getMovieId();
			int userId = suggestion.getSuggestedBy();
			String movieTitle = movieDAO.findById(movieId).getTitle();
			String username = userDAO.findById(userId).getUsername();
			suggestions.add(movieTitle + " (" + username + ")");
		}
		return suggestions;
	}

	@Override
	public HashMap<String, Integer> getVotes(String ownerUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createLobby(String ownerUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUserToLobby(String ownerUser, String user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserFromLobby(String ownerUser, String user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteLobby(String ownerUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSuggestion(String ownerUser, String movieName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getBelongingLobbyOwner(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateVotesUserReady(String ownerUser, ArrayList<String> votedMoviesOfUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLobbyStillVoting(String ownerUser) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getLobbyReadyCount(String ownerUser) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUsernameExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int addUser(String username, String password, int age) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deleteUser(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void suggestMovie(String ownerUser, String movieName) {
		// TODO Auto-generated method stub
		// TODO depreciated...
	}

	
}
