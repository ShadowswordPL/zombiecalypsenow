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
package es.ucm.look.ar.ar3D.core.drawables.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import es.ucm.look.ar.ar3D.core.Color4;
import es.ucm.look.ar.ar3D.core.drawables.Mesh3D;
import es.ucm.look.ar.math.geom.Point3;


public class Ring extends Mesh3D {

	private FloatBuffer colorBuffer;

	private int iterations;

	private float incGrad;
	
	private float initAngle = 0;

	public Ring(Point3 center, float radius, float width, int iterations, Color4 c) {

		this.iterations = iterations;
		this.incGrad = 360 / iterations;

		float incAng = (float) (Math.PI * 2 / iterations);
		float angle = 0;

		float vertices[] = new float[4 * 3];
		for (int i = 0; i <= 2; i += 2) {
			float x = (float) (Math.cos(angle) * radius);
			float y = (float) (Math.sin(angle) * radius);
			vertices[i * 3] = center.x + x;
			vertices[i * 3 + 1] = center.y + y;
			vertices[i * 3 + 2] = center.z;
			x = (float) (Math.cos(angle) * (radius + width));
			y = (float) (Math.sin(angle) * (radius + width));
			vertices[(i + 1) * 3] = center.x + x;
			vertices[(i + 1) * 3 + 1] = center.y + y;
			vertices[(i + 1) * 3 + 2] = center.z;
			angle += incAng;
		}

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		// Color
		Color4 cb = c.brighter();
		
		float colors[] = new float[4 * 4];
		for (int i = 0; i < 2; i++) {
			int j = i * 8;
			for ( int n = 0; n < 4; n++ )
				colors[j++] = c.rgba[n];
			
			for ( int n = 0; n < 4; n++ )
				colors[j++] = cb.rgba[n];
		}

		byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuf.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}

	public void draw(GL10 gl) {
		gl.glRotatef(initAngle, 1.0f, 1.0f, 1.0f);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glPushMatrix();
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		for (int i = 0; i < iterations; i++) {
			gl.glRotatef(incGrad, 0.0f, 0.0f, 1.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 1, 3);
		}
		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
	
	public void update( long elapsed ){

	}
}
