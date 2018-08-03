package particles;

import java.awt.Color;

import core.physics.Point;
import core.physics.Vector;
import graphics.AdvancedGraphics;

public class RainParticle extends Particle{
	
	final static int SPEED = 2;
	final static int SIZE = 5;
	final static int DEPTH = -15;
	final static Color COLOR = new Color(100, 100, 150);

	public RainParticle(int x, int y) {
		super(x, y, SPEED, SIZE, DEPTH, COLOR);
		velocity = new Vector(1, 10);
	}
	
	@Override
	public void update(){
		
		location.move(velocity);
		
	}
	
	public void draw(AdvancedGraphics pen){
		
		Point projectedLocation = new Point(location.getX(), location.getY());
		Vector projectionVec = new Vector(velocity.getX(), velocity.getY());
		projectionVec.multiply(3.0);
		projectedLocation.move(projectionVec);
		
		pen.setColor(color);
		pen.drawLine(location, projectedLocation);
	}

}
