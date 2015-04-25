package io.zombie.entities.enemies;

import io.zombie.drawable2D.HealthBar;
import io.zombie.drawable2D.Undead2D;
import io.zombie.sound.Sound;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

public class Undead extends Enemy {

	private static final String TYPE = "UNDEAD";

	private static final int MAX_HEALTH = 3;

	public Undead(float x, float y, float z) {
		super(TYPE, x, y, z);
		Vector3 v = new Vector3(-x, -y, -z);
		v.normalize();
		setDirection(v);
		setSpeed(0);
		setDrawable2D(new Undead2D(this));
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

	@Override
	public void hurt(int damage) {
		super.hurt(damage);
		Sound.getInstance().playUndead();
	}

	@Override
	public double zIndex(){
		return 10;
	}
	
	@Override
	public double xWidth() {
		return 0.7;
	}


	@Override
	public double zWidth() {
		// TODO Auto-generated method stub
		return 0.7;
	}

	@Override
	public int getAtack() {
		// TODO Auto-generated method stub
		return 1;
	}

}
