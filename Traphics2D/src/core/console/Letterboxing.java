package core.console;
import java.awt.Color;

import core.basicInterfaces.*;
import graphics.AdvancedGraphics;

public class Letterboxing implements Drawable, Updateable{
	
	int width;
	int height;
	int currentThick;
	int maxThick;
	int changeRate;
	boolean grow;
	Color color = Color.BLACK;
	
	public Letterboxing(int width, int height, int maxThick, int changeRate){
		this.width = width;
		this.height = height;
		this.currentThick = 0;
		this.grow = false;
		this.maxThick = maxThick;
		this.changeRate = changeRate;
	}
	
	public void triggerGrowth(){
		grow = true;
	}
	
	public void triggerShrink(){
		grow = false;
	}
	
	@Override
	public void update() {
		if (grow){
			if (currentThick < maxThick){
				currentThick += changeRate;
			}
		}
		else
			if (currentThick > 0){
				currentThick -= changeRate;
			}
		
	}
	@Override
	public void draw(AdvancedGraphics pen) {
		pen.setColor(color);
		pen.fillRect(0, 0, width, currentThick);
		pen.fillRect(0, height - currentThick, width, currentThick);
		
	}
	@Override
	public void debugDraw(AdvancedGraphics pen) {
		//Do Nothing 
		
	}
	
	
}
