package core;

import java.awt.image.BufferedImage;

public class TileSet {

	BufferedImage image;
	
	int tileWidth;
	int tileHeight;
	
	int setWidth;
	int setHeight;
	
	public TileSet(BufferedImage image, int tileWidth, int tileHeight){
		
		this.image = image;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		setWidth = image.getWidth()/tileWidth;
		setHeight = image.getHeight()/tileHeight;
	}
	
	//returns the tile at a given coordinate.)
	public BufferedImage getTile(int xCoord, int yCoord){
		
		int x = xCoord * tileWidth;
		int y = yCoord * tileHeight;

		BufferedImage tile = image.getSubimage(x, y, tileWidth, tileHeight);
		return tile;
	}
	
	//counts from left to right, jumps down a row when the end of a row is reached.
	public BufferedImage getTile(int count){
		
		int remainder = count % setWidth;
		if (remainder ==0)
			remainder = setWidth;
		remainder --;
		
		int numRows = (count - remainder)/setWidth;
		
		return getTile(remainder, numRows);
		
	}
	
}
