package core.console.transitions;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import core.console.Console;
import core.console.cartridge.Cartridge;
import core.physics.Point;
import core.physics.Vector;
import graphics.AdvancedGraphics;

public class Swipe extends Transition{
	
	Point p1;
	Point p2;
	Point p3;
	Point p4;
	
	final static int SLANT = 500;
	final static double X_ACC = 4.0;
	ArrayList<Point> pointList;
	
	Vector velocity = new Vector(0,0);
	
	File soundFile;

	public Swipe(int width, int height, Cartridge toSwap) {
		this(width,height,Color.BLACK, null, toSwap);
	}
	
	public Swipe(int w, int h, Color color, File soundFile, Cartridge toSwap){
		super(w, h, toSwap);
		
		this.soundFile = soundFile;
		
		p1 = new Point(0-w*2, 0);
		p2 = new Point(0, 0);
		p3 = new Point(0-SLANT, h);
		p4 = new Point(0-w*2-SLANT, h);

		pointList = new ArrayList<Point>();
		
		pointList.add(p1);
		pointList.add(p2);
		pointList.add(p3);
		pointList.add(p4);	
		
		this.color = color;
		
		Console.playSound(soundFile, 0);
	}

	@Override
	public void draw(AdvancedGraphics pen) {
		debugDraw(pen);
	}

	@Override
	public void debugDraw(AdvancedGraphics pen) {
		
		pen.setColor(color);
		pen.fillPolygon(pointList);
	}

	@Override
	public void update() {

		velocity.add(new Vector(X_ACC, 0));
		
		for (Point p: pointList)
			p.move(velocity);
		
		if (p4.getIntX() >= width)
			isFinished = true;
		
		if (p3.getIntX() > width)
			readyForSwitch = true;
	}

}
