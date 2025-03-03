import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    //Game Loop Actions
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    //Snake Movement Controls
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_A && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_S && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_D && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    //Override Functions that don't need to do anything
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private static class Tile {
        int x;
        int y;
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int frameWidth;
    int frameHeight;
    int tileSize = 40;

    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random;

    //Game Logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        setPreferredSize(new Dimension(this.frameWidth, this.frameHeight));
        setBackground(new Color(18,77,22)); //Forest Green
        snakeHead = new Tile((frameWidth/tileSize)/2, (frameHeight/tileSize)/2);
        snakeBody = new ArrayList<Tile>();
        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        //Game Loop Refresh Rate
        gameLoop = new Timer(50,this);
        gameLoop.start();

        velocityX = 0;
        velocityY = 0;

        addKeyListener(this);
        setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //Grid
        for (int i = 0; i < frameWidth/tileSize; i++) {
            g.drawLine(i*tileSize, 0, i*tileSize, frameHeight);
            g.drawLine(0,i*tileSize, frameWidth, i*tileSize);
        }

        //Food
        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        //Snake Head
        g.setColor(Color.orange);
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);

        //Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        //Score/Game Over Text
        g.setFont(new Font("Arial", Font.BOLD,18));
        if (gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over! Final Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        } else {
            g.drawString("Score:" + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    //Food Generator/Replacer
    public void placeFood() {
        food.x = random.nextInt(frameWidth/tileSize);
        food.y = random.nextInt(frameHeight/tileSize);
    }

    public void move() {
        //Food Eating
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //Snake Body Movement
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake Movement
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //Game Over Condition: Head Collides with Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if(collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        //Game Over Condition: Head Collides with Borders
        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize >= frameWidth || snakeHead.y*tileSize < 0 || snakeHead.y*tileSize >= frameHeight) {
            gameOver = true;
        }
    }

    //Collision Conditions
    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
}
