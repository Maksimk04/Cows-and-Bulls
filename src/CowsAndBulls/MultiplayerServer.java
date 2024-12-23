package CowsAndBulls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class MultiplayerServer extends javax.swing.JFrame {
    
    String seq = "1234";
    Match game;
    int currentPlayer;
    boolean isHost;
    // 1 - host, 2 - client
    
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    
    public MultiplayerServer(int port) {
        /// for host
        initComponents();
        this.setLocationRelativeTo(null);
        int num;
        while(true) {
            num = new Random().nextInt(9876) + 1234;
            if(game.checkSeq(Integer.toString(num))) {
                break;
            } 
        }
        seq = Integer.toString(num);
        game = new Match(seq);
        currentPlayer = 1;
        isHost = true;
        
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Сервер запущен на порту " + port);
            clientSocket = serverSocket.accept();
            System.out.println("Клиент подключился");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("seq:" + seq);
            listenForMoves();
        } catch (IOException e) {
            //TODO
            // add error message
            dispose();
            new MainMenu().setVisible(true);
        }

    }
   
    
    private void listenForMoves() {
        new Thread(() -> {
            try {
                String move;
                while ((move = in.readLine()) != null) {
                    
                    if (currentPlayer == 1) continue;
                    String parts = move.trim();

                    GuessTextArea.append("Ход противника: " + parts + "\n\n");
                    game.checkAttemp(parts);
                    if (game.bulls == 4) {
                        //TODO
                        //opponent won
                        GuessTextArea.append("Противник выиграл!\n");
                    }
                    currentPlayer = 1;
                    
                }
            } catch (IOException e) {
                // не судьба
                closeConnection();
                dispose();
                new MainMenu().setVisible(true);
            }
        }).start();
    }
    
    public void closeConnection() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Соединение закрыто.");
        } catch (IOException e) {
            //System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        GuessTextArea = new java.awt.TextArea();
        guess = new java.awt.TextField();
        submitBtn = new javax.swing.JButton();
        ExitToMainMenuBtn = new javax.swing.JButton();
        CowsText = new javax.swing.JLabel();
        BullsText = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Одиночная игра");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Введите число:");

        GuessTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        GuessTextArea.setEditable(false);

        guess.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        guess.setName("guess"); // NOI18N

        submitBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        submitBtn.setText("Отправить");
        submitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBtnActionPerformed(evt);
            }
        });

        ExitToMainMenuBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ExitToMainMenuBtn.setText("Выход в гланое меню");
        ExitToMainMenuBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitToMainMenuBtnActionPerformed(evt);
            }
        });

        CowsText.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CowsText.setText("Коров:");

        BullsText.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BullsText.setText("Быков: ");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("X");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(submitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ExitToMainMenuBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CowsText)
                    .addComponent(BullsText)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(guess, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(39, 39, 39)
                .addComponent(GuessTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(GuessTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(guess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(23, 23, 23)
                .addComponent(submitBtn)
                .addGap(18, 18, 18)
                .addComponent(CowsText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BullsText)
                .addGap(18, 18, 18)
                .addComponent(ExitToMainMenuBtn)
                .addGap(36, 36, 36))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBtnActionPerformed
        if (currentPlayer == 2) return;
        String guessNum = guess.getText();
        
        if (!game.checkSeq(guessNum)) {
            GuessTextArea.append("Некорректный ввод!\n\n");
            CowsText.setText("Коров: " + 0);
            BullsText.setText("Быков: " + 0);
            guess.setText("");
            guess.requestFocus();
            return;
        }
        
        game.checkAttemp(guessNum);
        CowsText.setText("Коров: " + Integer.toString(game.cows));
        BullsText.setText("Быков: " + Integer.toString(game.bulls));
        out.println(guessNum);
        
        if (game.bulls == 4) {
            // TODO
            // add wining message
            
            new Final(game.turnCount).setVisible(true);
        }
        
        GuessTextArea.append("Ход №" + Integer.toString(game.turnCount + 1) +
                "         " + guessNum + "\nКоров: " + Integer.toString(game.cows) + 
                "     Быков: " + Integer.toString(game.bulls) + "\n\n");
        
        game.turnCount++;
        guess.setText("");
        guess.requestFocus();
        currentPlayer = 2;
    }//GEN-LAST:event_submitBtnActionPerformed

    private void ExitToMainMenuBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitToMainMenuBtnActionPerformed
        dispose();
        new MainMenu().setVisible(true);
    }//GEN-LAST:event_ExitToMainMenuBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        guess.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BullsText;
    private javax.swing.JLabel CowsText;
    private javax.swing.JButton ExitToMainMenuBtn;
    private java.awt.TextArea GuessTextArea;
    private java.awt.TextField guess;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton submitBtn;
    // End of variables declaration//GEN-END:variables
}
