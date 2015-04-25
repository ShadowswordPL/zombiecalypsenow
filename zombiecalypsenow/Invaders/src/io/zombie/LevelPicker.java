package io.zombie;

import io.zombie.R;
import io.zombie.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelPicker extends Activity {

	private GameConfig config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levels);
		config = GameConfig.getInstance(this.getPreferences(MODE_PRIVATE));
		int[] ids = { R.id.level1, R.id.level2, R.id.level3, R.id.back };
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/OhTheHorror.ttf");

		Button btn = (Button) findViewById(R.id.level1);
		btn.setTypeface(font);
		int hs = config.getHighScore(0);
		if (hs != 0) {
			btn.setText(btn.getText() + " (highscore: " + hs + ")");
		}
		btn = (Button) findViewById(R.id.level2);
		btn.setTypeface(font);
		hs = config.getHighScore(1);
		if (hs != 0) {
			btn.setText(btn.getText() + " (highscore: " + hs + ")");
		}
		btn = (Button) findViewById(R.id.level3);
		btn.setTypeface(font);
		hs = config.getHighScore(2);
		if (hs != 0) {
			btn.setText(btn.getText() + " (highscore: " + hs + ")");
		}
		btn = (Button) findViewById(R.id.back);
		btn.setTypeface(font);
	}

	public void startGame(View view) {
		Intent intent = new Intent(this, GameView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

		int id = Integer.parseInt((String) view.getTag()) - 1;

		int normal = config.getNormalZombies(id);
		int leeches = config.getLeeches(id);
		config.setCurrentLevel(id);
		intent.putExtra("NormalZombies", normal);
		intent.putExtra("LeechZombies", leeches);
		startActivity(intent);
		this.finish();
	}

	public void quitView(View view) {
		this.finish();
	}
}
