package particles;

import graphics.AdvancedGraphics;
import graphics.CustomColor;

import java.awt.Color;
import java.util.Random;

import core.basicInterfaces.Updateable;
import core.gameObjects.entities.Entity;
import core.physics.Vector;

public abstract class Particle extends Entity implements Updateable{
	
	protected Random rand = new Random();
	
	protected double size;
	protected Color color;
	
	protected int timer;
	protected int lifeSpan = 100;
	
	
	public Particle(int x, int y, double speed, double size, int depth, Color color, Vector gravity){
		//width, height set to 1. don't seem relevant
		//solid is false
		super(x, y, 1, 1, false, depth, new Vector(0,0), gravity);
		
		this.velocity = randomVector(speed);
		this.size = randomSize(size);

		this.color = color;
		this.timer = 0;
		
	}
	
	public Particle(int x, int y, double speed, double size, int depth, Color color){
		this(x, y, speed, size, depth, color, new Vector(0, .08));
	}
	
	public Vector randomVector(double velocity){
		return new Vector(rand.nextGaussian()*(velocity),rand.nextGaussian()*(velocity));
	}
	
	public double randomSize(double size){
		if ((size==0)||(size==1)){
			return size;
		}
		else
			return (size/2.0)+(rand.nextInt((int)(size/2.0)));
	}

	
	@Override
	public void update() {
		
		timer++;
		
		velocity.add(gravity);
		location.move(velocity);
		
		if (timer > lifeSpan)
			finished = true;
		
	}
	
	@Override
	public void draw(AdvancedGraphics pen){
		
		pen.setColor(getColor());
		pen.fillCircle(location.getIntX(), location.getIntY(), getSize());
	}
	
	@Override
	public void debugDraw(AdvancedGraphics pen) {
		
		pen.setColor(CustomColor.debugPurple);
		pen.fillCircle(location.getIntX(), location.getIntY(), getSize());
		
	}
	
	public int getTimer(){
		return timer;
	}
	
	public int getSize(){
		return (int)size;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
}
