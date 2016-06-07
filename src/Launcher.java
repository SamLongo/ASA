

import java.awt.Toolkit;
import javax.swing.JFrame;

public class Launcher extends JFrame {
    
    public static JFrame F;
    
    public static void main(String[] args) {
        F = new JFrame();
        Screen ScreenObject = new Screen();
        F.add(ScreenObject);
        F.setUndecorated(true);
        F.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        F.setVisible(true);
        F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
