import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Ship extends Polygon implements KeyListener{
	
	private boolean forward = false;
	private boolean left = false;
	private boolean right = false;
	private boolean fire = false;
	private boolean active = false;
	private Bullet bullet;
	
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public Ship(Point[] shape, Point position, double rotation) {
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
		
		//includes border-bounding for the ship.
		if(forward == true) {
			if(position.x < 750 && position.x > 0 && position.y < 550 && position.y > 0) {
				position.x += 3.0 * Math.cos(Math.toRadians(rotation));
				position.y += 3.0 * Math.sin(Math.toRadians(rotation));
			}else{
				if(position.x >= 750) {
					position.x = 730;
				}else if(position.x <= 0) {
					position.x = 20;
				}
				
				if(position.y >= 550) {
					position.y = 530;
				}else if(position.y <= 0) {
					position.y = 20;
				}
			}
		}
		
		if(left == true) {
			rotate(-5);
		}
		
		if(right == true) {
			rotate(5);
		}
		
		if(fire == true && active == false) {
			bullet = new Bullet(getPoints()[8], rotation);
			bullets.add(bullet);
			active = true;
			//TODO: Cannot shoot and move left at the same time. 
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		return;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
			case KeyEvent.VK_UP:
				forward = true; break;
			case KeyEvent.VK_RIGHT:
				right = true; break;
			case KeyEvent.VK_LEFT:
				left = true; break;
			case KeyEvent.VK_SPACE:
				fire = true; 
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
			case KeyEvent.VK_UP:
				forward = false; break; 
			case KeyEvent.VK_RIGHT:
				right = false; break;
			case KeyEvent.VK_LEFT:
				left = false; break;
			case KeyEvent.VK_SPACE:
				fire = false; 
				active = false; 
		}
	}

	// Detect if there was a collision
	public boolean collision(Polygon ship) {
		// get the Point objects for the Ship
		Point[] points = ship.getPoints();

		for(Point p : points) {
			if(this.contains(p)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Bullet> getBullets(){
		return bullets;
	}
	
}
