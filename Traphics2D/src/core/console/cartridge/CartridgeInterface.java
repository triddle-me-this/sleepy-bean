package core.console.cartridge;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import core.basicInterfaces.Drawable;
import core.basicInterfaces.Updateable;

interface CartridgeInterface extends Updateable, Drawable{
	
	public void start();
	
	public void keyAction(KeyEvent event);
	public void mouseAction(MouseEvent event);

	
	
	

}
