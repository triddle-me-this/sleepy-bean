package Shooter.weapons;


import java.awt.image.BufferedImage;

import core.console.Console;
import core.physics.Point;
import graphics.AdvancedGraphics;

public class HeroSword extends Weapon{

	final static int HOLD_DISTANCE = 10;
	final static int RECOIL_TIME = 40;
	final static int X_OFFSET = 1;
	final static int Y_OFFSET = -4;
	final static double ANG_OFFSET = -90;
	
	//the angle over which the weapon is swung
	final static double SWING_ARC = 180;
	//weapon moves this much of the swing arc per frame.
	final static double SWING_SPEED = .55;
	final static double SWING_RETURN_RATE = 6;
	
	final static double WINDUP_SPEED = .05;
	final static double WINDUP_ARC = 45;
	
	//"winding up" preparing to attack
	boolean windingUp;
	
	boolean swinging;
	//which direction the swing is going (false is returning to rest)
	boolean swingOut;
	
	public HeroSword(int x, int y) {
		super(x, y, Console.getImage("weapons/", "wep_heroSword.png"), HOLD_DISTANCE, RECOIL_TIME, X_OFFSET, Y_OFFSET, ANG_OFFSET);
	}

	public void fire() {
		windingUp = true;
	}
	
	public void weaponUpdate(){
		
		if (windingUp){
				swingOffset -= WINDUP_SPEED*(WINDUP_ARC - swingOffset);
				
				if (Math.abs(swingOffset) >= WINDUP_ARC-1){
					swingOffset = -1*WINDUP_ARC;
					windingUp = false;
					swinging = true;
					swingOut = true;
			}
		}
			

		
		if (swinging){
			if (swingOut){
				swingOffset += SWING_SPEED*(SWING_ARC -swingOffset);
				
				if (swingOffset >= SWING_ARC-1){
					swingOffset = SWING_ARC;
					swingOut = false;
				}
			}
			else{
				swingOffset -= SWING_RETURN_RATE;
				
				if (swingOffset <= 0){
					swingOffset = 0;
					swinging = false;
					swingOut = true;
				}
			}
			
		}
	}
	

}
