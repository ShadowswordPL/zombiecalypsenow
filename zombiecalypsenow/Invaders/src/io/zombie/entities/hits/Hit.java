package io.zombie.entities.hits;

import io.zombie.entities.MovableEntity;
import es.ucm.look.ar.ar3D.core.drawables.Entity3D;
import es.ucm.look.ar.math.geom.Vector3;

public abstract class Hit extends MovableEntity {
	
	public static final String TYPE = "Hit";
		
	/**
	 * Constructs a missile with the direction
	 * @param x
	 * @param y
	 * @param z
	 */
	public Hit(Vector3 v, float speed) {
		super(TYPE, v.x, v.y, v.z);
		this.setDirection(v);
		this.setSpeed(speed);
	}
	
	public abstract int getDamage( );
	
}
