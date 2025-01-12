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

import javax.swing.DefaultComboBoxModel;
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
    private Database db;
    
    private HashMap<Integer, String> movies = new HashMap<>();
    private DefaultListModel<String> moviesModel = new DefaultListModel<>();
    private DefaultListModel<String> lobbyUsers = new DefaultListModel<>();
    private DefaultListModel<String> suggestionsModel = new DefaultListModel<>();
    private ArrayList<Integer> suggestionMovieIds = new ArrayList<>();
    
    private String selectedMovie = "";
    private int selectedMovieId = 0;
    private Boolean searchEmpty = true;
    private ArrayList<Integer> searchedMovies = new ArrayList<>();
    
    private int genreIndex = 0;
    private String genreName = "";
    
    private ArrayList<Integer> votes = new ArrayList<>();
    private final SharedUserModel sharedUserModel;
    private final JFrame parentFrame;
        
    private final int DELAY = 1000;
    private final int READYWAITSECONDS = 1;
    private int readyWaitCounter = 0;
    private Timer timer;
    
    /**
     * Creates new form LobbyPanel
     */
    public LobbyPanel(Database db, SharedUserModel sharedUserModel, JFrame parentFrame) {
        initComponents();
        this.db = db;
        this.sharedUserModel = sharedUserModel;
        this.parentFrame = parentFrame;      
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
    }
    
    private static <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }

    public void init() {
        this.loggedUser = sharedUserModel.getUsername();
        this.ownerUser = db.getBelongingLobbyOwner(loggedUser);
        
        initSearch();
        loadMovies();
        loadLobbyUsers();
        loadSuggestions();
        loadVotes();
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
    
    private void showHome() {
        CardLayout cl = (CardLayout) parentFrame.getContentPane().getLayout();
        cl.show(parentFrame.getContentPane(), "home");
        for (Component component : parentFrame.getContentPane().getComponents()) {
            if (component instanceof HomePanel homePanel) {
                homePanel.init();
            }            
        }
    }

    private void loadMovies() {
        movies = (HashMap<Integer, String>) zipToMap(db.getMovieIds(), db.getMovieTitles());
        moviesModel.removeAllElements();
        moviesModel.addAll(movies.values());
        moviesList.setModel(moviesModel);
        
        searchedMovies.addAll(movies.keySet());
    }
    
    private void loadLobbyUsers() {
        lobbyUsers.removeAllElements();
        lobbyUsers.addAll(db.getUsersAtLobby(ownerUser));
        usersInLobbyList.setModel(lobbyUsers);
    }
    
    private void loadSuggestions() {
        suggestionsModel.removeAllElements();
        suggestionsModel.addAll(db.getSuggestionTitles(ownerUser));
        suggestionMovieIds = db.getSuggestedMovieIds(ownerUser);
        suggestionsList.setModel(suggestionsModel);
    }

    private void loadVotes() {
    	votes = db.getVoteMovieIdsOfUser(ownerUser, loggedUser);
    	System.out.println(votes);
    	System.out.println(selectedMovie);
    }
    
    private void showSelectedMovieInfo() {
        
    	movieGenresLabel.setText(db.getMovieGenresLabel(selectedMovieId));
        movieName.setText(selectedMovie);
        descriptionTextArea.setText(db.getDescription(selectedMovieId));
        
        if (suggestionMovieIds.contains(selectedMovieId)) {
            voteButton.setEnabled(true);
            if (votes.contains(selectedMovieId)) {
                voteButton.setSelected(true);
                suggestButton.setSelected(true);
                suggestButton.setEnabled(false);
            } else {
                voteButton.setSelected(false);
                suggestButton.setSelected(true);
                suggestButton.setEnabled(true);   
            }
            if (!db.getSuggestedByUsername(selectedMovieId, ownerUser).equals(loggedUser)) {
            	// Movie suggested by someone else
            	suggestButton.setEnabled(false);
            }
        } else {
            voteButton.setEnabled(false);
            voteButton.setSelected(false);
            suggestButton.setEnabled(true);
            suggestButton.setSelected(false);
        }
    }
    
    private ArrayList<String> parseGenreField() {
    	String[] splittedGenres = genreField.getText().split(" ");
    	ArrayList<String> parsedGenres = new ArrayList<>();
    	for (String p : splittedGenres) {
    		if (!p.isBlank()) {
    			parsedGenres.add(p);
    		}
    	}
    	return parsedGenres;
    }
    
    private void search(String input) {
        moviesModel.removeAllElements();
        moviesModel.addAll(movies.values());
        searchedMovies.clear();
    	ArrayList<Integer> genreMovieIds = db.findMovieIdsByGenres(parseGenreField());

        for (Integer movieId : movies.keySet()) {
        	String title = movies.get(movieId);

        	if (!genreMovieIds.isEmpty() && !genreMovieIds.contains(movieId)) {
        		moviesModel.removeElement(title);
        	} 

        	if (!title.contains(input)) {
                moviesModel.removeElement(title);
            }
        	
        	if (moviesModel.contains(title)) {
            	searchedMovies.add(movieId);
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
        genreField.setText("");
    }
    
    private void initDatabaseAccessTimer() {
        ActionListener listener = (ActionEvent e) -> {
            int userSelectedIndex = usersInLobbyList.getSelectedIndex();
            
//            loadLobbyUsers();
//            loadSuggestions();
//            loadVotes();
//            showSelectedMovieInfo();
//            
//            usersInLobbyList.isSelectedIndex(userSelectedIndex);
            
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
        genreField = new javax.swing.JTextField();
        usersInLobbyPanel = new javax.swing.JPanel();
        usersScrollPanel = new javax.swing.JScrollPane();
        usersInLobbyList = new javax.swing.JList<>();
        suggestionsPanel = new javax.swing.JPanel();
        suggestionsScrollPanel = new javax.swing.JScrollPane();
        suggestionsList = new javax.swing.JList<>();
        readyButton = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        movieInfoPanel = new javax.swing.JPanel();
        movieNameLabel = new javax.swing.JLabel();
        movieName = new javax.swing.JLabel();
        suggestButton = new javax.swing.JToggleButton();
        voteButton = new javax.swing.JToggleButton();
        voteStatusLabel = new javax.swing.JLabel();
        backToHomeButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        movieGenresLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        refreshButton = new javax.swing.JButton();
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

        genreField.setText("jTextField1");

        javax.swing.GroupLayout moviePanelLayout = new javax.swing.GroupLayout(moviePanel);
        moviePanel.setLayout(moviePanelLayout);
        moviePanelLayout.setHorizontalGroup(
            moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(moviePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchMovieField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(moviesScrollPanel)
                    .addComponent(genreField))
                .addContainerGap())
        );
        moviePanelLayout.setVerticalGroup(
            moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(moviePanelLayout.createSequentialGroup()
                .addComponent(searchMovieField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genreField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moviesScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
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
                .addComponent(usersScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
                .addComponent(suggestionsScrollPanel)
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

        backToHomeButton.setText("Back To Home");
        backToHomeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToHomeButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Genres:");

        movieGenresLabel.setText("jLabel2");

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        descriptionTextArea.setFocusable(false);
        jScrollPane2.setViewportView(descriptionTextArea);

        javax.swing.GroupLayout movieInfoPanelLayout = new javax.swing.GroupLayout(movieInfoPanel);
        movieInfoPanel.setLayout(movieInfoPanelLayout);
        movieInfoPanelLayout.setHorizontalGroup(
            movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(movieInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(movieInfoPanelLayout.createSequentialGroup()
                        .addGroup(movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(suggestButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(voteButton, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(movieInfoPanelLayout.createSequentialGroup()
                        .addGroup(movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(movieInfoPanelLayout.createSequentialGroup()
                                .addComponent(backToHomeButton)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(voteStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(movieInfoPanelLayout.createSequentialGroup()
                                .addGroup(movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(movieNameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(movieName, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                                    .addComponent(movieGenresLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())))
        );
        movieInfoPanelLayout.setVerticalGroup(
            movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(movieInfoPanelLayout.createSequentialGroup()
                .addGroup(movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(movieNameLabel)
                    .addComponent(movieName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(movieInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(movieGenresLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suggestButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(voteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 275, Short.MAX_VALUE)
                .addComponent(voteStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backToHomeButton)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(movieInfoPanel);

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(readyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshButton))
                    .addComponent(suggestionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(moviePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usersInLobbyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(suggestionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(readyButton, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                            .addComponent(refreshButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
            selectedMovieId = searchedMovies.get(moviesList.getSelectedIndex());
            showSelectedMovieInfo();
        }
    }//GEN-LAST:event_moviesListMouseClicked

    private void suggestionsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suggestionsListMouseClicked
        // TODO add your handling code here:
        if (suggestionsList.getSelectedValue() != null) {
            selectedMovie = suggestionsList.getSelectedValue();
            selectedMovieId = suggestionMovieIds.get(suggestionsList.getSelectedIndex());
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
            voteButton.setEnabled(false);
            db.removeSuggestion(ownerUser, movieId);
            db.removeVotesForMovie(ownerUser, movieId);
        }
        loadSuggestions();
        System.out.println(suggestionsModel);
    }//GEN-LAST:event_suggestButtonActionPerformed

    private void voteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voteButtonActionPerformed
        // TODO add your handling code here:
        if (voteButton.isSelected()) {
            suggestButton.setEnabled(false);
            db.voteMovie(loggedUser, ownerUser, findIdOfSelectedMovie());
        } else {
            suggestButton.setEnabled(true);
            db.removeVote(loggedUser, ownerUser, findIdOfSelectedMovie());
        }
        System.out.println(votes);
    }//GEN-LAST:event_voteButtonActionPerformed

    private void backToHomeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToHomeButtonActionPerformed
        // TODO add your handling code here:
    	showHome();
    }//GEN-LAST:event_backToHomeButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        loadLobbyUsers();
        loadSuggestions();
        loadVotes();
        showSelectedMovieInfo();        
    }//GEN-LAST:event_refreshButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backToHomeButton;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JTextField genreField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel movieGenresLabel;
    private javax.swing.JPanel movieInfoPanel;
    private javax.swing.JLabel movieName;
    private javax.swing.JLabel movieNameLabel;
    private javax.swing.JPanel moviePanel;
    private javax.swing.JList<String> moviesList;
    private javax.swing.JScrollPane moviesScrollPanel;
    private javax.swing.JToggleButton readyButton;
    private javax.swing.JButton refreshButton;
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
