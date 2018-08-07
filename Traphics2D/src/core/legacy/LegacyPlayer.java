package core.legacy;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import core.animation.Animation;
import core.animation.AnimationFactory;
import core.console.Console;
import core.gameObjects.entities.Entity;
import core.gameObjects.entities.activeEntities.ActiveEntity;
import core.physics.Point;
import core.physics.Vector;
import graphics.AdvancedGraphics;
import particles.DustParticle;
import particles.gore.BoneParticle;
import particles.gore.blood.BloodParticle;
import particles.gore.blood.DarkBloodParticle;
import particles.gore.blood.LightBloodParticle;

public class LegacyPlayer extends ActiveEntity{
	//Goal: High jump reaches jump above 4 blocks. Low jump reaches 2? blocks.
	
	LegacyWorld world;
	
	//Relevant Key Codes-----------------
	final static int JUMP_KEY = 32; //space
	final static int LEFT_KEY = 37; //left
	final static int RIGHT_KEY = 39; //right
	final static char GOD_KEY = 'q';
	
	//Gravities
	final static Vector NORMAL_GRAVITY = new Vector(0, .32);
	final static Vector FLIPPED_GRAVITY = new Vector(0, -.32);
	
	//Jump Frame Windows---------------------------
	//how many frames after last touching solid ground before the player 
	//can no longer jump
	final static int JUMP_FRAME_EXTENSION = 6; //6
	final static int HIGHJUMP_FRAME_WINDOW = 8;
	
	//Jumping-related Variables----------------
	final static double DEATH_JUMP_FORCE = 4.3; //4.3
	final static double JUMP_FORCE = 4.5; //5.2
	final static double SECOND_JUMP_FORCE = 2.0; //1.5
	
	//X accelerations (in terms of pixels per frame)------------
	//normal acceleration (grounded movement)
	final static double X_ACC = .6; //.6
	//in the air (not touching a wall)
	final static double AIR_X_ACC = .4; //.4
	double currentXAcc;
	
	//x decelerations (as a percent of the previous value)
	//These are just the same four states listed above for acc.
	final static double X_DCC = .82; //.82
	final static double AIR_X_DCC = .98; //.98
	double currentXDcc;
	
	//Speed Limitations------------------------------
	//max x velocity that a player can accelerate to.
	//Player can go faster than this, but they cannot influence themselves
	//to go faster. Force must come from outside.
	final static double MAX_X_RUN = 3.0; //2.0
	
	//player physically cannot move faster than this (x)
	final static double MAX_X_SPEED = 20.0; //20.0
	//The current speed that the player can accelerate to.
	double currentMaxX;
	final static double MAX_FALL_SPEED = 10.0;
	
	//State Variables------------------------
	boolean jumpable;
	boolean prevJumpable;
	boolean flipped;
	
	//frames since ground contact
	int framesSinceContact;
	//exactly what they sounds like 
	int framesSinceJump;
	
	//for direction of the sprite, not movement
	boolean facingLeft;
	boolean dead = false;
	
	Animation currentAnimation;
	HashMap<String, Animation> animationTable;
	
	Point initialLocation;
	
	int deathTimer;
	Random random = new Random();
	
	
	public LegacyPlayer(LegacyWorld world, int x, int y, int width, int height, int depth) {
		super(x, y, width, height, false, depth);
		
		flipped = false;
		deathTimer = 0;
		
		this.world = world;
		currentMaxX = MAX_X_RUN;
		
		currentXAcc = X_ACC;
		currentXDcc = X_DCC;
		
		facingLeft = false;

		clearJump();
		
		framesSinceContact = 20;
		framesSinceJump = 20;
		
		animationTable = new HashMap<String, Animation>();
		loadAnimations();
		currentAnimation = animationTable.get("idle");
		
		initialLocation = new Point(x,y);
	}
	
	public void flip(){
		if (jumpable){
			if (!flipped){
				this.getLocation().moveY(16);
			}
			else{
				this.getLocation().moveY(-16);
			}
			
			gravity.multiply(-1);
			flipped = !flipped;
			
			//add gravity and move the player such that they enter the grounded state immediately.
			velocity.add(gravity);
			List<Entity> solidList = getSolidList();
			this.moveVertical(world, solidList, (int)velocity.getY());
			
			touchGround(world);
		}
	}
	
	public void respawn(){
		dead = false;
		jumpable = true;
		setLocation(initialLocation.getIntX(), initialLocation.getIntY());
	}
	
	public boolean isFlippable(){
		return jumpable;
	}
	
