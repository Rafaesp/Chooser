package com.bunkerdev.chooser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.bunkerdev.chooser.OpenGLRenderer.TexturizedSquare;

public class WheelPosition extends Activity implements OnTouchListener{
	
	private OpenGLRenderer renderer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GLSurfaceView view = new GLSurfaceView(this);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mole128);
		renderer = new OpenGLRenderer(bmp);
		view.setRenderer(renderer);
		setContentView(view);
		
		view.setOnTouchListener(this);
		
	}

	public boolean onTouch(View v, MotionEvent event) {
		renderer.click(event.getX(), event.getY());
		return false;
	}

	
	
	
}
