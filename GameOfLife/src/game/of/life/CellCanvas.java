package game.of.life;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import java.util.Iterator;

public class CellCanvas extends Canvas implements KeyListener, MouseListener, MouseMotionListener {
	/**
	 * Ignorera
	 */
	private static final long serialVersionUID = -3166382484935617668L;

	/**
	 * Antalet rader och kolumner
	 */
	private int nRows = 26, nCols = 44, width, height;

	/**
	 *  Cellerna som ett multidimensionell fält
	 */
	private Cell cells[][] = new Cell[nRows][nCols];

	/**
	 *  För- och bakgrundsfärg
	 */
	private Color fcolor = new Color(0x999999);
	private Color bcolor = new Color(0xcccccc);

	/**
	 * Hash-tabell att lagra levande celler i
	 */
	private Vector<Cell> livingCells, next;

	/**
	 * Sparad cellstatus för den cellen man klickade på
	 */
	private boolean savedCellState;

	/**
	 *  Buffertbilden
	 */
	Image bufferedImage = null;

	/**
	 *  Ritytan koppad till buffertbilden
	 */
	Graphics gImage;


	/**
	 * Konstruktor för CellCanvas
	 * @param w integer med bredden
	 * @param h integer med höjden
	 */
	public CellCanvas(int w, int h) {
		this.width = w;
		this.height = h;
		this.setBackground(fcolor);
		this.setForeground(bcolor);
		
		this.livingCells = new Vector<Cell>();
		this.livingCells.clear();

		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				cells[i][j] = new Cell(i,j);
			}
		}

        this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
	}


	/**
	 * @see java.awt.Canvas#paint(Graphics)
	 */
	public void paint (Graphics g) {
		Iterator<Cell> i = this.livingCells.iterator();
		Cell c;
		while ( i.hasNext() ) {
			c = i.next();
			c.paint(g);
		}
		
		g.setColor(new Color(0xcccccc));
		for (int x = 0; x < width; x++) {
			g.drawLine(x*Cell.SIZE, 0, x*Cell.SIZE, Cell.SIZE*width);
		}
		for (int y = 0; y < height; y++) {
			g.drawLine(0, y*Cell.SIZE, Cell.SIZE*height, y*Cell.SIZE);
		}
	}


	/**
	 * @see java.awt.Canvas#update(Graphics)
	 */
	public void update(Graphics g){
		/** Double buffering to prevent flickering */
		this.bufferedImage = createImage(this.width, this.height);
		this.gImage = this.bufferedImage.getGraphics();
		this.gImage.clearRect(0, 0, this.width, this.height);
		paint(this.gImage);
		g.drawImage(this.bufferedImage, 0, 0, null);
	}


	/**
	 * Rita i en speciell cell
	 * @param x x-kordinaten för cellen
	 * @param y y-kordinaten för cellen
	 */
	public void draw(int x, int y) {
		try {
			Cell c = cells[y/Cell.SIZE][x/Cell.SIZE];
			c.setAlive(!this.savedCellState);
			if (!this.savedCellState) {
				this.livingCells.add(c);
			}
			else {
				this.livingCells.remove(c);
			}
		}
		catch (ArrayIndexOutOfBoundsException a) {
			// /ignore
		}
	}


	/**
	 * Hämtar cellen på det givna x- och y-värdet
	 * @param x x-kordinat att hämta cell på
	 * @param y y-kordinat att hämta cell på
	 * @return true eller false vid levande eller död
	 */
	public boolean getCell(int x, int y) {
		try {
			return cells[y/Cell.SIZE][x/Cell.SIZE].getAlive();
		}
		catch (ArrayIndexOutOfBoundsException a) {
			// /ignore
		}
		return false;
	}
	/**
	 * @see java.awt.event.KeyListener#keyTyped(KeyEvent)
	 */
	public void keyTyped(KeyEvent ke) {
		if (ke.getKeyChar() == KeyEvent.VK_R) {
			for (int r = 0; r < nRows; r++) {
	        	for (int c = 0; c < nCols; c++) {
	        		cells[r][c].setAlive(false);
	        	}
	        }
			repaint();
		}
	}
	/**
	 * @see java.awt.event.KeyListener#keyPressed(KeyEvent)
	 */
	public void keyPressed(KeyEvent ke) {}
	/**
	 * @see java.awt.event.KeyListener#keyReleased(KeyEvent)
	 */
	public void keyReleased(KeyEvent arg0) {}


	/**
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		draw(e.getX(), e.getY());
		repaint();
	}
	/**
	 * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
	 */
    public void mousePressed(MouseEvent e) {
    	this.savedCellState = getCell(e.getX(), e.getY());
    }
    /**
     * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {}
    /**
     * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {}
    /**
     * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
     */
    public void mouseExited(MouseEvent e) {}


    /**
     * @see java.awt.event.MouseMotionListener#mouseDragged(MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
		draw(e.getX(), e.getY());
		repaint();
    }
    /**
     * @see java.awt.event.MouseMotionListener#mouseMoved(MouseEvent)
     */
	public void mouseMoved(MouseEvent e) {}
}