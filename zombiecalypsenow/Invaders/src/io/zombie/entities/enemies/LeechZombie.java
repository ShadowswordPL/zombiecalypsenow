package io.zombie.entities.enemies;

import io.zombie.drawable2D.HealthBar;
import io.zombie.drawable2D.Undead2D;
import io.zombie.sound.Sound;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import io.zombie.R;
import es.ucm.look.ar.ar2D.drawables.Image2D;
import es.ucm.look.ar.ar3D.core.Color4;
import es.ucm.look.ar.ar3D.core.drawables.Entity3D;
import es.ucm.look.ar.ar3D.core.drawables.primitives.ObjMesh3D;
import es.ucm.look.ar.math.collision.Armature;
import es.ucm.look.ar.math.collision.SphericalArmature;
import es.ucm.look.ar.math.collision.SquareArmature;
import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.ar.math.geom.Vector3;
import es.ucm.look.ar.util.LookARUtil;

public class LeechZombie extends Enemy {

	private static final String TYPE = "UNDEAD";

	private static final int MAX_HEALTH = 5;
	
	public LeechZombie(float x, float y, float z) {
		super(TYPE, x, y, z);
		
		setDrawable2D(new Undead2D(this));
		Vector3 v = new Vector3(-x, -y, -z);
		v.normalize();
		setDirection(v);
		setSpeed(2);
	}

	public void update(long elapsed) {
		super.update(elapsed);
		if (Vector3.getVolatileVector(this.getLocation(), new Point3(0, 0, 0))
				.module() < 10)
			this.getDirection().set(0, 0, 0);
	}

	@Override
	public int getMaxHealth() {
		return MAX_HEALTH;
	}

	public static void initResources() {
	}
	
	@Override
	public void hurt(int damage) {
		super.hurt(damage);
		Sound.getInstance().playUndead();
	}

	@Override
	public double zIndex(){
		return 0;
	}
	
	@Override
	public double xWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double zWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAtack() {
		// TODO Auto-generated method stub
		return 0;
	}
}
