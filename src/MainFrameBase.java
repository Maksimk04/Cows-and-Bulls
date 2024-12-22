import javax.swing.*;
import java.awt.*;

public class MainFrameBase extends JFrame {
    public Board[][] boards = new Board[3][3];
    
    public void announceWinner(char winner){}
    public void updateBoardHighlights(int nextBoardRow, int nextBoardCol) {}
}
