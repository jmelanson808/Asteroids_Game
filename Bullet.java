import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends Circle {

	private double rotation;
	public static final int RADIUS = 10;

	public Bullet(Point center, double rotation) {
		super(center, RADIUS);
		this.rotation = rotation;
	}

	@Override
	public void paint(Graphics brush, Color color) {
		brush.setColor(color);
		brush.fillOval((int)center.x, (int)center.y, RADIUS, RADIUS);
	}

	@Override
	public void move() {
		center.x += 5.0 * Math.cos(Math.toRadians(rotation));
		center.y += 5.0 * Math.sin(Math.toRadians(rotation));
	}
	
	public boolean outOfBounds() {
		if(center.x > 800 || center.x < 0 || center.y > 600 || center.y < 0) {
			return true;
		}
		return false;
	}
	
	public Point getCenter() {
		return center;
	}

}
