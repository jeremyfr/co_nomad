package com.eads.co.nomad;

import android.os.Bundle;
import android.app.Activity;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Annexes extends Activity {

	LinearLayout layout;
	TextView textDocumentation;
	LinearLayout annexLayout;
	AnnexesState state = AnnexesState.NOT_DISPLAYED;
	static int xmax = 1250; // 2500 pour la Nexus 10, à changer dans le layout
							// annexes
							// aussi.
	int x = xmax;

	private void setAnnexeX(int x) {
		textDocumentation.setLayoutParams(new LayoutParams(x,
				LayoutParams.WRAP_CONTENT, 0f));
		annexLayout.setLayoutParams(new LayoutParams(xmax - x,
				LayoutParams.WRAP_CONTENT));
		this.x = x;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.annexes);
		layout = (LinearLayout) findViewById(R.id.linearLayout1);
		textDocumentation = (TextView) findViewById(R.id.textDocumentation);
		annexLayout = (LinearLayout) findViewById(R.id.annexLayout);

		layout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch (arg1.getAction()) {

				case MotionEvent.ACTION_DOWN: // PRESS
					switch (state) {
					case NOT_DISPLAYED:
						setAnnexeX(xmax / 2);
						state = AnnexesState.DISPLAYED_FREE;
						break;
					case DISPLAYED_FREE:
						if (arg1.getX() >= x - 100 && arg1.getX() <= x + 100) {
							state = AnnexesState.DISPLAYED_PRESSED;
						}
						break;
					case DISPLAYED_PRESSED:
						break;
					case DISPLAYED_FULLSCREEN:
						break;
					}
					break;

				case MotionEvent.ACTION_MOVE: // MOVE
					switch (state) {
					case NOT_DISPLAYED:
						break;
					case DISPLAYED_FREE:
						break;
					case DISPLAYED_PRESSED:
						setAnnexeX((int) arg1.getX());
						state = AnnexesState.DISPLAYED_PRESSED;
						break;
					case DISPLAYED_FULLSCREEN:
						break;
					}
					break;

				case MotionEvent.ACTION_UP: // RELEASE
					switch (state) {
					case NOT_DISPLAYED:
						break;
					case DISPLAYED_FREE:
						break;
					case DISPLAYED_PRESSED:
						state = AnnexesState.DISPLAYED_FREE;
						break;
					case DISPLAYED_FULLSCREEN:
						break;
					}
					break;
				}
				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
