/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package movienightgui;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author deneg
 */
public class CreateUserPanel extends javax.swing.JPanel {

    private IDatabase db;
    private JFrame parentFrame;
    
    /**
     * Creates new form CreateUserPanel
     */
    public CreateUserPanel(IDatabase db, JFrame parentFrame) {
        this.db = db;
        this.parentFrame = parentFrame;
        initComponents();
        initUsernamePasswordFields();
        validateUsernamePassword();
    }
    
    private void initUsernamePasswordFields() {
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateUsernamePassword();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateUsernamePassword();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateUsernamePassword();
            }
        });
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateUsernamePassword();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateUsernamePassword();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateUsernamePassword();
            }
        });
    }
    
    private void validateUsernamePassword() {
        String username = usernameField.getText();
        if (db.isUsernameExists(username)) {
            newUserButton.setEnabled(false);
            newUserButton.setText("Username Exists");
        } else if (usernameField.getText().isBlank()) {
            newUserButton.setEnabled(false);
            newUserButton.setText("Blank Username");            
        } else if (String.valueOf(passwordField.getPassword()).isBlank()) {
            newUserButton.setEnabled(false);
            newUserButton.setText("Blank Password");              
        } else {
            newUserButton.setEnabled(true);
            newUserButton.setText("Create User");
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

        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanel1 = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        newUserButton = new javax.swing.JButton();
        passwordField = new javax.swing.JPasswordField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        add(filler2);

        usernameLabel.setText("New Username");

        passwordLabel.setText("New Password");

        newUserButton.setText("Create User");
        newUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUserButtonActionPerformed(evt);
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameLabel)
                            .addComponent(passwordLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passwordField)
                            .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(newUserButton))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(newUserButton)
                .addContainerGap())
        );

        add(jPanel1);
        add(filler1);
    }// </editor-fold>//GEN-END:initComponents

    private void newUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUserButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) parentFrame.getContentPane().getLayout();
        cl.show(parentFrame.getContentPane(), "login");
        db.addUser(usernameField.getText(), String.valueOf(passwordField.getPassword()));
        usernameField.setText("");
        passwordField.setText("");
        
    }//GEN-LAST:event_newUserButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton newUserButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
