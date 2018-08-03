package core.legacy;
import particles.Particle;
import core.Camera;
import core.console.Letterboxing;
import core.gameObjects.EntityMotherList;
import core.physics.Point;

//Super dead. The old way of doing things. Kept around for reference.
public class LegacyWorld{
	
	//tileSet image name and the lowest possible depth for the game.
	final static String TILE_SET = "tileset_true.png";
	final int LOWEST_DEPTH = -10;
	
	//important objects.
	LegacyPlayer player;
	Camera cam;

	//System for storing all entities.
	public EntityMotherList entityStorage;

	int currentLevel = 1;
	int numSegments;
	public final static int NUM_LEVELS = 1;
	
	//because of some concurrent modification exceptions, the 
	//game will only call restart at the end of update(); 
	boolean readyToRestart;
	//Transition currentTransition = null;
	Letterboxing letterboxing;
	
	//pausing prevents updating. halting prevents new drawing.
	boolean paused = false;
	boolean halted = false;
	boolean debugDraw = false;

	//the global flip variable.
	boolean worldNormal;
	//worldFlipped = true means that the originally black tiles are solid.
	
	public int currentMapWidth;
	public int currentMapHeight;
	
	public int overallMapWidth;
	public int overallMapHeight;
	
	public int currentTileWidth;
	public int currentTileHeight;

