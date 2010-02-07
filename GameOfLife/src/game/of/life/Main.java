package game.of.life;

import java.awt.*;
import java.awt.event.*;

public class Main extends Frame implements Runnable {
	/**
	 * Ignorera
	 */
	private static final long serialVersionUID = -7756878323926974220L;

	/**
	 * Bredd och h�jd p� f�nstret
	 */
	private int width = 447, height = 326;

	/**
	 * Frame, sj�lva f�nstret
	 */
	private Frame frame;

	/**
	 * CellCanvasen som typ allt g�rs p�
	 */
	CellCanvas cc;

	/**
	 * StatusPanelen l�ngst ner i f�nstret
	 */
	StatusPanel sp;

	/**
	 * Konstruktor, initierar f�nstret och ser till s� att 
	 * vi kan st�nga f�nstret
	 */
	public Main() {
		createWindow();

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
				System.exit(0);
			}
		});
		
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * Skapar f�nstret och l�gger till CellCanvasen till det
	 */
	private void createWindow() {
		frame = new Frame("Game of Life");

		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());

		cc = new CellCanvas(width, height-44);
		frame.add(cc, BorderLayout.CENTER);
		cc.setFocusable(true);

		sp = new StatusPanel(width, 10);
		frame.add(sp, BorderLayout.SOUTH);

		frame.setLocation(windowLocation());

		frame.setVisible(true);
	}


	/**
	 * @see java.lang.Runnable#run
	 */
	public void run() {
		while (true) {
			try {
				sp.generations = cc.getGenerations();
				sp.repaint();

				Thread.sleep(cc.getSleepTime());
			} catch(InterruptedException ie) {}
		}
	}


	/**
	 * Ser till s� att f�nstret hamnar i mitten av sk�rmen
	 * @return Point med x- och y-v�rdet i 
	 */
	private Point windowLocation() {
		Point p = new Point();
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension scrnsize = tk.getScreenSize();

		p.setLocation((int)((scrnsize.width/2)-width/2), (int)((scrnsize.height/2)-height/2));

		return p;
	}


	/**
	 * Startar en instans av Main
	 * @param args argument att skicka till programmet, f�r n�rvarande st�ds inga argument
	 */
	public static void main(String[] args) {
		/** Create an instance of the class */
		new Main();
	}
}