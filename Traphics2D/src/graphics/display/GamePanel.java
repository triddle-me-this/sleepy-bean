package graphics.display;
import graphics.AdvancedGraphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import core.console.Console;
import core.console.cartridge.Cartridge;

//PREVIOUSLY:
//pretty much just manages the timing mechanism, and redirects all the updating and drawing to the GameWorld.
//Also exists as a component for the listeners to work in (but all redirect as well).

//GamePanel is initialized by its holder, a GameWindow
@SuppressWarnings("serial")
public class GamePanel extends JPanel{

	Color BACKGROUND_COLOR = Color.BLACK;
	Console console;

	//current frame of the game
	static BufferedImage gameImage;

	int WIDTH;
	int HEIGHT;
	int SCALE;
	
	//desired frame rate
	int FRAME_RATE;
	
	AdvancedGraphics advancedPen;
	
	public GamePanel(int width, int height, int scale, int desiredFrameRate, String soundPath, String imagePath, Cartridge initCart){
		
		super(true);
		console = new Console(soundPath, imagePath, initCart);
		
		addMouseListener(console);
		addMouseMotionListener(console);
		addKeyListener(console);
		
		WIDTH = width;
		HEIGHT = height;
		SCALE = scale;
		FRAME_RATE = desiredFrameRate;

		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		this.setMinimumSize(new Dimension(WIDTH* SCALE, HEIGHT*SCALE));
		this.setMaximumSize(new Dimension(WIDTH* SCALE, HEIGHT*SCALE));
		gameImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		//Sets basic attributes and listeners of the panel.
		setFocusable(true);
		requestFocusInWindow();
	
		launchGameLoop();
	}
	
	public GamePanel(int width, int height, int scale, int frameRate, String soundPath, String imagePath, Cartridge initCart,
			Color background){
		this(width,height,scale, frameRate, soundPath, imagePath, initCart);
		BACKGROUND_COLOR = background;
	}
	
	//returns the currently held image. Static so that it can be called from anywhere.
	//(specifically, the world object.)
	public static Image getGameImage(){
		return gameImage;
	}
	
	//GAME LOOP METHODS --------------------------------------------
	public void launchGameLoop(){
		Thread gameLoop = new Thread(){
			public void run(){
				gameLoop();
			}
		};
		gameLoop.start();
	}
	
	private void gameLoop(){
		final double TIME_BETWEEN_UPDATES =  1000000000/FRAME_RATE;
		double lastUpdateTime = System.nanoTime();
		boolean runLoop = true;
		
		while (runLoop){
			double now = System.nanoTime();
			
			if (now - lastUpdateTime >= TIME_BETWEEN_UPDATES){
				console.update();
				repaint();
				
				lastUpdateTime = now;
			}
		}
	}

	//The call to repaint() in the timer loop just redirects to here. The panel is cleared,
	//and the color set to black (black being the default color.) The world's draw method is then called.
	public void paint(Graphics mainPen){
	
		if (advancedPen == null){
			Graphics basicPen = gameImage.getGraphics();
			advancedPen = new AdvancedGraphics(basicPen);
		}
		
		clear(advancedPen);
		advancedPen.setColor(Color.BLACK);
		
		console.draw(advancedPen);
		
		mainPen.drawImage(gameImage, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		
	}
	
	//------------------------------
	
	//Just draws a rectangle the width and height of the panel. Essentially "clears" it.
	public void clear(AdvancedGraphics pen){
		pen.setColor(BACKGROUND_COLOR);
		pen.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	

}
