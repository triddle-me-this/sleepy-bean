package core.gameObjects.entities.activeEntities;

import java.awt.Color;
import core.animation.Animation;
import core.console.Console;
import core.gameObjects.entities.Entity;
import core.legacy.LegacyWorld;
import core.physics.Vector;
import graphics.AdvancedGraphics;
import particles.gore.BoneParticle;
import particles.gore.blood.BloodParticle;
import particles.gore.blood.DarkBloodParticle;
import particles.gore.blood.LightBloodParticle;

public abstract class Enemy extends ActiveEntity{
	
	Animation currentAnimation;
	int health;
	int maxHealth;
	int hitStunTimer;
	boolean airborne;
	boolean dead;
	double speed;
	double deathJumpForce;
	
	String hurtSound = "hurt.wav";
	
	public Enemy(LegacyWorld world, int x, int y, int width, int height, double speed, int maxHealth, double deathJumpForce) {
		super(x, y, width, height);
		
		dead = false;
		this.maxHealth = maxHealth;
		this.deathJumpForce = deathJumpForce;
		this.speed = speed;
		
		health = maxHealth;
		hitStunTimer = 0;
		airborne = false;
		
		Console.loadSound("", hurtSound);
	}

	
	public boolean isDead(){
		return dead;
	}
	
	public void hit(LegacyWorld world, int damage, int hitStun, int knockback){
		
		
		if (hitStunTimer > hitStun){
			health--;
			
			if (health <=0){
				death(world);
			}
			else{
				velocity.multiplyX(-1);
				deathParticles(world);
				Console.playLoadedSound(hurtSound, 0);
				hitStunTimer = 0;
				
				Vector knockbackVector = world.getPlayerCenterLocation().makeVector(getCenterLocation());
				knockbackVector.setLength(knockback);
				
				velocity.setY(-3);
				velocity.add(knockbackVector);
			}
		}

	}
	
	public void death(LegacyWorld world){
	
		if (!dead){
			velocity.multiplyX(-1);
			velocity.setY(-1*deathJumpForce); 
			deathParticles(world);
			
			dead = true;
			Console.playLoadedSound(hurtSound, 0);
		}
	}
	
	public void deathParticles(LegacyWorld world){
		for (int i=0; i<20; i++){
			world.addParticle(new BloodParticle(getCenterLocation().getIntX(),getCenterLocation().getIntY()));
			world.addParticle(new DarkBloodParticle(getCenterLocation().getIntX(),getCenterLocation().getIntY()));
			world.addParticle(new LightBloodParticle(getCenterLocation().getIntX(),getCenterLocation().getIntY()));
		}
			
		for (int i=0; i<5; i++){
			world.addParticle(new BoneParticle(getCenterLocation().getIntX(), getCenterLocation().getIntY()));
		}
	}
	
	@Override
	public void downCollision(LegacyWorld world, Entity collider){
		velocity.setY(0);
	}
	@Override
	public void upCollision(LegacyWorld world, Entity collider){
		velocity.setY(0);
	}

	@Override
	public void draw(AdvancedGraphics pen) {

		boolean mirrored;
		if (velocity.getX()>0)
			mirrored = false;
		else
			mirrored = true;
		
		//(size of animation - size of hitbox)/2
		int offsetX = (currentAnimation.getCurrentFrame().getWidth()-width)/2;
		int offsetY = (currentAnimation.getCurrentFrame().getHeight()-height);
		
		currentAnimation.draw(pen, location.getIntX()-offsetX, location.getIntY()-offsetY, mirrored, false);
			
		
	}

	@Override
	public void debugDraw(AdvancedGraphics pen) {
		if (airborne)
			pen.setColor(Color.BLUE);
		else
			pen.setColor(Color.PINK);
		
		pen.fillRect(location.getIntX(), location.getIntY(), width, height);
		
		
	}
	
	

}
