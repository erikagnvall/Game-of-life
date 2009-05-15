package game.of.life;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Iterator;

public class CellCanvas extends Canvas implements KeyListener, MouseListener, MouseMotionListener, Runnable {
	/**
	 * Ignorera
	 */
	private static final long serialVersionUID = -3166382484935617668L;

	/**
	 * Antalet rader och kolumner
	 */
	private int nRows = 26, nCols = 44, width, height, generations;

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
	private Vector<Cell> livingCells, nextLiving;

	/**
	 * Sparad cellstatus för den cellen man klickade på
	 */
	private boolean savedCellState = false;
	
	/**
	 * True om man ska köra programmet
	 */
	private boolean running = false;

	/**
	 *  Buffertbilden
	 */
	Image bufferedImage = null;

	/**
	 *  Ritytan koppad till buffertbilden
	 */
	Graphics gImage;

	/**
	 * Tråden som programmet körs i och sleeptime
	 */
	Thread t;
	int sleep = 500;


	/**
	 * Konstruktor för CellCanvas
	 * @param w integer med bredden
	 * @param h integer med höjden
	 */
	public CellCanvas(int w, int h) {
		//this.width = w;
		//this.height = h;
		this.setBackground(fcolor);
		this.setForeground(bcolor);

		this.livingCells = new Vector<Cell>();
		this.livingCells.clear();

		this.nextLiving = new Vector<Cell>();
		this.nextLiving.clear();


		for (int c = 0; c < nRows; c++) {
			for (int r = 0; r < nCols; r++) {
				cells[c][r] = new Cell(c,r);
			}
		}

        this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addKeyListener(this);

		this.t = new Thread(this);
		this.t.start();
		
		started();
	}


	/**
	 * @see java.awt.Canvas#paint(Graphics)
	 */
	public void paint (Graphics g) {
		Iterator<Cell> i = this.livingCells.iterator();
		while(i.hasNext()) {
			i.next().paint(g);
		}

		g.setColor(new Color(0xcccccc));
		for (int x = 0; x <= nCols; x++) {
			g.drawLine(x*Cell.SIZE, 0, x*Cell.SIZE, Cell.SIZE*nRows);
		}
		for (int y = 0; y <= nRows; y++) {
			g.drawLine(0, y*Cell.SIZE, Cell.SIZE*nCols, y*Cell.SIZE);
		}
	}


