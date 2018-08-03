package particles.gore;

import particles.Particle;
import graphics.CustomColor;


public class BoneParticle extends Particle{
	
	static int DEPTH = 0;

	public BoneParticle(int x, int y) {
		super(x, y, 1.5, 4, DEPTH, CustomColor.boneWhite);
	}

	
	
}
