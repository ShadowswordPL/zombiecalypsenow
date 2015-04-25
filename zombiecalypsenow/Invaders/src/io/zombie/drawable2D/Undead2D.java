package io.zombie.drawable2D;

import io.zombie.R;
import io.zombie.entities.enemies.Enemy;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import es.ucm.look.ar.ar2D.Drawable2D;
import es.ucm.look.ar.util.LookARUtil;

public class Undead2D implements Drawable2D {
	
	private static Bitmap bitmap = BitmapFactory.decodeResource(LookARUtil.getApp().getResources(), R.drawable.undead );
	private Enemy enemy;
	
	public Undead2D( Enemy enemy ){
		this.enemy = enemy;
	}

	@Override
	public void draw(Canvas c) {
		Paint p = new Paint();
		c.drawBitmap(bitmap, 0, 0, p);
	}

	@Override
	public void update(long elapsed) {
		
	}

	@Override
	public void drawTouchableArea(Canvas c, Paint p) {
		
		
	}

}
