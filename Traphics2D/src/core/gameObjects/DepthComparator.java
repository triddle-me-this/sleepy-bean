package core.gameObjects;

import java.util.Comparator;

import core.gameObjects.entities.Entity;

public class DepthComparator implements Comparator<Entity>{
	@Override
	public int compare(Entity a, Entity b){
		return a.getDepth() < b.getDepth() ? -1 : a.getDepth() == b.getDepth() ? 0: 1;
	}

}
