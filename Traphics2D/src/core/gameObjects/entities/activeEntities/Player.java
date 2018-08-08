package core.gameObjects.entities.activeEntities;

import java.awt.Color;
import java.util.HashMap;

import Shooter.weapons.BigPistol;
import Shooter.weapons.Weapon;
import core.animation.Animation;
import core.animation.AnimationFactory;
import core.console.Console;
import core.physics.Point;
import core.physics.Vector;
import graphics.AdvancedGraphics;

public class Player extends ActiveEntity{

	final static double MAX_MOVE_SPEED = 2.0;
	
	HashMap<String, Animation> animationTable;
	Animation currentAnimation;
	
	boolean faceRight;
	boolean faceUp;
	
	Weapon currentWeapon;

	
	public Player(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		animationTable = new HashMap<String, Animation>();
		loadAnimations();
		
		currentAnimation = animationTable.get("idle");
		
		faceRight = true;
		faceUp = true;
		
		currentWeapon = new BigPistol(location.getIntX(), location.getIntY(), Console.getImage("weapons/", "spr_bigPistol.png"));
		
	}
	
	public void loadAnimations(){
		
		Animation frontRun = AnimationFactory.createAnimation(Console.getImage("player/", "spr_frontRun.png"), 13, 24, 5, true);
		animationTable.put("frontRun", frontRun);
		
		Animation backRun = AnimationFactory.createAnimation(Console.getImage("player/", "spr_backRun.png"), 13, 24, 5, true);
		animationTable.put("backRun", backRun);
		
		Animation idle = AnimationFactory.createAnimation(Console.getImage("player/", "spr_frontIdle.png"), 9, 24, 1, false);
		animationTable.put("frontIdle", idle);
		
		Animation backIdle = AnimationFactory.createAnimation(Console.getImage("player/", "spr_backIdle.png"), 9, 24, 1, false);
		animationTable.put("backIdle", idle);
	}
		
	@Override
	public void update() {
		
		boolean upPress = Console.isKeyPressed('w');
		boolean leftPress = Console.isKeyPressed('a');
		boolean downPress = Console.isKeyPressed('s');
		boolean rightPress = Console.isKeyPressed('d');
		
		velocity = new Vector(0,0);
		
		if (upPress && !downPress){
			faceUp = true;
			velocity.addY(-1);
			
		}
		if (leftPress && !rightPress){
			faceRight = false;
			velocity.addX(-1);
		}
		if (downPress && !upPress){
			faceUp = false;
			velocity.addY(1);
		}
		if (rightPress && !leftPress){
			faceRight = true;	
			velocity.addX(1);
		}
		
		velocity.setLength(MAX_MOVE_SPEED);
		location.move(velocity);
		
		//Animation 
		String animation;
		
		if (velocity.getLength()>0.0){
			if (faceUp){
				animation = "backRun";
			}
			else{
				animation = "frontRun";
			}
		}
		else{
			if (faceUp){
				animation = "backIdle";
			}
			else
				animation = "frontIdle";
		}
		
		currentAnimation = animationTable.get(animation);
		
		currentAnimation.update();
		
		//weapon
		currentWeapon.update();
		
	}

	@Override
	public void draw(AdvancedGraphics pen) {
		pen.setColor(Color.BLUE);
		pen.drawRect(location.getIntX(), location.getIntY(), width, height);
		
		Point alLoc = getAlignedLocation();
		
		currentAnimation.draw(pen, alLoc.getIntX(), alLoc.getIntY(), !faceRight, false);	
		
		//
		currentWeapon.draw(pen);
	}
	
	public Point getAlignedLocation(){
		int aWidth = currentAnimation.getCurrentFrame().getWidth();
		int aHeight = currentAnimation.getCurrentFrame().getHeight();

		int xOffset = (width - aWidth)/2;
		int yOffset = (height - aHeight)-1;
		
		if (!faceRight){
			xOffset +=1;
		}
		
		Point aligned = new Point(location.getIntX() + xOffset, location.getIntY() + yOffset);
		return aligned;
	}

}
