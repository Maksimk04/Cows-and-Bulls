import javax.swing.*;
import java.awt.*;

public class MainFrameOffline extends MainFrameBase {
    public GameLogic gameLogic;
    public String gameName;

    public MainFrameOffline(String gameName) {
        this.gameName = gameName;
        gameLogic = new GameLogic();
        gameLogic.setMFO(this);
        setTitle("�����-��������-������ - " + gameName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ������������� ������� �����
        initializeBoards();

        // �������� ������ ���� (����� �������������� � �������� ������)
        loadGameData();

        // ��������� ������ "���������" (����� �������������� � �������� ������)
        addSaveButton();

        setSize(600, 650);
        setVisible(true);
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

    protected void loadGameData() {
        // �������� ������ �� ���� ������
        DatabaseHelper.loadGameMoves(gameName, gameLogic);
    }

    protected void addSaveButton() {
        JButton saveButton = new JButton("���������");
        saveButton.addActionListener(e -> {
            saveGameData();
            JOptionPane.showMessageDialog(this, "���� ���������.");
            dispose(); // ��������� ������� ����
            new StartMenu();
        });
        add(saveButton, BorderLayout.NORTH);
    }

    protected void saveGameData() {
        // ������� ������� ����� �����������
        DatabaseHelper.clearGameTable(gameName);

        // ���������� ���� �����, ����� ��������
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        char value = boards[i][j].getCell(k, l).getValue();
                        if (value != '\0' &&
                            !(i == gameLogic.getCurrentBoardRow() &&
                              j == gameLogic.getCurrentBoardCol() &&
                              k == gameLogic.getCurrentCellRow() &&
                              l == gameLogic.getCurrentCellCol())) {
                            DatabaseHelper.saveMove(gameName, value, i, j, k, l);
                        }
                    }
                }
            }
        }
        gameLogic.togglePlayer();
        // ���������� �������� ���� ��� ����������
        DatabaseHelper.saveMove(gameName,
                gameLogic.getCurrentPlayer(),
                gameLogic.getCurrentBoardRow(),
                gameLogic.getCurrentBoardCol(),
                gameLogic.getCurrentCellRow(),
                gameLogic.getCurrentCellCol());
    }

    @Override
    public void announceWinner(char winner) {
        JOptionPane.showMessageDialog(this, "����������: " + winner + "!");

        // �������� ������� ����
        DatabaseHelper.deleteGameTable(gameName);

        // ������� � ������� ����
        dispose(); // ��������� ������� ����
        new StartMenu(); // ��������� ��������� ����
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
}