	/*
	//simply restarts the current level.
	//negates the need to reload things.
	public void restart(){
		
		player.respawn();
	
		if (!worldNormal){
			flipWorld();
		}
		
		entityStorage.purge();
		entityStorage.addQueue(player);
		
		createImageTiles(levelSegments.get(0), new Point(0, 0));
		currentSegment = levelSegments.get(0);
		
		endX = levelSegments.get(0).getGameMap().getMapWidth()*16;
		mapOffsetY = 0;
		
		readyToRestart = false;
		letterboxing = new Letterboxing(Main.WIDTH, Main.HEIGHT, 30, 3);
		cam = new Camera(player.getLocation().getIntX(), player.getLocation().getIntY());
	}
	
	
	@Override
	public void start() {
		
		//levelSegments = new ArrayList<LevelSegment>();
		
		worldNormal = true;
		player = null;
		
		//mapOffsetY = 0;
		
		System.out.println("Current Level:" + currentLevel);
		
		readyToRestart = false;

		//BufferedImage coolSetImage = loadImage(TILE_SET);
		//coolSet = new TileSet(coolSetImage, 16, 16);

		entityStorage = new EntityMotherList();
		
		//Load all map segments----
		boolean failToLoad = false;
		int mapIndex = 0;
		while (!failToLoad){
			if ((new File("src/core/assets/levels/" + "runner_level" + mapIndex + ".tmx")).exists()){
				File mapFile = new File("src/core/assets/levels/" + "runner_level" + mapIndex + ".tmx");
				TmxMap newMap = new TmxMap(mapFile);
				//levelSegments.add(new LevelSegment(newMap));
				mapIndex++;
			}
			else{
				System.out.println("Level was not found.");
				failToLoad = true;
			}
		}
		numSegments = mapIndex+1;
		//-------------------
		
		//currentSegment = levelSegments.get(0);
		
		//createImageTiles(levelSegments.get(0), new Point(0, 0));
		//endX = levelSegments.get(0).getGameMap().getMapWidth()*16;

		letterboxing = new Letterboxing(Main.WIDTH, Main.HEIGHT, 30, 3);
		
		entityStorage.sortQueues();
		entityStorage.printSize();
		
		if (player == null)
			throw new RuntimeException("TMX Map has no player location.");
		
		cam = new Camera(player.getLocation().getIntX(), player.getLocation().getIntY());
		
		overallMapHeight = 0;
		overallMapWidth = 0;
		
		//-----------------------------
	}
	
	public boolean worldFlipped(){
		return worldNormal;
	}
	
	public Point getCameraLocation(){
		int x = cam.getIntX();
		int y = cam.getIntY();
		
		return new Point(x,y);
	}*/
	
	
/*	//takes a "Map" (constructed from a TMX file) and uses the data to make a bunch of image tiles.
	//anchor is the offest (in pixels) in the actual gameWorld. Not tiles.
	public void createImageTiles(Point anchor){
		
		TmxMap gameMap = segment.getGameMap();
		
		System.out.println("New Segment Loaded:");
		System.out.println("Map Width (tiles): " +gameMap.getMapWidth());
		System.out.println("Map Height (tiles): " +gameMap.getMapHeight());
		
		currentMapWidth = gameMap.getMapWidth();
		currentMapHeight = gameMap.getMapHeight();
		
		overallMapWidth += currentMapWidth;
		overallMapHeight += currentMapHeight;
		
		currentTileWidth = gameMap.getTileWidth();
		currentTileHeight = gameMap.getTileHeight();
		
		int startY = 0;
		int endY = 0;

		int currentDepth= LOWEST_DEPTH;
		
		int anchorX = anchor.getIntX();
		int anchorY = anchor.getIntY();
		
		boolean tileSolid;
		for (TmxLayer layer: gameMap.getLayerList()){

			String layerName = layer.getName();

			if (layerName.equals("solid"))
				tileSolid = true;
			else
				tileSolid = false;
			
			
			
			int tileCount = 0;

			for (Integer gid: layer.getTileList()){
				
				tileCount++;

				Point coords = TmxReader.countToCoords(tileCount, gameMap.getMapWidth());
				coords.setY(coords.getY()+1);
				
				int tileWidth = gameMap.getTileWidth();
				int tileHeight = gameMap.getTileHeight();
				
				if (gid != 0){
					
					int coordX = coords.getIntX()*tileWidth + anchorX;
					int coordY = coords.getIntY()*tileHeight + anchorY;
					//BufferedImage tileImage = coolSet.getTile(gid);
					//BufferedImage tileImageAlt = coolSet.getTile(gid+16);
					
					boolean tileFlipped = true;
					if (gid >=1 && gid <=4 ){
						tileFlipped = false;
					}
					
					//initial state of the tile changes if the world is flipped.
					tileFlipped = (tileFlipped && worldNormal);
					
					
					if (layerName.equals("player")){
						if (player==null){
							player = new LegacyPlayer(this, coordX + 4, coordY, 8, 16, currentDepth);
							entityStorage.addQueue(layer.getName(), player);
						}
					}
					
					else if (layerName.equals("start marker")){
						startY = coordY;
					}
					
					else if (layerName.equals("end marker")){
						endY = coordY;
					}

					else if (layerName.equals("top spikes")){
						//entityStorage.addQueue("spikes", new TopSpikeTile(coordX, coordY , tileSolid, currentDepth, tileImage, tileImageAlt, tileFlipped));
					}
					
					else if (layerName.equals("bottom spikes")){
						//entityStorage.addQueue("spikes", new BottomSpikeTile(coordX, coordY, tileSolid, currentDepth, tileImage, tileImageAlt, tileFlipped));
					}
					
					else if (layerName.equals("left spikes")){
						//entityStorage.addQueue("spikes", new LeftSpikeTile(coordX, coordY, tileSolid, currentDepth, tileImage, tileImageAlt, tileFlipped));
					}
					
					else if (layerName.equals("right spikes")){
						//entityStorage.addQueue("spikes", new RightSpikeTile(coordX, coordY,  tileSolid, currentDepth, tileImage, tileImageAlt, tileFlipped));
					}
					
					//Long Spikes---
					else if (layerName.equals("large top spikes")){
						//entityStorage.addQueue("spikes", new LargeTopSpikeTile(coordX, coordY , tileSolid, currentDepth, tileImage, tileImageAlt, tileFlipped));
					}
					
					else if (layerName.equals("large bottom spikes")){
						//entityStorage.addQueue("spikes", new LargeBottomSpikeTile(coordX, coordY, tileSolid, currentDepth, tileImage, tileImageAlt, tileFlipped));
					}
					
					else if (layerName.equals("large left spikes")){
						//entityStorage.addQueue("spikes", new LargeLeftSpikeTile(coordX, coordY, tileSolid, currentDepth, tileImage, tileImageAlt, tileFlipped));
					}
					
					else if (layerName.equals("large right spikes")){
						//entityStorage.addQueue("spikes", new LargeRightSpikeTile(coordX, coordY,  tileSolid, currentDepth, tileImage, tileImageAlt, tileFlipped));
					}
					
					//---
				
					else if (layerName.equals("black tiles")){
						entityStorage.addQueue("black tiles", new BlackWhiteTile(coordX, coordY, 16, 16, worldNormal, currentDepth));
					}
					
					else if (layerName.equals("white tiles")){
						entityStorage.addQueue("white tiles", new BlackWhiteTile(coordX, coordY, 16, 16, !worldNormal, currentDepth));
					}
					
					else{
						entityStorage.addQueue(layer.getName(), new ImageTile(coordX, coordY , 16, 16, tileSolid, currentDepth, tileImage));
						System.out.println("external entity");
					}
				}
			}
			currentDepth++;
		}
		
		//mapOffsetY += endY - startY;
		
	}*/
	

	
	//adds a particle to the entity storage system.
	public void addParticle(Particle particle){
		entityStorage.addQueue("particles", particle);
	}
	
	
/*	public void update(){
		if (!halted){
			if (!paused)
				actualUpdate();
				

			
			//letterboxing.update(this);
		}
	}*/
	
	
/*	public void actualUpdate() {
		
		//int extraSpace = endX - (cam.getIntX() + Main.WIDTH/2);
		if (extraSpace <=0){
			//excludes the first level.
			//TmxMap nextSegment = levelSegments.get(random.nextInt(numSegments-1));
			//LevelSegment nextSegment = levelSegments.get(random.nextInt(numSegments-1));
			
			//int markerOffset = currentSegment.calculateVertOffset(nextSegment) * 16;
			
			createImageTiles(nextSegment, new Point(endX, mapOffsetY + markerOffset));
			//endX += nextSegment.getGameMap().getMapWidth()*16;
			
		}
		
		if (!player.isDead())
			cam.jumpToTarget(player.getCenterLocation());
		
		cam.boundaryAlign(Integer.MIN_VALUE, Integer.MAX_VALUE,0 , Integer.MAX_VALUE); 
		
		if (player.isDead()&&(player.getDeathTimer()>10))
			setLevel(currentLevel);
		
		entityStorage.update(this);
		
		for (Entity warpPillar: entityStorage.getSubList("warp pillar").getEntityList())
			if (player.collidesWith(warpPillar)){
				WarpPillar wp = (WarpPillar) warpPillar;
				wp.trigger();
				nextLevel();
				break;
		}

		//Transition Stuff
		if (currentTransition != null){
			currentTransition.update(this);
			
		}
		
		if (readyToRestart){
			if (currentTransition == null)
				restart();
			else
				if (currentTransition.readyForSwitch())
					restart();
			
		}

	}*/
	

/*	@Override
	public void draw() {
		
		if (!halted){
			if (debugDraw){
				debugDraw();
			}
			else
				actualDraw();
		}
	}*/
	
