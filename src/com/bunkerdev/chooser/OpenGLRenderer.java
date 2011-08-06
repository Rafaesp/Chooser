package com.bunkerdev.chooser;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

public class OpenGLRenderer implements Renderer {

	private LinkedList<GLDrawable> drawables = new LinkedList<GLDrawable>();
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private Bitmap tokenBmp;
	private Bitmap bgBmp;
	private Bitmap needleBmp;
	private int width;
	private int height;
	public static double[] CENTER;

	private int lastFrameDraw = 0;
	private int frameSampleTime = 0;
	private int frameSamplesCollected = 0;
	private int fps = 0;
	private long now = 0l;

	public OpenGLRenderer(Bitmap t, Bitmap n, Bitmap bg) {
		tokenBmp = t;
		bgBmp = bg.copy(Config.RGB_565, false);
		needleBmp = n.copy(Config.ARGB_4444, false);

		CENTER = new double[2];
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}

	public void onSurfaceChanged(GL10 gl, int w, int h) {
		width = w;
		height = h;
		CENTER[0] = (double) width / 2;
		CENTER[1] = (double) height / 2;
		GLBitmap circle = new GLBitmap(bgBmp, width / 2, height / 2, width,
				width);
		drawables.add(circle);
		GLBitmap needle = new GLBitmap(needleBmp, width / 2, height / 2, width,
				width);
		needle.setRotAngle(10.0);
		drawables.add(needle);
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		// Poniendolo en ortogonal
		// left, right, bottom, top, near, far
		gl.glOrthof(0, w, -0, h, -1, 1);
		// Calculate the aspect ratio of the window
		// GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
		// 100.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();

	}

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		synchronized (drawables) {
			for (GLDrawable d : drawables) {
				d.draw(gl);
			}
		}
//		long now = System.currentTimeMillis();
//		if (lastFrameDraw != 0) {
//			int time = (int) (now - lastFrameDraw);
//			frameSampleTime += time;
//			frameSamplesCollected++;
//			if (frameSamplesCollected == 10) {
//				fps = (int) (10000 / frameSampleTime);
//				Main.debug("fps: %d   size: %d", fps, drawables.size());
//				frameSampleTime = 0;
//				frameSamplesCollected = 0;
//			}
//		}
//		lastFrameDraw = (int) now;
	}

	public void addDrawable(GLDrawable d) {
		synchronized (drawables) {
			drawables.add(d);
		}
	}

	public void addToken(float x, float y) {
		// x e y calculado porque android y opengl tienen distinto eje de
		// coordenadas
		Choice c = new Choice("");
		choices.add(c);
		synchronized (drawables) {
			drawables.add(new Token(c, tokenBmp, x, height - y));
		}
	}
	
	public void click(MotionEvent event){
//		if(event.getEventTime() - event.getDownTime() > 200)
			addToken(event.getX(), event.getY());
		Main.debug("time: %d", (int)(event.getEventTime() - event.getDownTime()));
			
	}

}
