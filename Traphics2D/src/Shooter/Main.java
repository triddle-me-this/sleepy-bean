package Shooter;

import java.awt.Color;

import core.console.cartridge.Cartridge;
import graphics.CustomColor;
import graphics.display.GameWindow;


public class Main {
	
	public final static int WIDTH = 480;
	public final static int HEIGHT = 270;
	public final static int SCALE = 2;
	public final static boolean FULLSCREEN = false;
	public final static int FRAME_RATE = 60;
	
	public final static Color BACKGROUND_COLOR = Color.WHITE;
	public final static Boolean CURSOR = false;
	
	public final static String SOUND_PATH = "src/core/assets/sounds/";
	public final static String IMAGE_PATH = "src/core/assets/images/";
	
	public final static Cartridge INITIAL_CARTRIDGE = new ShooterCartridge();
	

	public static void main(String args[]){
		
		new GameWindow(WIDTH, HEIGHT, SCALE, FRAME_RATE, BACKGROUND_COLOR, SOUND_PATH, IMAGE_PATH, INITIAL_CARTRIDGE, FULLSCREEN, CURSOR);
	}
}
 