package com.bunkerdev.chooserlite;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLCircle implements GLDrawable {

	private float x, y;
	private float diameter;
	private static final int nVertices = 40;
	private static final double radStep = 2*Math.PI/nVertices;
	private float[] vertices  = new float[nVertices*2+4];
	private FloatBuffer vertexBuffer;
	
	public GLCircle(float x, float y, float d){
		this.x=x;
		this.y=y;
		this.diameter = d;
		
		vertices[0] = 0;
		vertices[1] = 0;
		
		double i = 0;
		int j = 2;
		for(; i < 2*Math.PI ; i+=radStep , j+=2){
		    vertices[j]   = (float)(Math.cos((i)));
		    vertices[j+1] = (float)(Math.sin((i)));
		}
		vertices[j]   = vertices[2];
		vertices[j+1] = vertices[3];
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

	public float getDiameter() {
		return diameter;
	}
	public void setRadius(float diameter) {
		this.diameter = diameter;
	}
	public void draw(GL10 gl) {
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0f);
		gl.glScalef(diameter, diameter, 0f);
//		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
//				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, nVertices+2);
		gl.glPopMatrix();
	}
	public void setSide(float i) {
		diameter = i;
	}
	public float getSide() {
		return diameter;
	}

}
