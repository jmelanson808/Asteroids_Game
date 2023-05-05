
/*
CLASS: AsteroidsGame
DESCRIPTION: Extending Game, Asteroids is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.
Original code by Dan Leyzberg and Art Simon
 */
import java.awt.*;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings({ "serial", "unused" })
public class Asteroids extends Game {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	Ship ship;
	Star[] stars;
	Point[] points;
	Point[] mroids;
	Point origin;
	int index = 0;
	double rotate = 0.0;
	private boolean shipCollision = false;
	private int timer = 0;

	static int counter = 0;
	static int hull = 5;
	static int score = 0;
	static int asteroidCount = 0;
	static double diffMult;

	private java.util.List<Asteroid> randomAsteroids = new ArrayList<Asteroid>();
	private java.util.List<Asteroid> randomMinis = new ArrayList<Asteroid>();
	private java.util.List<Asteroid> randomDebris = new ArrayList<Asteroid>();
	private java.util.List<Asteroid> newRandom = new ArrayList<Asteroid>();
	private java.util.List<Asteroid> removeLarge = new ArrayList<Asteroid>();
	private java.util.List<Asteroid> removeMed = new ArrayList<Asteroid>();
	private java.util.List<Asteroid> removeSml = new ArrayList<Asteroid>();
	private java.util.List<Bullet> bullets = new ArrayList<Bullet>();
	private java.util.List<Bullet> removeBullets = new ArrayList<Bullet>();

	public Asteroids() {
		super("Asteroids!",SCREEN_WIDTH,SCREEN_HEIGHT);
		this.setFocusable(true);
		this.requestFocus();

		// create a number of random asteroid objects
		randomAsteroids = createRandomAsteroids(asteroidCount,60,30);
		
		//create a number of random small asteroid objects
		randomMinis = createRandomAsteroids(0,40,20);
		
		//create a number of random small asteroid objects
		randomDebris = createRandomAsteroids(0,20,10);
		
		//create ship with controller
		ship = buildShip();
		
		this.addKeyListener(ship);
		
		//create stars
		stars = createStars(220, 7);
	}
	
	private Ship buildShip() {
		points = new Point[24];
		points[0] = new Point(   0,177);
		points[1] = new Point(  7,167.5);
		points[2] = new Point(  14,165);
		points[3] = new Point(  14,167.5);
		points[4] = new Point(  20,167.5);
		points[5] = new Point(  20,165);
		points[6] = new Point( 50,177);
		points[7] = new Point( 50,182);
		points[8] = new Point(  40,182);
		points[9] = new Point(  40,187);
		points[10] = new Point( 50,187);
		points[11] = new Point( 50,192);
		points[12] = new Point( 32.5,197);
		points[13] = new Point( 35,200);
		points[14] = new Point( 37.5,200);
		points[15] = new Point( 37.5,205);
		points[16] = new Point( 30,205);
		points[17] = new Point( 27.5,202.5);
		points[18] = new Point( 20,205);
		points[19] = new Point( 20,202);
		points[20] = new Point( 14,202);
		points[21] = new Point( 14,205);
		points[22] = new Point( 7,202);
		points[23] = new Point(  0,192.5);
		origin = new Point(375,275);
		rotate = 0.0;
			
		return new Ship(points, origin, rotate);
			
	}

