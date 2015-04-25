package io.zombie;

import io.zombie.hud.Scope;
import android.os.Bundle;
import es.ucm.look.ar.LookAR;
import es.ucm.look.data.LookData;

public class GameView extends LookAR {
		
	public GameView( ){
		super( true, true, true, true, 100.0f );
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		
		int normal = extras.getInt("NormalZombies");
		int leeches = extras.getInt("LeechZombies");
		
		ZombieWorld w = new ZombieWorld(this, normal, leeches);
		
		Scope.setFont(getAssets());
		get2DLayer().getHUD().add(new Scope( w ));
		LookData.getInstance().setWorldEntityFactory(null);
		LookData.getInstance().setWorld(w);
		
	}

}
