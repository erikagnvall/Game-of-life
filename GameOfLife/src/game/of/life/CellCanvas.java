package game.of.life;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class CellCanvas extends Panel {
	private static final long serialVersionUID = -3166382484935617668L;
	private int nCells = 1144, width = 447, height = 293;
	private Cell cells[] = new Cell[this.nCells];
	private CellCanvas cc;

	// För- och bakgrundsfärg
	private Color fcolor = new Color(0x999999);
	private Color bcolor = new Color(0xcccccc);

	// Buffertbilden
	Image bufferedImage = null;

	// Ritytan koppad till buffertbilden
	Graphics gImage;

	/** Construct, <init> */
	public CellCanvas() {
		this.setSize(this.width, this.height);
		this.setBackground(fcolor);
		this.setForeground(bcolor);

		int h = 0;
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 44; j++) {
				cells[h++] = new Cell(j,i);
			}
		}

		cc = this;
		MouseListener ml = new MouseListener(){
            public void mouseClicked(MouseEvent e) {
            	Cell[] cells = getCells();
                for (int i = 0; i < getnCells(); i++) {
                	cells[i].click(e.getX(), e.getY(), cc);
                }
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        };
        MouseMotionListener mml = new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				if (e.getButton() == MouseEvent.NOBUTTON) {
					for (int i = 0; i < getnCells(); i++) {
	                	cells[i].click(e.getX(), e.getY(), cc);
	                }
				}
			}
			public void mouseMoved(MouseEvent e) {
			}
        };
        this.addMouseMotionListener(mml);
		this.addMouseListener(ml);
	}
	/** paint-method to paint all the pretty cells */
	public void paint (Graphics g) {
		for (int i = 0; i < this.nCells; i++) {
			cells[i].paint(g);
		}
	}

	/** update-method */
	public void update(Graphics g){
		/** Double buffering to prevent flickering */
		this.bufferedImage = createImage(this.width, this.height);
		this.gImage = this.bufferedImage.getGraphics();
		this.gImage.clearRect(0, 0, this.width, this.height);
		paint(this.gImage);
		g.drawImage(this.bufferedImage, 0, 0, this);
	}

	/** Returns number of cells on the canvas */
	public int getnCells() {
		return this.nCells;
	}

	/** Returns the cells as an array */
	public Cell[] getCells() {
		return this.cells;
	}
}