package game.of.life;

import java.awt.*;
import java.awt.event.*;
// GIT :D
public class Main extends Frame {
	private static final long serialVersionUID = -7756878323926974220L;
	CellCanvas cc;
	Frame frame = new Frame("Game of Life");

	/** Constructor, does almost everything */
	public Main() {
		setBackground(Color.blue);

		/** Creates the window */
		createWindow();

		/** Listen to the close button */
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
				System.exit(0);
			}
		});
	}

	/** Creates the window */
	public void createWindow() {
		/** Set visibility and size of the window, 
		 * make it unresizable and set the color.*/
		frame.setSize(447,293);
		frame.setResizable(false);

		cc = new CellCanvas();
		frame.add(cc);

		frame.setVisible(true);
	}

	/** Initializes the Main class and
	 * starts the program */
	public static void main(String[] args) {
		/** Create an instance of the class */
		new Main();
	}
}