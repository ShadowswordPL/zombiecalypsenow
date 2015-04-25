package io.zombie;

import io.zombie.R;
import io.zombie.R.id;
import io.zombie.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import es.ucm.look.ar.LookAR;

public class GameFinished extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamefinished);
		int points = this.getIntent().getIntExtra("points", 0);
		TextView temp = ((TextView) this.findViewById(R.id.textView2));
		temp.setText("You scored " + points + " points!");
		Button btn = (Button) findViewById(R.id.back);
		TextView txt = (TextView) findViewById(R.id.textView1);
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/OhTheHorror.ttf");
		btn.setTypeface(font);
		txt.setTypeface(font);
		temp.setTypeface(font);
		GameConfig.getInstance(this.getPreferences(MODE_PRIVATE))
				.setHighScore(points);
	}

	public void goToMenu(View view) {
		this.finish();
	}

}
