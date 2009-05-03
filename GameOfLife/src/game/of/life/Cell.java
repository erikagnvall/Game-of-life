package game.of.life;

import java.awt.*;

public class Cell {
	private int col, row, size = 10;
	private int neighbour;
	private boolean alive = false, entered = false;

	public Cell (int col, int row) {
		this.col = col;
		this.row = row;
		this.neighbour = 0;
	}

	public void paint(Graphics g) {
		if (alive) {
			g.setColor(new Color(0xffee00));
			g.fillRect(col*10, row*10, size, size);
		}
		else {
			g.setColor(new Color(0xcccccc));
			g.drawRect(col*10, row*10, size, size);
		}
	}

	public void setAlive(boolean a) {
		this.alive = a;
	}

	public boolean click(int x, int y, CellCanvas cc) {
		if (this.inside(x,y) && !this.entered) {
			this.alive = !this.alive;
			this.entered = true;
			cc.repaint();
			return true;
		}
		else if (x+10 < this.col*10 && x+10 > (this.col*10)+this.size && y+10 < this.row*10 && y+10 > (this.row*10)+this.size) {
			this.entered = false;
			cc.repaint();
			return true;
		}
		else {
			return false;
		}
	}

	public boolean inside(int x, int y) {
		if (x > this.col*10 && x < (this.col*10)+this.size && y > this.row*10 && y < (this.row*10)+this.size) {
			return true;
		}
		else {
			return false;
		}
	}
}