package com.bunkerdev.chooser;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;


public class Token implements GLDrawable{
	private double angle;
	private Choice choice;
	private GLBitmap glBitmap;
	
	public Token(){}
	
	public Token(Choice c, Bitmap bmap, float x, float y) {
		this(c, bmap, x, y, 50f, 50f);
		
	}
	public Token(Choice c, Bitmap bmap, float x, float y, float w, float h) {
		glBitmap = new GLBitmap(bmap, x, y, w, h);
		choice = c;
		angle = Math.atan2(y - OpenGLRenderer.CENTER[1], x - OpenGLRenderer.CENTER[0])*180/Math.PI;
		if(angle < 0){
			angle = 360 - Math.abs(angle);
		}
//		Main.debug("Token angle: %f", angle);
		c.setName(angle+"");
	}
	
	public void draw(GL10 gl){
		glBitmap.draw(gl);
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
		this.choice = choice;
	}
	public GLBitmap getGLBitmap() {
		return glBitmap;
	}
	public void setGLBitmap(GLBitmap bmp) {
		this.glBitmap = glBitmap;
	}
 }