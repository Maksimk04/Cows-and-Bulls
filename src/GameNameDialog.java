import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class GameNameDialog extends JDialog {
    private JTextField nameField;

    public GameNameDialog(JFrame parentFrame) throws SQLException {
        super(parentFrame, "Создать игру", true);
        setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("Введите название игры:");
        nameField = new JTextField(20);
        JButton createButton = new JButton("Создать игру");

        // Получаем список сохранённых игр
        List<String> savedGamesList = DatabaseHelper.getSavedGames();

        // Создаем JComboBox с использованием массива строк
        JComboBox<String> savedGamesComboBox = new JComboBox<>(savedGamesList.toArray(new String[0]));

        JButton loadButton = new JButton("Загрузить игру");
        JButton deleteButton = new JButton("Удалить игру");

        // Загрузить выбранную игру
        loadButton.addActionListener(e -> {
            String selectedGame = (String) savedGamesComboBox.getSelectedItem();
            if (selectedGame != null) {
                new MainFrameOffline(selectedGame);  // Загрузить выбранную игру
                dispose();  // Закрыть диалог
            }
        });

        // Удалить выбранную игру
        deleteButton.addActionListener(e -> {
            String selectedGame = (String) savedGamesComboBox.getSelectedItem();
            if (selectedGame != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Вы уверены, что хотите удалить игру \"" + selectedGame + "\"?",
                        "Подтверждение удаления",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    // Удаляем таблицу из базы данных
                    DatabaseHelper.deleteGameTable(selectedGame);

                    // Обновляем список игр
                    try {
                        savedGamesList.clear();
                        savedGamesList.addAll(DatabaseHelper.getSavedGames());
                        savedGamesComboBox.setModel(new DefaultComboBoxModel<>(savedGamesList.toArray(new String[0])));
                        JOptionPane.showMessageDialog(this, "Игра \"" + selectedGame + "\" успешно удалена.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Ошибка при обновлении списка игр.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Выберите игру для удаления.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Создать новую игру
        createButton.addActionListener(e -> {
            String gameName = nameField.getText();
            if (gameName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Название игры не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else {
                // Создаем новую игру в базе данных
                DatabaseHelper.createGameTable(gameName);
                new MainFrameOffline(gameName);  // Открываем новую игру
                dispose();  // Закрываем диалог
            }
        });

        // Добавляем компоненты
        add(nameLabel);
        add(nameField);
        add(createButton);
        add(new JLabel("Существующие игры:"));
        add(savedGamesComboBox);
        add(loadButton);
        add(deleteButton);

        setSize(300, 300);
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }
}
