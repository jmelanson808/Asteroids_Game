import java.awt.Color;
import java.awt.Graphics;

public class Asteroid extends Polygon {

	public Asteroid(Point[] shape, Point position, double rotation) {
		super(shape, position, rotation);
	}

	@Override
	public void paint(Graphics brush, Color color) {
		Point[] points = this.getPoints();
		java.awt.Polygon shape = new java.awt.Polygon();
		int x = 0;
		int y = 0;
		for(int i = 0; i < points.length; i++) {
			x = (int) points[i].x;
			y = (int) points[i].y;
			shape.addPoint(x, y);
		}
		brush.setColor(color);
		brush.fillPolygon(shape);
		brush.drawPolygon(shape);
	}

	@Override
	public void move() {
		position.x += Math.cos(Math.toRadians(rotation)) * 1.5;
		position.y += Math.sin(Math.toRadians(rotation)) * 1.5;
		
		if (position.x > 825){
			position.x = -25;
		}else if(position.x < -25) {
			position.x = 825;
		}
		
		if(position.y > 625) {
			position.y = -25;
		}else if(position.y < -25) {
			position.y = 625;
		}
	}
	
	// Detect if there was a collision
	public boolean collision(Polygon asteroid) {
		// get the Point objects for the Ship
			Point[] points = asteroid.getPoints();

			for(Point p : points) {
				if(this.contains(p)) {
					return true;
				}
			}
			return false;
		}
}
