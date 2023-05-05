import java.awt.Color;
import java.awt.Graphics;

public class Star extends Circle {

	public Star(Point center, int radius) {
		super(center, radius);
	}

	@Override
	public void paint(Graphics brush, Color color) {
		brush.setColor(color);
		brush.fillOval((int)center.x, (int)center.y, radius, radius);
	}

	@Override
	public void move() {
		// not used
	}

}
