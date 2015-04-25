package io.zombie;

import io.zombie.entities.Player;
import io.zombie.entities.enemies.Enemy;
import io.zombie.entities.enemies.Undead;
import io.zombie.entities.enemies.Enemy.State;
import io.zombie.entities.hits.CrowbarHit;
import io.zombie.entities.hits.Hit;
import io.zombie.shake.ShakeDetector;
import io.zombie.sound.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import es.ucm.look.ar.ar3D.core.camera.Camera3D;
import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.ar.math.geom.Vector3;
import es.ucm.look.ar.util.DeviceOrientation;
import es.ucm.look.ar.util.LookARUtil;
import es.ucm.look.data.World;

public class ZombieWorld extends World {

	public static Integer lockHits = 1;
	public static Integer lockEnemies = 1;
	public static Integer lockG = 1;
	
	private static final ScheduledExecutorService unevenAtacks = 
			  Executors.newSingleThreadScheduledExecutor();
	
	private static final int TIME_BETWEEN_ENEMIES = 5000;
	private static final int TIME_BETWEEN_HITS = 100;
	private static final int TIME_BETWEEN_SHIELDREGEN = 3000;
	private static final int TIME_BETWEEN_ZOMBIEATACKS = 1000;
	private static final int TIME_BETWEEN_CHECKSHAKE = 1000;
	
	private int timeToEnemy;
	private int timeToHit;
	private int timeToShieldregen;
	private int timeToZombieatack;
	private int timeToCheckshake;
	
	private List<Enemy> enemies;
	private List<Hit> hits;
	private List<Enemy> enemiesKilled;
	private Vibrator vibrator;
	private Player player;
	private GameView game;
	private boolean isGameOver;
	
	private ShakeDetector shakedetector;
	private int leechOnScreen;
	
	public int[] zombiesLeft; 
	public boolean leechExists;
	
	
	public int kills = 0;
	public int points = 0;

	public ZombieWorld(GameView zombie, int normal, int leeches) {
		
		zombiesLeft = new int[3];
		
		zombiesLeft[0] = normal;
		zombiesLeft[1] = leeches;
		
		leechExists = false;
		timeToEnemy = 0;
		timeToHit = 0;
		timeToShieldregen = 0;
		timeToZombieatack = 0;
		timeToCheckshake = 0;
		
		enemiesKilled = new ArrayList<Enemy>();
		enemies = new ArrayList<Enemy>();
		hits = new ArrayList<Hit>();
		
		shakedetector = new ShakeDetector();
		leechOnScreen = 0;
		
		Player.getInstance().resetPlayer();
		LookARUtil.getApp();
		vibrator = (Vibrator) LookARUtil.getApp().getSystemService(Context.VIBRATOR_SERVICE);
		player = Player.getInstance();
		
		this.game = zombie;
		isGameOver = false;
	}

	//Boolean if enemy was hited by hit
	private boolean zombieWasHited(Point3 hit, Enemy enemy)
	{
		double hitVectorLength = Math.sqrt(Math.pow(hit.x, 2) + Math.pow(hit.y, 2) + Math.pow(hit.z, 2));
		double enemyVectorLength = Math.sqrt(Math.pow(enemy.getLocation().x, 2) + Math.pow(enemy.getLocation().y, 2) + Math.pow(enemy.getLocation().z, 2));
		double enemyMultiplier = 10 / enemyVectorLength; 
		double hitMultiplier = 10 / hitVectorLength;		
		
		if(Math.abs(hit.x * hitMultiplier - enemy.getLocation().x * enemyMultiplier) / Math.abs(enemy.getLocation().x * enemyMultiplier) > Math.abs(enemy.xWidth()))
			return false;
		if(hit.y < 0)
			return false;;
		if(Math.abs(hit.z * hitMultiplier - enemy.getLocation().z * enemyMultiplier) / Math.abs(enemy.getLocation().z * enemyMultiplier) > enemy.zWidth())
			return false;
			
		return true;
	}
	
	public void checkShake(long elapsed){
		shakeDetectorData();
		timeToCheckshake -= elapsed;
		if (timeToCheckshake <= 0){
			timeToCheckshake = TIME_BETWEEN_CHECKSHAKE;
			if(shakedetector.wasShake()){
				killedLeech();
			}
		}
	}
	
	public void killedLeech(){
		synchronized (lockEnemies) {
			player.killedLeech();
			zombiesLeft[1] -= leechOnScreen;
			leechOnScreen = 0;
		}
	}
	
	public void shakeDetectorData(){
		shakedetector.collectData(
				DeviceOrientation.getDeviceOrientation(LookARUtil.getApp()).getPitch(), 
				DeviceOrientation.getDeviceOrientation(LookARUtil.getApp()).getAzimuth()
				); 
	}
	
