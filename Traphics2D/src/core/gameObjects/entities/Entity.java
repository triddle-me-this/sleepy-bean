package core.gameObjects.entities;

import core.basicInterfaces.Drawable;
import core.physics.Point;
import core.physics.Vector;

public abstract class Entity implements EntityInterface, Drawable{

	protected int depth;
	protected Point location;
	protected int width;
	protected int height;
	protected boolean solid;
	protected boolean finished;
	protected Vector velocity;
	protected Vector gravity;

	
	public Entity(int x, int y, int width, int height, boolean solid, int depth, Vector velocity, Vector gravity){
		
		this.location = new Point(x,y);
		this.width = width;
		this.height = height;
		this.solid = solid;
		this.finished = false;
		this.depth = depth;
		this.velocity = velocity;
		this.gravity = gravity;
	}

	@Override
	public int getDepth() {
		return depth;
	}
	
	@Override 
	public void setDepth(int newDepth){
		depth = newDepth;
	}

	@Override
	public Point getLocation() {
		return location;
	}
	
	@Override
	public void setLocation(int x, int y){
		location = new Point(x, y);
	}
	
	@Override
	public Point getTopRight(){
		return new Point(location.getX()+width, location.getY());
	}
	
	@Override
	public Point getBottomLeft(){
		return new Point(location.getX(), location.getY() + height);
	}
	
	@Override
	public Point getBottomRight(){
		return new Point(location.getX()+width, location.getY() + height);
	}
	

	@Override
	public Point getCenterLocation() {
		return new Point(location.getX()+width/2, location.getY()+height/2);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean isSolid() {
		return solid;
	}
	
	@Override public void setSolid(boolean newSolid){
		solid = newSolid;
	}
	
	@Override
	public boolean isFinished(){
		return finished;
	}
	
	@Override
	public Vector getVelocity(){
		return velocity;
	}
	
	@Override 
	public Vector getGravity(){
		return gravity;
	}
	
	public boolean collidesWith(Entity e){
		if 
		((location.getIntX() < e.getLocation().getIntX() + e.getWidth())&&
		(location.getIntX() + getWidth() > e.getLocation().getIntX())&&
		(location.getIntY() < e.getLocation().getIntY() + e.getHeight())&&
		(location.getIntY() + getHeight() > e.getLocation().getIntY())){
			return true;
		}
		else
			return false;
	}
	
}
