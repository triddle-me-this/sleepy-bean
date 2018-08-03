package core;

import java.awt.Color;

import core.physics.Point;
import core.physics.Vector;
import graphics.AdvancedGraphics;
import testSetup.Main;

public class Camera {

	Point location;
	Vector velocity;
	
	final static double ACC = 3.0;
	final static double SLOWING_RANGE = 60.0;
	final static double SLOWING_RATE = .5;
	
	final static double STATIONARY_RANGE = 10.0;

	public Camera(int x, int y){
		
		location = new Point(x,y);
		velocity = new Vector(0,0);
		
	}
	
	public void friction(double rate){
		velocity.multiply(rate);
	}
	
	public void jumpToTarget(Point target){
		location = new Point(target.getX(), target.getY());
	}
	
	public void targetUpdate(Point target){
		
		Vector vec = location.makeVector(target);
		vec.setLength(ACC);
		velocity.add(vec);
		
		velocity.limit(10.0);
		
		if (target.dist(location) <= SLOWING_RANGE){
			velocity.multiply(SLOWING_RATE);
		}
		
		if (target.dist(location) <= STATIONARY_RANGE){
			velocity.setX(0);
			velocity.setY(0);
		}
	}
	
	public void boundaryAlign(int top, int bottom, int left, int right){
		if (location.getIntY() + Main.HEIGHT/2 > bottom)
			location.setY(bottom - Main.HEIGHT/2);
		if (location.getIntY() - Main.HEIGHT/2 < top)
			location.setY(top + Main.HEIGHT/2);
		if (location.getIntX() + Main.WIDTH/2 > right)
			location.setX(right - Main.WIDTH/2);
		if (location.getIntX() - Main.WIDTH/2 < left)
			location.setX(left + Main.WIDTH/2);
		
	}
	
	public void move(){
		location.move(velocity);
	}
	
	public int getIntX(){
		return location.getIntX();
	}
	
	public int getIntY(){
		return location.getIntY();
	}
	
	public void debugDraw(AdvancedGraphics pen){
		pen.setColor(Color.RED);
		pen.drawCenteredCircle(location.getIntX(), location.getIntY(), 16);
	}
	
	public Point getLocation(){
		return location;
	}
	
}
