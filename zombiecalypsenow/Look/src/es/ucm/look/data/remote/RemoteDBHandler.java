/*******************************************************************************
 * Look! is a Framework of Augmented Reality for Android. 
 * 
 * Copyright (C) 2011 
 * 		Sergio Bellón Alcarazo
 * 		Jorge Creixell Rojo
 * 		Ángel Serrano Laguna
 * 	
 * 	   Final Year Project developed to Sistemas Informáticos 2010/2011 - Facultad de Informática - Universidad Complutense de Madrid - Spain
 * 	
 * 	   Project led by: Jorge J. Gómez Sánz
 * 
 * 
 * ****************************************************************************
 * 
 * This file is part of Look! (http://lookar.sf.net/)
 * 
 * Look! is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/
 ******************************************************************************/
package es.ucm.look.data.remote;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.RemoteException;
import es.ucm.look.data.EntityData;
import es.ucm.look.data.interfaces.DataGetter;
import es.ucm.look.data.interfaces.DataHandler;
import es.ucm.look.data.interfaces.DataSetter;
import es.ucm.look.data.local.DBDataHandler;
import es.ucm.look.data.remote.restful.ServiceManager;

/**
 * Implement the {@link DataHandler} to the persistence with the remote service
 * 
 * @author Sergio
 *
 */
public class RemoteDBHandler extends DBDataHandler implements DataGetter, DataSetter {
	
	private RemoteExceptionHandler handler;
	
	private ServiceManager serviceManager;
	
	private Logger logger;
	
	/**
	 * Constructor class. 
	 * 
	 * @param c
	 * 		Context when it is created
	 * @param s
	 * 		ServiceManager
	 * @param serverURL
	 * 		URL server
	 * @param fileURL
	 * 		URL to access to the files
	 */
	public RemoteDBHandler( Context c, ServiceManager s, String serverURL, String fileURL ){
		super( c );
		this.serviceManager = s;
		serviceManager.startService();
		serviceManager.bindService();
		ConfigNet.setNetConfiguration(serverURL, fileURL );
		logger = new Logger();
	}
	
	@Override
	public List<EntityData> getElementsUpdated(float x, float y, float z,
			float radius, Date date) {
		try {
			List<EntityData> list = serviceManager.restfulService.getElementsUpdated(x, y, z, radius, date.toString());
			for ( EntityData data: list ){
				super.addEntity(data);
			}
			return list;
		} catch (RemoteException e) {
			if (handler != null)
				handler.handle(e);
			else
				e.printStackTrace();
			return null;
		}
	}

	@Override
	public void addEntity(EntityData data) {
		try{
			serviceManager.restfulService.addElement(data.getType(), data.getLocation().x,
				data.getLocation().y, data.getLocation().z, data.getProperties());
			
		} catch (RemoteException e) {
			if (handler != null)
				handler.handle(e);
			else
				e.printStackTrace();
		}
	}

	@Override
	public void updatePosition(EntityData data, float x, float y, float z) {
		try {
			serviceManager.restfulService.updateElementPosition(data.getId(), x, y, z);
			
			super.updatePosition(data,x,y,z);
		} catch (RemoteException e) {
			if (handler != null)
				handler.handle(e);
			else
				e.printStackTrace();
		}
	}

	@Override
	public void updateProperty(EntityData data, String property, String newValue){
		try{
			serviceManager.restfulService.updateOrAddProperty(data.getId(), property, newValue);
		} catch (RemoteException e) {
			if (handler != null)
				handler.handle(e);
			else
				e.printStackTrace();
		}

	
	}
	
	
	//------------------------------------------

	public ServiceManager getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(ServiceManager serviceManager2) {
		serviceManager = serviceManager2;
	}

	
	
	
	public Logger getLogger() {
		return logger;
	}
	
	public void setRemoteExceptionHandler(RemoteExceptionHandler handler) {
		this.handler = handler;
	}
	
}
