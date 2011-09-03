package com.bunkerdev.chooserlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class WheelPosition extends Activity implements OnTouchListener{

	private OpenGLRenderer renderer;
	private GLSurfaceView view;
	private AlertDialog proDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
	}

	public boolean onTouch(View v, MotionEvent event) {
		if((event.getAction() & 255) == MotionEvent.ACTION_UP){
			renderer.actionUp(event);
		}else if((event.getAction() & 255) == MotionEvent.ACTION_DOWN){
			renderer.actionDown(new PointF(event.getX(), event.getY()));
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		view = new GLSurfaceView(this);
		Bitmap mole = BitmapFactory.decodeResource(getResources(),
				R.drawable.sphere);
		Bitmap bg = BitmapFactory.decodeResource(getResources(),
				R.drawable.bgwheel);
		Bitmap needle = BitmapFactory.decodeResource(getResources(),
				R.drawable.needle);
		
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message m) {
				if(proDialog == null){
					AlertDialog.Builder builder = new Builder(WheelPosition.this);
					builder.setTitle(R.string.proDialogTitle);
					builder.setMessage(R.string.proDialogMessage);
					//Negative-upgrade, Positive-Cancel (order in screen)
					builder.setNegativeButton(R.string.proDialogOK, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							Main.tracker.trackEvent("Click", "Buttons", "GO PRO", 1);
							Intent market = new Intent(Intent.ACTION_VIEW,
									Uri.parse("market://details?id=com.bunkerdev.chooser"));
							startActivity(market);
						}
					});
					builder.setPositiveButton(R.string.proDialogCancel, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							proDialog.dismiss();
						}
					});
					proDialog = builder.create();
				}
				proDialog.show();
			}
		};
		
		renderer = new OpenGLRenderer(view, handler, mole, needle, bg);
		view.setRenderer(renderer);
		view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		view.setOnTouchListener(this);
		setContentView(view);
		Main.tracker.trackPageView("/Wheel");
	}

}
