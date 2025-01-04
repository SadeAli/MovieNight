/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package movienightgui;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author deneg
 */
public interface IDatabase {
    
    public HashMap<String, String> getUsersAndPasswords();
    
    public ArrayList<String> getUsers();
    
    public ArrayList<String> getInvitiationsForUser(String username);
    
    public void sendInvitationToUser(String fromUser, String toUser);
    
    public void removeInvitationFromUser(String user, String sender);
    
    public ArrayList<String> getUsersAtLobby(String ownerUser);
    
    public ArrayList<String> getMovies();
    
    public void suggestMovie(String ownerUser, String movieName);
        
    public ArrayList<String> getSuggestions(String ownerUser);
    
    public HashMap<String, Integer> getVotes(String ownerUser);
    
    public void createLobby(String ownerUser);
    
    public void addUserToLobby(String ownerUser, String user);
    
    public void removeUserFromLobby(String ownerUser, String user);

    public void deleteLobby(String ownerUser);
    
    public void removeSuggestion(String ownerUser, String movieName);
    
    public String getBelongingLobbyOwner(String user);
    
    public void updateVotesUserReady(String ownerUser, ArrayList<String> votedMoviesOfUser);
    
    public boolean isLobbyStillVoting(String ownerUser);
    
    public int getLobbyReadyCount(String ownerUser);
}
