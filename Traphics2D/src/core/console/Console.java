package core.console;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import core.basicInterfaces.Drawable;
import core.basicInterfaces.Updateable;
import core.console.cartridge.CartLib;
import core.console.cartridge.Cartridge;
import core.console.transitions.Swappable;
import core.console.transitions.Transition;
import core.physics.Point;
import graphics.AdvancedGraphics;
import testSetup.Main;

public class Console extends Swappable implements MouseListener, MouseMotionListener, KeyListener, Drawable, Updateable{

	static String soundPath;
	static String imagePath;
	
	private static int relativeMouseX;
	private static int relativeMouseY;
	
	private long initialTime;
	private int framesElapsed;
	private long secondsElapsed;
	//calculated "actual" frame rate
	private long frameRate;
	
	private static Cartridge currentCartridge;
	
	private Letterboxing letterboxing;
	private Transition<Cartridge> currentTransition;
	
	//input tables
	private static Hashtable<Integer, Boolean> keyPressTable = new Hashtable<Integer, Boolean>();
	private static Hashtable<Integer, Boolean> mousePressTable = new Hashtable<Integer, Boolean>();
	
	//asset tables
	private static Hashtable<String, BufferedImage> imageTable = new Hashtable<String, BufferedImage>();
	private static Hashtable<String, File> soundTable = new Hashtable<String, File>();
	
	//the input queues. Deals only with key "triggers" (the first frame the key/mouse is pressed)
	private LinkedList<KeyEvent> keyInputQueue = new LinkedList<KeyEvent>();
	private LinkedList<MouseEvent> mouseInputQueue = new LinkedList<MouseEvent>();
	
	
	public Console(String sp, String ip, Cartridge initCart){
		this(sp, ip);
		insertCartridge(initCart);
	}
	
	public Console(String sp, String ip){
		
		soundPath = sp;
		imagePath = ip;
		
		//Overlay items
		currentTransition = null;
		letterboxing = new Letterboxing(Main.WIDTH, Main.HEIGHT, 30, 3);
		
		relativeMouseX = 0;
		relativeMouseY = 0;
	
		framesElapsed = 0;
		secondsElapsed = 0;
		frameRate = 0;
		initialTime = System.currentTimeMillis();
	}
	
	public static void insertCartridge(Cartridge cartridge){
		currentCartridge = cartridge;
		currentCartridge.start();
	}
	
	public static void removeCartridge(){
		currentCartridge = null;
	}
	
	public static CartLib getCartLib(){
		return currentCartridge.librarian;
	}
	

	//UPDATE METHODS ----------------------------------------------
	@Override
	public void update(){
		
		emptyInputQueues();
		
		if (currentCartridge != null){
			currentCartridge.update();
		}
		
		letterboxing.update();
		updateTransition();
		framesElapsed++;
		updateFrameRate();
	}
	
	public void updateTransition(){
		
		if (currentTransition != null){
			currentTransition.update();
			
			if (currentTransition.readyForSwitch()){
				currentCartridge = (Cartridge) currentTransition.getToSwap();
			}
			
			if (currentTransition.isFinished()){
				currentTransition = null;
			}
		}
	}
	
	public void emptyInputQueues(){
		while(!keyInputQueue.isEmpty()){
			KeyEvent key = keyInputQueue.remove();
			keyAction(key);
		}
		while(!mouseInputQueue.isEmpty()){
			MouseEvent mouse = mouseInputQueue.remove();
			mouseAction(mouse);
		}
	}
	
	public void updateFrameRate(){
		long currentMilTime = System.currentTimeMillis();
		long milPassed = currentMilTime - initialTime;
		
		secondsElapsed = milPassed/1000;
		
		if (secondsElapsed >= 1.00){
			
			frameRate = framesElapsed;
			
			framesElapsed = 0;
			initialTime = System.currentTimeMillis();
		}
	}
	//-------------------------------------------------
	