	//  Create an array of random asteroids
	private java.util.List<Asteroid> createRandomAsteroids(int numberOfAsteroids, int maxAsteroidWidth,
			int minAsteroidWidth) {
		java.util.List<Asteroid> asteroids = new ArrayList<>(numberOfAsteroids);

		for(int i = 0; i < numberOfAsteroids; ++i) {
			// Create random asteroids by sampling points on a circle
			// Find the radius first.
			int radius = (int) (Math.random() * maxAsteroidWidth);
			if(radius < minAsteroidWidth) {
				radius += minAsteroidWidth;
			}
			// Find the circles angle
			double angle = (Math.random() * Math.PI * 1.0/2.0);
			if(angle < Math.PI * 1.0/5.0) {
				angle += Math.PI * 1.0/5.0;
			}
			// Sample and store points around that circle
			ArrayList<Point> asteroidSides = new ArrayList<Point>();
			double originalAngle = angle;
			while(angle < 2*Math.PI) {
				double x = Math.cos(angle) * radius;
				double y = Math.sin(angle) * radius;
				asteroidSides.add(new Point(x, y));
				angle += originalAngle;
			}
			// Set everything up to create the asteroid
			Point[] inSides = asteroidSides.toArray(new Point[asteroidSides.size()]);
			Point inPosition = new Point(Math.random() * SCREEN_WIDTH, Math.random() * SCREEN_HEIGHT);
			
			// Ensures asteroids will spawn closer to the edges of the screen at start.
			while(inPosition.x < 500 && inPosition.x > 300 && inPosition.y < 400 && inPosition.y > 200) {
				inPosition = new Point(Math.random() * SCREEN_WIDTH, Math.random() * SCREEN_HEIGHT);
			}
			
			double inRotation = Math.random() * 360;
			asteroids.add(new Asteroid(inSides, inPosition, inRotation));
		}
		return asteroids;
	}

