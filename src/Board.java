import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private Cell[][] cells = new Cell[3][3];
    private GameLogic gameLogic;
    private int row, col;
    private char winner = '\0';  // ��� �������� ���������� �� ���� �����

    public Board(GameLogic gameLogic, int row, int col) {
        this.gameLogic = gameLogic;
        this.row = row;
        this.col = col;
        setLayout(new GridLayout(3, 3));

        // ������������� ������
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = new Cell(gameLogic, this, i, j);
                add(cells[i][j]);
            }
        }
    }

    // ����� ��� ������ ��������� ���������� � ���������� �����������
    public void clearWinner() {
        winner = '\0';  // ����� ������� ����������
        repaint();      // �����������, ����� ������ ������
    }

    public void resetBoard() {
        clearWinner();  // ����� ��������� ���������� ����� �������� ������
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].resetCell(); // ������� ��� ������
            }
        }
    }

    // ������������� ��������� �����
    public void setHighlight(boolean highlight) {
        setBackground(highlight ? Color.LIGHT_GRAY : Color.WHITE);
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.setEnabled(highlight && cell.isEmpty());
            }
        }
    }

    // ���������, ��������� �� �����
    public boolean isFull() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }


    // ����� ��������� ������ �� �����������
    public Cell getCell(int row, int col) {
        return cells[row][col];  // ���������� ������ �� �������
    }
    
    public void setWinnerColor(char winner) {
        this.winner = winner;

        // ������������� ����� ��� ���� ������, ���� ��� ������, ���������� ����������
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.isEmpty()) {
                    // ��������� ������ ������ �������
                    cell.setText("");
                }
                // ��� ������ �������� ���������, �� ������ ������ ��� � ����������� ��������
                cell.setEnabled(false);  // ��������� ������ ��� ����
            }
        }
        repaint();  // �������������� ������, ����� ����������� ������
    }

    // �������� ��������� ����� (������ �������� ������)
    public char[][] getBoard() {
        char[][] boardState = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardState[i][j] = cells[i][j].getValue();
            }
        }
        return boardState;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // ������� ����� ��� ���������� �������� ����
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(5));
        g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1); // �������������� ����� �����
        g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight()); // ������������ ����� ������

        // ������ ����� ��� ����� ����� (������ 3x3)
        g2.setStroke(new BasicStroke(2));
        for (int i = 1; i < 3; i++) {
            // ����� �������������� �����
            g2.drawLine(0, (getHeight() / 3) * i, getWidth(), (getHeight() / 3) * i);
            // ����� ������������ �����
            g2.drawLine((getWidth() / 3) * i, 0, (getWidth() / 3) * i, getHeight());
        }

        // ���� ����� ���������, ������ ���������� �� ������
        if (winner != '\0') {
            String winnerText = String.valueOf(winner);
            Font font = new Font("Arial", Font.BOLD, 120);  // ����������� ������ ������
            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();

            // ��������� ������� ��� ������������� ������
            int textWidth = fm.stringWidth(winnerText);
            int textHeight = fm.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textHeight) / 2 - fm.getDescent();  // ������������ ��� ������������ �� ���������

            g2.setColor(winner == 'X' ? Color.BLUE : Color.ORANGE);  // ���� ����������
            g2.drawString(winnerText, x, y);  // ������ ������ ����������
        }
    }

}