	public void update(long elapsed) {
		super.update(elapsed);
		
		spawnEnemies(elapsed);
		playerReadyToHitRegeneration(elapsed);
		playerShieldRegeneration(elapsed);
		zombieAtack(elapsed);
		checkShake(elapsed);
		
		
		synchronized (lockEnemies) {
			synchronized (lockHits) 
			{
				processHitsAndDeadZombies();
			}
			
			hits.clear();
			
			for (Enemy killed : enemiesKilled) {
				enemies.remove(killed);
				removeEntity(killed.getId());
				kills++;
			}
			
			processGameOver();
		}
		
		processGameFinished();
				
	}
	
	private void processHitsAndDeadZombies() {
		for (Enemy enemy : enemies) {
			if (enemy.getState() == State.DEAD)
				enemiesKilled.add(enemy);
			else
				for (Hit hit : hits) {	
					if (zombieWasHited(hit.getLocation(), enemy)) 
					{
						enemy.hurt(hit.getDamage());
						points++;
					}
				}
		}
	}
	
	private void processGameOver() {
		if(player.isDead())
			synchronized(lockG) {
				if(!isGameOver){
					isGameOver = true;
					Intent intent = new Intent(this.game, Gameover.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					game.startActivity(intent);
					game.finish();
				}
			}
	}
	
	private void processGameFinished() {
		if(zombiesLeft[0] == 0 && zombiesLeft[1] == 0 && enemies.size() < 1) {
			System.out.println("All zombies dead, game shutdown");
				synchronized(lockG) {
					if(!isGameOver)	{
						isGameOver = true;
						Intent intent = new Intent(this.game, GameFinished.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						points += player.gotLife();
						intent.putExtra("points", points);
						game.startActivity(intent);
						game.finish();
					}
				}
		}
	}

	public void spawnEnemies(long elapsed){
		synchronized (lockEnemies) {
			timeToEnemy -= elapsed;
			if (timeToEnemy <= 0)
			{
				timeToEnemy = TIME_BETWEEN_ENEMIES;
				
				int type = 1;
				
				if(zombiesLeft[0] > 0 && zombiesLeft[1] > 0)
				{
					Random rand = new Random();
					type = rand.nextInt(2);
				}
				else
				{
					if(zombiesLeft[0] > 0)
					{
						type = 0;
					}
					else
						if(zombiesLeft[1] > 0)
							type = 1;
						else
							type = 2;
				}
				
				addEnemy(type);
			}
		}
	}
	
	private Runnable unevenAtack(final Enemy enemy){
		Runnable r = new Runnable() {
		    public void run() {
		    	player.gotHit(enemy.getAtack());
		    }
		  };
		  return r;
	}
	
	public void zombieAtack(long elapsed){
		timeToZombieatack -= elapsed;
		Random generator = new Random();
		if(timeToZombieatack < 0){
			timeToZombieatack = TIME_BETWEEN_ZOMBIEATACKS;
			for(Enemy enemy : enemies)
			{
				unevenAtacks.schedule(unevenAtack(enemy), (long) (generator.nextDouble()*1000), TimeUnit.MILLISECONDS);
			}
		}
	}
	
	public void playerReadyToHitRegeneration(long elapsed){
		timeToHit -= elapsed;
		if(timeToHit < 0)	{
			timeToHit = TIME_BETWEEN_HITS;
			player.readyToHit();
		}
	}
	
	public void playerShieldRegeneration(long elapsed){
		timeToShieldregen -= elapsed;
		if(timeToShieldregen < 0)	{
			Sound.getInstance().playShieldregen();
			timeToShieldregen = TIME_BETWEEN_SHIELDREGEN;
			player.regenerateShield();
		}
	}
	
	private void addEnemy(int type) {
		Vector3 v = new Vector3(0, 0, 1 );
		float angle = (float) (Math.random() * 2 * Math.PI);
		v.rotateY(angle);
		float y = (float) (5.0f - Math.random() * 10.0f);
		
		Enemy u;
		
		if(type == 2)
			return;
		
		if(type == 0)
		{
			u = new Undead(  v.x * 50.0f, y, v.z * 50.0f );
			enemies.add(u);
			this.addEntity(u);
		}
		else 
		{
			player.gotLeech();
			leechOnScreen++;
		}
		
		zombiesLeft[type]--;	
	}

	public void hit() {
		vibrator.vibrate(50);
		Vector3 v = new Vector3(Camera3D.NORTH);
		v.rotateX(-DeviceOrientation.getDeviceOrientation(LookARUtil.getApp()).getPitch());
		v.rotateY(DeviceOrientation.getDeviceOrientation(LookARUtil.getApp()).getAzimuth());
		CrowbarHit hit = new CrowbarHit(v);
		synchronized (lockHits) {
			hits.add(hit);
		}
	}
}
