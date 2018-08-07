package Shooter;
import java.awt.image.BufferedImage;

import core.basicInterfaces.*;
import core.console.Console;
import core.physics.Point;
import graphics.AdvancedGraphics;

public class Recticle implements Updateable, Drawable{

	BufferedImage image;
	Point location;
	
	public Recticle(BufferedImage image){
		this.image = image;
	}
	
	@Override
	public void update() {
		location = Console.getMousePositionInGamePanel();
		
		int x = location.getIntX()/3 - image.getWidth()/2;
		int y = location.getIntY()/3 - image.getHeight()/2;
		
		location = new Point(x,y);
		
	}
	
	@Override
	public void draw(AdvancedGraphics pen) {
		pen.drawImage(image, location);

	}

	@Override
	public void debugDraw(AdvancedGraphics pen) {
		pen.fillHexagon(location.getIntX(), location.getIntY(), 0, 5);
		
	}



}
