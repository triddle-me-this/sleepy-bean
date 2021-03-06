package Shooter.weapons;

import java.awt.Color;
import java.awt.image.BufferedImage;

import core.basicInterfaces.Drawable;
import core.basicInterfaces.Updateable;
import core.console.Console;
import core.gameObjects.EntityList;
import core.gameObjects.entities.activeEntities.Player;
import core.physics.Point;
import core.physics.Vector;
import graphics.AdvancedGraphics;
import Shooter.Main;

public abstract class Weapon implements WeaponInterface{

	Point location;
	//location at which the weapon is pointed.
	Point target;
	
	BufferedImage image;
	//direction the weapon is actually pointing
	double aimAngle;
	//changes WHERE the weapon is held, not what it looks like
	double angleOffset;
	//change in angle due to animation (sword swing)
	double swingOffset;
	//change in draw angle due to blocking
	double blockOffset;
	
	//distance held away from the player
	double holdDistance;
	
	//frames required to reload
	int recoilTime;
	int recoilCount;
	boolean fireable;
	

	//sprite offset for this specific weapon.
	int xOffset;
	int yOffset;
	
	//where the tip of the weapon is. for muzzle flash effects.
	Point muzzleLocation;
	
	public Weapon(int x, int y, BufferedImage image, double holdDistance, int recoilTime, int xOff, int yOff, double angOff){
		
		location = new Point(x, y);
		target = new Point(0,0);
		
		this.image = image;
		
		this.holdDistance = holdDistance;
		this.recoilTime = recoilTime;
		recoilCount = 0;
		
		fireable = true;
		
		xOffset = xOff;
		yOffset = yOff;
		angleOffset = angOff;
		swingOffset = 0;
		blockOffset = 0;
		
		muzzleLocation = new Point(x, y);
	}
	
	public void update(Point location) {
		//update with new location -------------
		this.location = location;
		
		//aim Weapon at the mouse -------------
		Point mouse = Console.getMousePositionInGamePanel();
		int x = mouse.getIntX()/Main.SCALE;
		int y = mouse.getIntY()/Main.SCALE;
		target = new Point(x,y);
		
		aimWeapon(target);
		
		//deal with recoil (time between shots)
		if (!fireable){
			recoilCount ++;
			if (recoilCount >= recoilTime){
				fireable = true;
			}
		}	
		
		weaponUpdate();
	}
	
	public void aimWeapon(Point target){
		
		//determine aim angle
		aimAngle = Math.toDegrees(location.horAngleBetween(target));
		if (target.getIntX() <= location.getIntX()){
			aimAngle += 180;
		}
		//make all angles between 0, 360 ----------
		aimAngle = normalizeAngle(aimAngle);
	}
	
	public void attemptFire(){
		if (fireable){
			fire();
			fireable = false;
			recoilCount = 0;
		}
	}

	
	//untested. For muzzle flash effects.
	public Point getMuzzlePoint(){
		//Determine the point to draw at--------------
		Vector weaponOffset = location.makeVector(target);
		
		//apply angular offset-------------
		double theta = Math.toDegrees(Math.atan(weaponOffset.getY() / weaponOffset.getX()));
		theta += angleOffset;
		theta += swingOffset;
		
		double theta2 = Math.toRadians(theta);
		
		double newX = Math.cos(theta2);
		double newY = Math.sin(theta2);
		
		if (weaponOffset.getX()<0){
			newX *=-1;
			newY *=-1;
		}
		
		Vector offset = new Vector(newX, newY);
		offset.setLength(image.getWidth());
		
		Point muzzle = new Point(location.getX(), location.getY());
		muzzle.move(offset);
		
		muzzle.moveX(xOffset);
		muzzle.moveY(yOffset);
		
		return muzzle;
	}
	
	//where the weapon is actually drawn at.
	public Point getOffsetPoint(){
		//Determine the point to draw at--------------
		Vector weaponOffset = location.makeVector(target);
		
		//apply angular offset-------------
		double theta = Math.toDegrees(Math.atan(weaponOffset.getY() / weaponOffset.getX()));
		theta += angleOffset;
		theta += swingOffset;
		
		double theta2 = Math.toRadians(theta);
		
		double newX = Math.cos(theta2);
		double newY = Math.sin(theta2);
		
		if (weaponOffset.getX()<0){
			newX *=-1;
			newY *=-1;
		}
		
		
		Vector finalOffset = new Vector(newX, newY);
		finalOffset.setLength(holdDistance);
		//-----------------------------------
		
		Point drawPoint = new Point(location.getIntX() -image.getWidth()/2, location.getIntY()-image.getWidth()/2);
		drawPoint.move(finalOffset);
		
		//shift to look correct on the player sprite
		drawPoint.moveY(yOffset); //-4
		drawPoint.moveX(xOffset); //1
		
		return drawPoint;
	}
	
	//The weapons "orientation" relative to the player
	//angle at which the weapon is drawn at (for draw order with player)
	public double getDrawAngle(){
		
		double ang = aimAngle + angleOffset + swingOffset + blockOffset;
		return normalizeAngle(ang);
		
	}
	
	public double normalizeAngle(double angle){
		while (angle <0){
			angle += 360;
		}
		while (angle > 360){
			angle -= 360;
		}
		
		return angle;
	}
	

	public void draw(AdvancedGraphics pen) {
		
		Point drawPoint = getOffsetPoint();
		
		//flip the image for specific angles
		BufferedImage drawImage = image;
		if (getDrawAngle() > 90 && getDrawAngle() < 270){
			drawImage = (BufferedImage) pen.getMirroredImage(drawImage, false, true);
		}
		
		aimAngle = aimAngle - (aimAngle % 10);
		
		
		pen.drawRotatedImage(drawImage, new Point(drawPoint.getIntX(), drawPoint.getIntY()), (int)(getDrawAngle()));
		//debug: -----------------------
		//pen.setColor(Color.RED);
		//pen.drawRect(drawPoint.getIntX(), drawPoint.getIntY(), image.getWidth(), image.getWidth());
	}
}
