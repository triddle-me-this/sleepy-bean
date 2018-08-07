package core.animation;

import java.awt.image.BufferedImage;

import graphics.AdvancedGraphics;

public class Animation{
	
	Frame[] animationFrames;
	boolean loop;
	
	int currentFrame;
	int numFrames;
	
	int counter;
	//for looping animations
	boolean finished;
	
	//frameTime argument is the duration of all frames in the animation.
	public Animation(BufferedImage[] imageFrames, int frameTime, boolean loop){
		
		finished = false;
		animationFrames = new Frame[imageFrames.length];
		
		//Transfers the array of given images into an array of Frames.
		for (int i=0; i< imageFrames.length; i++){
			BufferedImage image = imageFrames[i];
			Frame frame = new Frame(image, frameTime);
			animationFrames[i] = frame;
		}
		
		this.loop = loop;
		currentFrame = 0;
		numFrames = animationFrames.length;
		
		counter = 0;
	}
	
	
	public void nextFrame(){
		if (currentFrame<numFrames-1)
			currentFrame++;
		else
			if (loop)
				currentFrame = 0;
			else
				finished = true;
			
	}
	
	public void prevFrame(){
		if (currentFrame>0)
			currentFrame--;
		else
			if (loop)
				currentFrame = (numFrames -1);
			else
				finished = true;
	}
	
	public void resetAnimation(){
		currentFrame = 0;
		finished = false;
	}
	
	public Frame getCurrentFrame(){
		return animationFrames[currentFrame];
	}
	
	

	public void update() {
		counter++;
		
		if (counter >= getCurrentFrame().getDuration()){
			counter = 0;
			nextFrame();
		}
	}
	
	
	public void draw(AdvancedGraphics pen, int x, int y) {
		BufferedImage image = getCurrentFrame().getImage();
		pen.drawImage(image, x, y);
	}
	
	public void draw(AdvancedGraphics pen, int x, int y, boolean mirroredX, boolean mirroredY){
		
		BufferedImage image = getCurrentFrame().getImage();
		pen.drawMirroredImage(image, x, y, mirroredX, mirroredY);

	}
	
	public void draw(AdvancedGraphics pen, int x, int y, boolean xFlip, boolean yFlip, boolean centered){
		int cX = x-getCurrentFrame().getWidth()/2;
		int cY = y-getCurrentFrame().getHeight()/2;
		
		draw(pen, cX, cY, xFlip, yFlip);
	}
	
	public int getNumFrames(){
		return numFrames;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	

}
