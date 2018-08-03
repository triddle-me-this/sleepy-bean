package particles.gore.blood;

import java.awt.Color;

import core.console.Console;
import core.gameObjects.entities.Entity;
import particles.Particle;
import graphics.AdvancedGraphics;
import graphics.CustomColor;


public class BloodParticle extends Particle{
	
	double angle;
	double rotationSpeed;
	
	static int DEPTH;

	public BloodParticle(int x, int y) {
		this(x, y, CustomColor.bloodRed);
	}

	public BloodParticle(int x, int y, Color color){
		super(x, y, 1.0, 5.0, DEPTH, color);
		
		rotationSpeed = ((rand.nextDouble()) * 4.0) - 2.0;
	}
	
	@Override
	public void update(){
		
		angle += rotationSpeed;
		velocity.add(gravity);
		
		location.move(velocity);
		
		for (Entity solid: Console.getCartLib().getSublistClone("solid").getEntityList()){
			if (collidesWith(solid)){
				finished = true;
			}
		}
			

		
	}
	
/*	@Override
	public void downCollision(World world){
		velocity.setY(0);
	}
	
	@Override
	public void upCollision(World world){
		velocity.setY(0);
	}
	
	@Override
	public void rightCollision(World world){
		velocity.setX(0);
	}
	
	@Override
	public void leftCollision(World world){
		velocity.setX(0);
	}*/
	
	
	@Override
	public void draw(AdvancedGraphics pen){
		
		pen.setColor(getColor());
		pen.fillHexagon(location.getIntX(), location.getIntY(), (int)angle, getSize());
		
	}




}
