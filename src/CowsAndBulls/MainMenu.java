package CowsAndBulls;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenu extends javax.swing.JFrame {

    public MainMenu() {
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        LocalMatchBtn = new javax.swing.JButton();
        NetworkMatchBtn = new javax.swing.JButton();
        MatchAIBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Быки коровы");
        setBackground(new java.awt.Color(204, 255, 255));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Быки коровы");

        LocalMatchBtn.setText("Начать локальную игру");
        LocalMatchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LocalMatchBtnActionPerformed(evt);
            }
        });

        NetworkMatchBtn.setText("Начать сетевую игру");
        NetworkMatchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NetworkMatchBtnActionPerformed(evt);
            }
        });

        MatchAIBtn.setText("Игра с ИИ");
        MatchAIBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MatchAIBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LocalMatchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(NetworkMatchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MatchAIBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(21, 21, 21)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(MatchAIBtn)
                .addGap(18, 18, 18)
                .addComponent(LocalMatchBtn)
                .addGap(18, 18, 18)
                .addComponent(NetworkMatchBtn)
                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LocalMatchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LocalMatchBtnActionPerformed
        dispose();
        new MakeNumber().setVisible(true);
    }//GEN-LAST:event_LocalMatchBtnActionPerformed

    private void NetworkMatchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NetworkMatchBtnActionPerformed
        dispose();
        new ChooseHostOrClent().setVisible(true);

    }//GEN-LAST:event_NetworkMatchBtnActionPerformed

    private void MatchAIBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MatchAIBtnActionPerformed
        dispose();
        new SingleGame().setVisible(true);
    }//GEN-LAST:event_MatchAIBtnActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LocalMatchBtn;
    private javax.swing.JButton MatchAIBtn;
    private javax.swing.JButton NetworkMatchBtn;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
