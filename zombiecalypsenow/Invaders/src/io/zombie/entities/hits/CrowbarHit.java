package io.zombie.entities.hits;
import es.ucm.look.ar.math.geom.Vector3;

public class CrowbarHit extends Hit {
	
	public static final String TYPE = "Crowbar hit";
		
	/**
	 * Constructs a missile with the direction
	 * @param x
	 * @param y
	 * @param z
	 */
	public CrowbarHit(Vector3 v) {
		super(v, 100);
	}
	
	public int getDamage( ){
		return 1;
	}
	
}
