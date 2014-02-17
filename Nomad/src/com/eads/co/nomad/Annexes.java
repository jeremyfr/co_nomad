package com.eads.co.nomad;

import android.os.Bundle;
import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Annexes extends Activity {

	static int xmax = 1250; // 2500 pour la Nexus 10, à changer dans le layout
							// annexes aussi.
	static int xmin = xmax / 5;
	static int xseparator = 160;

	LinearLayout layout;
	TextView textDocumentation;
	ImageView separator;
	LinearLayout annexLayout;
	Button closeAnnexButton, fullScreenAnnexButton;
	AnnexesState state = AnnexesState.NOT_DISPLAYED;

	int x = xmax;

	private void setAnnexeX(int x) {
		textDocumentation.setLayoutParams(new LayoutParams(x - xseparator/3,
				LayoutParams.WRAP_CONTENT));
		annexLayout.setLayoutParams(new LayoutParams(
				xmax - x - xseparator/3, LayoutParams.WRAP_CONTENT));
	}

	private void setAnnexeXAndX(int x) {
		setAnnexeX(x);
		this.x = x;
	}

	private void displaySeparator() {
		separator.setImageResource(R.drawable.vertical_line);
	}

	private void hideSeparator() {
		separator.setImageResource(R.drawable.vertical_line_empty);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.annexes);
		layout = (LinearLayout) findViewById(R.id.linearLayout1);
		textDocumentation = (TextView) findViewById(R.id.textDocumentation);
		separator = (ImageView) findViewById(R.id.separator);
		annexLayout = (LinearLayout) findViewById(R.id.annexLayout);
		closeAnnexButton = (Button) findViewById(R.id.closeAnnexButton);
		fullScreenAnnexButton = (Button) findViewById(R.id.fullScreenAnnexButton);

		// Ajout du lien sur la doc texte + le scroll.
		final SpannableString textToShow = new SpannableString(textDocumentation.getText());
		textToShow.setSpan(new ClickableSpan() {  
	        @Override
	        public void onClick(View v) {  
	        	switch (state) {
				case NOT_DISPLAYED:
					setAnnexeXAndX(xmax / 2);
					state = AnnexesState.DISPLAYED_FREE;
					break;
				case DISPLAYED_FREE:
					setAnnexeX(xmax + xseparator/3);
					state = AnnexesState.NOT_DISPLAYED;
					break;
				case DISPLAYED_PRESSED:
					setAnnexeX(xmax + xseparator/3);
					state = AnnexesState.NOT_DISPLAYED;
					break;
				case DISPLAYED_FULLSCREEN:
					break;
				}
	        } }, 17, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		textDocumentation.setText(textToShow);
		textDocumentation.setMovementMethod(LinkMovementMethod.getInstance());

	    
		// Listener sur le layout entier.
		layout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN: // PRESS
					switch (state) {
					case NOT_DISPLAYED:
						break;
					case DISPLAYED_FREE:
						if (event.getX() >= x - 100 && event.getX() <= x + 100) {
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
						if (event.getX() >= xmin && event.getX() <= xmax - xmin) {
							setAnnexeXAndX((int) event.getX());
						}
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
				return false;
			}
		});

		// Listener sur le bouton fermer.
		closeAnnexButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (state) {
				case NOT_DISPLAYED:
					break;
				case DISPLAYED_FREE:
					setAnnexeX(xmax + xseparator/3);
					state = AnnexesState.NOT_DISPLAYED;
					break;
				case DISPLAYED_PRESSED:
					break;
				case DISPLAYED_FULLSCREEN:
					setAnnexeX(xmax + xseparator/3);
					displaySeparator();
					fullScreenAnnexButton.setText("FullScreen");
					state = AnnexesState.NOT_DISPLAYED;
					break;
				}
			}
		});

		// Listener sur le bouton plein écran.
		fullScreenAnnexButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (state) {
				case NOT_DISPLAYED:
					break;
				case DISPLAYED_FREE:
					setAnnexeX(xseparator/3);
					fullScreenAnnexButton.setText("Window");
					hideSeparator();
					state = AnnexesState.DISPLAYED_FULLSCREEN;
					break;
				case DISPLAYED_PRESSED:
					break;
				case DISPLAYED_FULLSCREEN:
					setAnnexeX(x);
					displaySeparator();
					fullScreenAnnexButton.setText("FullScreen");
					state = AnnexesState.DISPLAYED_FREE;
					break;
				}
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
