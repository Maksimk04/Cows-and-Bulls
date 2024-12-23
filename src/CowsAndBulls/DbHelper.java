package CowsAndBulls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHelper {
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
                         "seq CHAR(4)," +
                         "guess CHAR(4)," +
                         "cows INTEGER," +
                         "bulls INTEGER," +
                         "is_winning BOOLEAN DEFAULT 0)"; // Добавляем поле is_last_move
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
        // Метод для сохранения хода игры
    public static void saveMove(String gameName, char player, String seq, String guess,
            int cows, int bulls, boolean isWinning) {
        // Сначала устанавливаем старые ходы как не последние
//        String updateSql = "UPDATE " + gameName + " SET is_last_move = 0 WHERE is_last_move = 1";
//        try (Connection conn = DriverManager.getConnection(DB_URL);
//             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        // Теперь сохраняем новый ход с is_last_move = 1
        String sql = "INSERT INTO " + gameName + " (player, seq, guess, cows, bulls, is_winning) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(player));
            pstmt.setString(2, seq);
            pstmt.setString(3, guess);
            pstmt.setInt(4, cows);
            pstmt.setInt(5, bulls);
            if (bulls == 4) pstmt.setBoolean(6, true);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
