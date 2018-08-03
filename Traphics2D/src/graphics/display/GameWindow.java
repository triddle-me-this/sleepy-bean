package graphics.display;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;

import core.console.Console;
import core.console.cartridge.Cartridge;

//GameWindow is the first thing that is initialized. It holds a Jframe, and the JPanel that
//occupies all of the space of that Jframe.
public class GameWindow {
	
	//I'm really sketched about making this static, but I need to be able to access it :(
	static final JFrame mainFrame = new JFrame("Game Window");
	static GamePanel gamePanel;
	
	static int GAME_SCALE; 
	static int GAME_WIDTH; 
	static int GAME_HEIGHT;
	
	static int FRAME_WIDTH;
	static int FRAME_HEIGHT;
	static int FRAME_RATE;
	static boolean FULLSCREEN;
	
	public GameWindow(int gameWidth, int gameHeight, int gameScale, int frameRate, Color background, 
			String soundPath, String imagePath, Cartridge initCart, boolean fullScreen){
		
		GAME_SCALE = gameScale;
		GAME_WIDTH = gameWidth;
		GAME_HEIGHT = gameHeight;
		
		FULLSCREEN = fullScreen;
		FRAME_RATE = frameRate;
		
		
		//Create the Jframe.
		//mainFrame = new JFrame("Game Window");
		
		if (FULLSCREEN)
			initFullscreen();
		else
			initWindowed();
	
		
		mainFrame.getContentPane().setBackground(Color.BLACK);
		mainFrame.setLayout(new GridBagLayout());
		
		//Set attributes of the Jframe.
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		
		//Creates a new "World"
		//Makes the gamePanel, and adds it to the Jframe.
		//(the world is fed to the GamePanel constructor)
		gamePanel = new GamePanel(GAME_WIDTH, GAME_HEIGHT, GAME_SCALE, FRAME_RATE, soundPath, imagePath, initCart, background);
		mainFrame.getContentPane().add(gamePanel);
		
		if (FULLSCREEN)
			mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		else
			mainFrame.pack();
		
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setIconImage(Console.getImage("window/", "icon.png"));
		
		mainFrame.setVisible(true);
	}	
	
	public static void pack(){
		mainFrame.pack();
	}
	public void initFullscreen(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		
		FRAME_WIDTH = screenWidth;
		FRAME_HEIGHT = screenHeight;
		
		System.out.println("Screen Width: " +screenWidth);
		System.out.println("Screen Height: " + screenHeight);
		
		mainFrame.setUndecorated(true);
		mainFrame.setAlwaysOnTop(true);
	}
	
	public static void initWindowed(){
		FRAME_WIDTH = GAME_WIDTH * GAME_SCALE;
		FRAME_HEIGHT = GAME_HEIGHT * GAME_SCALE;
		
		System.out.println("Window Width: " +FRAME_WIDTH);
		System.out.println("Window Height: " + FRAME_HEIGHT);
	}

	
}