	public void setLetterbox(boolean set){
		if (set)
			letterboxing.triggerGrowth();
		if (!set)
			letterboxing.triggerShrink();
	}

	
/*	public void drawBackgroundGradient(){
		int numGrad = 40;
		int step = Main.WIDTH/numGrad;
		for (int x=0; x<numGrad; x++){
			pen.setColor(new Color(230 + x/2, 230 + x/2, 230 + x/2));
			pen.fillRect(x *step , 0, step, Main.HEIGHT);
		}
	}*/
	
	/*public void actualDraw(){

		//drawBackgroundGradient();
		//pen.drawImage(background, 0, 0, Main.WIDTH, Main.HEIGHT);
		
		if (worldNormal){
			pen.setColor(Color.WHITE);
		}
		else
			pen.setColor(Color.BLACK);
		
		pen.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		
		//the "beat line"
		//pen.setColor(Color.WHITE);
		//pen.drawLine(new Point(0, Main.HEIGHT/2), new Point(Main.WIDTH, Main.HEIGHT/2));
		
		int parallaxX = (cam.getLocation().getIntX() - Main.WIDTH/2);
		int parallaxY = (cam.getLocation().getIntY() - Main.HEIGHT/2);
		//pen.moveCameraPosition(parallaxX/10, parallaxY/10);
		
		//pen.moveCameraPosition(-parallaxX/10, -parallaxY/10);
		pen.moveCameraPosition(parallaxX, parallaxY);
		
		entityStorage.draw(pen);
		//Calculating the position of the sun on screen.
		//240 is the location (top right) of the sun. 60 is the radius.
		//int sunX = parallaxX + 240 + 40 - parallaxX/10;
		//int sunY = parallaxY - 20 + 40 - parallaxY/10;
		//Point sunOnScreen = new Point(sunX, sunY);

		//cam.debugDraw(pen);
		
		//Reset Camera
		pen.moveCameraPosition(-1 * parallaxX, -1 * parallaxY);
		
		if (paused)
			drawPause();
		
		letterboxing.draw(pen);
		drawCurrentTransition();
	}*/
/*	
	public void drawPause(){
		
		if (paused){
			pen.setColor(new Color(0, 0, 0, 70));
			pen.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		}
	}

	
	public void debugDraw(){

		int parallaxX = (cam.getLocation().getIntX() - Main.WIDTH/2);
		int parallaxY = (cam.getLocation().getIntY() - Main.HEIGHT/2);

		pen.moveCameraPosition(parallaxX, parallaxY);
		
		entityStorage.debugDraw(pen);
		
		//Reset Camera
		pen.moveCameraPosition(-1 * parallaxX, -1 * parallaxY);
		
		//drawGrid(16);
		drawCurrentTransition();
		
	}*/
/*	
	public void drawGrid(int gridSize){
		pen.setColor(Color.GRAY);
		for (int x=0; x< Main.WIDTH/gridSize + 1; x++)
			pen.drawLine(new Point(gridSize * x, 0),  new Point(gridSize * x, Main.HEIGHT));
		for (int y=0; y<Main.HEIGHT/gridSize + 1; y++)
			pen.drawLine(new Point(0, y*gridSize),  new Point(Main.WIDTH, y*gridSize));
		
	}
	
	public void drawCurrentTransition(){
		if (currentTransition != null){
			currentTransition.draw(pen);
			
			if (currentTransition.isFinished())
				currentTransition = null;
		}
	}*/
	
/*	public void previousLevel(Transition transition){
		if (currentLevel > 1){
			setLevel(currentLevel-1, transition);
		}
	}*/
	/*
	public void previousLevel(){
		previousLevel(new Swipe(Main.WIDTH, Main.WIDTH));
	}
	*/
/*	public void nextLevel(Transition transition){
		if (currentLevel < NUM_LEVELS){
			setLevel(currentLevel+1, transition);
		}
	}*/
	/*
	public void nextLevel(){
		nextLevel(new Swipe(Main.WIDTH, Main.WIDTH));
	}*/
	
/*	public void setLevel(int level, Transition transition){
		if (currentTransition == null){
			currentLevel = level;
			readyToRestart = true;
			currentTransition = transition;
			
			setLetterbox(true);
			GameWorld.playSound("warp.wav", 0);
		}
	}*/
	/*
	public void setLevel(int level){
		setLevel(level, new Swipe(Main.WIDTH, Main.HEIGHT));
	}
*/
	public void pauseGame(){
		paused = true;
		setLetterbox(true);
	}
	
