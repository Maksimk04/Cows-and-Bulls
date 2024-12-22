import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class GameNameDialog extends JDialog {
    private JTextField nameField;

    public GameNameDialog(JFrame parentFrame) throws SQLException {
        super(parentFrame, "������� ����", true);
        setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("������� �������� ����:");
        nameField = new JTextField(20);
        JButton createButton = new JButton("������� ����");

        // �������� ������ ���������� ���
        List<String> savedGamesList = DatabaseHelper.getSavedGames();

        // ������� JComboBox � �������������� ������� �����
        JComboBox<String> savedGamesComboBox = new JComboBox<>(savedGamesList.toArray(new String[0]));

        JButton loadButton = new JButton("��������� ����");
        JButton deleteButton = new JButton("������� ����");

        // ��������� ��������� ����
        loadButton.addActionListener(e -> {
            String selectedGame = (String) savedGamesComboBox.getSelectedItem();
            if (selectedGame != null) {
                new MainFrameOffline(selectedGame);  // ��������� ��������� ����
                dispose();  // ������� ������
            }
        });

        // ������� ��������� ����
        deleteButton.addActionListener(e -> {
            String selectedGame = (String) savedGamesComboBox.getSelectedItem();
            if (selectedGame != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "�� �������, ��� ������ ������� ���� \"" + selectedGame + "\"?",
                        "������������� ��������",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    // ������� ������� �� ���� ������
                    DatabaseHelper.deleteGameTable(selectedGame);

                    // ��������� ������ ���
                    try {
                        savedGamesList.clear();
                        savedGamesList.addAll(DatabaseHelper.getSavedGames());
                        savedGamesComboBox.setModel(new DefaultComboBoxModel<>(savedGamesList.toArray(new String[0])));
                        JOptionPane.showMessageDialog(this, "���� \"" + selectedGame + "\" ������� �������.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "������ ��� ���������� ������ ���.", "������", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "�������� ���� ��� ��������.", "������", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ������� ����� ����
        createButton.addActionListener(e -> {
            String gameName = nameField.getText();
            if (gameName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "�������� ���� �� ����� ���� ������.", "������", JOptionPane.ERROR_MESSAGE);
            } else {
                // ������� ����� ���� � ���� ������
                DatabaseHelper.createGameTable(gameName);
                new MainFrameOffline(gameName);  // ��������� ����� ����
                dispose();  // ��������� ������
            }
        });

        // ��������� ����������
        add(nameLabel);
        add(nameField);
        add(createButton);
        add(new JLabel("������������ ����:"));
        add(savedGamesComboBox);
        add(loadButton);
        add(deleteButton);

        setSize(300, 300);
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }
}
