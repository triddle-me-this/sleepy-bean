package Shooter;

import core.console.Console;
import core.console.cartridge.gameState.GameState;
import core.gameObjects.entities.activeEntities.Player;
import graphics.AdvancedGraphics;

public class Shooter1 extends GameState{

	Player player;
	Recticle recticle;
	
	public Shooter1(){
		player = new Player(0, 0, 10, 10);
		recticle = new Recticle(Console.getImage("HUD/", "spr_recticle.png"));
		entityStorage.addQueue("player", player);
	}
	
	@Override
	public void update() {
		entityStorage.update();
		recticle.update();
		
	}

	@Override
	public void draw(AdvancedGraphics pen) {
		entityStorage.draw(pen);
		recticle.draw(pen);
		
	}

	@Override
	public void debugDraw(AdvancedGraphics pen) {
		entityStorage.debugDraw(pen);
		recticle.debugDraw(pen);
	}

}