	//DRAW METHODS------------------------------------
	@Override
	public void draw(AdvancedGraphics pen){
		
		if (currentCartridge != null){
			currentCartridge.draw(pen);
		}
		
		drawTransition(pen);
		letterboxing.draw(pen);
		
		pen.setColor(Color.BLACK);
		pen.drawString(Long.toString(frameRate), 5, 15);
	}
	
	@Override
	public void debugDraw(AdvancedGraphics pen){
		
	}
	
	public void drawTransition(AdvancedGraphics pen){
		if (currentTransition != null){
			currentTransition.draw(pen);
		}
	}
	//-----------------------------------------------------------
	
	//SOUND ASSET METHODS--------------------------------------------------------------
	public static void loadSound(String subPath, String name){
		try {
	    	File file = new File((soundPath+ subPath + name));
	    	soundTable.put(name, file);
	    	
	      } catch (Exception e) {
	        System.err.println(e.getMessage());
	      }
	}
	
	public static File getLoadedSound(String name){
		return soundTable.get(name);
	}
	
	public static void playLoadedSound(String name, int numLoop){
		new Thread(new Runnable() {
			  // The wrapper thread is unnecessary, unless it blocks on the
			  // Clip finishing; see comments.
			    public void run() {
			      try {
			    	File file = soundTable.get(name);
			    	AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
			    	
			        Clip clip = AudioSystem.getClip();
			        clip.open(inputStream);
			        clip.loop(numLoop);
			        clip.start(); 
			      } catch (Exception e) {
			        System.err.println(e.getMessage());
			      }
			    }
			  }).start();
	}
	
	public static void playSound(File soundFile, int numLoop){
		new Thread(new Runnable() {
			  // The wrapper thread is unnecessary, unless it blocks on the
			  // Clip finishing; see comments.
			    public void run() {
			      try {
			    	AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
			    	
			        Clip clip = AudioSystem.getClip();
			        clip.open(inputStream);
			        clip.loop(numLoop);
			        clip.start(); 
			      } catch (Exception e) {
			        System.err.println(e.getMessage());
			      }
			    }
			  }).start();
	}
	
	public static File loadAndGetSound(String subPath, String name){
		loadSound(subPath, name);
		return getSound(subPath, name);
	}
	
	//Does not require use of the table and is set as static for universal use.
	public static File getSound(String subPath, String name){
		try {
	    	File file = new File((soundPath + subPath + name));
	    	return file;
	    	
	      } catch (Exception e) {
	        System.err.println(e.getMessage());
	      }
		
		return null;
	}
	//----------------------------------------------------------------------------
	
