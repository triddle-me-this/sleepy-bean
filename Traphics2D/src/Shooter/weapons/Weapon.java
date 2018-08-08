package Shooter.weapons;

import java.awt.Color;
import java.awt.image.BufferedImage;

import core.basicInterfaces.Drawable;
import core.basicInterfaces.Updateable;
import core.console.Console;
import core.gameObjects.EntityList;
import core.gameObjects.entities.activeEntities.Player;
import core.physics.Point;
import graphics.AdvancedGraphics;

public abstract class Weapon implements Updateable, Drawable{

	Point location;
	
	BufferedImage image;
	double aimAngle;
	
	public Weapon(int x, int y, BufferedImage image){
		location = new Point(x, y);
		this.image = image;
	}
	
	@Override
	public void update() {
		Point mouse = Console.getMousePositionInGamePanel();
		
		EntityList playerList = Console.getCartLib().getSublistClone("player");
		
		if (playerList.getSize()>0){
			Player player = (Player) playerList.get(0);
			location = player.getCenterLocation();
			
			int xDiff = mouse.getIntX() - location.getIntX();
			int yDiff = mouse.getIntY() - location.getIntY();
			
			aimAngle = Math.toDegrees(Math.tan(yDiff/xDiff));
		}
		
		
	}

	@Override
	public void draw(AdvancedGraphics pen) {
		pen.setColor(Color.RED);
		
		pen.drawRotatedImage(image, location, image.getWidth(), image.getHeight(), (int)aimAngle);
		pen.fillHexagon(location.getIntX(), location.getIntY(), 0, 5);
		
	}

	@Override
	public void debugDraw(AdvancedGraphics pen) {
		// TODO Auto-generated method stub
		
	}


}