	public void paint(Graphics brush) {
		
		brush.setColor(Color.black);
		brush.fillRect(0,0,width,height);
		
		// counter is incremented and this message printed
		// each time the canvas is repainted
		counter++;
		
		brush.setColor(Color.white);
		
		//  Display game stats.
		brush.drawString("Counter is " + counter,10,10);
		brush.drawString("Large : " + randomAsteroids.size(), 10, 30);
		brush.drawString("Medium : " + randomMinis.size(), 10, 50);
		brush.drawString("Small : " + randomDebris.size(), 10, 70);
		brush.drawString("Hull Integrity : " + hull, 10, 90);
		
		// generate background with twinkling stars.
		for (int i = 0; i < stars.length; i++) {
			int blink = (int) (Math.random() * stars.length);
			if(i == blink) {
				stars[i].paint(brush, Color.black);
			}else {
				stars[i].paint(brush, Color.white);
			}
			
		}

		// display the random asteroids
		for (Asteroid asteroid : randomAsteroids) {
			asteroid.paint(brush,Color.lightGray);
			asteroid.move();
			if(asteroid.collision(ship)) {
				shipCollision = true;
			}
			//When a Lg hits a Lg.
			if(counter > 300) {
				for (Asteroid other : randomAsteroids) {
					if(other.equals(asteroid) == false && other.collision(asteroid) && removeLarge.contains(other) == false) {
					
						newRandom = createRandomAsteroids(4, 30, 20);
						
						newRandom.get(0).position = new Point(other.position.x + 25.0, other.position.y + 10.0);
						newRandom.get(1).position = new Point(other.position.x - 25.0, other.position.y - 10.0);
						newRandom.get(2).position = new Point(other.position.x + 25.0, other.position.y + 10.0);
						newRandom.get(3).position = new Point(other.position.x - 25.0, other.position.y - 10.0);
						
						randomMinis.addAll(newRandom); 
						
						removeLarge.add(asteroid);
						removeLarge.add(other);
					}
				}
			}
			//When a Sm. hits a Lg.
			if(counter > 800) {
				for (Asteroid debris : randomDebris) {
					if(debris.collision(asteroid)) {
						removeSml.add(debris);
					}
				}
				randomDebris.removeAll(removeSml);
			}
				
		}
		
		for (Asteroid miniroid : randomMinis) {
			miniroid.paint(brush, Color.gray);
			miniroid.move();
			if(miniroid.collision(ship)) {
				shipCollision = true;
			}
			if(counter > 800) {
				//When a Med. hits a Lg.
				for (Asteroid asteroid : randomAsteroids) {
					if (miniroid.collision(asteroid)){
						newRandom = createRandomAsteroids(2, 20, 15);
						newRandom.get(0).position = new Point(miniroid.position.x + 10.0, miniroid.position.y + 10.0);
						newRandom.get(1).position = new Point(miniroid.position.x - 10.0, miniroid.position.y - 10.0);
						
						randomDebris.addAll(newRandom);
						
						removeMed.add(miniroid);
						index++;
					}
				}
				//When a Med. hits a Med.
				for (Asteroid other : randomMinis) {
					if(other.equals(miniroid) == false && other.collision(miniroid) && removeMed.contains(other) == false) {
						
						newRandom = createRandomAsteroids(4, 20, 15);
						
						newRandom.get(0).position = new Point(other.position.x + 25.0, other.position.y + 10.0);
						newRandom.get(1).position = new Point(other.position.x - 25.0, other.position.y - 10.0);
						newRandom.get(2).position = new Point(other.position.x + 25.0, other.position.y + 10.0);
						newRandom.get(3).position = new Point(other.position.x - 25.0, other.position.y - 10.0);
						
						randomDebris.addAll(newRandom); 
						
						removeMed.add(miniroid);
						removeMed.add(other);
					}
				}
				
			}
		}
		
		for (Asteroid debris : randomDebris) {
			debris.paint(brush, Color.darkGray);
			debris.move();
			if(debris.collision(ship)) {
				shipCollision = true;
			}
			//When a sm hits a large.
			for (Asteroid asteroid : randomAsteroids) {
				if(debris.collision(asteroid)) {
					removeSml.add(debris);
				}
			}
		}
		
		//When a ship hits an asteroid of any size.
		if(shipCollision == true && timer < 60) {
			ship.paint(brush, Color.red);
			timer++;
		}
		
		if(timer >= 60) {
			shipCollision = false;
			timer = 0;
			hull--;
			score =- 1000;
		}
		
		if(shipCollision == false) {
			ship.paint(brush, Color.yellow);
		}
		
		bullets = ship.getBullets();
		for(Bullet bullet : bullets) {
			bullet.paint(brush, Color.blue);
			bullet.move();
			if(bullet.outOfBounds()) {
				removeBullets.add(bullet);
			}
			//When a bullet hits a Large.
			for(Asteroid large : randomAsteroids) {
				if(large.contains(bullet.getCenter())){
					newRandom = createRandomAsteroids(2, 40, 20);
					newRandom.get(0).position = new Point(large.position.x + 10.0, large.position.y + 10.0);
					newRandom.get(1).position = new Point(large.position.x - 10.0, large.position.y - 10.0);
					
					randomMinis.addAll(newRandom);
					removeLarge.add(large);	
					removeBullets.add(bullet);
					score += 250;
				}
			}
			//When a bullet hits a Med.
			for(Asteroid med : randomMinis) {
				if(med.contains(bullet.getCenter())) {
					newRandom = createRandomAsteroids(2, 20, 15);
					newRandom.get(0).position = new Point(med.position.x + 10.0, med.position.y + 10.0);
					newRandom.get(1).position = new Point(med.position.x - 10.0, med.position.y - 10.0);
					
					randomDebris.addAll(newRandom);
					removeMed.add(med);
					removeBullets.add(bullet);
					score += 500;
				}
			}
			//When a bullet hits a Sml.
			for(Asteroid sml : randomDebris) {
				if(sml.contains(bullet.getCenter())) {
					removeSml.add(sml);
					removeBullets.add(bullet);
					score += 1000;
				}	
			}
		}
		
		//remove all objects marked for deletion.
		bullets.removeAll(removeBullets);
		randomDebris.removeAll(removeSml);
		randomMinis.removeAll(removeMed);
		randomAsteroids.removeAll(removeLarge);
		
		//reset lists for next use.
		removeBullets.clear();
		removeSml.clear();
		removeMed.clear();
		removeLarge.clear();
		newRandom.clear();
		
		ship.move();
		
		//Victory Screen
		if(randomAsteroids.isEmpty() && randomMinis.isEmpty() && randomDebris.isEmpty()) {
			score += 6000 - counter;
			score *= diffMult;
			brush.setColor(Color.black);
			brush.fillRect(0,0,width,height);
			brush.setColor(Color.white);
			brush.drawString("You Won!!!", 350, 250);
			brush.drawString("Time : " + counter, 350, 270);
			brush.drawString("Hull Remaining : " + hull, 350, 290);
			brush.drawString("Total Score : " + score, 350, 310);
			
			on = false;
		}
		
		//Defeat Screen
		if(hull == 0) {
			score += 6000 - counter;
			score-= 5000;
			brush.setColor(Color.gray);
			brush.fillRect(0,0,width,height);
			brush.setColor(Color.black);
			brush.drawString("Critical Damage, ship lost!", 350, 250);
			brush.drawString("Time : " + counter, 350, 270);
			brush.drawString("Hull Remaining : " + hull, 350, 290);
			brush.drawString("Total Score : " + score, 350, 310);
			
			on = false;
		}
		
	}
	
