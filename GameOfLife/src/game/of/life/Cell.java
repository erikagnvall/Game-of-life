package game.of.life;

import java.awt.*;

/**
 * Cell-klass, varje cell på canvasen är en instans av den här klassen
 * 
 * @author Erik
 */
public class Cell {
	/**
	 * Raden och kolumnen på cellen
	 */
	private int row, col;
	/**
	 * Storleken på cellen som en konstant
	 */
	public static final int SIZE = 10;
	/**
	 * Antal grannar för cellen
	 */
	private int neighbour;
	/**
	 * Boolean som indikerar som cellen lever eller inte
	 */
	private boolean alive = false;


	/**
	 * @param r raden som cellen ska visas på
	 * @param c kolumnen som cellen ska visas på
	 */
	public Cell (int r, int c) {
		this.row = r;
		this.col = c;
		this.neighbour = 0;
	}


	/**
	 * Sätter cellen till att vara levande 
	 * @param a boolean, true för levande
	 */
	public void setAlive(boolean a) {
		this.alive = a;
	}


	/**
	 * Hämtar cellens status, levande eller död
	 * @return boolean true eller false vid levande eller död
	 */
	public boolean getAlive() {
		return this.alive;
	}


	/**
	 * Ange nytt antal grannar
	 * @param n nytt antal grannar
	 */
	public void setNeighbours(int n) {
		this.neighbour = n;
	}


	/** Paint-metoden som ritar ut cellen på CellCanvasen
	 *  @param g Graphics-objekt från CellCanvasen
	 */
	public void paint(Graphics g) {
		// Kollar om cellen är levande eller inte för att rita ut olika färger
		if (this.alive) {
			// alive = true, färgen sätts till gul
			g.setColor(new Color(0xffee00));
			g.fillRect(this.col*10, this.row*10, SIZE, SIZE);
		}
		else {
			// alive = false, färden sätts till ljusgrå
			g.setColor(new Color(0xcccccc));
			g.drawRect(this.col*10, this.row*10, SIZE, SIZE);
		}
	}
}