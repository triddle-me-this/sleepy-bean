
package core.gameObjects.entities.activeEntities;

import graphics.AdvancedGraphics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import core.basicInterfaces.Drawable;
import core.basicInterfaces.Updateable;
import core.gameObjects.entities.Entity;
import core.legacy.LegacyWorld;
import core.physics.Vector;


public abstract class ActiveEntity extends Entity implements Updateable, Drawable{
	
	final static double STANDARD_GRAVITY = .32;
	
	//"leftover" fractions of a pixel. Useful when speed isn't an integer.
	double xScrap;
	double yScrap;
	
	public ActiveEntity(int x, int y, int width, int height, boolean solid, int depth, Vector velocity, Vector gravity){
		super(x,y,width,height,solid, depth, velocity, gravity);
		
		xScrap = 0;
		yScrap = 0;
	}
	
	public ActiveEntity(int x, int y, int width, int height, boolean solid, int depth){
		this(x,y, width, height,solid,depth,new Vector(0,0), new Vector(0, STANDARD_GRAVITY));
	}
	
	public ActiveEntity(int x, int y, int width, int height){
		this(x, y, width, height, false, 0);
	}
	
	
	//Override these methods.  -------------------
	public void downCollision(Entity collider){}
	
	public void upCollision(Entity collider){}
	
	public void rightCollision(Entity collider){}
	
	public void leftCollision(Entity collider){}
	//-----------------------------------------
	
	//Moves the ActiveEntity by the components of the given vector.
	public void move(List<Entity> solidEntityList, Vector vector){
		double dubX = vector.getX();
		double dubY = vector.getY();
		
		int intX = vector.getIntX();
		int intY = vector.getIntY();
		
		xScrap += (dubX - intX);
		yScrap += (dubY - intY);
		
		if (Math.abs(xScrap) >= 1){
			intX += (int)xScrap;
			xScrap -= ((int)xScrap);
		}
		if (Math.abs(yScrap) >=1){
			intY += (int)yScrap;
			yScrap -= ((int)yScrap);
		}
		
		moveAligned(solidEntityList, intX, intY);
	}
	
	//Moves the ActiveEntity horizontally and vertically by the given amount.
	public void moveAligned(List<Entity> solidEntityList, int xDist, int yDist){
		moveHorizontal(solidEntityList, xDist);
		moveVertical(solidEntityList, yDist);
	}
	
	//Moves the ActiveEntity Horizontally by the given amount.
	public void moveHorizontal(List<Entity> solidEntityList, int dist){
		if (dist>0){
			moveRight(solidEntityList, dist, true);
		}
		else if (dist<0){
			moveLeft(solidEntityList, -1*dist, true);
		}
	}
	
	//Moves the ActiveEntity Vertically by the given amount.
	public void moveVertical(List<Entity> solidEntityList, int dist){
		if (dist >0){
			moveDown(solidEntityList, dist, true);
		}
		if (dist<0){
			moveUp(solidEntityList, -1*dist, true);
		}
	}
	
	//Moves the ActiveEntity right by the given amount.
	//see moveLeft for documentation clarification
	public boolean moveRight(List<Entity> solidEntityList, int dist, boolean actuallyMove){
		
		boolean collision = false;
		
		int moveAmount = dist;
		ArrayList<Integer> intRows = getIntRows();
		//The received intRows are correct.
		
		int colPoint = (int)location.getX() + width;
		
		ArrayList<Entity> rowMatchedBlocks = new ArrayList<Entity>();
		
		for (Integer row: intRows){
			for (Entity b: solidEntityList){
				if (row == (int)b.getLocation().getY()){
					if (b.getLocation().getX() >= colPoint){
						if (b.isSolid())
							rowMatchedBlocks.add(b);
						}}}
		}
		
		double minDist = Double.POSITIVE_INFINITY;
		Entity closestBlock = null;
		
		for (Entity b: rowMatchedBlocks){
			if (Math.abs(b.getLocation().getX()-colPoint) < minDist){
				minDist = (Math.abs(b.getLocation().getX()-colPoint));
				closestBlock = b;
			}
		}
		
		if ((int)minDist < moveAmount){
			collision = true;
			if (actuallyMove)
				rightCollision(closestBlock);
		}
		
		moveAmount = getMin((int)minDist, moveAmount);
		if (actuallyMove)
			location.moveX(moveAmount);
		
		return collision;
	}
	
	//Moves the ActiveEntity left by the given amount.
	//actually move determines if the entity actually moves.
	//Because these functions can be used to find if a collision WOULD happen, this boolean argument
	//is used when testing for hypothetical collisions.
	public boolean moveLeft(List<Entity> solidEntityList, int dist, boolean actuallyMove){
		
		boolean collision = false;
		
		int moveAmount = dist;
		ArrayList<Integer> intRows = getIntRows();
		//The received intRows are correct.
		
		int colPoint = (int)location.getX();
		
		ArrayList<Entity> rowMatchedBlocks = new ArrayList<Entity>();
		
		for (Integer row: intRows){
			for (Entity b: solidEntityList){
				if (row == (int)b.getLocation().getY()){
					if (b.getLocation().getX() <= colPoint){
						if (b.isSolid())
							rowMatchedBlocks.add(b);
						}}}
		}
		
		double minDist = Double.POSITIVE_INFINITY;
		Entity closestBlock = null;
		
		for (Entity b: rowMatchedBlocks){
			if (Math.abs((b.getLocation().getX()+b.getWidth())-colPoint) < minDist){
				minDist = (Math.abs((b.getLocation().getX()+b.getWidth())-colPoint));
				closestBlock = b;
			}
		}
		
		if ((int)minDist < moveAmount){
			collision = true;
			if (actuallyMove)
				leftCollision(closestBlock);
			
		}
		
		moveAmount = getMin((int)minDist, moveAmount);
		if (actuallyMove)
			location.moveX(-1*moveAmount);
		
		return collision;
	}
	
