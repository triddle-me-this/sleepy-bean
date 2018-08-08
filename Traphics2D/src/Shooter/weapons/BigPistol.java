package Shooter.weapons;



import core.console.Console;

public class BigPistol extends Weapon{
	
	final static int HOLD_DISTANCE = 10;
	final static int RELOAD_TIME = 10;
	final static int X_OFFSET = 1;
	final static int Y_OFFSET = -4;
	final static double ANG_OFFSET = 0;
	
	public BigPistol(int x, int y) {
		super(x, y, Console.getImage("weapons/", "wep_bigPistol.png"), HOLD_DISTANCE, 
				RELOAD_TIME, X_OFFSET, Y_OFFSET, ANG_OFFSET);
	}

	public void fire() {
		System.out.println("Fire!");
		
	}



}