	public void unpauseGame(){
		paused = false;
		setLetterbox(false);
	}
	/*
	public void flipPause(){
		if (paused){
			unpauseGame();
			inputQueue.clear();
		}
		
		else
			pauseGame();
	}*/
	
	public void haltGame(){
		halted = true;
	}
	
	public void unhaltGame(){
		halted = false;
	}
	
	public void flipHalt(){
		halted =!halted;
	}
	/*
	//I feel like this whole input queue thing might work better in GameWorld.
	public void keyAction(KeyEvent key){
		
		if (key.getKeyChar()== 'p')
			flipPause();
		
		if (key.getKeyChar() == 'r')
			setLevel(currentLevel);
		
		else if (key.getKeyChar() == 't')
			previousLevel();
	
		else if (key.getKeyChar() == 'y')
			nextLevel();

		else if (key.getKeyChar() == '1')
			debugDraw =! debugDraw;
		
		//WHY DOES THE GAME NOT ENTIRELY PACK SOMETIMES? 
		else if (key.getKeyChar()=='x')
			GameWindow.pack();
		
		else if (key.getKeyChar()=='2')
			GameWindow.setScale(1);
	
		else if (key.getKeyChar()=='3')
			GameWindow.setScale(2);
		
		else if (key.getKeyChar()=='4')
			GameWindow.setScale(3);
		
		else if (key.getKeyChar()=='f'){
			flipWorld();
		}
		
			
		player.keyTriggered(key);
	}*/
	/*
	public void flipWorld(){
		if (player.isFlippable()){
			worldNormal = !worldNormal;
			
			EntityList blackTiles = entityStorage.getSubList("black tiles");
			EntityList whiteTiles = entityStorage.getSubList("white tiles");
			
			for (Entity e: blackTiles.getEntityList()){
				((BlackWhiteTile)e).flip();
			}
			
			for (Entity e: whiteTiles.getEntityList()){
				((BlackWhiteTile)e).flip();
			}
			
			for (Entity e: entityStorage.getSubList("spikes").getEntityList()){
				((SpikeTile)e).flip();
			}
			
			player.flip();
		}
	}*/
	
	public Point getPlayerLocation(){
		return new Point(player.getLocation().getIntX(), player.getLocation().getIntY());
	}
	
	public Point getPlayerCenterLocation(){
		return new Point(player.getCenterLocation().getIntX(), player.getCenterLocation().getIntY());
	}
	
	//Reads a text file from the specified path. x's become solid blocks. Everything else is empty space.
/*	public void readTestFile(String path){
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			int row = 0;
			while (line != null){
				for (int i=0; i< line.length(); i++){
					char c = line.charAt(i);
					if (c == 'x'){
						entityStorage.addQueue("imageTiles", new ImageTile(i*16, row*16, 16, 16, true, 0));
					}
				}
				row++;
				line = br.readLine();
			}
			player = new LegacyPlayer(this,16, 16*(row-2), 16, 16, 0);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
