package com.bunkerdev.chooser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class WheelPosition extends Activity implements OnTouchListener{

	private OpenGLRenderer renderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GLSurfaceView view = new GLSurfaceView(this);
		Bitmap mole = BitmapFactory.decodeResource(getResources(),
				R.drawable.mole128);
		Bitmap bg = BitmapFactory.decodeResource(getResources(),
				R.drawable.circle);
		Bitmap needle = BitmapFactory.decodeResource(getResources(),
				R.drawable.needle);
		renderer = new OpenGLRenderer(view, mole, needle, bg);
		view.setRenderer(renderer);
		view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		view.setOnTouchListener(this);
		setContentView(view);
	}

	public boolean onTouch(View v, MotionEvent event) {
		if((event.getAction() & 255) == MotionEvent.ACTION_UP){
			renderer.actionUp(event);
		}else if((event.getAction() & 255) == MotionEvent.ACTION_DOWN){
			renderer.actionDown(new PointF(event.getX(), event.getY()));
		}
		return true;
	}


}
