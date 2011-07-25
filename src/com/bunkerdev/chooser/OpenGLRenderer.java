package com.bunkerdev.chooser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

public class OpenGLRenderer implements Renderer {

	private LinkedList<TexturizedSquare> squares =new LinkedList<OpenGLRenderer.TexturizedSquare>();
	private int textureId = -1;
	private int angle = 0;
	private int width;
	private int height;
	
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
	}

	public void onSurfaceChanged(GL10 gl, int w, int h) {
		width = w;
		height = h;
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		//Poniendolo en ortogonal
		//left, right, bottom, top, near, far
		gl.glOrthof(-5f, 5f, -5f, 5f, -1, 1);
//		gl.glOrthof(-1.0f, 1.0f, -1.0f / (width / height), 
//				1.0f / (width / height), 0.01f, 10000.0f);   
		// Calculate the aspect ratio of the window
//		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();
	
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		if(!squares.isEmpty()){
			int i = 0;
			for(int y=-4; y<=4; y+=2){
				for(int x=-4; x<=4; x+=2){
					gl.glPushMatrix();
					gl.glTranslatef((float)x, (float)y, 0f);
					gl.glRotatef(angle*10f, 0f, 0f, 1f);
					squares.get(i).draw(gl);
					gl.glPopMatrix();
					i++;
				}
			}
			angle++;
		}
	}
	
	public void addSquare(TexturizedSquare s){
		squares.add(s);
	}

	
	public class TexturizedSquare {
		
		private Bitmap bitmap;
		private boolean shouldLoadTexture;
		
		// Our vertices.
		private float vertices[] = {
			      -1.0f,  1.0f, 0.0f,  // 0, Top Left
			      -1.0f, -1.0f, 0.0f,  // 1, Bottom Left
			       1.0f, -1.0f, 0.0f,  // 2, Bottom Right
			       1.0f,  1.0f, 0.0f,  // 3, Top Right
			};

		// The order we like to connect them.
		private short[] indices = { 0, 1, 2, 0, 2, 3 };
		
		private float textureCoords[] = {0.0f, 0.0f,
                						 0.0f, 1.0f,
                						 1.0f, 1.0f,
                						 1.0f, 0.0f };

		// Our vertex buffer.
		private FloatBuffer vertexBuffer;

		// Our index buffer.
		private ShortBuffer indexBuffer;
		
		private FloatBuffer textureBuffer;

		public TexturizedSquare(Bitmap bmp) {
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
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(
                    textureCoords.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf.asFloatBuffer();
			textureBuffer.put(textureCoords);
			textureBuffer.position(0);
			
			bitmap = bmp;
			shouldLoadTexture = true;
		}

		/**
		 * This function draws our square on screen.
		 * @param gl
		 */
		public void draw(GL10 gl) {
            // Counter-clockwise winding.
            gl.glFrontFace(GL10.GL_CCW);
            // Enable face culling.
            gl.glEnable(GL10.GL_CULL_FACE);
            // What faces to remove with the face culling.
            gl.glCullFace(GL10.GL_BACK);
            // Enabled the vertices buffer for writing and to be used during
            // rendering.
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            // Specifies the location and data format of an array of vertex
            // coordinates to use when rendering.
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

            if (shouldLoadTexture) {
                    loadGLTexture(gl);
                    shouldLoadTexture = false;
            }
            gl.glEnable(GL10.GL_TEXTURE_2D);
            // Enable the texture state
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            // Point to our buffers
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

            // Point out the where the color buffer is.
            gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
                            GL10.GL_UNSIGNED_SHORT, indexBuffer);
            // Disable the vertices buffer.
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            // Disable face culling.
            gl.glDisable(GL10.GL_CULL_FACE);
		}
		
		private void loadGLTexture(GL10 gl) { // New function
            // Generate one texture pointer...
            int[] textures = new int[1];
            gl.glGenTextures(1, textures, 0);
            textureId = textures[0];

            // ...and bind it to our array
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

            // Create Nearest Filtered Texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                            GL10.GL_LINEAR);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                            GL10.GL_LINEAR);

            // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                            GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                            GL10.GL_REPEAT);

            // Use the Android GLUtils to specify a two-dimensional texture image
            // from our bitmap
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
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

