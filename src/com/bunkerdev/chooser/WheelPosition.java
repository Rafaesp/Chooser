package com.bunkerdev.chooser;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class WheelPosition extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GLSurfaceView view = new GLSurfaceView(this);
		view.setRenderer(new OpenGLRenderer());
		setContentView(view);
		    
	}
	
	
	
}
