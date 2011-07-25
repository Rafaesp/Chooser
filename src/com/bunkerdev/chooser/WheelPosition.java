package com.bunkerdev.chooser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.bunkerdev.chooser.OpenGLRenderer.TexturizedSquare;

public class WheelPosition extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GLSurfaceView view = new GLSurfaceView(this);
		OpenGLRenderer renderer = new OpenGLRenderer();
		view.setRenderer(renderer);
		setContentView(view);
		
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mole128);
		for(int i = 0; i<25; ++i){
			renderer.addSquare(renderer.new TexturizedSquare(bmp));
		}
		
	}	
	
	
	
}
