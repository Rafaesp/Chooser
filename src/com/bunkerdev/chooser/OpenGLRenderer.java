package com.bunkerdev.chooser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.view.MotionEvent;

public class OpenGLRenderer implements Renderer {

	private static final boolean PRO = false;
	private GLSurfaceView view;
	private Handler handler;
	private LinkedList<Token> tokens = new LinkedList<Token>();
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private GLBitmap needle;
	private GLBitmap circle;
	private GLDrawable lastNearest;
	private Bitmap tokenBmp;
	private Bitmap bgBmp;
	private Bitmap needleBmp;
	private WeightedRandom random;
	private int width;
	private int height;
	private int sideToken;
	private int sideTokenBig;
	public static double[] CENTER;
	private PointF pointDown;
	private long timeDown;
	private Timer timer;
	private TimerTask updateTask;
	private double remainingDegrees;
	private boolean running;

	public OpenGLRenderer(GLSurfaceView v, Handler h, Bitmap t, Bitmap n, Bitmap bg) {
		view = v;
		handler = h;
		tokenBmp = t.copy(Config.ARGB_4444, false);
		bgBmp = bg.copy(Config.ARGB_8888, false);
		needleBmp = n.copy(Config.ARGB_8888, false);
		random = new WeightedRandom(choices);

		CENTER = new double[2];
		
		running = false;
		timer = new Timer();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
//		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearColor((float)183/255, (float)209/255, (float)163/255, 1.0f);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE);
		// Para que las transparencias de bitmaps se "fundan"
		gl.glEnable(GL10.GL_BLEND);
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK);
		// Para el blend
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	public void onSurfaceDestroyed(GL10 gl, int w, int h){
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}
	
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		width = w;
		height = h;
		CENTER[0] = (double) width / 2;
		CENTER[1] = (double) height / 2;
		int side = Math.min(width, height);
		sideToken = side/5;
		sideTokenBig = side/3;
		circle = new GLBitmap(bgBmp, width / 2, height / 2, side, side);
		needle = new GLBitmap(needleBmp, width / 2, height / 2, side, side);
		needle.setAngle(0);
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Poniendolo en ortogonal
		// left, right, bottom, top, near, far
		gl.glOrthof(0, w, -0, h, -1, 1);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		circle.draw(gl);
		needle.draw(gl);
		synchronized (tokens) {
			for (Token d : tokens) {
				d.draw(gl);
			}
		}
	}

	public void addToken(float x, float y) {
		if(tokens.size()>=3){
			if(PRO){
				Choice c = new Choice("");
				choices.add(c);
				synchronized (tokens) {
					tokens.add(new Token(c, tokenBmp, x, height - y, sideToken));
				}
			}else{
				Main.tracker.trackEvent("Click", "Buttons", "NeedPRO", 1);
				handler.sendEmptyMessage(0);
			}
		}else{
			Choice c = new Choice("");
			choices.add(c);
			synchronized (tokens) {
				tokens.add(new Token(c, tokenBmp, x, height - y, sideToken));
			}
		}
	}
	
	public void actionDown(PointF p) {
		pointDown = p;
		timeDown = System.currentTimeMillis();
	}

	public void actionUp(MotionEvent event) {
		float xDelta = (pointDown.x - event.getX())
				* (pointDown.x - event.getX());
		float yDelta = (pointDown.y - event.getY())
				* (pointDown.y - event.getY());
		float distance = (float) Math.sqrt(xDelta + yDelta);
		if (distance < 10.0) {
			addToken(event.getX(), event.getY());
			view.requestRender();
		} else {
			if (!tokens.isEmpty()) {
				random = new WeightedRandom(choices);
				Token chosen = choose();
				
				float speed = distance
						/ (System.currentTimeMillis() - timeDown);
				int turns = (int) Math.round(2.857142857 * speed + 2);

				remainingDegrees = 360 * turns;
				double angle1 = Math.abs(needle.getAngle() - chosen.getAngle());
				double angle2 = 360 - (Math.abs(needle.getAngle() - chosen.getAngle()));

				if(Math.abs((needle.getAngle()+angle1) - chosen.getAngle())<0.01)
					remainingDegrees += angle1;
				else
					remainingDegrees += angle2;
				
				if(!running){
					running = true;
					initializeTimer();
					timer.schedule(updateTask, 0, 50);
				}
			}
		}
	}

	private void update() {
		if (!tokens.isEmpty()) {
			double step = -0.0202 * Math.pow(remainingDegrees / 360, 3.0)
					+ 0.1096 * Math.pow(remainingDegrees / 360, 2.0) + 2.8465
					* remainingDegrees / 360 + 2;
			if(remainingDegrees < 2.0)
				step = remainingDegrees;
			
			needle.incrementAngle(step);
			remainingDegrees -= step;
	
			GLDrawable actual = getNearestToken().getDraw();
			if (lastNearest != null && !actual.equals(lastNearest)) {
				lastNearest.setSide(sideToken);
			}
			actual.setSide(sideTokenBig);
			lastNearest = actual;
			view.requestRender();
		}
	}

	private void initializeTimer() {
		updateTask = new TimerTask() {

			@Override
			public void run() {
				if (remainingDegrees <= 0){
					updateTask.cancel();
					running = false;
					Main.tracker.trackEvent("Click", "Buttons", "chooseToken", choices.size());
				}
				update();
			}
		};
	}

	private Token getNearestToken() {
		Token nearest = null;
		double min = Double.MAX_VALUE;
		double actual = 0.0;
		for (Token t : tokens) {
			actual = Math.min(Math.abs(needle.getAngle() - t.getAngle()),
					360 - (Math.abs(needle.getAngle() - t.getAngle())));
			// Main.debug("distancia a %s: %f",
			// choices.get(tokens.indexOf(t)).getName(), actual);
			if (actual < min) {
				min = actual;
				nearest = t;
			}
		}
		// Main.debug("nearest: %s, distancia: %f, aguja: %f",
		// choices.get(tokens.indexOf(nearest)).getName(), min,
		// needle.getAngle());
		return nearest;
	}

	private Token choose() {
		Choice c = random.getChoice(1).get(0);
		return tokens.get(choices.indexOf(c));
	}

}
