package particles;

import java.awt.Color;

public class SparkleParticle extends SmokeParticle{

	static final int VELOCITY = 1;
	
	public SparkleParticle(int x, int y) {
		super(x, y, VELOCITY);
		this.color = new Color(0, 0, 200);
	}


}
