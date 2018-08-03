package core.physics;

public class Vector {

	private double x;
	private double y;
	
	//Preparation for upscaling the game.
	public final static double vecScale = 1.0;
	
	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public int getIntX(){
		return (int)getX();
	}
	
	public int getIntY(){
		return (int)getY();
	}
	
	public double getScaledX(){
		return this.x * vecScale;
	}
	
	public double getScaledY(){
		return this.y * vecScale;
	}
	
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
	
	public void addX(double d){
		this.x += d;
	}
	
	public void addY(double d){
		this.y += d;
	}
	
	public void multiply(double factor){
		this.x *= factor;
		this.y *= factor;
	}
	
	public void multiplyX(double factor){
		this.x *= factor;
	}
	
	public void multiplyY(double factor){
		this.y*= factor;
	}
	
	public void divide(double factor){
		this.x /= factor;
		this.y /= factor;
	}
	
	public void divideX(double factor){
		this.x /= factor;
	}
	
	public void divideY(double factor){
		this.y /= factor;
	}
	
	public void add(Vector vec){
		this.x += vec.getX();
		this.y += vec.getY();
	}
	
	
	public void subtract(Vector vec){
		this.x -= vec.getX();
		this.y -= vec.getY();
	}
	
	public double getLength(){
		double sum = (x*x)+(y*y);
		return Math.sqrt(sum);
	}
	
	public void normalize(){
		double length = getLength();
		
		if (length !=0){
			x /= length;
			y /= length;
		}
	}
	
	public void limit(double limit){
		if (getLength() > limit){
			setLength(limit);
		}
	}
	
	public void limitX(double limit){
		if (x>limit){
			x = limit;
		}
		if (x<-1*limit){
			x = -1*limit;
		}
	}
	
	public void limitY(double limit){
		if (y>limit){
			y = limit;
		}
		if (y < -1*limit){
			y = -1*limit;
		}
	}
	
	public void setLength(double length){
		normalize();
		multiply(length);
	}
}
