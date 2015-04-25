package es.ucm.look.data.remote;

import android.os.RemoteException;

public interface RemoteExceptionHandler {
	
	public void handle( RemoteException e );

}
