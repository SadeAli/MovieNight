/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package movienightgui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author deneg
 */
public class HomePanel extends javax.swing.JPanel {
    
    private String loggedUser;
    private ArrayList<String> users = new ArrayList<>();
    private DefaultListModel<String> usersModel = new DefaultListModel<>();
    private HashMap<String, Boolean> usersAndInvitations = new HashMap<>();
    private Database db;
    
    private String selectedUser = "";
    private ArrayList<String> invitedUsers = new ArrayList<>();
    
    private Boolean searchEmpty = true;
    
    private ArrayList<String> invitations = new ArrayList<>();
    private DefaultListModel<String> invitationsModel = new DefaultListModel<>();
    private String acceptedInvitation;
    private int numOfInvited = 0;
    
    private Boolean isCreatingNewLobby = false;
    
    private SharedUserModel sharedUserModel;
    private JFrame parentFrame;
    
    public void loginAs(String username) {
        loggedUserLabel.setText(username);
        this.loggedUser = username;
        selectedUser = loggedUser;
        selectedUserLabel.setText(loggedUser);
        userInviteCancelButton.setEnabled(false);
    }
    
    /**
     * Creates new form HomePanel
     *
     * @param loggedUser
     * @param db
     */
    public HomePanel(Database db, SharedUserModel sharedUserModel, JFrame parentFrame) {
        initComponents();
        this.db = db;
        this.sharedUserModel = sharedUserModel;
        this.parentFrame = parentFrame;
    }
    
    public void init() {
        initSearch();

        loginAs(sharedUserModel.getUsername());
        loadUsers();
        loadInvitations();
        this.acceptedInvitation = null;

        userInviteCancelButton.setEnabled(false);
    }
    
