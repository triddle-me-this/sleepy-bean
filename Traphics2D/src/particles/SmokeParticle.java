package particles;

import java.awt.Color;

import core.physics.Vector;

public class SmokeParticle extends Particle{
	
	static Color COLOR = new Color(0,0,0,100);
	static int SIZE = 7;
	static int DEPTH = 0;
	static int VELOCITY = 1;

	public SmokeParticle(int x, int y, double velocity) {
		super(x, y, velocity, SIZE, DEPTH, COLOR);
		
		this.gravity = new Vector(0, -.05);
	}

}