	private void loadAnimations(){
		animationTable.put("idle", useAnimationFactory("idleAnimation.png", 16, 16, 10, true));
		animationTable.put("run", useAnimationFactory("runAnimation.png", 16, 16, 6, true));
		animationTable.put("jump", useAnimationFactory("jumpAnimation.png", 16, 16, 10, true));
		animationTable.put("wall", useAnimationFactory("walljumpAnimation.png", 16, 16, 1, false));
		animationTable.put("dead", useAnimationFactory("deadAnimation.png", 16, 16, 20, false));
	}
	
	//just calls the static AnimationFactory class to create a player animation with the given info.
	//Typing out calls to Animation factory is annoying and long.
	private Animation useAnimationFactory(String imageName, int frameWidth, int frameHeight, int frameDuration, boolean loop){
		return  AnimationFactory.createAnimation(Console.getImage("legacyPlayer", imageName), frameWidth, frameHeight, frameDuration, loop);
	}
	
	private void setAnimation(String newAnimation){
		currentAnimation = animationTable.get(newAnimation);
	}

	public void death(){
		
		if (!dead){
			
			setAnimation("dead");
			
			velocity.multiplyX(-1);
			velocity.multiplyY(-1);
			
			velocity.setY(-1*DEATH_JUMP_FORCE); 
			
			deathParticles();
			dead = true;
			jumpable = false;
			
			world.setLetterbox(true);

			//Console.playSound("death.wav", 0);	
		}
		
	}
	
	public void deathParticles(){
		for (int i=0; i<50; i++){
			world.addParticle(new BloodParticle(getCenterLocation().getIntX(),getCenterLocation().getIntY()));
			world.addParticle(new DarkBloodParticle(getCenterLocation().getIntX(),getCenterLocation().getIntY()));
			world.addParticle(new LightBloodParticle(getCenterLocation().getIntX(),getCenterLocation().getIntY()));
		}
			
		for (int i=0; i<10; i++){
			world.addParticle(new BoneParticle(getCenterLocation().getIntX(), getCenterLocation().getIntY()));
		}
	}
	
	public void keyTriggered(KeyEvent key){
		
		//System.out.println(key.getKeyCode());
		
		if (key.getKeyChar() == JUMP_KEY)
			jumpTriggered();		
	}


	@Override
	public void update() {
		
		boolean leftPressed;
		boolean rightPressed;
		boolean jumpPressed;
		
		//In full control, left right arrow keys to move, shift to sprint, space to jump.
		leftPressed = false; //LEFT_KEY
		rightPressed = true; //RIGHT_KEY
		jumpPressed = Console.isKeyPressed(JUMP_KEY);


		if (!dead){
			
			handleMovementAlive(leftPressed, rightPressed, jumpPressed);
			handleAnimationAlive();
		
			//Touching spikes
			for (Entity spike: world.entityStorage.getSubList("spikes").getEntityList()){
				if (collidesWith(spike)){
					if (!Console.isKeyPressed(GOD_KEY))
						death();
				}
			}
			
			//Touching enemies
/*			for (Entity enemyEntity: world.entityStorage.getSubList("enemies").getEntityList()){
				Enemy enemy = (Enemy)enemyEntity;
				if (collidesWith(enemy)&&(!enemy.isDead())){
						if (!Console.isKeyPressed(GOD_KEY))
							death();
					}
				}*/
			//Falling off the map
			/*
			int mapHeight = world.currentMapHeight * world.currentTileHeight;
			int windowHeight = Main.HEIGHT;
			
			int deathHeight;
			
			if (mapHeight > windowHeight)
				deathHeight = mapHeight;
			else
				deathHeight = windowHeight;
			
			if (location.getIntY() > deathHeight)
				death();*/
			//-------------------
		}
		else{
			handleMovementDead();
			handleAnimationDead();
			
			deathTimer++;
		}	
	}
		
	public int getDeathTimer(){
		return deathTimer;
	}
	
	public void resetDeathTimer(){
		deathTimer = 0;
	}
	
	private void handleAnimationDead(){
		setAnimation("dead");
	}
	
	public boolean isDead(){
		return dead;
	}
	
	private void handleAnimationAlive(){
		//Current facing direction (sprite)
		if ((int)velocity.getX() > 0)
			facingLeft = false;
		if ((int)velocity.getX() < 0)
			facingLeft = true;
		//------------------------
				
		//if the player is on the ground --------
		if (jumpable){
			if (Math.abs(velocity.getX()) <= .6)
				setAnimation("idle");
			else 
				setAnimation("run");
		}
		
		//if the player is in the air -----------
		else{
			setAnimation("jump");
		}
	
		currentAnimation.update();
	}
	
