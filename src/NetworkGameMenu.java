import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.*;
import java.util.List;

public class NetworkGameMenu extends JFrame {
    
    public NetworkGameMenu() {
        setTitle("Сетевая игра");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        // Кнопка для создания сервера
        JButton createServerButton = new JButton("Создать сервер");
        createServerButton.addActionListener(e -> openServerSetupWindow());

        // Кнопка для подключения к серверу
        JButton joinServerButton = new JButton("Подключиться к серверу");
        joinServerButton.addActionListener(e -> openClientSetupWindow());

        // Кнопка "Назад"
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> {
            dispose(); // Закрыть текущее окно
            new StartMenu(); // Вернуться в главное меню
        });

        add(createServerButton);
        add(joinServerButton);
        add(backButton);

        setVisible(true);
    }

    // Окно настройки сервера
    private void openServerSetupWindow() {
        JFrame serverSetupFrame = new JFrame("Настройка сервера");
        serverSetupFrame.setSize(400, 400);
        serverSetupFrame.setLayout(new GridLayout(6, 1));

        JLabel ipLabel = new JLabel("Ваш IP: " + getLocalIpAddress());
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel portLabel = new JLabel("Введите порт:");
        JTextField portField = new JTextField();

        // Создаём панель для работы с играми
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new FlowLayout());

        JLabel gameLabel = new JLabel("Игры:");
        JComboBox<String> savedGamesComboBox = new JComboBox<>();
        JTextField newGameField = new JTextField(10);
        JButton refreshButton = new JButton("Обновить");
        JButton createButton = new JButton("Создать");
        JButton deleteButton = new JButton("Удалить");

        // Загрузка списка игр
        refreshButton.addActionListener(e -> {
            try {
                List<String> savedGames = DatabaseHelper.getSavedGames();
                savedGamesComboBox.setModel(new DefaultComboBoxModel<>(savedGames.toArray(new String[0])));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(serverSetupFrame, "Ошибка загрузки списка игр.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        refreshButton.doClick();

        // Создание новой игры
        createButton.addActionListener(e -> {
            String gameName = newGameField.getText().trim();
            if (gameName.isEmpty()) {
                JOptionPane.showMessageDialog(serverSetupFrame, "Введите название игры.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else {
                    DatabaseHelper.createGameTable(gameName);
                    JOptionPane.showMessageDialog(serverSetupFrame, "Игра \"" + gameName + "\" создана.");
                    refreshButton.doClick();
            }
        });

        // Удаление выбранной игры
        deleteButton.addActionListener(e -> {
            String selectedGame = (String) savedGamesComboBox.getSelectedItem();
            if (selectedGame == null) {
                JOptionPane.showMessageDialog(serverSetupFrame, "Выберите игру для удаления.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else {
                int confirm = JOptionPane.showConfirmDialog(
                    serverSetupFrame,
                    "Вы уверены, что хотите удалить игру \"" + selectedGame + "\"?",
                    "Подтверждение удаления",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                        DatabaseHelper.deleteGameTable(selectedGame);
                        JOptionPane.showMessageDialog(serverSetupFrame, "Игра \"" + selectedGame + "\" удалена.");
                        refreshButton.doClick();
                }
            }
        });

        // Кнопка запуска сервера
        JButton startServerButton = new JButton("Запустить сервер");
        startServerButton.addActionListener(e -> {
            try {
                int port = Integer.parseInt(portField.getText().trim());
                String selectedGame = (String) savedGamesComboBox.getSelectedItem();
                if (selectedGame == null || selectedGame.isEmpty()) {
                    JOptionPane.showMessageDialog(serverSetupFrame, "Выберите игру для запуска сервера.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                new MainFrameServer(selectedGame, port); // передаём имя и порт
                serverSetupFrame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(serverSetupFrame, "Некорректный порт.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(serverSetupFrame, "Ошибка запуска сервера.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Добавляем компоненты в окно
        gamePanel.add(gameLabel);
        gamePanel.add(savedGamesComboBox);
        gamePanel.add(newGameField);
        gamePanel.add(createButton);
        gamePanel.add(deleteButton);
        gamePanel.add(refreshButton);

        serverSetupFrame.add(ipLabel);
        serverSetupFrame.add(portLabel);
        serverSetupFrame.add(portField);
        serverSetupFrame.add(gamePanel);
        serverSetupFrame.add(startServerButton);

        serverSetupFrame.setVisible(true);
    }



    // Окно подключения к серверу
    private void openClientSetupWindow() {
        JFrame clientSetupFrame = new JFrame("Подключение к серверу");
        clientSetupFrame.setSize(400, 200);
        clientSetupFrame.setLayout(new GridLayout(4, 1));

        JLabel ipLabel = new JLabel("Введите IP сервера:");
        JTextField ipField = new JTextField();

        JLabel portLabel = new JLabel("Введите порт:");
        JTextField portField = new JTextField();

        JButton connectButton = new JButton("Подключиться");
        connectButton.addActionListener(e -> {
        try {
            String serverIp = ipField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());
            new MainFrameClient(serverIp, port); // передаём ip и порт
            clientSetupFrame.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(clientSetupFrame, "Ошибка подключения к серверу.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    });


        clientSetupFrame.add(ipLabel);
        clientSetupFrame.add(ipField);
        clientSetupFrame.add(portLabel);
        clientSetupFrame.add(portField);
        clientSetupFrame.add(connectButton);

        clientSetupFrame.setVisible(true);
    }

    // Метод для получения локального IP-адреса
    private String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "Не удалось определить IP-адрес";
        }
    }
}
