package core.testSetup;

import core.console.cartridge.Cartridge;
import graphics.AdvancedGraphics;

public class TestCartridge extends Cartridge{

	public TestCartridge(){
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start() {
		currentGS = new TestState();
	}

	@Override
	public void update() {
		currentGS.update();
	}

	@Override
	public void draw(AdvancedGraphics pen) {
		pen.drawString("Cartridge Copyright!", 200, 200);
		currentGS.draw(pen);
	}

	@Override
	public void debugDraw(AdvancedGraphics pen) {
		currentGS.debugDraw(pen);
	}

}