package core.console.cartridge;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import core.Camera;
import core.console.cartridge.gameState.GameState;
import core.console.transitions.Swappable;

public abstract class Cartridge extends Swappable implements CartridgeInterface{
	
	public CartLib librarian;
	
	Camera camera;
	public GameState currentGS;

	public Cartridge(){
		
		librarian = new CartLib(this);
		camera = new Camera(0, 0);
	}
	
	@Override 
	//Should be overridden if used.
	public void keyAction(KeyEvent event){
	}
	
	@Override
	//should be overridden if used.
	public void mouseAction(MouseEvent event){
	}

	public GameState getCurrentGS(){
		return currentGS;
	}
	
}