	//Moves the ActiveEntity down by the given amount.
	public boolean moveDown(List<Entity> solidEntityList, int dist, boolean actuallyMove){
		
		boolean collision = false;
		
		int moveAmount = dist;
		ArrayList<Integer> intCols = getIntCols();
		
		int colPoint = (int)(location.getY() + height);
		
		ArrayList<Entity> colMatchedBlocks = new ArrayList<Entity>();
		
		for (Integer col: intCols){
			for (Entity b: solidEntityList){
				if (col == b.getLocation().getX()){
					if (b.getLocation().getY() >= colPoint){
						if (b.isSolid())
							colMatchedBlocks.add(b);
						}}}
		}
		
		double minDist = Double.POSITIVE_INFINITY;
		Entity closestBlock = null;
		
		for (Entity b: colMatchedBlocks){
			if (Math.abs(b.getLocation().getY()-colPoint)<minDist){
				minDist = Math.abs(b.getLocation().getY() - colPoint);
				closestBlock = b;
			}
		}
		
		if ((int)minDist < moveAmount){
			collision = true;
			if (actuallyMove)
				downCollision(closestBlock);
			
		}
		
		moveAmount = getMin((int)minDist, moveAmount);
		if (actuallyMove)	
			location.moveY(moveAmount);
		
		return collision;
	}
	
	//Moves the ActiveEntity up by the given amount.
	public boolean moveUp(List<Entity> solidEntityList, int dist, boolean actuallyMove){
		
		boolean collision = false;
		
		int moveAmount = dist;
		ArrayList<Integer> intCols = getIntCols();
		
		int colPoint = (int)(location.getY());
		
		ArrayList<Entity> colMatchedBlocks = new ArrayList<Entity>();
		
		for (Integer col: intCols){
			for (Entity b: solidEntityList){
				if (col == b.getLocation().getX()){
					if (b.getLocation().getY() <= colPoint){
						if (b.isSolid())
							colMatchedBlocks.add(b);
						}}}
		}
		
		double minDist = Double.POSITIVE_INFINITY;
		Entity closestBlock = null;
		
		for (Entity b: colMatchedBlocks){
			if (Math.abs((b.getLocation().getY()+b.getHeight())-colPoint)<minDist){
				minDist = Math.abs((b.getLocation().getY()+b.getHeight()) - colPoint);
				closestBlock = b;
			}
		}
		
		if ((int)minDist < moveAmount){
			collision = true;
			if (actuallyMove)
				upCollision(closestBlock);
			
		}
		
		moveAmount = getMin((int)minDist, moveAmount);
		

		if (actuallyMove)
			location.moveY(-1*moveAmount);
		
		return collision;
	}

	
	//returns the minimum of two given values.
	private int getMin(int v1, int v2){
		if (v1 < v2){
			return v1;
		}
		else if (v2<v1){
			return v2;
		}
		else
			return v1;
	}
	
	
	//Calculates which horizontal rows are intersected by the ActiveEntity.
	private ArrayList<Integer> getIntRows(){
		
		ArrayList<Integer> intRows = new ArrayList<Integer>();
		
		int numToCheck;
		if (height>=16){
			numToCheck = ((int)(height/16)) + 1;
			int leftover = location.getIntY()%16;
			//System.out.println("L:" + leftover);
			if (leftover==0)
				{numToCheck -=1;}
		}
		
		else{
			//some other way of finding numToCheck
			int topRound = (location.getIntY()-(location.getIntY()%16));
			int bottomRound = ((location.getIntY()+(width)) - ((location.getIntY()+(width))%16));
			
			//System.out.println(topRound + "   " + bottomRound);
			
			if ((location.getIntY()+(width))%16==0){
				numToCheck =1;
			}
			
			else{
				if (topRound == bottomRound)
					numToCheck = 1;
				else
					numToCheck = 2;
			}
			
		}
		
		int startRow = (location.getIntY()-(location.getIntY()%16))/16;
		
		for (int i=0; i<numToCheck; i++){
			intRows.add((startRow + i)*16);
		}
		
		//System.out.println(intRows);
	
		return intRows;
	}

	
	//calculates which vertical rows are intersected by the ActiveEntity.
	private ArrayList<Integer> getIntCols(){
		

		ArrayList<Integer> intCols = new ArrayList<Integer>();
		
		int numToCheck;
		if (width>=16){
			numToCheck = ((int)(width/16)) + 1;
			int leftover = location.getIntX()%16;
			//System.out.println("L:" + leftover);
			if (leftover==0)
				{numToCheck -=1;}
		}
		else{
			//some other way of finding numToCheck
			
			int leftRound = (location.getIntX()-(location.getIntX()%16));
			int rightRound = ((location.getIntX()+width) - ((location.getIntX()+width)%16));
			
			if (((location.getIntX()+width)%16)==0){
				numToCheck = 1;
			}
			else{
				if (leftRound == rightRound)
					numToCheck = 1;
				else
					numToCheck = 2;
			}
		}
		
		int startCol = (location.getIntX()-(location.getIntX()%16))/16;
		
		for (int i=0; i<numToCheck; i++){
			intCols.add((startCol + i)*16);
		}
		
		//System.out.println(intCols);
		return intCols;
		
	}
	
	@Override
	public void debugDraw(AdvancedGraphics pen){
		pen.setColor(Color.BLUE);
		pen.fillRect(location.getIntX(), location.getIntY(), width, height);
	}
	



}
