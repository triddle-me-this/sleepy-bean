package core.console.cartridge;

import java.util.ArrayList;
import java.util.Collections;

import core.gameObjects.EntityList;
import core.gameObjects.entities.Entity;

//the "Cartridge Librarian"
//Used to access all "public records" of a cartridge.
public class CartLib {

	Cartridge cartridge;
	
	public CartLib(Cartridge cartridge){
		this.cartridge = cartridge;
	}
	
	public EntityList getSublistClone(String tag){
		ArrayList<Entity> originalList = (ArrayList<Entity>) cartridge.getCurrentGS().getEntityStorage().getSubList(tag).getEntityList();
		ArrayList<Entity> readOnly = (ArrayList<Entity>) Collections.unmodifiableCollection(originalList);
		
		return new EntityList(readOnly);
	}
	
	
}
