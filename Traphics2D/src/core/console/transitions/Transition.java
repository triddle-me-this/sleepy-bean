package core.console.transitions;

import java.awt.Color;

import core.basicInterfaces.Drawable;
import core.basicInterfaces.Updateable;

public abstract class Transition<T extends Swappable> implements Drawable, Updateable{

	boolean isFinished = false;
	boolean readyForSwitch = false;
	
	int width;
	int height;
	
	Color color;
	
	T toSwap;
	
	public Transition(int w, int h, T toSwap){
		width = w;
		height = h;
		this.toSwap = toSwap;
	}
	
	//When that transition is fully completed.
	public boolean isFinished(){
		return isFinished;
	}
	
	//When the screen is ideally completely obscured and the content behind it can be switched.
	public boolean readyForSwitch(){
		return readyForSwitch;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public T getToSwap(){
		return toSwap;
	}
	
}
