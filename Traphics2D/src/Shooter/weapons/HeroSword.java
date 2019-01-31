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
	
	boolean swinging;
	//which direction the swing is going (false is returning to rest)
	boolean swingOut;
	
	public HeroSword(int x, int y) {
		super(x, y, Console.getImage("weapons/", "wep_heroSword.png"), HOLD_DISTANCE, 
				RECOIL_TIME, X_OFFSET, Y_OFFSET, ANG_OFFSET);
	}

	public void fire() {
		System.out.println("Swish!");
		swinging = true;
		swingOut = true;
	}
	
	public void weaponUpdate(){
		
		if (swinging){
			if (swingOut){
				swingOffset += .55*(180 -swingOffset);
				
				if (swingOffset >= 179){
					swingOffset = 180;
					swingOut = false;
				}
			}
			else{
				swingOffset -= 6;
				
				if (swingOffset <= 0){
					swingOffset = 0;
					swinging = false;
					swingOut = true;
				}
			}

		}
	}
	

}
