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
        setTitle("������� ����");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        // ������ ��� �������� �������
        JButton createServerButton = new JButton("������� ������");
        createServerButton.addActionListener(e -> openServerSetupWindow());

        // ������ ��� ����������� � �������
        JButton joinServerButton = new JButton("������������ � �������");
        joinServerButton.addActionListener(e -> openClientSetupWindow());

        // ������ "�����"
        JButton backButton = new JButton("�����");
        backButton.addActionListener(e -> {
            dispose(); // ������� ������� ����
            new StartMenu(); // ��������� � ������� ����
        });

        add(createServerButton);
        add(joinServerButton);
        add(backButton);

        setVisible(true);
    }

    // ���� ��������� �������
    private void openServerSetupWindow() {
        JFrame serverSetupFrame = new JFrame("��������� �������");
        serverSetupFrame.setSize(400, 400);
        serverSetupFrame.setLayout(new GridLayout(6, 1));

        JLabel ipLabel = new JLabel("��� IP: " + getLocalIpAddress());
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel portLabel = new JLabel("������� ����:");
        JTextField portField = new JTextField();

        // ������ ������ ��� ������ � ������
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new FlowLayout());

        JLabel gameLabel = new JLabel("����:");
        JComboBox<String> savedGamesComboBox = new JComboBox<>();
        JTextField newGameField = new JTextField(10);
        JButton refreshButton = new JButton("��������");
        JButton createButton = new JButton("�������");
        JButton deleteButton = new JButton("�������");

        // �������� ������ ���
        refreshButton.addActionListener(e -> {
            try {
                List<String> savedGames = DatabaseHelper.getSavedGames();
                savedGamesComboBox.setModel(new DefaultComboBoxModel<>(savedGames.toArray(new String[0])));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(serverSetupFrame, "������ �������� ������ ���.", "������", JOptionPane.ERROR_MESSAGE);
            }
        });
        refreshButton.doClick();

        // �������� ����� ����
        createButton.addActionListener(e -> {
            String gameName = newGameField.getText().trim();
            if (gameName.isEmpty()) {
                JOptionPane.showMessageDialog(serverSetupFrame, "������� �������� ����.", "������", JOptionPane.ERROR_MESSAGE);
            } else {
                    DatabaseHelper.createGameTable(gameName);
                    JOptionPane.showMessageDialog(serverSetupFrame, "���� \"" + gameName + "\" �������.");
                    refreshButton.doClick();
            }
        });

        // �������� ��������� ����
        deleteButton.addActionListener(e -> {
            String selectedGame = (String) savedGamesComboBox.getSelectedItem();
            if (selectedGame == null) {
                JOptionPane.showMessageDialog(serverSetupFrame, "�������� ���� ��� ��������.", "������", JOptionPane.ERROR_MESSAGE);
            } else {
                int confirm = JOptionPane.showConfirmDialog(
                    serverSetupFrame,
                    "�� �������, ��� ������ ������� ���� \"" + selectedGame + "\"?",
                    "������������� ��������",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                        DatabaseHelper.deleteGameTable(selectedGame);
                        JOptionPane.showMessageDialog(serverSetupFrame, "���� \"" + selectedGame + "\" �������.");
                        refreshButton.doClick();
                }
            }
        });

        // ������ ������� �������
        JButton startServerButton = new JButton("��������� ������");
        startServerButton.addActionListener(e -> {
            try {
                int port = Integer.parseInt(portField.getText().trim());
                String selectedGame = (String) savedGamesComboBox.getSelectedItem();
                if (selectedGame == null || selectedGame.isEmpty()) {
                    JOptionPane.showMessageDialog(serverSetupFrame, "�������� ���� ��� ������� �������.", "������", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                new MainFrameServer(selectedGame, port); // ������� ��� � ����
                serverSetupFrame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(serverSetupFrame, "������������ ����.", "������", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(serverSetupFrame, "������ ������� �������.", "������", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ��������� ���������� � ����
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



    // ���� ����������� � �������
    private void openClientSetupWindow() {
        JFrame clientSetupFrame = new JFrame("����������� � �������");
        clientSetupFrame.setSize(400, 200);
        clientSetupFrame.setLayout(new GridLayout(4, 1));

        JLabel ipLabel = new JLabel("������� IP �������:");
        JTextField ipField = new JTextField();

        JLabel portLabel = new JLabel("������� ����:");
        JTextField portField = new JTextField();

        JButton connectButton = new JButton("������������");
        connectButton.addActionListener(e -> {
        try {
            String serverIp = ipField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());
            new MainFrameClient(serverIp, port); // ������� ip � ����
            clientSetupFrame.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(clientSetupFrame, "������ ����������� � �������.", "������", JOptionPane.ERROR_MESSAGE);
        }
    });


        clientSetupFrame.add(ipLabel);
        clientSetupFrame.add(ipField);
        clientSetupFrame.add(portLabel);
        clientSetupFrame.add(portField);
        clientSetupFrame.add(connectButton);

        clientSetupFrame.setVisible(true);
    }

    // ����� ��� ��������� ���������� IP-������
    private String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "�� ������� ���������� IP-�����";
        }
    }
}