	//IMAGE ASSET METHODS ---------------------------------------------------------------
	//loads an Image into the imageTable. Loaded images must come from the assets package.
	public static BufferedImage loadImage(String subPath, String name) {
		try {
			imageTable.put(name, ImageIO.read(new File(imagePath + subPath + name)));
			
			return imageTable.get(name);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//returns an image from the imageTable.
	public static BufferedImage getLoadedImage(String name){
		return imageTable.get(name);
	}
	
	//saves to the table and returns
	public static BufferedImage loadAndGetImage(String subPath, String name){
		
		BufferedImage image;
		
		try {
			image = ImageIO.read(new File(imagePath + subPath + name));
			imageTable.put(name, image);
		} catch (IOException e) {
			e.printStackTrace();
			image = null;
		}
		
		return image;
	}
	
	//does not require use of the table. Set as static for universal use.
	public static BufferedImage getImage(String subPath, String name){
		BufferedImage image;
		
		try {
			image = ImageIO.read(new File(imagePath + subPath + name));
		} catch (IOException e) {
			e.printStackTrace();
			image = null;
		}
		
		return image;
	}
	//---------------------------------------------------------------
	

	//The following listeners all just redirect to the listeners of the same names held by the GameWorld object.
	@Override
	public void mouseClicked(MouseEvent event) {
		//Currently no use for this method.
	}
	
	@Override
	public void mouseEntered(MouseEvent event) {
		//Currently no use for this method.
	}
	
	@Override
	public void mouseExited(MouseEvent event) {
		//Currently no use for this method.
	}
	
	@Override
	public final void mousePressed(MouseEvent event) {
		
		//if the mouse was not pressed before, call the trigger function.
		
		if ((mousePressTable.containsKey((int)event.getButton()))){
			if (mousePressTable.get(event.getButton())==false){
				mouseTriggered(event);
				//System.out.println("(Mouse) This value ("+ Integer.toString(event.getButton()) +") was previously false.");
			}
		}
		else{
			mouseTriggered(event);
			//System.out.println("(Mouse) This value ("+Integer.toString(event.getButton()) +") was not in the table.");
		}
		
		mousePressTable.put((int)event.getButton(), true);

	}
	
	@Override
	public final void mouseReleased(MouseEvent event) {
		mousePressTable.put((int)event.getButton(), false);
	}
	
	//called once when mouse is first pressed
	public void mouseTriggered(MouseEvent event){
		mouseInputQueue.add(event);
	}

	public final static boolean isMousePressed(int b){
		if (mousePressTable.containsKey(b)){
			return mousePressTable.get(b);
		}
		else
			return false;
	}
	
	
	@Override
	public void mouseDragged(MouseEvent event) {
		//Currently no use for this method.
	}
	
	
	@Override
	public void mouseMoved(MouseEvent event) {
		//Currently no use for this method.
		
		relativeMouseX = event.getX();
		relativeMouseY = event.getY();
	}
	
	public static Point getMousePositionInGamePanel(){
		return new Point(relativeMouseX, relativeMouseY);
	}
	
	//returns the location of the mouse. (relative to the entire screen.)
	public final static Point getMouseLocationOnScreen(){
		double newX = MouseInfo.getPointerInfo().getLocation().getX();
		double newY = MouseInfo.getPointerInfo().getLocation().getY();
		
		Point newPoint = new Point(newX, newY);
		return newPoint;
	}
	
	@Override
	//Whoaaaa, the final modifier works with methods, sweet.
	public final void keyPressed(KeyEvent event) {
		
		int kc = event.getExtendedKeyCode();
		
		//If the table previous held a false value, this is the first iteration at which the key
		//has been pressed. The "key tapped" function (keyTriggered) is called.
		if ((keyPressTable).containsKey(kc)){
			if (keyPressTable.get(kc)==false){
				keyTriggered(event);
				//System.out.println("(Key) This value ("+ Integer.toString(kc)+") was previously false");
			}
		}
		else{
			keyTriggered(event);
			//System.out.println("(Key) The value ("+ Integer.toString(kc)+") was not in the table.");
		}
		
		keyPressTable.put(kc, true);
	}
	
	@Override
	public final void keyReleased(KeyEvent event) {
		
		int kc = event.getExtendedKeyCode();
		keyPressTable.put(kc, false);
	}
	
	//When a key is triggered, the input is added to a queue of button presses.
	//These button presses are run through the keyAction function during the next update cycle.
	public void keyTriggered(KeyEvent key){
		
		if (key.getExtendedKeyCode() == 27)
			System.exit(0);
		else
			keyInputQueue.add(key);
	}
	
	public final static boolean isKeyPressed(int kc){
		
		if (keyPressTable.containsKey(kc)){
			return keyPressTable.get(kc);
		}
		else
			return false;
	}
	
	public final static boolean isKeyPressed(char c){
		
		c = Character.toUpperCase(c);
		int kc = (int)c;
		
		if (keyPressTable.containsKey(kc)){
			return keyPressTable.get(kc);
		}
		else
			return false;
	}
	
	//what happens when a key is finally pulled from the input queue.
	public void keyAction(KeyEvent event){
		currentCartridge.keyAction(event);
	}
	
	//what happens when a mouseEvent is pulled from the mouse queue
	public void mouseAction(MouseEvent event){
		currentCartridge.mouseAction(event);
	}
	
	@Override
	public void keyTyped(KeyEvent event) {
		//Probably don't use. Doesn't work as well for games as the other two key listeners.
	}

	
	
}
