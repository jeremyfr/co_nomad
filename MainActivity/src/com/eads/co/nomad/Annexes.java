package com.eads.co.nomad;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.text.Layout;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Annexes extends Activity {

	int x; // abscisse de la séparation entre la zone de texte et l'annexe.
	static int xmax; // largeur maximale de la zone de texte ou de l'annexe.
	static int xmin; // largeur minimale de la zone de texte ou de l'annexe.
	static int xseparator = 160; // largeur de la barre de séparation.
	static int yinfobulle = 185; // hauteur de l'image infobulle.

	static int start_link = 1204; // numéro du premier caractère du lien.
	static int end_link = 1213; // numéro du dernier caractère du lien.

	LinearLayout layout;
	TextView textDocumentation;
	ImageView separator;
	ImageView infobulle;
	LinearLayout annexLayout;
	Button closeAnnexButton, fullScreenAnnexButton;
	AnnexesState state = AnnexesState.NOT_DISPLAYED;

	private void setAnnexeX(int x) {
		textDocumentation.setLayoutParams(new LayoutParams(x - xseparator / 3,
				LayoutParams.WRAP_CONTENT));
		annexLayout.setLayoutParams(new LayoutParams(xmax - x - xseparator / 3,
				LayoutParams.WRAP_CONTENT));
		setInfobulle(getY(start_link)); // Mise à jour de la position de
										// l'infobulle.
	}

	private void setAnnexeXAndX(int x) {
		setAnnexeX(x);
		this.x = x;
	}

	private void displaySeparator() {
		separator.setImageResource(R.drawable.vertical_line);
		infobulle.setImageResource(R.drawable.infobulle);
	}

	private void hideSeparator() {
		separator.setImageResource(R.drawable.vertical_line_empty);
		infobulle.setImageResource(R.drawable.vertical_line_empty);
	}

	private void setInfobulle(int y) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				infobulle.getLayoutParams());
		params.topMargin = y - yinfobulle / 3;
		infobulle.setLayoutParams(params);
	}

	// Retourne l'ordonnée en pixel de la ligne contenant le caractère à la
	// position offset.
	private int getY(int offset) {
		int line = textDocumentation.getLayout().getLineForOffset(offset);
		// Position de la ligne contenant le caractère positionné à offset -
		// valeur du scroll + épaisseur d'une ligne
		return textDocumentation.getLayout().getLineTop(line)
				- textDocumentation.getScrollY()
				+ textDocumentation.getLineHeight()/2;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.annexes);
		layout = (LinearLayout) findViewById(R.id.linearLayout1);
		textDocumentation = (TextView) findViewById(R.id.textDocumentation);
		separator = (ImageView) findViewById(R.id.separator);
		infobulle = (ImageView) findViewById(R.id.infobulle);
		annexLayout = (LinearLayout) findViewById(R.id.annexLayout);
		closeAnnexButton = (Button) findViewById(R.id.closeAnnexButton);
		fullScreenAnnexButton = (Button) findViewById(R.id.fullScreenAnnexButton);

		// Récupération de la largeur de l'écran.
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		xmax = size.x - 40; // Padding de 20px à gauche et à droite.
		xmin = xmax / 5;
		x = xmax / 2;

		// Ajout du lien sur la documentation textuelle.
		SpannableString textToShow = new SpannableString(
				textDocumentation.getText());
		textToShow.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View v) {
				switch (state) {
				case NOT_DISPLAYED:
					setAnnexeXAndX(xmax / 2);
					state = AnnexesState.DISPLAYED_FREE;

					// Timer pour la mise à jour de la position de l'infobulle.
					Timer t = new Timer();
					class SetInfobulleTask extends TimerTask {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									setInfobulle(getY(start_link));
								}
							});
						}
					}
					t.schedule(new SetInfobulleTask(), 100);

					break;
				case DISPLAYED_FREE:
					setAnnexeX(xmax + xseparator / 3);
					state = AnnexesState.NOT_DISPLAYED;
					break;
				case DISPLAYED_PRESSED:
					setAnnexeX(xmax + xseparator / 3);
					state = AnnexesState.NOT_DISPLAYED;
					break;
				case DISPLAYED_FULLSCREEN:
					break;
				}
			}
		}, start_link, end_link, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		textDocumentation.setText(textToShow);

		// Listener sur la documentation complète et sur les liens de la
		// documentation.
		// Ajoute le scrolling vertical.
		class DocumentationMovementMethod extends LinkMovementMethod {
			@Override
			public boolean onTouchEvent(TextView widget, Spannable buffer,
					MotionEvent event) {
				setInfobulle(getY(start_link)); // Mise à jour de la position de
												// l'infobulle.
				int action = event.getAction();
				if (action == MotionEvent.ACTION_UP
						|| action == MotionEvent.ACTION_DOWN) {
					int x = (int) event.getX();
					int y = (int) event.getY();
					x -= widget.getTotalPaddingLeft();
					y -= widget.getTotalPaddingTop();
					x += widget.getScrollX();
					y += widget.getScrollY();
					Layout layout = widget.getLayout();
					int line = layout.getLineForVertical(y);
					int off = layout.getOffsetForHorizontal(line, x);
					ClickableSpan[] link = buffer.getSpans(off, off,
							ClickableSpan.class);
					if (link.length != 0) {
						if (action == MotionEvent.ACTION_UP) {
							link[0].onClick(widget);
						} else if (action == MotionEvent.ACTION_DOWN) {
							Selection.setSelection(buffer,
									buffer.getSpanStart(link[0]),
									buffer.getSpanEnd(link[0]));
						}
						return true;
					} else {
						Selection.removeSelection(buffer);
					}
				}
				return Touch.onTouchEvent(widget, buffer, event);
			}
		}
		textDocumentation.setMovementMethod(new DocumentationMovementMethod());

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
					setAnnexeX(xmax + xseparator / 3);
					state = AnnexesState.NOT_DISPLAYED;
					break;
				case DISPLAYED_PRESSED:
					break;
				case DISPLAYED_FULLSCREEN:
					setAnnexeX(xmax + xseparator / 3);
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
					setAnnexeX(xseparator / 3);
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