    private void initSearch() {
        searchUserField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchUserField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchUserField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchUserField.getText());
            }
        });
        refreshSearch();
    }
    
    private void refreshSearch() {
        searchEmpty = true;
        searchUserField.setText("Search user...");
        searchUserField.setForeground(Color.GRAY);
    }
    
    private void search(String input) {
        usersModel.removeAllElements();
        usersModel.addAll(users);
        for (String user : users) {
            if (!user.contains(input)) {
                usersModel.removeElement(user);
            }
        }
    }
    
    private void loadUsers() {
        users = db.getUsers();
        usersModel.removeAllElements();
        usersModel.addAll(users); 
        usersList.setModel(usersModel);
        
        for (String user : users) {
        	if (!usersAndInvitations.containsKey(user)) {
        		usersAndInvitations.put(user, false);
        	}
        } // Update for newly created users
        
        for (String user : usersAndInvitations.keySet()) {
        	if (!users.contains(user)) {
        		usersAndInvitations.remove(user);
        	}
        } // Update for deleted users
    }
    
    private void loadInvitations() {
    	// invitations to logged user
    	invitations = db.getInvitiationsForUser(loggedUser);
    	invitationsModel.removeAllElements();
    	invitationsModel.addAll(invitations);
    	invitationsList.setModel(invitationsModel);
    	
    	System.out.println(db.getInvitationsOfUser(loggedUser));
    	
    	// invitations sent from the logged user
    	for (String user : db.getInvitationsOfUser(loggedUser)) {
    		usersAndInvitations.put(user, true);
    	}
    	
        invitationAcceptButton.setText("Accept");
        invitationAcceptButton.setEnabled(false);
    	if (db.getInvitationsOfUser(loggedUser).size() > 0) {
    		displayLobbyCreate();
    	}
    }
    
    private void displayInvitations() {
        loadInvitations();
        invitationAcceptButton.setText("Accept");
    }
    
    private void displayLobbyCreate() {
        isCreatingNewLobby = true;
        invitationsModel.removeAllElements();
        invitationsModel.addElement(loggedUser);      
        invitationAcceptButton.setText("Create Lobby");
    }
    
    private void showLobby() {
        CardLayout cl = (CardLayout) parentFrame.getContentPane().getLayout();
        cl.show(parentFrame.getContentPane(), "lobby");
        for (Component component : parentFrame.getContentPane().getComponents()) {
            if (component instanceof LobbyPanel lobbyPanel) {
                lobbyPanel.init();
            }
        }
    }
    
    private void showLogin() {
        CardLayout cl = (CardLayout) parentFrame.getContentPane().getLayout();
        cl.show(parentFrame.getContentPane(), "login");
        sharedUserModel.setUsername(null);
    }
    

    private void showPassChange() {
        CardLayout cl = (CardLayout) parentFrame.getContentPane().getLayout();
        cl.show(parentFrame.getContentPane(), "change");
	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanel1 = new javax.swing.JPanel();
        invitationsPanel = new javax.swing.JPanel();
        invitationsScroll = new javax.swing.JScrollPane();
        invitationsList = new javax.swing.JList<>();
        invitationAcceptButton = new javax.swing.JButton();
        usersPanel = new javax.swing.JPanel();
        usersScroll = new javax.swing.JScrollPane();
        usersList = new javax.swing.JList<>();
        userInviteCancelButton = new javax.swing.JButton();
        selectedUserLabel = new javax.swing.JLabel();
        searchUserField = new javax.swing.JTextField();
        loggedUserPanel = new javax.swing.JPanel();
        loggedUserLabel = new javax.swing.JLabel();
        deleteButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        changePassButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        add(filler2);

        invitationsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Invitations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Black", 0, 12))); // NOI18N

        invitationsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        invitationsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invitationsListMouseClicked(evt);
            }
        });
        invitationsScroll.setViewportView(invitationsList);

        invitationAcceptButton.setText("Accept");
        invitationAcceptButton.setEnabled(false);
        invitationAcceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invitationAcceptButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout invitationsPanelLayout = new javax.swing.GroupLayout(invitationsPanel);
        invitationsPanel.setLayout(invitationsPanelLayout);
        invitationsPanelLayout.setHorizontalGroup(
            invitationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invitationsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(invitationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invitationsScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(invitationAcceptButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        invitationsPanelLayout.setVerticalGroup(
            invitationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invitationsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(invitationsScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(invitationAcceptButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        usersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "List of Users", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Black", 0, 12))); // NOI18N

        usersList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        usersList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usersListMouseClicked(evt);
            }
        });
        usersScroll.setViewportView(usersList);

        userInviteCancelButton.setText("Invite");
        userInviteCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userInviteCancelButtonActionPerformed(evt);
            }
        });

        selectedUserLabel.setText("Username");

        searchUserField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchUserFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchUserFieldFocusLost(evt);
            }
        });

        javax.swing.GroupLayout usersPanelLayout = new javax.swing.GroupLayout(usersPanel);
        usersPanel.setLayout(usersPanelLayout);
        usersPanelLayout.setHorizontalGroup(
            usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usersScroll)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, usersPanelLayout.createSequentialGroup()
                        .addComponent(selectedUserLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(userInviteCancelButton))
                    .addComponent(searchUserField))
                .addContainerGap())
        );
        usersPanelLayout.setVerticalGroup(
            usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersPanelLayout.createSequentialGroup()
                .addComponent(searchUserField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usersScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectedUserLabel)
                    .addComponent(userInviteCancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        loggedUserPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Logged in as", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Black", 0, 12))); // NOI18N

        loggedUserLabel.setText("Logged-in username");

        deleteButton.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        deleteButton.setText("Delete User");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        logoutButton.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        changePassButton.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        changePassButton.setText("Change Password");
        changePassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePassButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loggedUserPanelLayout = new javax.swing.GroupLayout(loggedUserPanel);
        loggedUserPanel.setLayout(loggedUserPanelLayout);
        loggedUserPanelLayout.setHorizontalGroup(
            loggedUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loggedUserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loggedUserLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(changePassButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoutButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addContainerGap())
        );
        loggedUserPanelLayout.setVerticalGroup(
            loggedUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loggedUserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loggedUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(logoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, loggedUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(loggedUserLabel)
                        .addComponent(deleteButton)
                        .addComponent(changePassButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(invitationsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(loggedUserPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(usersPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(invitationsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(loggedUserPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(jPanel1);
        add(filler1);
    }// </editor-fold>//GEN-END:initComponents

    private void invitationsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invitationsListMouseClicked
        // TODO add your handling code here:
        if (invitationsList.getSelectedValue() != null && acceptedInvitation == null) {
            invitationAcceptButton.setEnabled(true);
        }
    }//GEN-LAST:event_invitationsListMouseClicked

    private void invitationAcceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invitationAcceptButtonActionPerformed
        // TODO add your handling code here:
        if (invitationsList.getSelectedValue() != null) {
            acceptedInvitation = invitationsList.getSelectedValue();
            invitationAcceptButton.setEnabled(false);
            
            if (numOfInvited == 0) {
                System.out.println("Accepted Invitation of " + acceptedInvitation);
                sharedUserModel.setLobby(acceptedInvitation);
            } else {
                System.out.println("Creating a new lobby...");
                sharedUserModel.setLobby(loggedUser);
            }
            db.addUserToLobby(acceptedInvitation, loggedUser);
            showLobby();
        }
    }//GEN-LAST:event_invitationAcceptButtonActionPerformed

    private void usersListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersListMouseClicked
        // TODO add your handling code here:
        if (usersList.getSelectedValue() != null) {
            selectedUser = usersList.getSelectedValue();
        }
        selectedUserLabel.setText(selectedUser);

        if (selectedUser.equals(loggedUser)) {
            userInviteCancelButton.setEnabled(false);
        } else {
            userInviteCancelButton.setEnabled(true);
        }
        
        System.out.println(usersAndInvitations);
        if (!usersAndInvitations.get(selectedUser)) {
            userInviteCancelButton.setText("Invite");
        } else {
            userInviteCancelButton.setText("Cancel");
        }
        System.out.println(selectedUser);
    }//GEN-LAST:event_usersListMouseClicked

    private void userInviteCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userInviteCancelButtonActionPerformed
        // TODO add your handling code here:
        usersAndInvitations.put(selectedUser, !usersAndInvitations.get(selectedUser));
        
        if (!usersAndInvitations.get(selectedUser)) {
            numOfInvited -= 1;
            userInviteCancelButton.setText("Invite");
            db.removeInvitationFromUser(selectedUser, loggedUser);
            
            // If no other user is invited, show invitations again.
            if (numOfInvited == 0) {
                displayInvitations();
                db.deleteLobby(loggedUser);
            }
        } else {
            numOfInvited += 1;
            
            // If invited any user, stop displaying other invitations and only
            // display created lobby invitation.            
            if (numOfInvited == 1) {
            	System.out.println("Creating lobby...");
                db.createLobby(loggedUser);
                displayLobbyCreate();
            } else if (numOfInvited > 1) {
                displayLobbyCreate();
            }
            
            // Create a lobby first, then send invitation.
            userInviteCancelButton.setText("Cancel");
            db.sendInvitationToUser(loggedUser, selectedUser);
        }
        System.out.println(selectedUser + db.getInvitiationsForUser(selectedUser));
    }//GEN-LAST:event_userInviteCancelButtonActionPerformed

    private void searchUserFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchUserFieldFocusGained
        // TODO add your handling code here:
        if (searchEmpty) {
            searchUserField.setText("");
            searchUserField.setForeground(Color.BLACK);
            search("");
        }
    }//GEN-LAST:event_searchUserFieldFocusGained

    private void searchUserFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchUserFieldFocusLost
        // TODO add your handling code here:
        if (searchUserField.getText().equals("")) {
            searchEmpty = true;
            searchUserField.setText("Search user...");
            searchUserField.setForeground(Color.GRAY);
            search("");
        }
    }//GEN-LAST:event_searchUserFieldFocusLost

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        refreshSearch();
        loadUsers();
        loadInvitations();
        if (numOfInvited >= 1) {
            displayLobbyCreate();
        } else {
            displayInvitations();
        }
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        if (db.deleteUser(loggedUser)) {
            showLogin();
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        // TODO add your handling code here:
        showLogin();
    }//GEN-LAST:event_logoutButtonActionPerformed
    
	private void changePassButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		showPassChange();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changePassButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JButton invitationAcceptButton;
    private javax.swing.JList<String> invitationsList;
    private javax.swing.JPanel invitationsPanel;
    private javax.swing.JScrollPane invitationsScroll;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel loggedUserLabel;
    private javax.swing.JPanel loggedUserPanel;
    private javax.swing.JButton logoutButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTextField searchUserField;
    private javax.swing.JLabel selectedUserLabel;
    private javax.swing.JButton userInviteCancelButton;
    private javax.swing.JList<String> usersList;
    private javax.swing.JPanel usersPanel;
    private javax.swing.JScrollPane usersScroll;
    // End of variables declaration//GEN-END:variables
}
