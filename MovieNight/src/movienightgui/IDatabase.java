
package movienightgui;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface to access to the database. These are the methods that GUI use.
 * @author deneg
 */
public interface IDatabase {
    
    /**
     * Get usernames and passwords of all users from the database.
     * @return <code>HashMap</code> with usernames as keys and passwords as values
     */
    public HashMap<String, String> getUsersAndPasswords();
    
    /**
     * Get usernames.
     * @return <code>ArrayList</code> users
     */
    public ArrayList<String> getUsers();
    
    /**
     * Get invitations sent to <code>user</code>
     * @param username
     * @return <code>ArrayList</code> invitations for user
     */
    public ArrayList<String> getInvitiationsForUser(String username);
    
    /**
     * Send an invitation to user.
     * @param fromUser
     * @param toUser
     */
    public void sendInvitationToUser(String fromUser, String toUser);
    
    /**
     * Cancel sendinde the invitation.
     * @param user
     * @param sender
     */
    public void removeInvitationFromUser(String user, String sender);
    
    /**
     * Get the users which joined to the lobby created by <code>ownerUser</code>.
     * @param ownerUser
     * @return <code>ArrayList</code> users in lobby
     */
    public ArrayList<String> getUsersAtLobby(String ownerUser);
    
    /**
     * Get all movies in the database.
     * @return movies
     */
    public ArrayList<String> getMovies();
    
    /**
     * Suggest an existing movie in the lobby of <code>ownerUser</code>.
     * @param ownerUser
     * @param movieName
     */
    public void suggestMovie(String ownerUser, String movieName);
        
    /**
     * Get suggestions that have given in the lobby of <code>ownerUser</code>
     * @param ownerUser
     * @return <code>ArrayList</code> containing suggestions
     */
    public ArrayList<String> getSuggestions(String ownerUser);
    
    /**
     * Get the final votings of a lobby. Call this method only after all the 
     * users in the lobby got ready.
     * @param ownerUser
     * @return <code>HashMap</code> final votes
     */
    public HashMap<String, Integer> getVotes(String ownerUser);
    
    /**
     * Create <code>ownerUser</code> a new lobby.
     * @param ownerUser
     */
    public void createLobby(String ownerUser);
    
    /**
     * Add a user to a lobby.
     * @param ownerUser
     * @param user
     */
    public void addUserToLobby(String ownerUser, String user);
    
    /**
     * Remove a user from a lobby.
     * @param ownerUser
     * @param user
     */
    public void removeUserFromLobby(String ownerUser, String user);

    /**
     * Delete a lobby and associated suggestions and votes from the database.
     * @param ownerUser
     */
    public void deleteLobby(String ownerUser);
    
    /**
     * Remove a suggestion in a lobby.
     * @param ownerUser
     * @param movieName
     */
    public void removeSuggestion(String ownerUser, String movieName);
    
    /**
     * Get the <code>ownerUser</code> of a lobby.
     * @param user
     * @return <code>ownerUser</code>
     */
    public String getBelongingLobbyOwner(String user);
    
    /**
     * Call this method when the user in a lobby is ready (completed voting).
     * Send the votes given by the user to the database. Then the votes will 
     * get counted in the database. But keep in mind, this function is only intended
     * to get called for a single user. The voting in the lobby might still
     * going on. Call <code>isLobbyStillVoting</code> to get voting status.
     * @param ownerUser
     * @param votedMoviesOfUser
     */
    public void updateVotesUserReady(String ownerUser, ArrayList<String> votedMoviesOfUser);
    
    /**
     * Get voting status. Return <code>true</code> when all users in the lobby
     * completed voting (they all are ready).
     * @param ownerUser
     * @return voting status
     */
    public boolean isLobbyStillVoting(String ownerUser);
    
    /**
     * Get number of users in the lobby which are ready (completed voting).
     * @param ownerUser
     * @return number of ready users
     */
    public int getLobbyReadyCount(String ownerUser);
    
    /**
     * Check whether a username exists or not.
     * @param username
     * @return <code>true</code> if username already exists
     */
    public boolean isUsernameExists(String username);
    
    /**
     * Add a new user to the database. Returns an integer based on the result
     * of the operation. Returns 0 if successful. Returns 1 if username is blank.
     * Returns 2 if username exists. Returns 3 if password is blank. Returns 4
     * if age constraint is violated. Only adds a user when state is 0.
     * @param username
     * @param password
     * @param age
     * @return <code>integer</code> based on the parameters
     */
    public int addUser(String username, String password, int age);
    
    public boolean deleteUser(String username);
    
    public boolean validateLogin(String username, String password);
    
    public void suggestMovie(String lobbyId, String user, int movieId);
    
    public HashMap<Integer, Integer> getVotes2(String ownerUser);

	public ArrayList<String> getInvitationsOfUser(String username);
    
    public void removeSuggestion(String ownerUser, String user, String movieName);

    public void setLobbyReady(String ownerUser);

	public void emptyLobby(String ownerUser);
	
	public void emptyInvitations(String sender);

	ArrayList<Integer> getMovieIds();

	void removeSuggestion(String ownerUser, int movieId);
}
