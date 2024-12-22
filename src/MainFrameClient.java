import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class MainFrameClient extends MainFrameBase {
    public GameLogic gameLogic;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public MainFrameClient(String serverIp, int port) {
        try {
            socket = new Socket(serverIp, port);
            System.out.println("����������� � �������...");

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("������������ �� " + serverIp + ":" + port);

            // ������ ������ ����
            gameLogic = new GameLogic();
            gameLogic.setMFC(this);
            setTitle("������ ����");
            
            // ������������� ������� �����
            initializeBoards();     
            
            setSize(600, 650);
            setVisible(true);
            
            // ������� ���� �� �������
            listenForMoves();
            
        } catch (IOException e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(this, "������ ����������� � �������");
        }
    }
    

    
    // ����� ��� �������� ���� �� ������
    public void sendMoveToServer(int boardRow, int boardCol, int cellRow, int cellCol, char player) {
        String move = player + "," + boardRow + "," + boardCol + "," + cellRow + "," + cellCol;
        out.println(move);
        //System.out.println("������ �������� ��� �������: " + move);
    }

    protected void initializeBoards() {
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boards = new Board[3][3]; // ������������� ������� Board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boards[i][j] = new Board(gameLogic, i, j); // �������� ������� ������� Board
                boardPanel.add(boards[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }

    private void listenForMoves() {
        new Thread(() -> {
            try {
                String move;
                while ((move = in.readLine()) != null) {
                    //System.out.println("������ ������� ��� �� �������: " + move);
                    String[] parts = move.split(",");
                    char player = parts[0].charAt(0); // ����� (X ��� O)
                    int boardRow = Integer.parseInt(parts[1]);
                    int boardCol = Integer.parseInt(parts[2]);
                    int cellRow = Integer.parseInt(parts[3]);
                    int cellCol = Integer.parseInt(parts[4]);

                    // ������������� �������� ������
                    gameLogic.setCurrentPlayer(player);

                    // ��������� ����
                    gameLogic.processMove(boardRow, boardCol, cellRow, cellCol, false); // false - �� ����������
                }
            } catch (IOException e) {
                System.out.println("������ ����������.");
                //e.printStackTrace();
            }
        }).start();
    }
    
    @Override
    public void updateBoardHighlights(int nextBoardRow, int nextBoardCol) {
        boolean highlightAll = (nextBoardRow == -1 && nextBoardCol == -1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boolean isBoardAvailable = !gameLogic.isBoardFinished(i, j);
                if (isBoardAvailable) {
                    boards[i][j].setHighlight(highlightAll || (i == nextBoardRow && j == nextBoardCol));
                } else {
                    boards[i][j].setHighlight(false);
                }
            }
        }
    }

    public void highlightWinningLine(int[] winningLine) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (winningLine[i * 3 + j] == 1) {
                    boards[i][j].setHighlight(true);
                }
            }
        }
    }
    
    public void showEndGameMessage(char winner) {
        String message = (winner == '\0') ? "�����!" : "������ ������ " + winner;
        //System.out.println(message);
        // ����� ����� �������� ����������� ���� � ����������
        JOptionPane.showMessageDialog(null, message, "����� ����", JOptionPane.INFORMATION_MESSAGE);
    }


    public void closeConnection() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("������ ������ ����������.");
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void returnToStartMenu() {
        // ������� � ������� ����
        dispose(); // ��������� ������� ����
    }

}
