import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int frameWidth = 1200;
        int frameHeight = 1200;

        JFrame frame = new JFrame("Snake");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setSize(frameWidth,frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(frameWidth, frameHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}