import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class MainFrameServer extends MainFrameBase {
    private ServerSocket serverSocket;
    public GameLogic gameLogic;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String gameName;

    public MainFrameServer(String gameName, int port) {
        this.gameName = gameName;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Сервер запущен на порту " + port);
            clientSocket = serverSocket.accept();
            System.out.println("Клиент подключился");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Запуск логики игры
            gameLogic = new GameLogic();
            gameLogic.setMFS(this);
            setTitle("Хост: " + gameName);
            
            // Инициализация игровых досок
            initializeBoards();

            // Загрузка данных игры и передача клиенту
            loadGameData();
            
            // Добавляем кнопку "Сохранить"
            addSaveButton();
            
            setSize(600, 650);
            setVisible(true);
            listenForClientMoves();
            
        } catch (IOException e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка сервера");
        }
    }
    
    
    // Метод для обработки ходов, приходящих от клиента
    private void listenForClientMoves() {
        new Thread(() -> {
            try {
                String move;
                while ((move = in.readLine()) != null) {
                    //System.out.println("Сервер получил ход от клиента: " + move);

                    // Разбираем ход и передаем его в логику игры
                    String[] parts = move.split(",");
                    char player = parts[0].charAt(0);
                    int boardRow = Integer.parseInt(parts[1]);
                    int boardCol = Integer.parseInt(parts[2]);
                    int cellRow = Integer.parseInt(parts[3]);
                    int cellCol = Integer.parseInt(parts[4]);

                    gameLogic.setCurrentPlayer(player);
                    gameLogic.processMove(boardRow, boardCol, cellRow, cellCol, false); // false - не отправляем
                }
            } catch (IOException e) {
                //System.out.println("Клиент отключился.");
            }
        }).start();
    }

    protected void initializeBoards() {
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boards = new Board[3][3]; // Инициализация массива Board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boards[i][j] = new Board(gameLogic, i, j); // Создание каждого объекта Board
                boardPanel.add(boards[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }

    protected void loadGameData() {
        DatabaseHelper.loadGameMoves(gameName, gameLogic);
    }
    
    // Отправляем на клиент ход
    public void sendMove(int boardRow, int boardCol, int cellRow, int cellCol, char player) {
        String move = player + "," + boardRow + "," + boardCol + "," + cellRow + "," + cellCol;
        out.println(move);
        //System.out.println("Сервер отправил ход клиенту: " + move);
    }
    
    protected void addSaveButton() {
        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            saveGameData(); // Записываем в БД
            System.out.println("БД сохранена");
        });
        add(saveButton, BorderLayout.NORTH);
    }

    protected void saveGameData() {
        // Очистка таблицы перед сохранением
        DatabaseHelper.clearGameTable(gameName);

        // Сохранение всех ходов, кроме текущего
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
        // Сохранение текущего хода как последнего
        DatabaseHelper.saveMove(gameName,
                gameLogic.getCurrentPlayer(),
                gameLogic.getCurrentBoardRow(),
                gameLogic.getCurrentBoardCol(),
                gameLogic.getCurrentCellRow(),
                gameLogic.getCurrentCellCol());
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

    public void showEndGameMessage(char winner) {
        String message = (winner == '\0') ? "Ничья!" : "Победа игрока " + winner;
        //System.out.println(message);
        // Можно также показать графическое окно с сообщением
        JOptionPane.showMessageDialog(null, message, "Конец игры", JOptionPane.INFORMATION_MESSAGE);
    }

    public void returnToStartMenu() {
        // Переход в главное меню
        dispose(); // Закрываем текущее окно
    }

}
