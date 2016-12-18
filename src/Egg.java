import java.awt.*;
import java.util.Random;

/**
 * Created by jianbojia on 12/16/16.
 */
public class Egg {
    int row, col;
    int w = Yard.BLOCK_SIZE;
    int h = Yard.BLOCK_SIZE;
    private static Random r = new Random();
    private Color color = Color.black.GREEN;

    public Egg(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Egg() {
        this(r.nextInt(Yard.ROWS - 3) , r.nextInt(Yard.COLS - 3));
    }

    public void reAppear() {
        this.row = r.nextInt(Yard.ROWS - 3);
        this.col = r.nextInt(Yard.COLS - 3);
    }
    public Rectangle getRect() {
        return new Rectangle(Yard.BLOCK_SIZE * col, Yard.BLOCK_SIZE * row, w, h);
    }

    public void  draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(color);
        g.fillOval(Yard.BLOCK_SIZE * col, Yard.BLOCK_SIZE * row, w, h);
        g.setColor(c);
        if (color == Color.GREEN) {
            color = Color.RED;
        } else {
            color = Color.GREEN;
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
