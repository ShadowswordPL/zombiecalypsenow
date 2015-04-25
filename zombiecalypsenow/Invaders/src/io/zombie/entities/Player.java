package io.zombie.entities;

import io.zombie.sound.Sound;

public final class Player {
    private final static Player ourInstance = new Player();
    
    public enum State {normal, withLeech}
    
    private final int maxlife = 10;
    private final int maxshield = 3;
        
    private int shield;
    private int life;
    private int gotHits = 0;
    private State mystate = State.normal;
    
    private boolean readytohit = true;
    
    
    public static Player getInstance() {
    	return ourInstance;
    }
 
    private Player() {
        resetPlayer();
    }
        
    public void gotLeech()
    {
    	Sound.getInstance().playLeech();
    	mystate = State.withLeech;
    }
    
    public void killedLeech()
    {
    	mystate = State.normal;
    }
    
    public boolean haveLeech(){
    	return mystate == State.withLeech;
    }
    
    public void gotHit(int x){
    	Sound.getInstance().playGothit();
    	if (shield > x)
    	{
    		shield -= x;
    	}
    	else
    	{
    		life -= (x - shield);
    		shield = 0;
    	}
    	gotHits++;
    }
    
    public int numberOfBloods(){
    	int r = gotHits;
    	gotHits = 0;
    	return r;
    }
    
    public void regenerateShield(){
    	shield = maxshield;
    }
    
    public boolean isDead(){
    	return life <= 0;
    }
    
    public void resetPlayer(){
        shield = maxshield;
        life = maxlife;
    }
    
    public float lifeFraction(){
    	return (float)life / (float)maxlife;
    }
    
    public float shieldFraction(){
    	return (float) shield / (float)maxshield;
    }
    
    public int gotLife(){
    	return life;
    }
    
    public boolean isReadyToHit(){
    	if(haveLeech())
    		return false;
    	
    	return readytohit;
    }
    
    public void readyToHit(){
    	readytohit = true;
    }
    
    public void notReadyToHit(){
    	readytohit = false;
    }
    
}