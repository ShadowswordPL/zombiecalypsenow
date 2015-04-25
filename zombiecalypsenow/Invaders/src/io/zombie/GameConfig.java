package io.zombie;

import android.content.SharedPreferences;

public class GameConfig {
	private final String HS_KEY = "highScore";
	private static GameConfig instance;
	private SharedPreferences prefs;
	private final int[] normalZombiesCount = { 3, 5, 10 };
	private final int[] leechesCount = { 0, 2, 4 };
	private int currentLevel;

	private GameConfig(SharedPreferences _prefs) {
		this.prefs = _prefs;
		currentLevel = 0;
	}

	public static GameConfig getInstance(SharedPreferences _prefs) {
		if (instance == null) {
			instance = new GameConfig(_prefs);
		}
		return instance;
	}

	public int getHighScore(int levelNumber) {
		return this.prefs.getInt(HS_KEY + levelNumber, 0);
	}

	public int getNormalZombies(int levelNumber) {
		return this.normalZombiesCount[levelNumber];
	}

	public int getLeeches(int levelNumber) {
		return this.leechesCount[levelNumber];
	}

	public void setCurrentLevel(int level) {
		this.currentLevel = level;
	}

	public void setHighScore(int score) {
		int currentScore = this.prefs.getInt(HS_KEY + currentLevel, 0);
		SharedPreferences.Editor edit = this.prefs.edit();
		edit.putInt(HS_KEY + currentLevel, max(score, currentScore));
		edit.commit();
	}

	private int max(int score, int currentScore) {
		return score > currentScore ? score : currentScore;
	}
}
