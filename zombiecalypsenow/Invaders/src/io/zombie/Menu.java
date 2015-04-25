package io.zombie;

import io.zombie.R;
import io.zombie.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import es.ucm.look.ar.LookAR;

public class Menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		int[] ids = { R.id.startGame, R.id.about, R.id.quit };
		for (int id : ids) {
			Button txt = (Button) findViewById(id);
			Typeface font = Typeface.createFromAsset(getAssets(),
					"fonts/OhTheHorror.ttf");
			txt.setTypeface(font);
		}
	}

	public void level1(View view) {
		Intent intent = new Intent(this, GameView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

	public void goToLevels(View view) {
		Intent intent = new Intent(this, LevelPicker.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

	public void goToAbout(View view) {
		Intent intent = new Intent(this, About.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

	public void quitApp(View view) {
		this.finish();
	}
}
