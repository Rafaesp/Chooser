package com.bunkerdev.chooserlite;

import javax.microedition.khronos.opengles.GL10;

public interface GLDrawable {
	public void draw(GL10 gl);

	public void setSide(float i);
	public float getSide();
}