	private void handleMovementDead(){
		applyGravity();
		velocity.multiplyX(.98);
		location.move(velocity);
		
		if (random.nextInt(3)==1)
			world.addParticle(new BloodParticle(getCenterLocation().getIntX(),getCenterLocation().getIntY()));
		if (random.nextInt(3)==1)
			world.addParticle(new DarkBloodParticle(getCenterLocation().getIntX(),getCenterLocation().getIntY()));
		if (random.nextInt(3)==1)
			world.addParticle(new LightBloodParticle(getCenterLocation().getIntX(),getCenterLocation().getIntY()));
		
	}
	
	private void handleAccelerations(){
		
		//While touching the ground

		if (jumpable){
			currentXAcc = X_ACC;
			currentXDcc = X_DCC;
		}
		//In the air (not touching a wall)
		else{
			currentXAcc = AIR_X_ACC;
			currentXDcc = AIR_X_DCC;
		}
		
	}
	
	
	private void handleMovementAlive(boolean leftPressed, boolean rightPressed, boolean jumpPressed){
		
		prevJumpable = jumpable;

		handleAccelerations();
		MovementInputUpdate(leftPressed, rightPressed);
		applyGravity();
		
		move();

		//For checking if the second jump velocity should be applied.
		framesSinceJump++;
		
		if (framesSinceJump == HIGHJUMP_FRAME_WINDOW){
			if (jumpPressed)
				applyHighJump();
		}
		
		//Player can jump within 6 frames of touching the ground regardless of whether or not they are currently touching the ground.
		//For when you run off a ledge and die because you didn't jump when you totally should have.
		framesSinceContact ++;
		if (framesSinceContact >= JUMP_FRAME_EXTENSION)
			clearJump();
		
/*		if ((jumpable)&&(!prevJumpable)){
			GameWorld.playSound("landing.wav", 0);
		}*/
	}
	
	//Deals with input from the sprint key.
	@SuppressWarnings("unused")
	private void sprintInputUpdate(boolean sprintPressed){
		
		if (jumpable){
			if ((Math.abs(velocity.getX()) > MAX_X_RUN)){
				velocity.multiplyX(.95);
				currentMaxX = MAX_X_RUN;
			}
		}
	}
	
	//Deals with input from the left and right movement keys.
	private void MovementInputUpdate(boolean leftPressed, boolean rightPressed){
		//Simplified for the mobile format.
		
		if (rightPressed){
			velocity.setX(MAX_X_RUN);
		}
/*		if ((leftPressed)&&(!rightPressed)){
			if (velocity.getX() > -1*currentMaxX){
					velocity.add(new Vector(-1*currentXAcc, 0));
			}
		}
		else if ((!leftPressed)&&(rightPressed)){
			if (velocity.getX() < currentMaxX){
					velocity.add(new Vector(1*currentXAcc, 0));	
			}
		}
		else if ((leftPressed)&&(rightPressed)){
			//Do nothing
		}
		else{
			//If velocity is less than .5, round down to 0.
			if (Math.abs(velocity.getX()) < .5){
				velocity.setX(0);
			}
			else{
				velocity.multiplyX(currentXDcc);
			}
		}*/
	}
	
	//Applies Gravity. 
	private void applyGravity(){
		boolean applyGravity = true;
		double maxFall;
		
		maxFall = MAX_FALL_SPEED;
		if (!flipped){
			if (velocity.getY() > maxFall){
				//difference in speed between allowed and current fall speeds;
				double speedDifference = velocity.getY()-maxFall;
				double deltaSpeed = speedDifference*.1;
				velocity.addY(deltaSpeed);
				//If the fall speed is greater than the allowed speed, the players speed will move
				//10% of the way to the allowed speed (asymptotic approach).
				applyGravity = false;
			}
			
			if (applyGravity)
				velocity.add(gravity);
		}
		else{
			if (velocity.getY() < maxFall*-1){
				//difference in speed between allowed and current fall speeds;
				double speedDifference = velocity.getY()-maxFall*-1;
				double deltaSpeed = speedDifference*.1;
				velocity.addY(deltaSpeed*-1);
				//If the fall speed is greater than the allowed speed, the players speed will move
				//10% of the way to the allowed speed (asymptotic approach).
				applyGravity = false;
			}
			
			if (applyGravity)
				velocity.add(gravity);
		}
	}
	
