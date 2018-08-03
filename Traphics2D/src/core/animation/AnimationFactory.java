package core.animation;

import java.awt.image.BufferedImage;

public class AnimationFactory {

	
	public static BufferedImage[] createImageArray(BufferedImage frameSheet, int frameWidth, int frameHeight){
		 
		int arrayLength = (int)(frameSheet.getHeight() / frameHeight);
		BufferedImage[] imageArray = new BufferedImage[arrayLength];
		
		for (int i=0; i < arrayLength; i++){
			BufferedImage image = frameSheet.getSubimage(0, frameHeight * i, frameWidth, frameHeight);
			imageArray[i] = image;
		}
		
		return imageArray;
		
	}
	
	public static Animation createAnimation(BufferedImage frameSheet, int frameWidth, int frameHeight, int frameTime, boolean loop){
		
		BufferedImage[] imageArray = createImageArray(frameSheet, frameWidth, frameHeight);
		Animation createdAnimation = new Animation(imageArray, frameTime, loop);
		
		return createdAnimation;
	}
	
}

