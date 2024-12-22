import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:game.db";
    
    static {
        try {
            // Загружаем драйвер для SQLite
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Метод для создания таблицы для новой игры
    public static void createGameTable(String gameName) {
    try (Connection conn = DriverManager.getConnection(DB_URL);
         Statement stmt = conn.createStatement()) {
        String sql = "CREATE TABLE IF NOT EXISTS " + gameName + " (" +
                     "move_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "player CHAR(1)," +
                     "board_row INTEGER," +
                     "board_col INTEGER," +
                     "cell_row INTEGER," +
                     "cell_col INTEGER," +
                     "is_last_move BOOLEAN DEFAULT 0)"; // Добавляем поле is_last_move
        stmt.executeUpdate(sql);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Метод для сохранения хода игры
    public static void saveMove(String gameName, char player, int boardRow, int boardCol, int cellRow, int cellCol) {
        // Сначала устанавливаем старые ходы как не последние
        String updateSql = "UPDATE " + gameName + " SET is_last_move = 0 WHERE is_last_move = 1";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Теперь сохраняем новый ход с is_last_move = 1
        String sql = "INSERT INTO " + gameName + " (player, board_row, board_col, cell_row, cell_col, is_last_move) VALUES (?, ?, ?, ?, ?, 1)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(player));
            pstmt.setInt(2, boardRow);
            pstmt.setInt(3, boardCol);
            pstmt.setInt(4, cellRow);
            pstmt.setInt(5, cellCol);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Метод для загрузки истории ходов игры
    public static void loadGameMoves(String gameName, GameLogic gameLogic) {
        String sql = "SELECT * FROM " + gameName + " ORDER BY move_id";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                char player = rs.getString("player").charAt(0);
                int boardRow = rs.getInt("board_row");
                int boardCol = rs.getInt("board_col");
                int cellRow = rs.getInt("cell_row");
                int cellCol = rs.getInt("cell_col");

                // Загрузка каждого хода
                gameLogic.setCurrentPlayer(player);
                gameLogic.processMove(boardRow, boardCol, cellRow, cellCol, true);
                
            }
            System.out.println("Заполнение полей Хоста и Клиента прошли успешно");
        } catch (SQLException e) {
            System.out.println("Поля не заполнились. Ошибка");
            e.printStackTrace();
        }
    }

    
    // Метод для очистки таблицы игры
    public static void clearGameTable(String gameName) {
        String sql = "DELETE FROM " + gameName;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Метод для удаления таблицы игры
    public static void deleteGameTable(String gameName) {
        if (gameName == null || gameName.trim().isEmpty()) {
            System.out.println("Ошибка: имя таблицы не может быть пустым или null.");
            return;
        }
        // Оборачиваем имя таблицы в кавычки, если оно содержит специальные символы
        String sql = "DROP TABLE IF EXISTS \"" + gameName + "\"";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для загрузки всех сохраненных игр
    public static List<String> getSavedGames() throws SQLException {
        List<String> savedGames = new ArrayList<>();
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'";  // Извлекаем только реальные таблицы, исключая системные

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                savedGames.add(resultSet.getString("name"));
            }
        }
        return savedGames;
    }
}
