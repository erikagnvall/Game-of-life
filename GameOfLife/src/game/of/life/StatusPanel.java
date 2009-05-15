package game.of.life;

import java.awt.*;

public class StatusPanel extends Panel {
	private int width, height;
	public int generations = 0;
	Label lblGenerations;

	public StatusPanel(int w, int h) {
		this.width = w;
		this.height = h;
		setSize(this.width, this.height);
		lblGenerations = new Label("Generations: ");
		add(lblGenerations);
	}

	public void paint(Graphics g) {
		g.drawString(this.generations + "", 264, 21);
	}
}