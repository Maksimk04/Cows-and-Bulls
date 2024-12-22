import CowsAndBulls.MainMenu;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartMenu extends JFrame {
    public StartMenu() {
        setTitle("���� ������");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        JButton localGameButton = new JButton("��������� ����");
        localGameButton.addActionListener(e -> {
            dispose();
            try {
                new GameNameDialog(this);
            } catch (SQLException ex) {
                Logger.getLogger(StartMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        JButton networkGameButton = new JButton("������� ����");
        networkGameButton.addActionListener(e -> {
            dispose();
            new MainMenu();
        });

        add(localGameButton);
        add(networkGameButton);

        setSize(300, 200);
        setVisible(true);
    }

    public static void main(String[] args) {
        new StartMenu();
    }
}
