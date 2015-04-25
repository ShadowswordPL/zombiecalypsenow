package io.zombie.entities;

import es.ucm.look.ar.math.geom.Vector3;
import es.ucm.look.data.EntityData;
import es.ucm.look.data.WorldEntity;

public class MovableEntity extends WorldEntity {
	
	private static int ID_GENERATOR = 0;
	
	private Vector3 direction;
	
	private float speed;

	public MovableEntity(String type, float x, float y, float z) {
		super(new EntityData( ID_GENERATOR++, type, x, y, z));
	}
	
	public void setDirection( Vector3 direction ){
		this.direction = direction;
	}
	
	public void setSpeed( float speed ){
		this.speed = speed;
	}
	
	public Vector3 getDirection( ){
		return direction;
	}
	
	public float getSpeed( ){
		return speed;
	}
	
	public void update( long elapsed ){
		super.update(elapsed);
		float advance = ((float) elapsed / 1000.0f )* speed;
		this.getLocation().add(direction.x * advance, direction.y * advance, direction.z * advance);
	}

}
