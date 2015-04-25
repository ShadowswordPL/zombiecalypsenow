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
package es.ucm.look.ar.ar3D;

import javax.microedition.khronos.opengles.GL10;

/**
 * Implemented by all those classes that can be drawn in a {@link GL10} context
 * 
 * @author Ángel Serrano
 * 
 */
public interface Drawable3D {

	/**
	 * Draws the element in the {@link GL10} context
	 * 
	 * @param gl
	 *            the {@link GL10} context
	 * 
	 */
	void draw(GL10 gl);

	/**
	 * Updates drawable with the elapsed time
	 * 
	 * @param elapsed
	 *            elapsed time since last update
	 */
	void update(long elapsed);
}
