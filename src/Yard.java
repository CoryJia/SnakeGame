import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by jianbojia on 12/16/16.
 */
public class Yard extends Frame {

    private boolean gameOver = false;
    PaintThread paintThread = new PaintThread();

    public static final int ROWS = 30;
    public static final int COLS = 30;
    public static final int BLOCK_SIZE = 15;

    private int score = 0;

    Snake snake = new Snake(this);
    Egg egg = new Egg();

    Image offScreenImage = null;

    public void launch() {
        this.setLocation(200, 200);
        this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setVisible(true);
        this.addKeyListener(new KeyMonitor());

        new Thread(new PaintThread()).start();
    }

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        g.setColor(Color.DARK_GRAY);

        for (int i = 1; i < ROWS; i++) {
            g.drawLine(0, BLOCK_SIZE * i, COLS * BLOCK_SIZE, BLOCK_SIZE * i);
        }

        for (int i = 1; i < COLS; i++) {
            g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, ROWS * BLOCK_SIZE);
        }

        g.setColor(Color.YELLOW);
        g.drawString("Score:" + score, 10, 60);

        if (gameOver) {
            g.setFont(new Font("Verdana", Font.BOLD | Font.CENTER_BASELINE, 30));
            g.drawString("Game Over", 120, 180);

            paintThread.pause();
        }

        g.setColor(c);

        snake.eat(egg);
        egg.draw(g);
        snake.draw(g);

    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        }

        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);

    }

    private class PaintThread implements Runnable {
        private boolean running = true;
        private boolean pause = false;

        @Override
        public void run() {
            while (running) {
                if(pause) continue;
                else repaint();
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void pause() {
            this.pause = true;
        }

        public void reStart() {
            this.pause = false;
            snake = new Snake(Yard.this);
            egg = new Egg();
            score = 0;
            gameOver = false;
        }

        public void gameOver() {
            running = false;
        }
    }

    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_F2) {
                paintThread.reStart();
            }
            snake.keyPressed(e);
        }
    }

    public void stop() {
        gameOver = true;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static void main(String[] args) {
        new Yard().launch();
    }

}
