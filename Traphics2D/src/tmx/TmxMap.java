package tmx;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TmxMap {

	List<TmxLayer> layerList;
	Map<String, TmxLayer> layerDict;
	
	int mapWidth;
	int mapHeight;
	int tileHeight;
	int tileWidth;
	
	double version;
	String orientation;
	Color backgroundColor;
	
	public TmxMap(File xmlFile){
		
		layerList = TmxReader.readLayers(xmlFile);

		layerDict = new HashMap<String, TmxLayer>();
		for (TmxLayer layer: layerList){
			layerDict.put(layer.getName(), layer);
		}
		
		mapHeight = TmxReader.readMapHeight(xmlFile);
		mapWidth = TmxReader.readMapWidth(xmlFile);
		
		tileHeight = TmxReader.readTileHeight(xmlFile);
		tileWidth = TmxReader.readTileWidth(xmlFile);
		
		
		version = TmxReader.readMapVersion(xmlFile);
		orientation = TmxReader.readMapOrientation(xmlFile);
		backgroundColor = TmxReader.readMapBackgroundColor(xmlFile);
	
	}
	
	
	
	public void printSelf(){
		for (TmxLayer layer: layerList){
			System.out.println("layer: "+ layer.getName());
			
			for (Integer i: layer.getTileList()){
				System.out.println("     gid: " + i);
			}
		}
	}
	
	public List<TmxLayer> getLayerList(){
		return layerList;
	}
	
	public TmxLayer getLayer(String layerTag){
		return layerDict.get(layerTag);
	}
	
	public int getMapWidth(){
		return mapWidth;
	}
	
	public int getMapHeight(){
		return mapHeight;
	}
	
	public int getTileWidth(){
		return tileWidth;
	}
	
	public int getTileHeight(){
		return tileHeight;
	}
	
	public double getVersion(){
		return version;
	}
	
	public String getOrientation(){
		return orientation;
	}
	
	public Color getBackgroundColor(){
		return backgroundColor;
	}
}
