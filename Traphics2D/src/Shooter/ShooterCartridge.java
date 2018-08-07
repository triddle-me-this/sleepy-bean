package Shooter;

import core.console.cartridge.Cartridge;
import graphics.AdvancedGraphics;

public class ShooterCartridge extends Cartridge{

	public ShooterCartridge(){
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start() {
		currentGS = new Shooter1();
	}

	@Override
	public void update() {
		currentGS.update();
	}

	@Override
	public void draw(AdvancedGraphics pen) {
		currentGS.draw(pen);
	}

	@Override
	public void debugDraw(AdvancedGraphics pen) {
		currentGS.debugDraw(pen);
	}

}
