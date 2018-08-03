package graphics;

import java.awt.Color;
import java.util.Random;


public class CustomColor {
	
	
	private static Random rand = new Random();
	
	 public static Color skyBlue = new Color(135, 206, 250);
	 public static Color deepSkyBlue = new Color(0, 191, 255);
	 public static Color bloodRed = new Color(200, 0, 0);
	 public static Color darkBloodRed = new Color(180, 0, 0);
	 public static Color lightBloodRed = new Color(220, 0, 0);
	 public static Color boneWhite = new Color(230, 230, 230);
	 public static Color debugPurple = new Color(255, 0, 255);
	 
	 
	 public static Color getRandomRed(){
		 int r = rand.nextInt(55)+200;
		 int g = rand.nextInt(20)+20;
		 int b = rand.nextInt(20)+20;
		 return new Color(r,g,b);
	 }

}
