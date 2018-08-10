package core.console.cartridge;

import java.util.ArrayList;
import java.util.Collection;
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
		Collection<Entity> readOnly = Collections.unmodifiableCollection(originalList);
		
		ArrayList<Entity> newList = new ArrayList<Entity>();
		
		for (Entity e: readOnly){
			newList.add(e);
		}
		
		return new EntityList(newList);
	}
	
	
}