	//Moves the player, taking into account collision.
	private void move(){
		
		List<Entity> solidList = getSolidList();
		
		this.moveHorizontal(world, solidList, (int)velocity.getX());
		this.moveVertical(world, solidList, (int)velocity.getY());
		
	}
	
	public List<Entity> getSolidList(){
/*		List<Entity> solidList;
		if (world.worldFlipped()){
			solidList = world.entityStorage.getSubList("black tiles").getEntityList();
		}
		else{
			solidList = world.entityStorage.getSubList("white tiles").getEntityList();
		}
		return solidList;*/
		
		return null;
	}

	//Called whenever the space bar is pressed.
	public void jumpTriggered(){
		jumpAttempt();
	}
	
	
	//Attempt a grounded jump.
	public void jumpAttempt(){
		double jumpForce;
		if (!flipped)
			jumpForce = -1*JUMP_FORCE;
		else
			jumpForce = JUMP_FORCE;
		
		if ((jumpable)){
			velocity.setY(jumpForce);
			jumpable = false;
			jumpParticles();
			framesSinceJump = 0;
			//Console.playSound("jump.wav", 0);
		}	
	}
	
	//Secondary Jump velocity
	public void applyHighJump(){
		if (!flipped){
			velocity.addY(-1*SECOND_JUMP_FORCE);
		}
		else
			velocity.addY(SECOND_JUMP_FORCE);
	}
	
	public void jumpParticles(){
		for (int i =0; i<7; i ++)
			world.addParticle(new DustParticle(getCenterLocation().getIntX(), location.getIntY()+getHeight(), .35));
	}
	
	public void wallJumpLeftParticles(){
		for (int i =0; i<5; i ++)
			world.addParticle(new DustParticle(location.getIntX() + getWidth(), location.getIntY()+getHeight(), .35));
	}
	
	public void wallJumpRightParticles(){
		for (int i =0; i<10; i ++)
			world.addParticle(new DustParticle(location.getIntX(), location.getIntY()+getHeight(), .35));
	}
	
	//Makes the player be able to jump from the ground.
	public void resetJump(){
		jumpable = true;
		framesSinceContact = 0;

	}
	
	//Makes the player not be able to jump from the ground.
	public void clearJump(){
		jumpable = false;
	}

	
	//Directional Collisions- Called whenever the player collides with a block in the world.
	
	public void touchGround(LegacyWorld world){
		resetJump();
		velocity.setY(0);
	}
	
	public void touchCeiling(LegacyWorld world){
		velocity.setY(0);
	}
	
	@Override
	public void upCollision(LegacyWorld world, Entity collider){
		if (flipped){
			touchGround(world);
		}
		else
			touchCeiling(world);
			
	}
	
	@Override
	public void downCollision(LegacyWorld world, Entity collider){
		if (flipped){
			touchCeiling(world);
		}
		else
			touchGround(world);
		
	}
	
/*	@Override
	public void rightCollision(LegacyWorld world, Entity collider){
		velocity.setX(0);
		//world.pauseGame();
		System.out.println("RIGHT COLLISION-----");
		Point point= collider.getLocation();
		Class c = collider.getClass();
		System.out.println("(" +point.getIntX() + ", " + point.getIntY() + ")   " + "Class: "+ c.getName());
		System.out.println("Player Location:   " + getLocation().getIntX() + " " + getLocation().getIntY());
		//System.out.println("Tile Solid?:" + ((BlackWhiteTile)collider).isSolid());
		
		//collider.highlight();
	}
	*/
	@Override
	public void leftCollision(LegacyWorld world, Entity collider){
		velocity.setX(0);
	}
	//------------------------------------------

	//Draws actual appearance of the player.
	@Override
	public void draw(AdvancedGraphics pen) {
		
		//Centers the sprite on the hitbox.
		int xDisplacement = currentAnimation.getCurrentFrame().getWidth() - getWidth();
		xDisplacement /=2;
		
		int drawX = location.getIntX() - xDisplacement;
		
		currentAnimation.draw(pen, drawX, location.getIntY(), facingLeft, flipped);
	}

	//Draws hitbox in a color based on jump state.
	@Override
	public void debugDraw(AdvancedGraphics pen) {
		
		pen.setColor(Color.BLUE);
		
		if (jumpable)
			pen.setColor(Color.RED);
		
		pen.fillRect(location.getIntX(), location.getIntY(), width, height);
	}
	
}
