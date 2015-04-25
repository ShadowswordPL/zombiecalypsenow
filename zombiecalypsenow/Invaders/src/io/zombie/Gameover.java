package io.zombie;

import io.zombie.R;
import io.zombie.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import es.ucm.look.ar.LookAR;

public class Gameover extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameover);
		Button btn = (Button) findViewById(R.id.back);
		TextView txt = (TextView) findViewById(R.id.textView1);
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/OhTheHorror.ttf");
		btn.setTypeface(font);
		txt.setTypeface(font);
	}

	public void goToMenu(View view) {
		this.finish();
	}

}
