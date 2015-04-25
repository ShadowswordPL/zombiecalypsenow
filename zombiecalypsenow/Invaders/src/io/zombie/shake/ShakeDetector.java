package io.zombie.shake;

import java.util.Vector;

import android.Manifest;
import android.util.Log;

public class ShakeDetector{

	private Vector<Float> datax;
	private Vector<Float> datay;
	
	int MIN_NUMBER_OF_RUMBLE = 3;
	
	public ShakeDetector(){
		datax = new Vector<Float>();
		datay = new Vector<Float>();
	}
	
	public void collectData(float pitch, float azimuth){
		datax.add(pitch);
		datay.add(azimuth);
	}
	
	public boolean wasShake(){
		int rumblex = 0;
		int rumbley = 0;
		boolean wasLowerx = false;
		boolean wasLowery = false;
		for(int i = 0; i < datax.size() - 2; i++){
			if(datax.get(i) > datax.get(i+1) && datax.get(i+1) < datax.get(i+2) && wasLowerx ||
				datax.get(i) < datax.get(i+1) && datax.get(i+1) > datax.get(i+2) && !wasLowerx){
				if(Math.abs(datax.get(i) - datax.get(i+1)) > 0.1){
					wasLowerx = !wasLowerx;
					rumblex++;
				}
			}
		}
		
		for(int i = 0; i < datax.size() - 2; i++){
			if(datay.get(i) > datay.get(i+1) && datay.get(i+1) < datay.get(i+2) && wasLowery ||
				datay.get(i) < datay.get(i+1) && datay.get(i+1) > datay.get(i+2) && !wasLowery){
				if(Math.abs(datay.get(i) - datay.get(i+1)) > 0.1){
					wasLowery = !wasLowery;
					rumbley++;
				}
			}
		}
		
		datax.clear();
		datay.clear();
		return rumblex >= MIN_NUMBER_OF_RUMBLE && rumbley >= MIN_NUMBER_OF_RUMBLE;
	}
}
