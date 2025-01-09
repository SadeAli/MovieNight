/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package movienightgui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author deneg
 */
public class LobbyPanel extends javax.swing.JPanel {

    private String ownerUser;
    private String loggedUser;
    private IDatabase db;
    
    private HashMap<Integer, String> movies;
    private ArrayList<Integer> suggestedMovieIds;
    private DefaultListModel<String> moviesModel = new DefaultListModel<>();
    private DefaultListModel<String> lobbyUsers = new DefaultListModel<>();
    private DefaultListModel<String> suggestionsModel = new DefaultListModel<>();
    
    private String selectedMovie;
    private Boolean searchEmpty = true;
    
    private ArrayList<String> votes = new ArrayList<>();
    private final SharedUserModel sharedUserModel;
    private final JFrame parentFrame;
        
    private final int DELAY = 500;
    private final int READYWAITSECONDS = 10;
    private int readyWaitCounter = 0;
    private Timer timer;
    
    /**
     * Creates new form LobbyPanel
     */
    public LobbyPanel(IDatabase db, SharedUserModel sharedUserModel, JFrame parentFrame) {
        initComponents();
        this.db = db;
        this.sharedUserModel = sharedUserModel;
        this.parentFrame = parentFrame;        
    }
    
    private static <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }

    public void init() {
        this.loggedUser = sharedUserModel.getUsername();
        this.ownerUser = db.getBelongingLobbyOwner(loggedUser);
        
        loadMovies();
        loadLobbyUsers();
        loadSuggestions();
        initSearch();
        initDatabaseAccessTimer();
        voteStatusLabel.setText("User \"" + loggedUser + "\" is voting...");
        this.parentFrame.pack();
        
        String username = sharedUserModel.getUsername();
        this.ownerUser = db.getBelongingLobbyOwner(username);
        if (!username.equals(ownerUser)) {
        	readyButton.setEnabled(false);
        } else {
        	readyButton.setEnabled(true);
        }
    }

    private void loadMovies() {
        movies = (HashMap<Integer, String>) zipToMap(db.getMovieIds(), db.getMovieTitles());
        moviesModel.removeAllElements();
        moviesModel.addAll(movies.values());
        moviesList.setModel(moviesModel);
    }
    
    private void loadLobbyUsers() {
        lobbyUsers.removeAllElements();
        lobbyUsers.addAll(db.getUsersAtLobby(ownerUser));
        usersInLobbyList.setModel(lobbyUsers);
    }
    
    private void loadSuggestions() {
        suggestionsModel.removeAllElements();
        suggestionsModel.addAll(db.getSuggestionTitles(ownerUser));
        suggestionsList.setModel(suggestionsModel);
    }
    
    private void showSelectedMovieInfo() {
        // Pull info, show info...
        movieName.setText(selectedMovie);
        
        if (suggestionsModel.contains(selectedMovie)) {
            voteButton.setEnabled(true);
            if (votes.contains(selectedMovie)) {
                voteButton.setSelected(true);
                suggestButton.setSelected(true);
                suggestButton.setEnabled(false);
            } else {
                voteButton.setSelected(false);
                suggestButton.setSelected(true);
                suggestButton.setEnabled(true);   
            }
        } else {
            voteButton.setEnabled(false);
            voteButton.setSelected(false);
            suggestButton.setEnabled(true);
            suggestButton.setSelected(false);
        }
    }
    
    private void search(String input) {
        moviesModel.removeAllElements();
        moviesModel.addAll(movies.values());
        for (Integer movieId : movies.keySet()) {
        	// check genre, actor etc. too!
        	// then remove them.
        	String title = movies.get(movieId);
            if (!title.contains(input)) {
                moviesModel.removeElement(title);
            }
        }
    }
    
    private void initSearch() {
        searchMovieField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchMovieField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchMovieField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchMovieField.getText());
            }
        });
        refreshSearch();
        moviesList.requestFocus();  // To prevent search field to focus with placeholder.
    }
    
    private Integer findIdOfSelectedMovie() {
    	for (int movieId : movies.keySet()) {
    		if (selectedMovie.contains(movies.get(movieId))) {
    			return movieId;
    		}
    	}
    	return null;
    }
    
    private void refreshSearch() {
        searchEmpty = true;
        searchMovieField.setText("Search movie...");
        searchMovieField.setForeground(Color.GRAY);
    }
    
    private void initDatabaseAccessTimer() {
        ActionListener listener = (ActionEvent e) -> {
            int userSelectedIndex = usersInLobbyList.getSelectedIndex();
            
            loadLobbyUsers();
            loadSuggestions();
            
            usersInLobbyList.isSelectedIndex(userSelectedIndex);
            
            if (!db.isLobbyStillVoting(ownerUser)) {
                if (READYWAITSECONDS * 1000 / DELAY >= readyWaitCounter) {
                    voteStatusLabel.setText(
                            "Voting done: " + (READYWAITSECONDS - readyWaitCounter * DELAY / 1000)
                    );
                    readyWaitCounter += 1;
                } else {
                    showResults();
                    timer.stop();
                }
            } 
        };
        this.timer = new Timer(DELAY, listener);
        this.timer.start();
    }
    
    private void showResults() {
        CardLayout cl = (CardLayout) parentFrame.getContentPane().getLayout();
        cl.show(parentFrame.getContentPane(), "result");
        for (Component component : parentFrame.getContentPane().getComponents()) {
            if (component instanceof ResultPanel resultPanel) {
                resultPanel.init();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanel1 = new javax.swing.JPanel();
        moviePanel = new javax.swing.JPanel();
        searchMovieField = new javax.swing.JTextField();
        moviesScrollPanel = new javax.swing.JScrollPane();
        moviesList = new javax.swing.JList<>();
        usersInLobbyPanel = new javax.swing.JPanel();
        usersScrollPanel = new javax.swing.JScrollPane();
        usersInLobbyList = new javax.swing.JList<>();
        suggestionsPanel = new javax.swing.JPanel();
        suggestionsScrollPanel = new javax.swing.JScrollPane();
        suggestionsList = new javax.swing.JList<>();
        readyButton = new javax.swing.JToggleButton();
        movieInfoPanel = new javax.swing.JPanel();
        movieNameLabel = new javax.swing.JLabel();
        movieName = new javax.swing.JLabel();
        suggestButton = new javax.swing.JToggleButton();
        voteButton = new javax.swing.JToggleButton();
        voteStatusLabel = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        add(filler1);

        moviePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Movies", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Black", 0, 12))); // NOI18N

        searchMovieField.setText("jTextField1");
        searchMovieField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchMovieFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchMovieFieldFocusLost(evt);
            }
        });

        moviesScrollPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        moviesList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        moviesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        moviesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moviesListMouseClicked(evt);
            }
        });
        moviesScrollPanel.setViewportView(moviesList);

        javax.swing.GroupLayout moviePanelLayout = new javax.swing.GroupLayout(moviePanel);
        moviePanel.setLayout(moviePanelLayout);
        moviePanelLayout.setHorizontalGroup(
            moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, moviePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(moviesScrollPanel)
                    .addComponent(searchMovieField))
                .addContainerGap())
        );
        moviePanelLayout.setVerticalGroup(
            moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(moviePanelLayout.createSequentialGroup()
                .addComponent(searchMovieField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moviesScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );

        usersInLobbyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Users in Lobby", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Black", 0, 12))); // NOI18N

        usersInLobbyList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        usersScrollPanel.setViewportView(usersInLobbyList);

        javax.swing.GroupLayout usersInLobbyPanelLayout = new javax.swing.GroupLayout(usersInLobbyPanel);
        usersInLobbyPanel.setLayout(usersInLobbyPanelLayout);
        usersInLobbyPanelLayout.setHorizontalGroup(
            usersInLobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, usersInLobbyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usersScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addContainerGap())
        );
        usersInLobbyPanelLayout.setVerticalGroup(
            usersInLobbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, usersInLobbyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usersScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                .addContainerGap())
        );

        suggestionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Suggestions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Black", 0, 12))); // NOI18N

        suggestionsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        suggestionsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        suggestionsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                suggestionsListMouseClicked(evt);
            }
        });
        suggestionsScrollPanel.setViewportView(suggestionsList);

        javax.swing.GroupLayout suggestionsPanelLayout = new javax.swing.GroupLayout(suggestionsPanel);
        suggestionsPanel.setLayout(suggestionsPanelLayout);
        suggestionsPanelLayout.setHorizontalGroup(
            suggestionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suggestionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(suggestionsScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );
        suggestionsPanelLayout.setVerticalGroup(
            suggestionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suggestionsPanelLayout.createSequentialGroup()
                .addComponent(suggestionsScrollPanel)
                .addContainerGap())
        );

        readyButton.setText("Ready (Voting Done)");
        readyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readyButtonActionPerformed(evt);
            }
        });

        movieInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Movie Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Black", 0, 12))); // NOI18N

        movieNameLabel.setText("Movie Name:");

        movieName.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        movieName.setText("MOVIE NAME");

        suggestButton.setText("Suggest");
        suggestButton.setEnabled(false);
        suggestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suggestButtonActionPerformed(evt);
            }
        });

        voteButton.setText("Vote");
        voteButton.setEnabled(false);
        voteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voteButtonActionPerformed(evt);
            }
        });

        voteStatusLabel.setFont(new java.awt.Font("Segoe Print", 0, 12)); // NOI18N
        voteStatusLabel.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout movieInfoPanelLayout = new javax.swing.GroupLayout(movieInfoPanel);
        movieInfoPanel.setLayout(movieInfoPanelLayout);
        movieInfoPanelLayout.setHorizontalGroup(
            movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(movieInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(movieInfoPanelLayout.createSequentialGroup()
                        .addComponent(movieNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(movieName, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
                    .addGroup(movieInfoPanelLayout.createSequentialGroup()
                        .addGroup(movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(suggestButton, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(voteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(voteStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        movieInfoPanelLayout.setVerticalGroup(
            movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(movieInfoPanelLayout.createSequentialGroup()
                .addGroup(movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(movieNameLabel)
                    .addComponent(movieName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suggestButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(voteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(voteStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(moviePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usersInLobbyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(movieInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(readyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(suggestionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(moviePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usersInLobbyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(suggestionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(readyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(movieInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        add(jPanel1);
        add(filler2);
    }// </editor-fold>//GEN-END:initComponents

    private void searchMovieFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchMovieFieldFocusGained
        // TODO add your handling code here:
        if (searchEmpty) {
            searchMovieField.setText("");
            searchMovieField.setForeground(Color.BLACK);
            search("");
        }
    }//GEN-LAST:event_searchMovieFieldFocusGained

    private void searchMovieFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchMovieFieldFocusLost
        // TODO add your handling code here:
        if (searchMovieField.getText().equals("")) {
            searchEmpty = true;
            searchMovieField.setText("Search movie...");
            searchMovieField.setForeground(Color.GRAY);
            search("");
        }
    }//GEN-LAST:event_searchMovieFieldFocusLost

    private void moviesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moviesListMouseClicked
        // TODO add your handling code here:
        if (moviesList.getSelectedValue() != null) {
            selectedMovie = moviesList.getSelectedValue();
            showSelectedMovieInfo();
        }
    }//GEN-LAST:event_moviesListMouseClicked

    private void suggestionsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suggestionsListMouseClicked
        // TODO add your handling code here:
        if (suggestionsList.getSelectedValue() != null) {
            selectedMovie = suggestionsList.getSelectedValue();
            showSelectedMovieInfo();
        }
    }//GEN-LAST:event_suggestionsListMouseClicked

    private void readyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readyButtonActionPerformed
        System.out.println("User ready!");
        System.out.println(votes);
        db.setLobbyReady(ownerUser);
        readyButton.setEnabled(false);
        System.out.println("ready then?");
    }//GEN-LAST:event_readyButtonActionPerformed

    private void suggestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suggestButtonActionPerformed
        // TODO add your handling code here:
        int movieId = findIdOfSelectedMovie();
        if (suggestButton.isSelected()) {
            suggestionsModel.addElement(selectedMovie);
            voteButton.setEnabled(true);
            db.suggestMovie(ownerUser, loggedUser, movieId);
        } else {
            suggestionsModel.removeElement(selectedMovie);
            votes.remove(selectedMovie);
            voteButton.setEnabled(false);
            db.removeSuggestion(ownerUser, movieId);
        }
        System.out.println(suggestionsModel);
    }//GEN-LAST:event_suggestButtonActionPerformed

    private void voteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voteButtonActionPerformed
        // TODO add your handling code here:
        if (voteButton.isSelected()) {
            votes.add(selectedMovie);
            suggestButton.setEnabled(false);
            db.voteMovie(loggedUser, ownerUser, findIdOfSelectedMovie());
        } else {
            votes.remove(selectedMovie);
            suggestButton.setEnabled(true);
            db.removeVote(loggedUser, ownerUser, findIdOfSelectedMovie());
        }
        System.out.println(votes);
    }//GEN-LAST:event_voteButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel movieInfoPanel;
    private javax.swing.JLabel movieName;
    private javax.swing.JLabel movieNameLabel;
    private javax.swing.JPanel moviePanel;
    private javax.swing.JList<String> moviesList;
    private javax.swing.JScrollPane moviesScrollPanel;
    private javax.swing.JToggleButton readyButton;
    private javax.swing.JTextField searchMovieField;
    private javax.swing.JToggleButton suggestButton;
    private javax.swing.JList<String> suggestionsList;
    private javax.swing.JPanel suggestionsPanel;
    private javax.swing.JScrollPane suggestionsScrollPanel;
    private javax.swing.JList<String> usersInLobbyList;
    private javax.swing.JPanel usersInLobbyPanel;
    private javax.swing.JScrollPane usersScrollPanel;
    private javax.swing.JToggleButton voteButton;
    private javax.swing.JLabel voteStatusLabel;
    // End of variables declaration//GEN-END:variables
}
