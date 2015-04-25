package io.zombie.hud;

import java.util.Random;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import io.zombie.R;
import io.zombie.ZombieWorld;
import io.zombie.entities.Player;
import es.ucm.look.ar.ar2D.HUDElement;
import es.ucm.look.ar.util.LookARUtil;

public class Scope implements HUDElement {
	
	private ZombieWorld world;
	private Bitmap crowbar;
	private Bitmap blood;
	private Bitmap leech;
	private Player player;
	private boolean animateHit;
	static Typeface horror;
	final int lifeBarWidth = 400;
	
	public Scope( ZombieWorld world ){
		this.world = world;
		animateHit = false;
		this.player = Player.getInstance();
		crowbar = BitmapFactory.decodeResource(LookARUtil.getApp().getResources(), R.drawable.crowbar );
		blood = BitmapFactory.decodeResource(LookARUtil.getApp().getResources(), R.drawable.blood );
		leech = BitmapFactory.decodeResource(LookARUtil.getApp().getResources(), R.drawable.leech );
	}

	//Rysujemy pasek zycia
	private void drawLifebar(Canvas c){
		//Bottom of lifebar
		Paint redp = new Paint();
		redp.setColor(Color.RED);
		
		//Lifebar
		Paint lifep = new Paint();
		lifep.setColor(Color.GREEN);
		
		//Shieldbar
		Paint shieldp = new Paint();
		shieldp.setColor(Color.CYAN);
		float x = player.shieldFraction();
		c.drawRect(20, 20, 400, 50, redp);
		c.drawRect(20, 20, 400*player.lifeFraction(), 50, lifep);
		c.drawRect(20, 20, 400*player.shieldFraction(), 50, shieldp);
	}
	
	//Rysujemy punkty
	private void drawPoints(Canvas c){
		Paint p = new Paint();
		
		p.setTypeface(horror);
		p.setTextSize(40);
		p.setColor(Color.RED);
		p.setShadowLayer(5, 2, 2, Color.BLACK);
		c.drawText("Points: " + world.points, 20, c.getHeight()-20, p);
	}
	
	//Rysujemy crowbar
	private void drawCrowbar(Canvas c){
		if(!player.haveLeech())
		{
			Paint p = new Paint();
			Bitmap rotated;
			if(animateHit){
				Matrix matrix = new Matrix();
				matrix.postRotate(20);
				rotated = Bitmap.createBitmap(crowbar, 0, 0, crowbar.getWidth(), crowbar.getHeight(), matrix, true);
				animateHit = false;
			}
			else{
				rotated = crowbar;
			}
			c.drawBitmap(rotated, c.getWidth() / 2 - crowbar.getWidth() / 2 - 80, c.getHeight() - crowbar.getHeight(), p);
		}
	}
	
	private void drawLeech(Canvas c){
		if(player.haveLeech())
		{
			Paint p = new Paint();
			c.drawBitmap(leech, 10, 10, p);
		}
	}
	
	//Rysujemy krew po oberwaniu
	private void drawBlood(Canvas c){
		Paint p = new Paint();
		Random generator = new Random();
		for(int i = 0; i < Player.getInstance().numberOfBloods(); i++){	 
			c.drawBitmap(blood, (int)(generator.nextDouble()*c.getWidth()), (int)(generator.nextDouble()*c.getHeight()), p);
		}
	}
	
	@Override
	public void draw(Canvas c) {
		drawLifebar(c);
		drawPoints(c);
		drawCrowbar(c);
		drawBlood(c);
		drawLeech(c);
	}

	@Override
	public void update(long elapsed) {	
	}

	@Override	
	public void drawTouchableArea(Canvas c, Paint p) {
		
		
	}

	@Override
	public boolean contains(float x, float y) {
		return true;
	}

	@Override
	public boolean touch(MotionEvent motionEvent) {
		if(player.isReadyToHit())
		{
			player.notReadyToHit();
			animateHit = true;
			if ( motionEvent.getAction() == MotionEvent.ACTION_DOWN )
				world.hit();
		}
		return true;
	}
	
	public static void setFont(AssetManager a){
		horror = Typeface.createFromAsset(a, "fonts/OhTheHorror.ttf");	
	}

}
