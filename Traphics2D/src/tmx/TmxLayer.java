package tmx;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


//Storage object for TMX layers.
public class TmxLayer {
	
	ArrayList<Integer> tileList;
	String layerName;

	public TmxLayer(String name, Node dataNode){
		
		layerName = name;
		
		tileList = new ArrayList<Integer>();
		
		NodeList tileNodeList = dataNode.getChildNodes();
		for (int index=0; index< tileNodeList.getLength(); index++){
			Node tileNode = tileNodeList.item(index);

			if (tileNode.getNodeName()=="tile"){
				Element element = (Element)tileNode;
				String string = (element.getAttribute("gid"));
				
				int gid = Integer.parseInt(string);
				
				tileList.add(gid);
			}
		}
	}
	
	public ArrayList<Integer> getTileList(){
		return tileList;
	}
	
	public String getName(){
		return layerName;
	}
	
	
	
}
