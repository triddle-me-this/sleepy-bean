package particles;

import java.awt.Color;

import core.physics.Vector;
import graphics.AdvancedGraphics;

public class DustParticle extends Particle{
	
	static final Color COLOR = new Color(255,255,255);
	static final int SIZE = 6;
	static final double SPEED = .1;
	static final int DEPTH = 0;
	static final int LIFESPAN = 50;

	public DustParticle(int x, int y) {
		this(x, y, SPEED);
	}
	
	public DustParticle(int x, int y, double speed){
		super(x, y, speed, SIZE, DEPTH, COLOR);
		this.gravity = new Vector(0, -.01);
		//Overrides the original lifespan value of 100;
		this.lifeSpan = LIFESPAN;
		
	}
	
	@Override 
	public void draw(AdvancedGraphics pen){
		Color baseColor = getColor();
		int r = baseColor.getRed();
		int g = baseColor.getGreen();
		int b = baseColor.getBlue();
		
		double alpha = (1.0-((double)timer/(double)lifeSpan)) * 255.0;
		
		Color fadedColor = new Color(r, g, b, (int)alpha);

		pen.setColor(fadedColor);
		pen.fillCircle(location.getIntX(), location.getIntY(), getSize());
	}
	
	


}
