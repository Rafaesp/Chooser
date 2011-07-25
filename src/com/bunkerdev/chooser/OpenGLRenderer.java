package com.bunkerdev.chooser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class OpenGLRenderer implements Renderer {

	private Square square;
	private Triforce triforce;
	private Circle circle;
	private float angle;
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		square = new Square();
		triforce = new Triforce();
		circle = new Circle(3f);
		angle = 0f;
	
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();
	
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -15);
		
		circle.draw(gl);
		
		angle++;
	}

	
	public class Square {
		// Our vertices.
		private float vertices[] = {
			      -1.0f,  1.0f, 0.0f,  // 0, Top Left
			      -1.0f, -1.0f, 0.0f,  // 1, Bottom Left
			       1.0f, -1.0f, 0.0f,  // 2, Bottom Right
			       1.0f,  1.0f, 0.0f,  // 3, Top Right
			};

		// The order we like to connect them.
		private short[] indices = { 0, 1, 2, 0, 2, 3 };

		// Our vertex buffer.
		private FloatBuffer vertexBuffer;

		// Our index buffer.
		private ShortBuffer indexBuffer;

		public Square() {
			// a float is 4 bytes, therefore we multiply the number if
			// vertices with 4.
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			vertexBuffer = vbb.asFloatBuffer();
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);

			// short is 2 bytes, therefore we multiply the number if
			// vertices with 2.
			ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
			ibb.order(ByteOrder.nativeOrder());
			indexBuffer = ibb.asShortBuffer();
			indexBuffer.put(indices);
			indexBuffer.position(0);
		}

		/**
		 * This function draws our square on screen.
		 * @param gl
		 */
		public void draw(GL10 gl) {
			// Counter-clockwise winding.
			gl.glFrontFace(GL10.GL_CCW); // OpenGL docs
			// Enable face culling.
			gl.glEnable(GL10.GL_CULL_FACE); // OpenGL docs
			// What faces to remove with the face culling.
			gl.glCullFace(GL10.GL_BACK); // OpenGL docs

			// Enabled the vertices buffer for writing and to be used during
			// rendering.
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// OpenGL docs.
			// Specifies the location and data format of an array of vertex
			// coordinates to use when rendering.
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, // OpenGL docs
	                                 vertexBuffer);

			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,// OpenGL docs
					  GL10.GL_UNSIGNED_SHORT, indexBuffer);

			// Disable the vertices buffer.
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // OpenGL docs
			// Disable face culling.
			gl.glDisable(GL10.GL_CULL_FACE); // OpenGL docs
		}

	}
	
	public class Triforce{
		
		private float vertices[] = {-1.0f, 0.0f, 0.0f,
									1.0f, 0.0f, 0.0f,
									0.0f, 1.5f, 0.0f,
									-2.0f, -1.5f, 0.0f,
									0.0f, -1.5f, 0.0f,
									2.0f, -1.5f, 0.0f}; 
		
		private short indices[] = {0, 1, 2, 0, 3, 4, 1, 4, 5};
		
		private FloatBuffer vertexBuffer;
		private ShortBuffer indexBuffer;
		
		public Triforce(){
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			vertexBuffer = vbb.asFloatBuffer();
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);
			
			ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
			ibb.order(ByteOrder.nativeOrder());
			indexBuffer = ibb.asShortBuffer();
			indexBuffer.put(indices);
			indexBuffer.position(0);
		}
		
		public void draw(GL10 gl){
			gl.glFrontFace(GL10.GL_CCW);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glCullFace(GL10.GL_BACK);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, 
					GL10.GL_UNSIGNED_SHORT,	indexBuffer);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisable(GL10.GL_CULL_FACE);
		}
	
	}
	
	public class Circle{
		
		private float vertices[] = new float[722];
		private short indices[];
		private FloatBuffer vertexBuffer;
		private ShortBuffer indexBuffer;
		
		public Circle(float radius){
			
			vertices[0] = 0f;
			vertices[1] = 0f;
			for (int i = 0; i < 720; i += 2) {
			    vertices[i+2]  = Double.valueOf(Math.cos(Math.PI*i/2*180) * radius).floatValue();
			    vertices[i+3] = Double.valueOf(Math.sin(Math.PI*i/2*180) * radius).floatValue();
			}
			vertices[719] = 0f;
			vertices[720] = 1f;
			
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			vertexBuffer = vbb.asFloatBuffer();
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);
			
//			ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
//			ibb.order(ByteOrder.nativeOrder());
//			indexBuffer = ibb.asShortBuffer();
//			indexBuffer.put(indices);
//			indexBuffer.position(0);
		}
		
		public Circle(){
			this(1);
		}
		
		public void draw(GL10 gl){
			gl.glFrontFace(GL10.GL_CCW);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glCullFace(GL10.GL_BACK);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 361);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisable(GL10.GL_CULL_FACE);
		}
		
	}
}

