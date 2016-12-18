import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by jianbojia on 12/16/16.
 */
public class Snake {
    Node head = null;
    Node tail = null;
    int size = 0;

    private Node n = new Node(20, 30, Direction.L);

    private Yard yard;

    public Snake(Yard yard) {
        head = n;
        tail = n;
        size = 1;
        this.yard = yard;
    }

    public void addToTail() {
        Node node = null;
        switch (tail.dir) {
            case L:
                node = new Node(tail.row, tail.col + 1, tail.dir);
                break;
            case U:
                node = new Node(tail.row + 1, tail.col, tail.dir);
                break;
            case R:
                node = new Node(tail.row, tail.col - 1, tail.dir);
                break;
            case D:
                node = new Node(tail.row - 1, tail.col, tail.dir);
                break;
        }

        tail.next = node;
        node.prev = tail;
        tail = node;
        size++;
    }

    public void addToHead() {
        Node node = null;
        switch (tail.dir) {
            case L:
                node = new Node(head.row, head.col - 1, head.dir);
                break;
            case U:
                node = new Node(head.row - 1, head.col, head.dir);
                break;
            case R:
                node = new Node(head.row, head.col + 1, head.dir);
                break;
            case D:
                node = new Node(head.row + 1, head.col, head.dir);
                break;
        }

        node.next = head;
        head.prev = node;
        head = node;
        size++;
    }

    public void draw(Graphics g) {
        if (size <= 0) {
            return;
        }
        move();

        for (Node n = head; n != null; n = n.next) {
            n.draw(g);
        }

    }

    private void move() {
        addToHead();
        deleteTail();
        checkDeath();
    }

    private void checkDeath() {

        // check boundary
        if (head.row < 2 || head.col < 0 || head.row > yard.ROWS || head.col > yard.COLS) {
            yard.stop();
        }

        // check head overlap with tail or body
        for (Node n = head.next; n != null; n = n.next) {
            if (head.row == n.row && head.col == n.col) {
                yard.stop();
            }
        }
    }

    private void deleteTail() {
        if (size == 0) {
            return;
        }

        tail = tail.prev;
        tail.next = null;
    }

    private class Node {
        int w = yard.BLOCK_SIZE;
        int h = yard.BLOCK_SIZE;
        int row, col;
        Direction dir = Direction.L;
        Node next = null;
        Node prev = null;

        Node(int row, int col, Direction dir) {
            this.row = row;
            this.col = col;
            this.dir = dir;
        }

        void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.black);
            g.fillRect(yard.BLOCK_SIZE * col, yard.BLOCK_SIZE * row, w, h);
            g.setColor(c);
        }
    }

    public void eat(Egg egg) {
        if (this.getRect().intersects(egg.getRect())) {
            egg.reAppear();
            this.addToHead();
            yard.setScore(yard.getScore() + 5);
        }
    }

    private Rectangle getRect() {
        return new Rectangle(Yard.BLOCK_SIZE * head.col, Yard.BLOCK_SIZE * head.row, head.w, head.h);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                if (head.dir != Direction.R)
                    head.dir = Direction.L;
                break;
            case KeyEvent.VK_UP:
                if (head.dir != Direction.D)
                    head.dir = Direction.U;
                break;
            case KeyEvent.VK_RIGHT:
                if (head.dir != Direction.L)
                    head.dir = Direction.R;
                break;
            case KeyEvent.VK_DOWN:
                if (head.dir != Direction.U)
                    head.dir = Direction.D;
                break;
        }
    }
}
