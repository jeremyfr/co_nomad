package com.eads.co.nomad;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

/**
 * A simple demo. This shows more how to use jPCT-AE than it shows how to write
 * a proper application for Android. It includes basic activity management to
 * handle pause and resume...
 * 
 * @author EgonOlsen
 * 
 */
public class Plane3D extends Activity {

	// Used to handle pause and resume...
	private static Plane3D master = null;

	private GLSurfaceView mGLView;
	private MyRenderer renderer = null;
	private FrameBuffer fb = null;
	private World world = null;

	private float touchTurn = 0;
	private float touchTurnUp = 0;

	private float xpos = -1;
	private float ypos = -1;
	private float xpos1 = -1;
	private float ypos1 = -1;

	private boolean cameraZoom;
	private float zoomValueLast;
	private float zoomValue;

	private Object3D plane = null;
	private int fps = 0;

	private Light sun = null;

	protected void onCreate(Bundle savedInstanceState) {

		Logger.log("onCreate");

		if (master != null) {
			copy(master);
		}

		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplication());

		mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
				// Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
				// back to Pixelflinger on some device (read: Samsung I7500)
				int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16,
						EGL10.EGL_NONE };
				EGLConfig[] configs = new EGLConfig[1];
				int[] result = new int[1];
				egl.eglChooseConfig(display, attributes, configs, 1, result);
				return configs[0];
			}
		});

		renderer = new MyRenderer();
		mGLView.setRenderer(renderer);
		setContentView(mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void copy(Object src) {
		try {
			Logger.log("Copying data from master Activity!");
			Field[] fs = src.getClass().getDeclaredFields();
			for (Field f : fs) {
				f.setAccessible(true);
				f.set(this, f.get(src));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean onTouchEvent(MotionEvent me) {

		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			xpos = me.getX();
			ypos = me.getY();
			return true;
		}

		if (me.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
			cameraZoom = true;
			xpos1 = me.getX(1);
			ypos1 = me.getY(1);
			zoomValueLast = zoomValue();
			touchTurn = 0;
			touchTurnUp = 0;
		}

		if (me.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
			cameraZoom = false;
			xpos1 = -1;
			ypos1 = -1;
		}

		if (me.getAction() == MotionEvent.ACTION_UP) {
			xpos = -1;
			ypos = -1;
			touchTurn = 0;
			touchTurnUp = 0;
			return true;
		}

		if (me.getAction() == MotionEvent.ACTION_MOVE) {
			float xd = me.getX() - xpos;
			float yd = me.getY() - ypos;

			xpos = me.getX();
			ypos = me.getY();
			if (cameraZoom) {
				xpos1 = me.getX(1);
				ypos1 = me.getY(1);
				float currentZoomValue = zoomValue();
				float newValue = 1 + (zoomValueLast - currentZoomValue) / 400f;
				zoomValueLast = currentZoomValue;
				zoomValue = newValue;
			} else {
				touchTurn = xd / -100f;
				touchTurnUp = yd / -100f;
			}
			return true;
		}

		try {
			Thread.sleep(15);
		} catch (Exception e) {
			// No need for this...
		}

		return super.onTouchEvent(me);
	}

	private float zoomValue() {
		float distance = (float) Math.sqrt((xpos - xpos1) * (xpos - xpos1)
				+ (ypos - ypos1) * (ypos - ypos1));
		return distance;
	}

	protected boolean isFullscreenOpaque() {
		return true;
	}

	class MyRenderer implements GLSurfaceView.Renderer {

		private long time = System.currentTimeMillis();

		public MyRenderer() {
		}

		public void onSurfaceChanged(GL10 gl, int w, int h) {
			if (fb != null) {
				fb.dispose();
			}
			fb = new FrameBuffer(gl, w, h);

			if (master == null) {

				world = new World();
				world.setAmbientLight(200, 200, 200);

				sun = new Light(world);
				sun.enable();
				sun.setIntensity(500, 500, 500);

				try {

					Texture b737_800_2_T = new Texture(getResources()
							.getAssets().open("A380/b737_800_2_T.png"));
					TextureManager.getInstance().addTexture("b737_800_2_T.png",
							b737_800_2_T);

					Texture a380_01 = new Texture(getResources().getAssets()
							.open("A380/a380_01.png"));
					TextureManager.getInstance().addTexture("a380_01.png",
							a380_01);

					Texture A380_mw = new Texture(getResources().getAssets()
							.open("A380/A380_mw.png"));
					TextureManager.getInstance().addTexture("A380_mw.png",
							A380_mw);

					Texture A380_R = new Texture(getResources().getAssets()
							.open("A380/A380_R.png"));
					TextureManager.getInstance().addTexture("A380_R.png",
							A380_R);

					Texture A380_part1 = new Texture(getResources().getAssets()
							.open("A380/A380_part1.png"));
					TextureManager.getInstance().addTexture("A380_part1.png",
							A380_part1);

					Texture A380_part2 = new Texture(getResources().getAssets()
							.open("A380/A380_part2.png"));
					TextureManager.getInstance().addTexture("A380_part2.png",
							A380_part2);

					Texture A380_part3 = new Texture(getResources().getAssets()
							.open("A380/A380_part3.png"));
					TextureManager.getInstance().addTexture("A380_part3.png",
							A380_part3);

					plane = Object3D.mergeAll(Loader.loadOBJ(getResources()
							.getAssets().open("A380/A380.obj"), getResources()
							.getAssets().open("A380/A380.mtl"), 0.007f));
				} catch (IOException e) {
					System.out.println("mauvais chemin");
				}

				plane.setCulling(false);
				plane.strip();
				plane.build();
				world.addObject(plane);

				Camera cam = world.getCamera();
				cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
				cam.lookAt(plane.getTransformedCenter());

				SimpleVector sv = new SimpleVector();
				sv.set(plane.getTransformedCenter());
				sv.y -= 100;
				sv.z -= 100;
				sun.setPosition(sv);
				MemoryHelper.compact();

				if (master == null) {
					Logger.log("Saving master Activity!");
					master = Plane3D.this;
				}
			}
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		}

		public void onDrawFrame(GL10 gl) {
			if (touchTurn != 0) {
				plane.rotateY(touchTurn);
				touchTurn = 0;
			}

			if (touchTurnUp != 0) {
				plane.rotateX(touchTurnUp);
				touchTurnUp = 0;
			}

			// zoom
			if (cameraZoom) {
				Camera cam = world.getCamera();
				if (zoomValue > 1.0) {
					cam.moveCamera(Camera.CAMERA_MOVEOUT, zoomValue * 0.5f);
				} else if (zoomValue < 1.0) {
					cam.moveCamera(Camera.CAMERA_MOVEIN, zoomValue * 0.5f);
				}
				cam.lookAt(plane.getTransformedCenter());
			}

			world.renderScene(fb);
			world.draw(fb);
			fb.display();

			if (System.currentTimeMillis() - time >= 1000) {
				Logger.log(fps + "fps");
				fps = 0;
				time = System.currentTimeMillis();
			}
			fps++;
		}
	}
}