package core.gameObjects.entities;

import core.physics.Point;
import core.physics.Vector;

public interface EntityInterface {

	int getDepth();
	void setDepth(int newDepth);
	Point getLocation();
	void setLocation(int x, int y);
	Point getTopRight();
	Point getBottomLeft();
	Point getBottomRight();
	Point getCenterLocation();
	
	Vector getVelocity();
	Vector getGravity();
	
	int getWidth();
	int getHeight();
	boolean isSolid();
	void setSolid(boolean newSolid);
	boolean isFinished();

	
}
