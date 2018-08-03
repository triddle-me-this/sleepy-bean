package core.gameObjects;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import core.basicInterfaces.Drawable;
import core.basicInterfaces.Updateable;
import core.gameObjects.entities.Entity;
import graphics.AdvancedGraphics;

public class EntityMotherList implements Updateable, Drawable{

	EntityList allEntities;
	Map<String, EntityList> entityDict;
	
	//LinkedList is an implementation of Queue.
	LinkedList<EntityQueueObject> addQueue;
	LinkedList<Entity> removeQueue;
	
	public EntityMotherList(){
		
		allEntities = new EntityList();
		entityDict = new HashMap<String, EntityList>();
		
		removeQueue = new LinkedList<Entity>();
		addQueue = new LinkedList<EntityQueueObject>();
	}
	
	//prints size
	public void printSize(){
		System.out.println("Number of Entities: " + allEntities.getSize());
	}
	
	public void purge(){
		allEntities = new EntityList();
		entityDict = new HashMap<String, EntityList>();
		
		removeQueue = new LinkedList<Entity>();
		addQueue = new LinkedList<EntityQueueObject>();
	}
	
	//places an entity in queue to be added to the main list.
	public void addQueue(Entity e){
		addQueue.add(new EntityQueueObject(null, e));
	}
	
	//places an entity in queue to be added to the main list and a sublist.
	public void addQueue(String subListKey, Entity e){
		addQueue.add(new EntityQueueObject(subListKey, e));
	}
	
	//Be careful invoking this. Can cause concurrent modification exceptions.
	private void add(String subListKey, Entity entity){
		//If there is not a subList for the given tag, create one
		if (!entityDict.containsKey(subListKey))
			entityDict.put(subListKey, new EntityList());
			
		//add the entity to the specified subList
		EntityList subList = entityDict.get(subListKey);
		subList.add(entity);
		
		//add the entity to the masterList
		allEntities.add(entity);
	}
	
	//Be careful invoking this. Can cause concurrent modification exceptions.
	private void add(Entity entity){
		allEntities.add(entity);
	}
	
	//returns the entity subList associated with the given tag.
	public EntityList getSubList(String subListKey){
		EntityList subList = entityDict.get(subListKey);
		if (subList == null)
			return new EntityList();
		else
			return subList;
	}
	
	//replaces the entitylist of the given tag
	public void setSubList(String subListKey, EntityList newList){
		entityDict.put(subListKey, newList);
	}
	
	//returns all entities currently in the storage unit.
	public EntityList getAllList(){
		return allEntities;
	}
	
	
	//calls draw() on all entities in storage.
	@Override
	public void draw(AdvancedGraphics pen){
		
		//I guess this works? I'm not actually changing anything
		//but I guess it just doesn't like me iterating through stuff regardless of whether
		//or not I'm actually changing anything
		for (int i=0; i<allEntities.getSize(); i++){
			Entity e = allEntities.get(i);
			e.draw(pen);
		}
				
		//for (Entity e: allEntities.getEntityList())
		//	e.draw(pen);
	}
	
	//calls debugDraw() on all entities in storage.
	@Override
	public void debugDraw(AdvancedGraphics pen){

			for (int i=0; i<allEntities.getSize(); i++){
				Entity e = allEntities.get(i);
				e.debugDraw(pen);
			}
	}
	
	//calls update on all entities. Deals with the add and remove queues. Sorts by depth.
	@Override
	public void update(){
		for (Entity e: allEntities.getEntityList())
			if (e instanceof Updateable){
				((Updateable) e).update();
				if (e.isFinished()){
					removeQueue.add(e);
				}
			}
		
		sortQueues();
		Collections.sort(allEntities.getEntityList(), new DepthComparator());

	}
	
	//Removes entities that are in the remove queue and adds those that are in the add queue.
	public void sortQueues(){
		while (!removeQueue.isEmpty()){
			Entity eToRemove = removeQueue.remove();
			remove(eToRemove);
		}
		
		while (!addQueue.isEmpty()){
			EntityQueueObject queuedEntity = addQueue.remove();
			
			String subListKey = queuedEntity.getSubList();
			Entity entity = queuedEntity.getEntity();
			
			if (subListKey != null)
				add(subListKey, entity);
			else
				add(entity);
		}
	}
	
	//removes the given entity from the main list and all sublists.
	public void remove(Entity e){
		allEntities.remove(e);
		for (String key: entityDict.keySet()){
			EntityList subList = entityDict.get(key);
			subList.remove(e);
		}
	}
	
	//returns the set of tags for the entity dictionary.
	public Set<String> getKeySet(){
		return entityDict.keySet();
	}
	
	//returns number of entities in storage.
	public int getNumEntities(){
		return allEntities.getSize();
	}
	
	//Creates an Empty sublist
	public void createSubList(String subListKey){
		if (!entityDict.containsKey(subListKey))
			entityDict.put(subListKey, new EntityList());
	}
	
	public void clearSubList(String subListKey){
		entityDict.replace(subListKey, new EntityList());
	}

}
