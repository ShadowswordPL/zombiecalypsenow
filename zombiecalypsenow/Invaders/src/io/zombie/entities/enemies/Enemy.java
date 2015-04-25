package io.zombie.entities.enemies;

import io.zombie.entities.MovableEntity;

public abstract class Enemy extends MovableEntity {

	public enum State {
		UNDEAD, DEAD;
	}

	private int health;

	private State state;

	public Enemy(String type, float x, float y, float z) {
		super(type, x, y, z);
		state = State.UNDEAD;
		health = this.getMaxHealth();
	}

	public void hurt(int damage) {
		
		health -= damage;
		if (health <= 0) {
			state = State.DEAD;
		}
	}

	public void update(long elapsed) {
		super.update(elapsed);
	}

	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health
	 *            the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Returns the max health for the enemy
	 * 
	 * @return the max health for the enemy
	 */
	public abstract int getMaxHealth();
	
	public abstract int getAtack();
	
	//Returns variables for hitboxes
	public abstract double zIndex();
	public abstract double xWidth();
	public abstract double zWidth();

}
