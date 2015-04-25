package io.zombie.sound;

import android.media.MediaPlayer;
import es.ucm.look.ar.util.LookARUtil;
import io.zombie.R;

public final class Sound {
	private static volatile Sound instance = null;
	 
    public static Sound getInstance() {
        if (instance == null) {
            synchronized (Sound.class) {
                if (instance == null) {
                    instance = new Sound();
                }
            }
        }
        return instance;
    }
 
    private Sound() {
    }
    
    public void playUndead(){
    	MediaPlayer mp = MediaPlayer.create(LookARUtil.getApp(), R.raw.undeadwav);   
        mp.start();
    }
    
    public void playShieldregen(){
    	MediaPlayer mp = MediaPlayer.create(LookARUtil.getApp(), R.raw.shieldwav);   
        mp.start();
    }
    
    public void playGothit(){
    	MediaPlayer mp = MediaPlayer.create(LookARUtil.getApp(), R.raw.gothitwav);   
        mp.start();
    }
    
    public void playLeech(){
    	MediaPlayer mp = MediaPlayer.create(LookARUtil.getApp(), R.raw.leech);   
        mp.start();
    }
}
