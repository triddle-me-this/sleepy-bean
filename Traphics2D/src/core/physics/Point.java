package core.physics;

public class Point {

	private double x;
	private double y;
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double dist(Point otherPoint){
		double newX = otherPoint.x - x;
		double newY = otherPoint.y - y;
		
		double sum = (newX * newX) + (newY * newY);
		double dist = Math.sqrt(sum);
		
		return dist;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public int getIntX(){
		return (int)x;
	}
	
	public int getIntY(){
		return (int)y;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	
	public void moveX(double amount){
		this.x += amount;
	}
	
	public void moveY(double amount){
		this.y += amount;
	}
	
	public void move(Vector vec){
		this.x += vec.getX();
		this.y += vec.getY();
	}
	
	public Vector makeVector(Point otherPoint){
		double newX = otherPoint.getX() - this.x; 
		double newY = otherPoint.getY() - this.y;

		return new Vector(newX,newY);
	}
	
	//angle from the vertical
	public double vertAngleBetween (Point otherPoint){
		double xDiff = otherPoint.getX() - this.x; 
		double yDiff = otherPoint.getY() - this.y;

		double theta = Math.atan(xDiff/yDiff);
		return theta;
	}
	
	//angle from the horizontal
	public double horAngleBetween(Point otherPoint){
		double xDiff = otherPoint.getX() - this.x; 
		double yDiff = otherPoint.getY() - this.y;

		double theta = Math.atan(yDiff/xDiff);
		return theta;
	}
	
}
