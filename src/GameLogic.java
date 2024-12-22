public class GameLogic {
    public char currentPlayer = 'X';
    
    public MainFrameOffline mainFrameOffline;
    public MainFrameServer mainFrameServer;
    public MainFrameClient mainFrameClient;
    
    private int nextBoardRow = -1, nextBoardCol = -1;
    private boolean gameOver = false;
    public char[][] bigBoard = new char[3][3];
    
    private int currentBoardRow;
    private int currentBoardCol;
    private int currentCellRow;
    private int currentCellCol;

    public int getCurrentBoardRow() {
        return currentBoardRow;
    }

    public int getCurrentBoardCol() {
        return currentBoardCol;
    }

    public int getCurrentCellRow() {
        return currentCellRow;
    }

    public int getCurrentCellCol() {
        return currentCellCol;
    }
    
    public void setMFO(MainFrameOffline mainFrameOffline) {
        this.mainFrameOffline = mainFrameOffline;
    }
    
    public void setMFS(MainFrameServer mainFrameServer) {
        this.mainFrameServer = mainFrameServer;
    }
    
    public void setMFC(MainFrameClient mainFrameClient) {
        this.mainFrameClient = mainFrameClient;
    }
    
    public GameLogic() {
        // Конструктор
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void setCurrentPlayer(char curPlayer) {
        currentPlayer = curPlayer;
    }

    public boolean isBoardFinished(int boardRow, int boardCol) {
        return bigBoard[boardRow][boardCol] != '\0';
    }
    
    public void endGame(char winner) {
        gameOver = true;

        // Отправляем сообщение о завершении игры всем игрокам
        if (mainFrameServer != null) {
            mainFrameServer.showEndGameMessage(winner);  // Сервер уведомляет о завершении игры
            mainFrameServer.closeConnection();  // Закрываем соединение с клиентом
        } else if (mainFrameClient != null) {
            mainFrameClient.showEndGameMessage(winner);  // Клиент уведомляет о завершении игры
            mainFrameClient.closeConnection();  // Закрываем соединение с сервером
        }
        // Возвращаем игроков в главное меню
        showStartMenu();
    }

    private void showStartMenu() {
        // Тут логика для перехода в главное меню
        if (mainFrameServer != null) {
            mainFrameServer.returnToStartMenu();
        } else if (mainFrameClient != null) {
            mainFrameClient.returnToStartMenu();
        }
    }

    public void processMove(int boardRow, int boardCol, int cellRow, int cellCol, boolean sendBack) {
        if (gameOver) return;

        currentBoardRow = boardRow;
        currentBoardCol = boardCol;
        currentCellRow = cellRow;
        currentCellCol = cellCol;

        // Определяем, какой объект использовать
        if (mainFrameOffline != null) {
            processMoveForFrame(mainFrameOffline, boardRow, boardCol, cellRow, cellCol);
        } else if (mainFrameServer != null) {
            // Для сервера отправляем ход только если нужно
            if (sendBack) {
                mainFrameServer.sendMove(boardRow, boardCol, cellRow, cellCol, currentPlayer);
            }
            processMoveForFrame(mainFrameServer, boardRow, boardCol, cellRow, cellCol);
        } else if (mainFrameClient != null) {
            // Для клиента отправляем ход только если нужно
            if (sendBack) {
                mainFrameClient.sendMoveToServer(boardRow, boardCol, cellRow, cellCol, currentPlayer);
            }
            processMoveForFrame(mainFrameClient, boardRow, boardCol, cellRow, cellCol);
        }
    }

    private void processMoveForFrame(MainFrameBase frame, int boardRow, int boardCol, int cellRow, int cellCol) {
        frame.boards[boardRow][boardCol].getCell(cellRow, cellCol).setValue(currentPlayer);
        char winner = checkSmallBoardWin(frame, boardRow, boardCol);
        if (winner != '\0') {
            frame.boards[boardRow][boardCol].setWinnerColor(winner);
            bigBoard[boardRow][boardCol] = winner;

            if (checkBigBoardWin()) {
                gameOver = true;
                frame.announceWinner(currentPlayer);
                return;
            }
        }
        togglePlayer();

        if (!frame.boards[cellRow][cellCol].isFull() && !isBoardFinished(cellRow, cellCol)) {
            nextBoardRow = cellRow;
            nextBoardCol = cellCol;
        } else {
            nextBoardRow = -1;
            nextBoardCol = -1;
        }

        frame.updateBoardHighlights(nextBoardRow, nextBoardCol);
    }

    public void togglePlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public boolean checkBigBoardWin() {
        for (int i = 0; i < 3; i++) {
            if (bigBoard[i][0] != '\0' && bigBoard[i][0] == bigBoard[i][1] && bigBoard[i][1] == bigBoard[i][2]) {
                endGame(bigBoard[i][0]);  // Завершаем игру с победителем
                return true;
            }
            if (bigBoard[0][i] != '\0' && bigBoard[0][i] == bigBoard[1][i] && bigBoard[1][i] == bigBoard[2][i]) {
                endGame(bigBoard[0][i]);  // Завершаем игру с победителем
                return true;
            }
        }

        if (bigBoard[0][0] != '\0' && bigBoard[0][0] == bigBoard[1][1] && bigBoard[1][1] == bigBoard[2][2]) {
            endGame(bigBoard[0][0]);  // Завершаем игру с победителем
            return true;
        }

        if (bigBoard[0][2] != '\0' && bigBoard[0][2] == bigBoard[1][1] && bigBoard[1][1] == bigBoard[2][0]) {
            endGame(bigBoard[0][2]);  // Завершаем игру с победителем
            return true;
        }

        // Проверка на ничью
        boolean isDraw = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (bigBoard[i][j] == '\0') {
                    isDraw = false;
                    break;
                }
            }
        }

        if (isDraw) {
            endGame('\0');  // Ничья
        }

        return false;
    }

    public char checkSmallBoardWin(int boardRow, int boardCol) {
        if (mainFrameOffline != null) {
            return checkSmallBoardWin(mainFrameOffline, boardRow, boardCol);
        } else if (mainFrameServer != null) {
            return checkSmallBoardWin(mainFrameServer, boardRow, boardCol);
        } else if (mainFrameClient != null) {
            return checkSmallBoardWin(mainFrameClient, boardRow, boardCol);
        }
        return '\0';
    }

    private char checkSmallBoardWin(MainFrameBase frame, int boardRow, int boardCol) {
        char[][] board = frame.boards[boardRow][boardCol].getBoard();
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return board[i][0];
        }
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != '\0' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return board[0][i];
        }
        if (board[0][0] != '\0' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return board[0][0];
        if (board[0][2] != '\0' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return board[0][2];
        return '\0';
    }
}

