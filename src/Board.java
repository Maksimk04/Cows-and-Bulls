import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private Cell[][] cells = new Cell[3][3];
    private GameLogic gameLogic;
    private int row, col;
    private char winner = '\0';  // Для хранения победителя на этой доске

    public Board(GameLogic gameLogic, int row, int col) {
        this.gameLogic = gameLogic;
        this.row = row;
        this.col = col;
        setLayout(new GridLayout(3, 3));

        // Инициализация клеток
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = new Cell(gameLogic, this, i, j);
                add(cells[i][j]);
            }
        }
    }

    // Метод для сброса состояния победителя и обновления отображения
    public void clearWinner() {
        winner = '\0';  // Сброс символа победителя
        repaint();      // Перерисовка, чтобы убрать символ
    }

    public void resetBoard() {
        clearWinner();  // Сброс состояния победителя перед очисткой клеток
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].resetCell(); // Очищаем все ячейки
            }
        }
    }

    // Устанавливаем подсветку доски
    public void setHighlight(boolean highlight) {
        setBackground(highlight ? Color.LIGHT_GRAY : Color.WHITE);
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.setEnabled(highlight && cell.isEmpty());
            }
        }
    }

    // Проверяем, заполнена ли доска
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


    // Метод получения клетки по координатам
    public Cell getCell(int row, int col) {
        return cells[row][col];  // Возвращаем клетку по индексу
    }
    
    public void setWinnerColor(char winner) {
        this.winner = winner;

        // Устанавливаем текст для всех клеток, если они пустые, показываем победителя
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.isEmpty()) {
                    // Оставляем пустые клетки пустыми
                    cell.setText("");
                }
                // Все клетки остаются активными, но нельзя делать ход в завершённые квадраты
                cell.setEnabled(false);  // Блокируем клетки для хода
            }
        }
        repaint();  // Перерисовываем панель, чтобы отобразился символ
    }

    // Получаем состояние доски (массив значений клеток)
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

        // Толстые линии для разделения большого поля
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(5));
        g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1); // Горизонтальная линия снизу
        g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight()); // Вертикальная линия справа

        // Тонкие линии для малых полей (клетки 3x3)
        g2.setStroke(new BasicStroke(2));
        for (int i = 1; i < 3; i++) {
            // Малые горизонтальные линии
            g2.drawLine(0, (getHeight() / 3) * i, getWidth(), (getHeight() / 3) * i);
            // Малые вертикальные линии
            g2.drawLine((getWidth() / 3) * i, 0, (getWidth() / 3) * i, getHeight());
        }

        // Если доска завершена, рисуем победителя по центру
        if (winner != '\0') {
            String winnerText = String.valueOf(winner);
            Font font = new Font("Arial", Font.BOLD, 120);  // Увеличиваем размер шрифта
            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();

            // Вычисляем позицию для центрирования текста
            int textWidth = fm.stringWidth(winnerText);
            int textHeight = fm.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textHeight) / 2 - fm.getDescent();  // Корректируем для выравнивания по вертикали

            g2.setColor(winner == 'X' ? Color.BLUE : Color.ORANGE);  // Цвет победителя
            g2.drawString(winnerText, x, y);  // Рисуем символ победителя
        }
    }

}
