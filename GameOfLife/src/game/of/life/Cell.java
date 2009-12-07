package game.of.life;

import java.awt.*;

/**
 * Cell-klass, varje cell p� canvasen �r en instans av den h�r klassen
 * 
 * @author Erik
 */
public class Cell {
	/**
	 * Raden och kolumnen p� cellen
	 */
	public final int row, col;
	/**
	 * Storleken p� cellen som en konstant
	 */
	public static final int SIZE = 10;
	/**
	 * Antal grannar f�r cellen
	 */
	public int neighbour;
	/**
	 * Boolean som indikerar som cellen lever eller inte
	 */
	private boolean alive = false;


	/**
	 * @param r raden som cellen ska visas p�
	 * @param c kolumnen som cellen ska visas p�
	 */
	public Cell (int r, int c) {
		this.row = r;
		this.col = c;
		this.neighbour = 0;
	}


	/**
	 * S�tter cellen till att vara levande 
	 * @param a boolean, true f�r levande
	 */
	public void setAlive(boolean a) {
		this.alive = a;
	}


	/**
	 * H�mtar cellens status, levande eller d�d
	 * @return boolean true eller false vid levande eller d�d
	 */
	public boolean getAlive() {
		return this.alive;
	}


	/** Paint-metoden som ritar ut cellen p� CellCanvasen
	 *  @param g Graphics-objekt fr�n CellCanvasen
	 */
	public void paint(Graphics g) {
		// Kollar om cellen �r levande eller inte f�r att rita ut olika f�rger
		if (this.alive) {
			// alive = true, f�rgen s�tts till gul
			g.setColor(new Color(0xffee00));
			g.fillRect(this.col*10, this.row*10, SIZE, SIZE);
		}
		else {
			// alive = false, f�rden s�tts till ljusgr�
			g.setColor(new Color(0xcccccc));
			g.drawRect(this.col*10, this.row*10, SIZE, SIZE);
		}
	}
}