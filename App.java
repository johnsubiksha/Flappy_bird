import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        
        int width = 978;
        int height = 550;

        JFrame frame = new JFrame("Flappy Bird Game");
        frame.setSize(width,height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        flappyBird fb = new flappyBird();
        frame.add(fb);
        frame.pack();
        fb.requestFocus();

        frame.setVisible(true);

    }
}
