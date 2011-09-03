package com.bunkerdev.chooser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class GLBitmap implements GLDrawable{

	private Bitmap bitmap;
	private double angle;
	private double rotAngle;
	private boolean shouldLoadTexture;
	private float x, y;
	private float width;
	private float height;
	private int textureId = -1;

	// Our vertices.
	private float vertices[] = { 
			-0.5f, 0.5f, // 0, Top Left
			-0.5f, -0.5f, // 1, Bottom Left
			0.5f, -0.5f, // 2, Bottom Right
			0.5f, 0.5f // 3, Top Right
	};

	// The order we like to connect them.
	private short[] indices = { 0, 1, 2, 0, 2, 3 };

	private float textureCoords[] = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,
			0.0f };

	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private FloatBuffer textureBuffer;

	public GLBitmap(Bitmap bmp, float x, float y) {
		this(bmp, x, y, 50f, 50f);
	}
	public GLBitmap(Bitmap bmp, float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		angle = 0;
		rotAngle = 0;
		// Main.debug("angle: %f, deltax: %f, deltay: %f", angle,
		// (OpenGLRenderer.CENTER[1]-y), (OpenGLRenderer.CENTER[0]-x));

		bitmap = bmp;
		shouldLoadTexture = true;
		initBuffers();
	}
	
	private void initBuffers(){
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

		ByteBuffer byteBuf = ByteBuffer
				.allocateDirect(textureCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(textureCoords);
		textureBuffer.position(0);
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public void incrementAngle(double step) {
		if(angle+step > 360.0)
			angle -= 360.0;
		angle+=step;
	}
	
	public void incrementAngle() {
		angle++;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getSide() {
		return height == width? height : -1;
	}

	public void setSide(float side) {
		this.height = side;
		this.width = side;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public void setTextureCoords(float[] textureCoords) {
		this.textureCoords = textureCoords;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public double getRotAngle() {
		return rotAngle;
	}
	public void setRotAngle(double rotAngle) {
		this.rotAngle = rotAngle;
	}
	/**
	 * This function draws our square on screen.
	 * 
	 * @param gl
	 */
	public void draw(GL10 gl) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// Enable the texture state
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);

		if (shouldLoadTexture) {
			loadGLTexture(gl);
			shouldLoadTexture = false;
		}

		// Point to our buffers
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0f);
		gl.glRotatef((float) angle, 0f, 0f, 1f);
		angle += rotAngle;
		if(angle+rotAngle > 360.0)
			angle -= 360.0;
		gl.glScalef(width, height, 0f);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	private void loadGLTexture(GL10 gl) {
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
