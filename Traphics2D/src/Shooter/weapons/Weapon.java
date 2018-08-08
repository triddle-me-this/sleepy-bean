package Shooter.weapons;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Shooter.Main;
import core.basicInterfaces.Drawable;
import core.basicInterfaces.Updateable;
import core.console.Console;
import core.gameObjects.EntityList;
import core.gameObjects.entities.activeEntities.Player;
import core.physics.Point;
import core.physics.Vector;
import graphics.AdvancedGraphics;

public abstract class Weapon{

	Point location;
	Point target;
	
	BufferedImage image;
	double aimAngle;
	
	public Weapon(int x, int y, BufferedImage image){
		
		location = new Point(x, y);
		target = new Point(0,0);
		
		this.image = image;
	}
	
	public void update(Point location) {
		//update with new location
		this.location = location;
		
		//Determine target location
		Point mouse = Console.getMousePositionInGamePanel();
		
		int x = mouse.getIntX()/Main.SCALE;
		int y = mouse.getIntY()/Main.SCALE;
		
		target = new Point(x,y);
		
		//determine aim angle
		aimAngle = Math.toDegrees(location.horAngleBetween(target));
		if (target.getIntX() <= location.getIntX()){
			aimAngle += 180;
		}
		//make all angles between 0, 360 ----------
		while (aimAngle <0){
			aimAngle += 360;
		}
		while (aimAngle > 360){
			aimAngle -= 360;
		}
	}
	

	public void draw(AdvancedGraphics pen) {
		
		//Determine the point to draw at--------------
		Vector weaponOffset = location.makeVector(target);
		weaponOffset.setLength(10);
		
		Point drawPoint = new Point(location.getIntX() -image.getWidth()/2, location.getIntY()-image.getWidth()/2);
		drawPoint.move(weaponOffset);
		
		drawPoint.moveY(-4);
		drawPoint.moveX(1);
		
		//determine the image to use, draw the image. -------------
		
		BufferedImage drawImage = image;
		
		if (aimAngle > 90 && aimAngle < 270){
			drawImage = (BufferedImage) pen.getMirroredImage(drawImage, false, true);
		}
		
		pen.drawRotatedImage(drawImage, new Point(drawPoint.getIntX(), drawPoint.getIntY()), image.getWidth(), image.getWidth(), (int)aimAngle);

		
	}



}
