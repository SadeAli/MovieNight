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

    private Database db;
    private JFrame parentFrame;
    
    /**
     * Creates new form CreateUserPanel
     */
    public CreateUserPanel(Database db, JFrame parentFrame) {
        this.db = db;
        this.parentFrame = parentFrame;
        initComponents();
        validateFields();
    }
    
    private boolean validateFields() {
        String username = usernameField.getText();

        return false;
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
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        birthDateField = new javax.swing.JTextField();
        creationStatusField = new javax.swing.JLabel();
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

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Birth Date");

        creationStatusField.setForeground(new java.awt.Color(255, 0, 0));
        creationStatusField.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(creationStatusField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(newUserButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameLabel)
                            .addComponent(passwordLabel)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passwordField)
                            .addComponent(usernameField, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                            .addComponent(birthDateField))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(birthDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(creationStatusField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newUserButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        add(jPanel1);
        add(filler1);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) parentFrame.getContentPane().getLayout();
        cl.show(parentFrame.getContentPane(), "login");
        usernameField.setText("");
        passwordField.setText("");
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void newUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUserButtonActionPerformed
        // TODO add your handling code here:
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());
        int age;
        try {
            age = java.time.LocalDateTime.now().getYear() - Integer.parseInt(birthDateField.getText()); 
        } catch (NumberFormatException e) {
            age = 0; 
        }
        int status = db.addUser(username, password, age);
        
        if (status == 1) {
            creationStatusField.setText("FAILED: Blank Username");
        } else if (status == 2) {
            creationStatusField.setText("FAILED: Username Exists");
        } else if (status == 3) {
            creationStatusField.setText("FAILED: Blank Password");
        } else if (status == 4) {
            creationStatusField.setText("FAILED: Age Constraint");
        } else {
            CardLayout cl = (CardLayout) parentFrame.getContentPane().getLayout();
            cl.show(parentFrame.getContentPane(), "login");
            usernameField.setText("");
            passwordField.setText("");
        }
    }//GEN-LAST:event_newUserButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField birthDateField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel creationStatusField;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton newUserButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
