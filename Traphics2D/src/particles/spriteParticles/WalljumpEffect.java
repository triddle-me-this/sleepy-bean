package particles.spriteParticles;

import java.awt.Color;
import java.awt.image.BufferedImage;

import core.animation.Animation;
import core.animation.AnimationFactory;
import core.console.Console;
import graphics.AdvancedGraphics;
import particles.Particle;

public class WalljumpEffect extends Particle{
	
	Animation animation;
	final static int FRAME_WIDTH = 8;
	final static int FRAME_HEIGHT = 8;
	final static int FRAME_TIME = 4;
	
	boolean mirrored;

	public WalljumpEffect(int x, int y, int depth, boolean mirrored) {
		super(x, y, 0, 5, depth, new Color(0, 0, 0));
		
		this.mirrored = mirrored;
		BufferedImage frameSheet = Console.getImage("legacyPLayer", "walljumpEffect.png");
		animation = AnimationFactory.createAnimation(frameSheet, FRAME_WIDTH, FRAME_HEIGHT, FRAME_TIME, false);
	}
	
	@Override
	public void update(){
		animation.update();
		
		if (animation.isFinished())
			finished = true;
		
	}
	
	@Override
	public void draw (AdvancedGraphics pen){
		
		animation.draw(pen, location.getIntX(), location.getIntY(), mirrored, false);
	}

}