	// Create a certain number of stars with a given max radius
	public Star[] createStars(int numberOfStars, int maxRadius) {
	 	Star[] stars = new Star[numberOfStars];
	 	for(int i = 0; i < numberOfStars; ++i) {
	 		Point center = new Point
	 		(Math.random() * SCREEN_WIDTH, Math.random() * SCREEN_HEIGHT);

	 		int radius = (int) (Math.random() * maxRadius);
	 		if(radius < 1) {
	 			radius = 1;
	 		}
	 		stars[i] = new Star(center, radius);
	 	}
	 	return stars;
	 }

	public static void main (String[] args) {
		
		boolean repeat = true;
		boolean game = false;
		char start;
		String difficulty;
		Scanner input = new Scanner(System.in);
		
		//Cycles selection menu if player chooses 'n' for 'ready?' or inputs invalid difficulty.
		while(game == false) {
			//Cycles selection menu if player enters invalid option for 'ready?'
			while(repeat == true) {
				System.out.println("~~~~~~~~~~~~~~ASTEROIDS~~~~~~~~~~~~~~");
				System.out.println("Select a Difficulty (easy / normal / hard)");
				
				difficulty = input.next().toString().toLowerCase();
				
				//Sets difficulty modifiers.
				switch(difficulty) {
					case "easy" : 
						repeat = false;
						diffMult = 0.8;
						hull = 10;
						asteroidCount = 6;
						System.out.println("You selected " + difficulty);break;
					case "normal" :
						repeat = false;
						diffMult = 1.0;
						hull = 6;
						asteroidCount = 10;
						System.out.println("You selected " + difficulty);break;
					case "hard" :
						repeat = false;
						diffMult = 1.2;
						hull = 3;
						asteroidCount = 12;
						System.out.println("You selected " + difficulty);break;
					default : 
						System.out.println("Invalid Choice");
				}
			}
			
			repeat = true;
			
			//Display Game Rules.
			System.out.println("Total Hull Points: " + hull);
			System.out.println("Starting Asteroids: " + asteroidCount);
			System.out.println();
			System.out.println("Large Asteroids: +250 pts.");
			System.out.println("Medium Asteroids: +500 pts.");
			System.out.println("Small Asteroids: +1000 pts.");
			System.out.println("Hull Damage: -1000 pts.");
			System.out.println("Game Loss: -5000 pts.");
			System.out.println("Time under 60 seconds: +1000 pts. per second under.");
			System.out.println("Time over 60 seconds: -1000 pts. per second over.");
			System.out.println("Difficulty Multiplier: " + diffMult);
			System.out.println();
			System.out.println("Start Game? (y / n)");
			
			start = input.next().toLowerCase().charAt(0);
			
			if(start == 'y') {
				game = true;
			}else if(start == 'n'){
				game = false;
			}else {
				System.out.println("Invalid Choice, enter 'y' or 'n'.");
				repeat = false;
			}
			
		}
		
		input.close();
		
		Asteroids a = new Asteroids();
		a.repaint();
		
	}
}
