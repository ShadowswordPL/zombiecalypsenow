package io.zombie.drawable2D;

import io.zombie.entities.enemies.Enemy;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import es.ucm.look.ar.ar2D.Drawable2D;

public class HealthBar implements Drawable2D {
	
	private Enemy enemy;
	
	public HealthBar( Enemy enemy ){
		this.enemy = enemy;
	}

	@Override
	public void draw(Canvas c) {
		float maxWidth = c.getWidth() / 16.0f;
		float barHeight = c.getHeight() / 40.0f;
		float healthWidth = enemy.getHealth() * maxWidth / enemy.getMaxHealth();
		
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setStyle(Style.FILL_AND_STROKE);
		
		c.drawRect(0, 0, maxWidth, barHeight, p);
		
		int color = Color.GREEN;
		if ( enemy.getHealth() <= enemy.getMaxHealth() / 4 ){
			color = Color.RED;
		}
		else if ( enemy.getHealth() <= enemy.getMaxHealth() / 2){
			color = Color.YELLOW;
		}
		p.setColor(color);
		p.setStyle(Style.FILL);
		c.drawRect(0, 0, healthWidth, barHeight, p);
		
	}

	@Override
	public void update(long elapsed) {
		
	}

	@Override
	public void drawTouchableArea(Canvas c, Paint p) {
		
		
	}

}
