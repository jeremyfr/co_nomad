package com.eads.co.nomad;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.eads.co.nomad.PanAndZoomListener.Anchor;

import android.R.color;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.Layout;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Annexes extends Activity {

	int x; // abscisse de la séparation entre la zone de texte et l'annexe.
	static int xmax; // largeur maximale de la zone de texte ou de l'annexe.
	static int ymax; // ordonnée d'apparition de la flèche basse.
	static int xmin; // largeur minimale de la zone de texte ou de l'annexe.
	static int ymin; // ordonnée d'apparition de la flèche haute.
	static int taille_listview; //taille de la listView
	static int xseparator = 160; // largeur de la barre de séparation.
	static int yinfobulle = 185; // hauteur de l'image infobulle.

	static int start_link = 1204; // numéro du premier caractère du lien.
	static int end_link = 1213; // numéro du dernier caractère du lien.
	
	static int start_link2 = 1272; // numéro du premier caractère du 2eme lien.
	static int end_link2 = 1282; // numéro du dernier caractère du 2eme lien.

	LinearLayout layout; // layout global contenant documentation et annexes.
	TextView textDocumentation; // documentation textuelle.
	TextView titreAnnexe; //titre de l'annexe 
	ImageView separator; // barre verticale.
	ImageView infobulle; // image de l'infobulle.
	
	//Pour annexe multiple
	ListView listview;
	private DrawerLayout mDrawerLayout;
	LinearLayout layoutDansAnnexe;
	int nb_annexe;
	
	//Création de la ArrayList qui nous permettra de remplire la listView
    ArrayList<HashMap<String, String>> listItem;
    HashMap<String, String> map;
	
	//Pour le multitouch
	ImageView annexImg; //Image de l'annexe
	FrameLayout layoutImg;
	
	LinearLayout annexLayout; // layout de l'annexe.
	Button closeAnnexButton, fullScreenAnnexButton, closeAllAnnexButton; // boutons d'options des
													// annexes.
	AnnexesState state = AnnexesState.NOT_DISPLAYED; // état de l'annexe.

	// Affiche l'annexe.
	private void setAnnexeX(int x) {
		mDrawerLayout.setDrawerLockMode(0);
		textDocumentation.setLayoutParams(new LayoutParams(x - xseparator / 3,
				LayoutParams.WRAP_CONTENT));
		annexLayout.setLayoutParams(new LayoutParams(xmax - x - xseparator / 3,
				ymax));
		if (nb_annexe>1){
			layoutDansAnnexe.setLayoutParams(new LayoutParams(xmax - x - taille_listview - xseparator / 3,
				ymax));
			closeAllAnnexButton.setVisibility(View.VISIBLE);
		}
		else {
			layoutDansAnnexe.setLayoutParams(new LayoutParams(xmax - x - xseparator / 3,
					ymax));
		}
		setInfobulle(getY(start_link)); // Mise à jour de la position de
										// l'infobulle.
	}

	// Affiche l'annexe et met l'abscisse du séparateur.
	private void setAnnexeXAndX(int x) {
		setAnnexeX(x);
		this.x = x;
	}

	private void setTitleAnnexe(CharSequence text,String img){
		int resID = getResources().getIdentifier(img, "drawable", "package.name");
        annexImg.setImageResource(resID);
		titreAnnexe.setText(text);
		int position = trouveDansListe(titreAnnexe.getText().toString())-1;
		listview.performItemClick(listview.getAdapter().getView(position, null, null), position, position);
	}
	// Affiche le séparateur et l'infobulle.
	private void displaySeparator() {
		separator.setImageResource(R.drawable.vertical_line);
		infobulle.setImageResource(R.drawable.infobulle);
	}

	// Cache le séparateur et l'infobulle.
	private void hideSeparator() {
		separator.setImageResource(R.drawable.vertical_line_empty);
		infobulle.setImageResource(R.drawable.vertical_line_empty);
	}

	// Place l'infobulle à l'ordonnée y.
	private void setInfobulle(int y) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				infobulle.getLayoutParams());
		if (y < ymin) {
			infobulle.setImageResource(R.drawable.fleche_haut);
			params.topMargin = ymin - yinfobulle / 3;
		} else if (y > ymax) {
			infobulle.setImageResource(R.drawable.fleche_bas);
			params.topMargin = ymax - yinfobulle / 3;
		} else {
			infobulle.setImageResource(R.drawable.infobulle);
			params.topMargin = y - yinfobulle / 3;
		}
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
				+ textDocumentation.getLineHeight() / 2;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.annexes);
		layout = (LinearLayout) findViewById(R.id.linearLayout1);
		textDocumentation = (TextView) findViewById(R.id.textDocumentation);
		separator = (ImageView) findViewById(R.id.separator);
		infobulle = (ImageView) findViewById(R.id.infobulle);
		titreAnnexe = (TextView) findViewById(R.id.annexTitle);
		
		//Pour le multitouch
		annexImg = (ImageView) findViewById(R.id.annexImage);
		
		//Pour annexe multiple
		layoutDansAnnexe = (LinearLayout) findViewById(R.id.annexDansLayout);
		listview = (ListView)findViewById(R.id.listview);
		listview.setSelector(R.drawable.selector);
		listItem = new ArrayList<HashMap<String, String>>();
		//Remplissage des fonctions sur le navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(1);
		nb_annexe = 0;
	
		layoutImg = (FrameLayout) findViewById(R.id.layoutImage);
		annexLayout = (LinearLayout) findViewById(R.id.annexLayout);
		closeAnnexButton = (Button) findViewById(R.id.closeAnnexButton);
		fullScreenAnnexButton = (Button) findViewById(R.id.fullScreenAnnexButton);
		closeAllAnnexButton = (Button) findViewById(R.id.closeAllAnnexButton);

		annexImg.setOnTouchListener(new PanAndZoomListener(layoutImg, annexImg, Anchor.TOPLEFT));
		// Récupération de la largeur et de la hauteur du layout.
		Timer t = new Timer();
		class SetMax extends TimerTask {
			@Override
			public void run() {
				ymax = layout.getHeight()- 40; // Padding de 40px.
				xmax = layout.getWidth() - 40; // Padding de 2*20px.
			}
		}
		t.schedule(new SetMax(), 500);

		xmin = xmax / 4;
		ymin = 0;
		x = xmax / 2;
		taille_listview = 0;

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				HashMap<String, String> map = (HashMap<String, String>) listview.getItemAtPosition(position);
				titreAnnexe.setText(map.get("titre"));
				int resID = getResources().getIdentifier(map.get("img"), "drawable", "package.name");
		        annexImg.setImageResource(resID);
				
        	}
         });
		
		
		// Ajout du lien sur la documentation textuelle.
		final SpannableString textToShow = new SpannableString(
				textDocumentation.getText());
		textToShow.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View v) {
				switch (state) {
				case NOT_DISPLAYED:
					ajouteList(textToShow.subSequence(start_link, end_link).toString(), String.valueOf(R.drawable.ata));
					setAnnexeXAndX(xmax / 2);
					setTitleAnnexe(textToShow.subSequence(start_link, end_link),String.valueOf(R.drawable.ata));
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
					if (testeActuel(textToShow.subSequence(start_link, end_link).toString())){
						setAnnexeX(xmax + xseparator / 3);
						supprimeElt(textToShow.subSequence(start_link, end_link).toString());
						state = AnnexesState.NOT_DISPLAYED;
					}
					else {
						if (!testeObjetDansListe(textToShow.subSequence(start_link, end_link).toString())){
							ajouteList(textToShow.subSequence(start_link, end_link).toString(), String.valueOf(R.drawable.ata));
						}
						setAnnexeXAndX(xmax / 2);
						setTitleAnnexe(textToShow.subSequence(start_link, end_link),String.valueOf(R.drawable.ata));
						state = AnnexesState.DISPLAYED_FREE;
						// Timer pour la mise à jour de la position de l'infobulle.
						t = new Timer();
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
					}
					break;
				case DISPLAYED_PRESSED:
					if (testeActuel(textToShow.subSequence(start_link, end_link).toString())){
						setAnnexeX(xmax + xseparator / 3);
						supprimeElt(textToShow.subSequence(start_link, end_link).toString());
						state = AnnexesState.NOT_DISPLAYED;
					}
					else {
						if (!testeObjetDansListe(textToShow.subSequence(start_link, end_link).toString())){
							ajouteList(textToShow.subSequence(start_link, end_link).toString(), String.valueOf(R.drawable.ata));
						}
						setAnnexeXAndX(xmax / 2);
						setTitleAnnexe(textToShow.subSequence(start_link, end_link),String.valueOf(R.drawable.ata));
						state = AnnexesState.DISPLAYED_FREE;
						// Timer pour la mise à jour de la position de l'infobulle.
						t = new Timer();
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
					}
					break;
				case DISPLAYED_FULLSCREEN:
					break;
				}
			}
		}, start_link, end_link, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		textToShow.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View v) {
				switch (state) {
				case NOT_DISPLAYED:
					ajouteList(textToShow.subSequence(start_link2, end_link2).toString(), String.valueOf(R.drawable.fleche_haut));
					setAnnexeXAndX(xmax / 2);
					setTitleAnnexe(textToShow.subSequence(start_link2, end_link2),String.valueOf(R.drawable.fleche_haut));
					state = AnnexesState.DISPLAYED_FREE;
					// Timer pour la mise à jour de la position de l'infobulle.
					Timer t = new Timer();
					class SetInfobulleTask extends TimerTask {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									setInfobulle(getY(start_link2));
								}
							});
						}
					}
					t.schedule(new SetInfobulleTask(), 100);

					break;
				case DISPLAYED_FREE:
					Log.w("Test dans Displayed_free","Resultat du test " + testeActuel(textToShow.subSequence(start_link2, end_link2).toString()));
					if (testeActuel(textToShow.subSequence(start_link2, end_link2).toString())){
						setAnnexeX(xmax + xseparator / 3);
						supprimeElt(textToShow.subSequence(start_link2, end_link2).toString());
						state = AnnexesState.NOT_DISPLAYED;
					}
					else{
						if (!testeObjetDansListe(textToShow.subSequence(start_link2, end_link2).toString())){
							ajouteList(textToShow.subSequence(start_link2, end_link2).toString(), String.valueOf(R.drawable.fleche_haut));
						}
						setAnnexeXAndX(xmax / 2);
						setTitleAnnexe(textToShow.subSequence(start_link2, end_link2),String.valueOf(R.drawable.fleche_haut));
						state = AnnexesState.DISPLAYED_FREE;
						// Timer pour la mise à jour de la position de l'infobulle.
						t = new Timer();
						class SetInfobulleTask extends TimerTask {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										setInfobulle(getY(start_link2));
									}
								});
							}
						}
						t.schedule(new SetInfobulleTask(), 100);
						
				
					}
					break;
				case DISPLAYED_PRESSED:
					if (testeActuel(textToShow.subSequence(start_link2, end_link2).toString())){
						setAnnexeX(xmax + xseparator / 3);
						state = AnnexesState.NOT_DISPLAYED;
					}
					else {
						if (!testeObjetDansListe(textToShow.subSequence(start_link2, end_link2).toString())){
							ajouteList(textToShow.subSequence(start_link2, end_link2).toString(), String.valueOf(R.drawable.fleche_haut));
						}
						setAnnexeXAndX(xmax / 2);
						setTitleAnnexe(textToShow.subSequence(start_link2, end_link2),String.valueOf(R.drawable.fleche_haut));
						state = AnnexesState.DISPLAYED_FREE;
						// Timer pour la mise à jour de la position de l'infobulle.
						t = new Timer();
						class SetInfobulleTask extends TimerTask {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										setInfobulle(getY(start_link2));
									}
								});
							}
						}
						t.schedule(new SetInfobulleTask(), 100);
					}
					break;
				case DISPLAYED_FULLSCREEN:
					break;
				}
			}
		}, start_link2, end_link2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
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

				// Recopie de LinkMovementMethod.
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
						if (event.getX() >= xmin && event.getX() <= xmax - xmin - taille_listview) {
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
					Log.w("Close", "Close de : " + titreAnnexe.getText().toString());
					supprimeElt(titreAnnexe.getText().toString());
					break;
				case DISPLAYED_PRESSED:
					break;
				case DISPLAYED_FULLSCREEN:
					supprimeElt(titreAnnexe.getText().toString());
					fullScreenAnnexButton.setText("FullScreen");
					break;
				}
			}
		});
		
		// Listener sur le bouton fermer.
				closeAllAnnexButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (state) {
						case NOT_DISPLAYED:
							break;
						case DISPLAYED_FREE:
							supprimeTout();
							break;
						case DISPLAYED_PRESSED:
							break;
						case DISPLAYED_FULLSCREEN:
							supprimeTout();
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
	
	private void supprimeTout(){
		listItem =  new ArrayList<HashMap<String, String>>();
        nb_annexe=0;
        setAnnexeX(xmax + xseparator / 3);
		state = AnnexesState.NOT_DISPLAYED;
		closeAllAnnexButton.setVisibility(View.INVISIBLE);
	}
	
	
	private void supprimeElt(String titre){
		if (listItem.size()!=1){
			Log.w("SupprimeElt","Indice de l'item : " + trouveDansListe(titre));
			listItem.remove(trouveDansListe(titre)-1);
			Log.w("SupprimeElt","Taille de listItem : " + listItem.size() );
	        nb_annexe--;
	        listview.invalidateViews();
	        map = (HashMap<String, String>) listview.getItemAtPosition(0);
	        titreAnnexe.setText(map.get("titre"));
	        int resID = getResources().getIdentifier(map.get("img"), "drawable", "package.name");
	        annexImg.setImageResource(resID);
	        if (listItem.size()==1){
	        	closeAllAnnexButton.setVisibility(View.INVISIBLE);
	        }
	        int position = trouveDansListe(titreAnnexe.getText().toString())-1;
			listview.performItemClick(listview.getAdapter().getView(position, null, null), position, position);
	        setAnnexeXAndX(x);
		}
		else {
			listItem.remove(trouveDansListe(titre)-1);
			listview.invalidateViews();
			nb_annexe--;
			setAnnexeX(xmax + xseparator / 3);
			mDrawerLayout.setDrawerLockMode(1);
			closeAllAnnexButton.setVisibility(View.INVISIBLE);
			state = AnnexesState.NOT_DISPLAYED;
		}
	}
	
	private int trouveDansListe(String titre){
		Iterator<HashMap<String,String>> iterateur=listItem.iterator();
		int numero = 0;
		boolean test = false;
		while (!test && iterateur.hasNext())
		{
			HashMap<String, String> mapElt = iterateur.next();
			test = (mapElt.get("titre").equals(titre));
			numero++;
		}
		return numero;
	}
	
	private boolean testeActuel(String titre){
		return (titre.equals(titreAnnexe.getText().toString()));
	}
	
	private boolean testeObjetDansListe(String titre){
		Iterator<HashMap<String,String>> iterateur=listItem.iterator();
		boolean test = false;
		while (!test && iterateur.hasNext())
		{
			HashMap<String, String> mapElt = iterateur.next();
			test = (mapElt.get("titre").equals(titre));
		}
		return test;
	}
	
	private void ajouteList(String titre, String img) {
		if (!testeObjetDansListe(titre)) {
			map = new HashMap<String, String>();
	        map.put("titre", titre);
	        map.put("img", img);
	        listItem.add(map);
	        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichage_annexes
	        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affiche_annexes,
	               new String[] {"img", "titre"}, new int[] {R.id.listImage, R.id.listTitreAnnexe});
	        listview.setAdapter(mSchedule);
	        // +1 annexe
	        nb_annexe++;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
