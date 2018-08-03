package core.console.cartridge.gameState;

import core.basicInterfaces.Drawable;
import core.basicInterfaces.Updateable;
import core.console.transitions.Swappable;
import core.gameObjects.EntityMotherList;

public abstract class GameState extends Swappable implements Updateable, Drawable{

	EntityMotherList entityStorage;
	
	public GameState(){
		entityStorage = new EntityMotherList();
	}
	
	public EntityMotherList getEntityStorage(){
		return entityStorage;
	}


}
