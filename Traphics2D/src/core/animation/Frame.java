package core.animation;

import java.awt.image.BufferedImage;

public class Frame {
	
	BufferedImage image;
	int frameDuration;
	
	
	public Frame(BufferedImage img, int frameLength){
		image = img;
		frameDuration = frameLength;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public int getDuration(){
		return frameDuration;
	}
	
	public int getWidth(){
		return image.getWidth();
	}
	
	public int getHeight(){
		return image.getHeight();
	}

}
