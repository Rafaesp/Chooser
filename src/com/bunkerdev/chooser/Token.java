package com.bunkerdev.chooser;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;


public class Token implements GLDrawable{
	private double angle;
	private Choice choice;
	private GLDrawable draw;
	
	public Token(){}
	
	public Token(Choice c, Bitmap bmap, float x, float y) {
		this(c, bmap, x, y, 25f, 25f);
		
	}
	
	public Token(Choice c, Bitmap bmap, float x, float y, float side) {
		this(c, bmap, x, y, side, side);
	}
	
	public Token(Choice c, Bitmap bmap, float x, float y, float w, float h) {
		draw = new GLBitmap(bmap, x, y, w, h);
		choice = c;
		angle = Math.atan2(y - OpenGLRenderer.CENTER[1], x - OpenGLRenderer.CENTER[0])*180/Math.PI;
		if(angle < 0){
			angle = 360 - Math.abs(angle);
		}
		c.setName(angle+"");
	}
	
	public void draw(GL10 gl){
		draw.draw(gl);
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public Choice getChoice() {
		return choice;
	}
	public void setChoice(Choice c) {
		this.choice = c;
	}
	public GLDrawable getDraw() {
		return draw;
	}
	public void setGLBitmap(GLDrawable bmp) {
		this.draw = bmp;
	}

	public void setSide(float i) {
		draw.setSide(i);
	}

	public float getSide() {
		return draw.getSide();
	}
 }