package core.gameObjects;

import graphics.AdvancedGraphics;
import java.util.ArrayList;
import java.util.List;

import core.basicInterfaces.Drawable;
import core.gameObjects.entities.Entity;

//Basically an ArrayList with some added functions to easily draw and update everything.
//updating of entities is done directly by the MotherList.
public class EntityList implements Drawable{
	List<Entity> entityList;
	
	public EntityList(){
		entityList = new ArrayList<Entity>();
	}
	
	public EntityList(List<Entity> entityList){
		this.entityList = entityList;
	}
	
	public void add(Entity entity){
		entityList.add(entity);
	}
	
	public void remove(Entity entity){
		entityList.remove(entity);
	}

	@Override
	public void draw(AdvancedGraphics pen){
		for (Entity e: entityList){
			e.draw(pen);
		}
	}
	
	public Entity get(int index){
		return entityList.get(index);
	}
	
	@Override
	public void debugDraw(AdvancedGraphics pen){
		for (Entity e: entityList)
			e.debugDraw(pen);
	}
	
	public List<Entity> getEntityList(){
		return entityList;
	}
	
	public int getSize(){
		return entityList.size();
	}
}
