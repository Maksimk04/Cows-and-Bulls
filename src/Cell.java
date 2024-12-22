import javax.swing.*;
import java.awt.*;

class Cell extends JButton {
    private char value;
    private GameLogic gameLogic;
    private Board parentBoard;
    private int row, col;

    public Cell(GameLogic gameLogic, Board parentBoard, int row, int col) {
        this.gameLogic = gameLogic;
        this.parentBoard = parentBoard;
        this.row = row;
        this.col = col;
        value = '\0';

        setFont(new Font("Arial", Font.BOLD, 40));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        addActionListener(e -> {
            if (value == '\0' && isEnabled()) {
                value = gameLogic.getCurrentPlayer();
                setText(String.valueOf(value));
                setForeground(getColorForPlayer(value));
                setEnabled(false);
                gameLogic.processMove(parentBoard.getRow(), parentBoard.getCol(), row, col, true);
            }
        });
    }

    public boolean isEmpty() {
        return value == '\0';
    }

    private Color getColorForPlayer(char player) {
        return player == 'X' ? Color.BLUE : Color.ORANGE;
    }

    public void resetCell() {
        value = '\0';
        setText("");
        setEnabled(true);
    }

    public char getValue() {
        return value;
    }

    // Метод для установки значения клетки
    public void setValue(char value) {
        this.value = value;
        setText(String.valueOf(value));  // Устанавливаем текст в клетку
        setForeground(getColorForPlayer(value));  // Устанавливаем цвет игрока
        setEnabled(false);  // Блокируем клетку после хода
    }
}

