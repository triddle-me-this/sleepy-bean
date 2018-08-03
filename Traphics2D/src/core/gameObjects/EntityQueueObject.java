package core.gameObjects;

import core.gameObjects.entities.Entity;

//for use with the entity mother list thing.
//just keeps track of an entity and a tag associated with it
//(for the subList it is contained in)
public class EntityQueueObject {
	
	Entity entity;
	String subList;
	
	public EntityQueueObject(String s, Entity e){
		entity = e;
		subList = s;
	}
	
	public Entity getEntity(){
		return entity;
	}
	
	public String getSubList(){
		return subList;
	}

}
