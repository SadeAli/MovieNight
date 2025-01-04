/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package movienightgui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author deneg
 */
public class DummyDatabase implements IDatabase {
    
    private ArrayList<String> users = new ArrayList<>();
    private HashMap<String, String> usersAndPasswords = new HashMap<>();
    private HashMap<String, ArrayList<String>> invitationsAll = new HashMap<>();
    
    private ArrayList<String> movies = new ArrayList<>();
    private HashMap<String, ArrayList<String>> lobbies = new HashMap<>();
    private HashMap<String, ArrayList<String>> suggestions = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> votes = new HashMap<>();
    private int lobbyReadyCount = 0;

    public DummyDatabase() {
        
        String[] dummyUsers = {"a", "b", "c", "d"};
        users.addAll(Arrays.asList(dummyUsers));
        
        for (String user : users) {
            usersAndPasswords.put(user, "123456");
        }
        
        for (int i = 0; i < users.size(); i++) {
            ArrayList<String> invitationsForUser = new ArrayList<>();
            for (int j = i+1; j < users.size(); j++) {
                invitationsForUser.add(users.get(j));
            }
            createLobby(users.get(i));
            invitationsAll.put(users.get(i), invitationsForUser);
        }   
        
        String[] dummyMovies = {"ABC", "DEF", "GHI", "JKL"};
        movies.addAll(Arrays.asList(dummyMovies));
    }
    
    @Override
    public HashMap<String, String> getUsersAndPasswords() {
        return usersAndPasswords;
    }

    @Override
    public ArrayList<String> getUsers() {
        return users;
    }

    @Override
    public ArrayList<String> getInvitiationsForUser(String username) {
        return invitationsAll.get(username);
    }

    @Override
    public void sendInvitationToUser(String fromUser, String toUser) {
        if (!invitationsAll.get(toUser).contains(fromUser)) {
            invitationsAll.get(toUser).add(fromUser);
        }
    }

    @Override
    public void removeInvitationFromUser(String user, String sender) {
        if (invitationsAll.get(user).contains(sender)) {
            invitationsAll.get(user).remove(sender);
        }
    }

    @Override
    public ArrayList<String> getUsersAtLobby(String ownerUser) {
        if (lobbies.containsKey(ownerUser)) {
            return lobbies.get(ownerUser);
        }
        return null;
    }

    @Override
    public ArrayList<String> getMovies() {
        return movies;
    }

    @Override
    public void suggestMovie(String ownerUser, String movieName) {
        if (movies.contains(movieName) && !suggestions.get(ownerUser).contains(movieName)) {
            suggestions.get(ownerUser).add(movieName);
        }
    }

    @Override
    public ArrayList<String> getSuggestions(String ownerUser) {
        return suggestions.get(ownerUser);
    }

    @Override
    public HashMap<String, Integer> getVotes(String ownerUser) {
        return votes.get(ownerUser);
    }

    @Override
    public void createLobby(String ownerUser) {
        if (!lobbies.containsKey(ownerUser)) {
            lobbies.put(ownerUser, new ArrayList<>());
            lobbies.get(ownerUser).add(ownerUser);
            suggestions.put(ownerUser, new ArrayList<>());
            
            HashMap<String, Integer> lobbyVotes = new HashMap<>();
            for (String movieName : movies) {
                lobbyVotes.put(movieName, 0);
            }
            votes.put(ownerUser, lobbyVotes);
        }
    }

    @Override
    public void addUserToLobby(String ownerUser, String user) {
        if (lobbies.containsKey(ownerUser) && !lobbies.get(ownerUser).contains(user)) {
            lobbies.get(ownerUser).add(user);
            removeInvitationFromUser(user, ownerUser);
        }
    }

    @Override
    public void removeUserFromLobby(String ownerUser, String user) {
        if (lobbies.containsKey(ownerUser) && lobbies.get(ownerUser).contains(user)) {
            lobbies.get(ownerUser).remove(user);
        }    
    }

    @Override
    public void deleteLobby(String ownerUser) {
        if (lobbies.containsKey(ownerUser)) {
            lobbies.remove(ownerUser);
            suggestions.remove(ownerUser);
            votes.remove(ownerUser);
        }    
    }

    @Override
    public void removeSuggestion(String ownerUser, String movieName) {
        if (movies.contains(movieName) && suggestions.get(ownerUser).contains(movieName)) {
            suggestions.get(ownerUser).remove(movieName);
        }
    }

    @Override
    public String getBelongingLobbyOwner(String user) {
        for (String owner : lobbies.keySet()) {
            if (lobbies.get(owner).contains(user)) {
                return owner;
            }
        }
        return null;
    }

    @Override
    public void updateVotesUserReady(String ownerUser, ArrayList<String> votedMoviesOfUser) {
        for (String movieName : votedMoviesOfUser) {
            int voteCount = votes.get(ownerUser).get(movieName);
            votes.get(ownerUser).put(movieName, voteCount + 1);
        }
        lobbyReadyCount += 1;
    }

    @Override
    public boolean isLobbyStillVoting(String ownerUser) {
        return getUsersAtLobby(ownerUser).size() > lobbyReadyCount;
    }

    @Override
    public int getLobbyReadyCount(String ownerUser) {
        return lobbyReadyCount;
    }
    
    
}