	/**
	 * @see java.awt.Canvas#update(Graphics)
	 */
	public void update(Graphics g){
		/** Double buffering to prevent flickering */
		Dimension d = this.getSize();
		this.bufferedImage = createImage(d.width, d.height);
		this.gImage = this.bufferedImage.getGraphics();
		this.gImage.clearRect(0, 0, d.width, d.height);
		paint(this.gImage);
		g.drawImage(this.bufferedImage, 0, 0, null);
	}

	
	public void started() {
		draw(20, 70);
		draw(30, 70);
		draw(20, 80);
		draw(30, 80);
		
		draw(110, 70);
		draw(120, 70);
		draw(100, 80);
		draw(120, 80);
		draw(100, 90);
		draw(110, 90);
		
		draw(180, 90);
		draw(190, 90);
		draw(180, 100);
		draw(200, 100);
		draw(180, 110);
		
		draw(250 ,50);
		draw(260, 50);
		draw(240, 60);
		draw(260, 60);
		draw(240, 70);
		draw(250, 70);
		
		draw(260 ,170);
		draw(270, 170);
		draw(280, 170);
		draw(260, 180);
		draw(270, 190);
		
		draw(360, 50);
		draw(370, 50);
		draw(360, 60);
		draw(370, 60);
		
		draw(370, 120);
		draw(380, 120);
		draw(370, 130);
		draw(390, 130);
		draw(370, 140);
		repaint();
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
			if (c.getAlive() && !this.livingCells.contains(c)) {
				this.livingCells.add(c);
			}
			else if(!c.getAlive() && this.livingCells.contains(c)) {
				this.livingCells.remove(c);
			}
			repaint();
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
	public boolean getCellState(int x, int y) {
		try {
			return cells[y/Cell.SIZE][x/Cell.SIZE].getAlive();
		}
		catch (ArrayIndexOutOfBoundsException a) {
			// /ignore
		}
		return false;
	}


	/**
	 * @see java.lang.Runnable#run
	 */
	public void run() {
		while (true) {
			if (this.running) {
				nextGen();
				repaint();
			}
			repaint();
			try { Thread.sleep(this.sleep); } catch(InterruptedException ie) { }
		}
	}
	
	public void nextGen() {
		Iterator<Cell> i;
		this.nextLiving.clear();

		this.generations++;

		i = this.livingCells.iterator();
		while (i.hasNext()) {
			try {
				Cell c = i.next();
				c.neighbour = 0;
			} catch(ConcurrentModificationException e) {}
		}

		i = this.livingCells.iterator();
		while (i.hasNext()) {
			try {
				Cell c = i.next();
				theNeighbours(c);
			} catch(ConcurrentModificationException e) {}
		}

		Vector<Cell> temp = new Vector<Cell>();
		temp = (Vector<Cell>) this.livingCells.clone();
		i = temp.iterator();
		while (i.hasNext()) {
			try {
				Cell c = i.next();
				if (c.neighbour != 2 && c.neighbour != 3) {
					c.setAlive(false);
					this.livingCells.remove(c);
				}
			} catch(ConcurrentModificationException e) {}
		}

		i = this.nextLiving.iterator();
		while (i.hasNext()) {
			Cell c = i.next();
			if (c.neighbour == 3 && !this.livingCells.contains(c)) {
				c.setAlive(true);
				this.livingCells.add(c);
			}
		}
	}


	/**
	 * Lägger till en granne på cellen för den givna kolumnen och raden
	 * @param c kolumnen på cellen
	 * @param r raden på cellen
	 */
	public void addNeighbour(int c, int r) {
		try {
			Cell cell = cells[r][c];
			if (!this.nextLiving.contains(cell)) {
				this.nextLiving.add(cell);
				cell.neighbour = 1;
			}
			else {
				cell.neighbour++;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			// /ignore
		}
	}


	/**
	 * GameOfLife-algoritmen för en levande cell
	 * @param c Cellen man ska kolla för
	 */
	public void theNeighbours(Cell c) {
		int col = c.col;
		int row = c.row;

		addNeighbour( col-1, row-1 );
		addNeighbour( col,   row-1 );
		addNeighbour( col+1, row-1 );
		addNeighbour( col-1, row   );
		addNeighbour( col+1, row   );
		addNeighbour( col-1, row+1 );
		addNeighbour( col,   row+1 );
		addNeighbour( col+1, row+1 );
	}


	/**
	 * Hämtar antalet generationer
	 * @return integer med antalet generationer
	 */
	public int getGenerations() {
		return this.generations;
	}


	/**
	 * Hämtar den nuvarande sleeptimen
	 * @return integer med sleeptime
	 */
	public int getSleepTime() {
		return this.sleep;
	}


	/**
	 * @see java.awt.event.KeyListener#keyTyped(KeyEvent)
	 */
	public void keyTyped(KeyEvent ke) {
		if (ke.getKeyChar() == KeyEvent.VK_R) {
			this.livingCells.clear();
			generations = 0;
			this.running = false;

			for (int r = 0; r < nRows; r++) {
	        	for (int c = 0; c < nCols; c++) {
	        		cells[r][c].setAlive(false);
	        	}
	        }
			repaint();
		}
		if (ke.getKeyChar() == KeyEvent.VK_SPACE) {
			this.running = !this.running;
		}
		if (ke.getKeyChar() == KeyEvent.VK_1) {
			this.sleep = 500;
		}
		if (ke.getKeyChar() == KeyEvent.VK_2) {
			this.sleep = 250;
		}
		if (ke.getKeyChar() == KeyEvent.VK_3) {
			this.sleep = 125;
		}
		if (ke.getKeyChar() == KeyEvent.VK_4) {
			this.sleep = 62;
		}
		if (ke.getKeyChar() == KeyEvent.VK_5) {
			this.sleep = 31;
		}
		if (ke.getKeyChar() == KeyEvent.VK_6) {
			this.sleep = 15;
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
	}
	/**
	 * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
	 */
    public void mousePressed(MouseEvent e) {
    	this.savedCellState = getCellState(e.getX(), e.getY());
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
    }
    /**
     * @see java.awt.event.MouseMotionListener#mouseMoved(MouseEvent)
     */
	public void mouseMoved(MouseEvent e) {}
}