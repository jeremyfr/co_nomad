package com.eads.co.nomad;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eads.co.nomad.PanAndZoomListener.Anchor;

/**
 * Class used to manage the display of the documentation.
 * 
 * @author Nicolas Bruniquel
 * @author Jérémy Fricou
 * @author Florian Lefebvre
 * @author Benjamin Louradour
 * @author Guillaume Saas
 */

public class AMMAnnexes extends Activity {

	private DataParsing parser;

	private int x; // abscisse de la séparation entre la zone de texte et
					// l'annexe.
	private static int xmax; // largeur maximale de la zone de texte ou de
								// l'annexe.
	private static int ymax; // ordonnée d'apparition de la flèche basse.
	private static int xmin; // largeur minimale de la zone de texte ou de
								// l'annexe.
	private static int ymin; // ordonnée d'apparition de la flèche haute.
	private static int xseparator = 160; // largeur de la barre de séparation.
	private static int yinfobulle = 185; // hauteur de l'image infobulle.

	private LinearLayout layout; // layout global contenant documentation et
									// annexes.

	private OurScrollView scrollView; // scrollview contenant la documentation.

	private WebView warningWV, jobSetUpWV, procedureWV, closeUpWV, toolsWV,
			picturesWV;

	private LinearLayout warnings, jobSetUp, procedure, closeUp, tools,
			pictures;

	private WebView clickedWB; // WebView contenant le lien de l'annexe cliqué.
	private String annexe; // Nom de l'annexe.

	private ImageView separator; // barre verticale.
	private ImageView infobulle; // image de l'infobulle.

	private LinearLayout annexLayout; // layout de l'annexe.
	private TextView titreAnnexe; // titre de l'annexe
	private ImageButton closeAnnexButton; // bouton femer.
	private ImageButton fullScreenAnnexButton; // bouton plein écran.
	private Button closeAllAnnexButton; //bouton ferme toutes les annexes


	//Pour les annexes multiples
	private DrawerLayout mDrawerLayout;
	private ListView listview;
	private int nb_annexe;
	//Création de la ArrayList qui nous permettra de remplire la listView
    ArrayList<HashMap<String, Object>> listItem;
    HashMap<String, Object> map;
    
    
	// Pour le multitouch
	private ImageView annexImg;
	private FrameLayout layoutImg;

	public AnnexesState state = AnnexesState.NOT_DISPLAYED; // état de l'annexe.

	// Affiche l'annexe.
	private void setAnnexeX(int x) {
		mDrawerLayout.setDrawerLockMode(0,Gravity.END);
		scrollView.setLayoutParams(new LayoutParams(x - xseparator / 3,
				LayoutParams.MATCH_PARENT));
		annexLayout.setLayoutParams(new LayoutParams(xmax - x - xseparator / 3,
				ymax));
		// setInfobulle();
	}

	// Affiche l'annexe et met l'abscisse du séparateur.
	private void setAnnexeXAndX(int _x) {
		setAnnexeX(x);
		x = _x;
	}
	//Affiche le titre de l'annexe et met l'image de l'annexe
	private void setTitleAndImgAnnexe(CharSequence text,String img){
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

	// Place l'infobulle selon l'ordonnée y relative à la WebView contenant le
	// lien vers l'annexe.
	public void setInfobulle(int y) {

		if (clickedWB.equals(warningWV)) {
			setInfobulle(!isCollapsed(R.id.stateWarning), y, 0);
		}

		if (clickedWB.equals(jobSetUpWV)) {
			setInfobulle(!isCollapsed(R.id.stateJobSetUp), y, 1);
		}

		if (clickedWB.equals(procedureWV)) {
			setInfobulle(!isCollapsed(R.id.stateProcedure), y, 2);
		}

		if (clickedWB.equals(closeUpWV)) {
			setInfobulle(!isCollapsed(R.id.stateCloseUp), y, 3);
		}

		if (clickedWB.equals(toolsWV)) {
			setInfobulle(!isCollapsed(R.id.stateTools), y, 4);
		}

		if (clickedWB.equals(picturesWV)) {
			setInfobulle(!isCollapsed(R.id.statePictures), y, 5);
		}
	}

	private void setInfobulle(boolean state, int y, int pos) {
		int y_absolue, y_relative;
		if (state) {
			y_absolue = 110 + 30 * pos + warnings.getHeight()
					+ (pos >= 1 ? 1 : 0) * jobSetUp.getHeight()
					+ (pos >= 2 ? 1 : 0) * procedure.getHeight()
					+ (pos >= 3 ? 1 : 0) * closeUp.getHeight()
					+ (pos >= 4 ? 1 : 0) * tools.getHeight()
					+ (pos >= 5 ? 1 : 0) * pictures.getHeight()
					- clickedWB.getHeight() + y;
		} else {
			y_absolue = (int) (30 * (1 + pos)
					+ ((pos >= 0 ? 0.5 : 0) + (pos >= 1 ? 0.5 : 0))
					* warnings.getHeight()
					+ ((pos >= 1 ? 0.5 : 0) + (pos >= 2 ? 0.5 : 0))
					* jobSetUp.getHeight()
					+ ((pos >= 2 ? 0.5 : 0) + (pos >= 3 ? 0.5 : 0))
					* procedure.getHeight()
					+ ((pos >= 3 ? 0.5 : 0) + (pos >= 4 ? 0.5 : 0))
					* closeUp.getHeight()
					+ ((pos >= 4 ? 0.5 : 0) + (pos >= 5 ? 0.5 : 0))
					* tools.getHeight() + (pos >= 5 ? 0.5 : 0)
					* pictures.getHeight());
		}
		y_relative = y_absolue - scrollView.getScrollY();
		displayInfobulle(y_relative);
	}

	private void displayInfobulle(final int y) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
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
		});
	}

	public void onAnnexeClic(WebView webView, String annexe) {
		Log.i("AMMAnnexes", "Clic Annexe : " + annexe);
		switch (state) {
		case NOT_DISPLAYED:
			setAnnexeXAndX(xmax / 2);
			scrollView.setAnnexe(webView, annexe);
			this.annexe = annexe;
			clickedWB = webView;
			clickedWB.loadUrl("javascript:getPosition('" + annexe + "')");
			//Image à mettre
			ajouteList(annexe,String.valueOf(R.drawable.ata),clickedWB);
			
			//Image à mettre 
			setTitleAndImgAnnexe(annexe,String.valueOf(R.drawable.ata));
			
			state = AnnexesState.DISPLAYED_FREE;
			break;
		case DISPLAYED_FREE:
			if (testeActuel(annexe,webView)){
				setAnnexeX(xmax + xseparator / 3);
				supprimeElt(annexe);
				state = AnnexesState.NOT_DISPLAYED;
			}
			else {
				setAnnexeXAndX(xmax / 2);		
				scrollView.setAnnexe(webView, annexe);
				this.annexe = annexe;
				clickedWB = webView;
				clickedWB.loadUrl("javascript:getPosition('" + annexe + "')");
					//Image a mettre
				ajouteList(annexe,String.valueOf(R.drawable.ata),clickedWB);
				//Image a mettre
				setTitleAndImgAnnexe(annexe,String.valueOf(R.drawable.ata));
				
				state = AnnexesState.DISPLAYED_FREE;		
			}
			break;
		case DISPLAYED_PRESSED:
			break;
		case DISPLAYED_FULLSCREEN:
			break;
		}
	}

	private void getWidthHeight() {
		// Récupération de la largeur et de la hauteur du layout.
		Timer t = new Timer();
		class SetMax extends TimerTask {
			@Override
			public void run() {
				ymax = layout.getHeight();
				xmax = layout.getWidth();
				xmin = xmax / 4;
				ymin = 0;
				x = xmax / 2;
			}
		}
		t.schedule(new SetMax(), 500);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		getWidthHeight();
	}

	@Override
	public void onResume() {
		super.onResume();
		getWidthHeight();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.amm_annexes);

		layout = (LinearLayout) findViewById(R.id.layout_amm);

		separator = (ImageView) findViewById(R.id.separator);
		infobulle = (ImageView) findViewById(R.id.infobulle);

		scrollView = (OurScrollView) findViewById(R.id.scrollView);
		scrollView.setActivity(this);

		annexLayout = (LinearLayout) findViewById(R.id.annexLayout);
		titreAnnexe = (TextView) findViewById(R.id.annexTitle);
		closeAnnexButton = (ImageButton) findViewById(R.id.closeAnnexButton);
		fullScreenAnnexButton = (ImageButton) findViewById(R.id.fullScreenAnnexButton);

		// Pour le multitouch
		annexImg = (ImageView) findViewById(R.id.annexImage);
		layoutImg = (FrameLayout) findViewById(R.id.layoutImage);
		annexImg.setOnTouchListener(new PanAndZoomListener(layoutImg, annexImg,
				Anchor.TOPLEFT));
		
		//Pour les annexes multiples
		listview = (ListView)findViewById(R.id.listview);
		listview.setSelector(R.drawable.selector);
		listItem = new ArrayList<HashMap<String, Object>>();
		//Remplissage des fonctions sur le navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(1, Gravity.END);
		nb_annexe = 0;
		
		closeAllAnnexButton = (Button) findViewById(R.id.closeAllAnnexButton);

		// Récupération de la largeur et de la hauteur du layout.
		getWidthHeight();
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				HashMap<String, Object> map = (HashMap<String, Object>) listview.getItemAtPosition(position);
				annexe = (String)map.get("titre");
				titreAnnexe.setText(annexe);
				int resID = getResources().getIdentifier((String)map.get("img"), "drawable", "package.name");
		        annexImg.setImageResource(resID); 
		        clickedWB = (WebView) map.get("webview");
				clickedWB.loadUrl("javascript:getPosition('" + annexe + "')");
				
        	}
         });

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
						clickedWB.loadUrl("javascript:getPosition('" + annexe
								+ "')");
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

		// Listener sur le bouton fermer.
		closeAnnexButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (state) {
				case NOT_DISPLAYED:
					break;
				case DISPLAYED_FREE:
					setAnnexeX(xmax + xseparator / 3);
					supprimeElt(titreAnnexe.getText().toString());
					state = AnnexesState.NOT_DISPLAYED;
					break;
				case DISPLAYED_PRESSED:
					break;
				case DISPLAYED_FULLSCREEN:
					setAnnexeX(xmax + xseparator / 3);
					supprimeElt(titreAnnexe.getText().toString());
					displaySeparator();
					state = AnnexesState.NOT_DISPLAYED;
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
					hideSeparator();
					state = AnnexesState.DISPLAYED_FULLSCREEN;
					break;
				case DISPLAYED_PRESSED:
					break;
				case DISPLAYED_FULLSCREEN:
					setAnnexeX(x);
					displaySeparator();
					state = AnnexesState.DISPLAYED_FREE;
					break;
				}
			}
		});

		/* Retrieving the documentation to show */
		Bundle bundle = getIntent().getExtras();
		String ammPart = "";
		if (bundle != null) {
			ammPart = (String) bundle.get("task");
		}
		InputStream input = null;
		try {
			input = getApplicationContext().getAssets().open(ammPart + ".xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			parser = new DataParsing(input);
			this.setTitle(parser.getTitle());
			SwitchTaskManager taskManager = new SwitchTaskManager(this, this);
			HashMap<String, String> h = ((History) this.getApplication())
					.getHistory();
			h.put(ammPart, parser.getTitle());

			/* Warnings part */
			warnings = (LinearLayout) findViewById(R.id.warnings);
			warnings.setOnClickListener(manageWarnings);
			((ImageView) findViewById(R.id.stateWarning))
					.setTag(R.drawable.expand);
			warningWV = ((WebView) findViewById(R.id.warnings_text));
			warningWV.loadDataWithBaseURL("file:///android_asset/",
					parser.getWarnings(), "text/html", "UTF-8", null);
			warningWV.setWebViewClient(taskManager);
			warningWV.getSettings().setJavaScriptEnabled(true);
			warningWV.addJavascriptInterface(new JavaScriptInterface(this),
					"MyAndroid");

			/* Job Setup part */
			jobSetUp = (LinearLayout) findViewById(R.id.jobSetUp);
			jobSetUp.setOnClickListener(manageJobSetUp);
			collapse((LinearLayout) findViewById(R.id.jobSetUp_layout));
			((ImageView) findViewById(R.id.stateJobSetUp))
					.setTag(R.drawable.collapse);
			jobSetUpWV = ((WebView) findViewById(R.id.jobSetUp_text));
			jobSetUpWV.loadDataWithBaseURL("file:///android_asset/",
					parser.getJobSetUp(), "text/html", "UTF-8", null);
			jobSetUpWV.setWebViewClient(taskManager);
			jobSetUpWV.getSettings().setJavaScriptEnabled(true);
			jobSetUpWV.addJavascriptInterface(new JavaScriptInterface(this),
					"MyAndroid");
			/* Procedure part */
			procedure = (LinearLayout) findViewById(R.id.procedure);
			procedure.setOnClickListener(manageProcedure);
			collapse((LinearLayout) findViewById(R.id.procedure_layout));
			((ImageView) findViewById(R.id.stateProcedure))
					.setTag(R.drawable.collapse);
			procedureWV = ((WebView) findViewById(R.id.procedure_text));
			procedureWV.loadDataWithBaseURL("file:///android_asset/",
					parser.getProcedure(), "text/html", "UTF-8", null);
			procedureWV.setWebViewClient(taskManager);
			procedureWV.getSettings().setJavaScriptEnabled(true);
			procedureWV.addJavascriptInterface(new JavaScriptInterface(this),
					"MyAndroid");
			/* Close Up part */
			closeUp = (LinearLayout) findViewById(R.id.closeUp);
			closeUp.setOnClickListener(manageCloseUp);
			collapse((LinearLayout) findViewById(R.id.closeUp_layout));
			((ImageView) findViewById(R.id.stateCloseUp))
					.setTag(R.drawable.collapse);
			closeUpWV = ((WebView) findViewById(R.id.closeUp_text));
			closeUpWV.loadDataWithBaseURL("file:///android_asset/",
					parser.getCloseUp(), "text/html", "UTF-8", null);
			closeUpWV.setWebViewClient(taskManager);
			closeUpWV.getSettings().setJavaScriptEnabled(true);
			closeUpWV.addJavascriptInterface(new JavaScriptInterface(this),
					"MyAndroid");
			/* Tools part */
			tools = (LinearLayout) findViewById(R.id.tools);
			tools.setOnClickListener(manageTools);
			collapse((LinearLayout) findViewById(R.id.tools_layout));
			((ImageView) findViewById(R.id.stateTools))
					.setTag(R.drawable.collapse);
			toolsWV = ((WebView) findViewById(R.id.tools_text));
			toolsWV.loadDataWithBaseURL("file:///android_asset/",
					parser.getTools(), "text/html", "UTF-8", null);
			toolsWV.setWebViewClient(taskManager);
			toolsWV.getSettings().setJavaScriptEnabled(true);
			toolsWV.addJavascriptInterface(new JavaScriptInterface(this),
					"MyAndroid");
			/* Pictures part */
			pictures = (LinearLayout) findViewById(R.id.pictures);
			pictures.setOnClickListener(managePictures);
			collapse((LinearLayout) findViewById(R.id.pictures_layout));
			((ImageView) findViewById(R.id.statePictures))
					.setTag(R.drawable.collapse);
			picturesWV = ((WebView) findViewById(R.id.pictures_text));
			picturesWV.loadDataWithBaseURL("file:///android_asset/",
					parser.getPictures(), "text/html", "UTF-8", null);
			picturesWV.setWebViewClient(taskManager);
			picturesWV.getSettings().setJavaScriptEnabled(true);
			picturesWV.addJavascriptInterface(new JavaScriptInterface(this),
					"MyAndroid");
		} catch (Exception e) {
			ammPart = ammPart.substring(ammPart.lastIndexOf('/') + 1);
			this.setTitle("Procedure " + ammPart + " introuvable");
			HashMap<String, String> h = ((History) this.getApplication())
					.getHistory();
			h.put(ammPart, ammPart + " - Unknown task");
			e.printStackTrace();
		}

		/* Gestion du slider gauche (historique) */
		ListView historique = (ListView) findViewById(R.id.left_drawer);
		historique.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		historique.setAdapter(new ArrayAdapter<String>(this,
				R.layout.historic_row, ((History) this.getApplication())
						.getTitles()));
		historique.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onHistoricItemClick(position);
			}
		});

		Thread t = new Thread() {

			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(50);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								switch (state) {
								case NOT_DISPLAYED:
									break;
								case DISPLAYED_FREE:
									clickedWB
											.loadUrl("javascript:getPosition('"
													+ annexe + "')");
									break;
								case DISPLAYED_PRESSED:
									clickedWB
											.loadUrl("javascript:getPosition('"
													+ annexe + "')");
									break;
								case DISPLAYED_FULLSCREEN:
									break;
								}

							}
						});
					}
				} catch (InterruptedException e) {
				}
			}
		};

		t.start();
	}
	//Fonction pour supprimer tout les éléments de la liste 
	private void supprimeTout(){
		listItem =  new ArrayList<HashMap<String, Object>>();
        nb_annexe=0;
        setAnnexeX(xmax + xseparator / 3);
		state = AnnexesState.NOT_DISPLAYED;
		closeAllAnnexButton.setVisibility(View.INVISIBLE);
	}
	//Fonction qui supprime un élément de la listview et agit en conséquence suivant la présence d'autres annexes
	private void supprimeElt(String titre){
		if (listItem.size()!=1){
			Log.w("SupprimeElt","Indice de l'item : " + trouveDansListe(titre));
			listItem.remove(trouveDansListe(titre)-1);
			Log.w("SupprimeElt","Taille de listItem : " + listItem.size() );
	        nb_annexe--;
	        listview.invalidateViews();
	        map = (HashMap<String, Object>) listview.getItemAtPosition(0);
	        titreAnnexe.setText((String)map.get("titre"));
	        int resID = getResources().getIdentifier((String)map.get("img"), "drawable", "package.name");
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
			mDrawerLayout.setDrawerLockMode(1,Gravity.END);
			closeAllAnnexButton.setVisibility(View.INVISIBLE);
			state = AnnexesState.NOT_DISPLAYED;
		}
	}
	//Fonction pour trouver l'indice de l'élément titre
	private int trouveDansListe(String titre){
		Iterator<HashMap<String,Object>> iterateur=listItem.iterator();
		int numero = 0;
		boolean test = false;
		while (!test && iterateur.hasNext())
		{
			HashMap<String, Object> mapElt = iterateur.next();
			test = (mapElt.get("titre").equals(titre));
			numero++;
		}
		return numero;
	}
	// Teste si l'objet de titre titre est l'annexe affichée actuellement
	private boolean testeActuel(String titre,WebView wb){
		return (titre.equals(titreAnnexe.getText().toString()) && clickedWB == wb);
	}
	// Teste si l'objet de titre titre est dans la listview
	private boolean testeObjetDansListe(String titre){
		Iterator<HashMap<String,Object>> iterateur=listItem.iterator();
		boolean test = false;
		while (!test && iterateur.hasNext())
		{
			HashMap<String, Object> mapElt = iterateur.next();
			test = (mapElt.get("titre").equals(titre));
		}
		return test;
	}
	//Ajoute l'élément titre avec son imag img à la listview
	private void ajouteList(String titre, String img, WebView wb) {
		if (!testeObjetDansListe(titre)) {
			map = new HashMap<String, Object>();
	        map.put("titre", titre);
	        map.put("img", img);
	        map.put("webview", wb);
	        listItem.add(map);
	        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichage_annexes
	        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affiche_annexes,
	               new String[] {"img", "titre"}, new int[] {R.id.listImage, R.id.listTitreAnnexe});
	        listview.setAdapter(mSchedule);
	        // +1 annexe
	        nb_annexe++;
	        if (nb_annexe > 1){
				closeAllAnnexButton.setVisibility(View.VISIBLE);
			}
		}
	}
	
	
	private void onHistoricItemClick(int position) {
		Intent i = new Intent(this, AMMAnnexes.class);
		i.putExtra("task", ((History) this.getApplication()).getKeyAt(position));
		this.startActivity(i);
	}

	private View.OnClickListener manageWarnings = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.warnings_layout, R.id.stateWarning);
		}
	};
	private View.OnClickListener manageJobSetUp = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.jobSetUp_layout, R.id.stateJobSetUp);
		}
	};
	private View.OnClickListener manageProcedure = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.procedure_layout, R.id.stateProcedure);
		}
	};
	private View.OnClickListener manageCloseUp = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.closeUp_layout, R.id.stateCloseUp);
		}
	};
	private View.OnClickListener manageTools = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.tools_layout, R.id.stateTools);
		}
	};
	private View.OnClickListener managePictures = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.pictures_layout, R.id.statePictures);
		}
	};

	/**
	 * Expand or collapse a documentation part.
	 * 
	 * @param part
	 *            , the part to expand or collapse
	 * @param icon
	 *            , the icon to manage
	 */
	private void expandOrCollapse(int part, int icon) {
		int tag = Integer.parseInt(((ImageView) findViewById(icon)).getTag()
				.toString());
		if (tag == R.drawable.collapse) {
			expand(part, icon);
		} else {
			collapse(part, icon);
		}
	}

	/**
	 * Expand a documentation part.
	 * 
	 * @param part
	 *            , the part to expand
	 * @param icon
	 *            , the icon to manage
	 */
	private void expand(int part, int icon) {
		int tag = Integer.parseInt(((ImageView) findViewById(icon)).getTag()
				.toString());
		if (tag != R.drawable.expand) {
			expand((LinearLayout) findViewById(part));
			((ImageView) findViewById(icon))
					.setImageResource(R.drawable.expand);
			((ImageView) findViewById(icon)).setTag(R.drawable.expand);
		}
	}

	/**
	 * Collapse a documentation part.
	 * 
	 * @param part
	 *            , the part to collapse
	 * @param icon
	 *            , the icon to manage
	 */
	private void collapse(int part, int icon) {
		int tag = Integer.parseInt(((ImageView) findViewById(icon)).getTag()
				.toString());
		if (tag != R.drawable.collapse) {
			collapse((LinearLayout) findViewById(part));
			((ImageView) findViewById(icon))
					.setImageResource(R.drawable.collapse);
			((ImageView) findViewById(icon)).setTag(R.drawable.collapse);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_amm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_expandAll:
			expand(R.id.warnings_layout, R.id.stateWarning);
			expand(R.id.jobSetUp_layout, R.id.stateJobSetUp);
			expand(R.id.procedure_layout, R.id.stateProcedure);
			expand(R.id.closeUp_layout, R.id.stateCloseUp);
			expand(R.id.tools_layout, R.id.stateTools);
			expand(R.id.pictures_layout, R.id.statePictures);
			return true;
		case R.id.menu_collapseAll:
			collapse(R.id.warnings_layout, R.id.stateWarning);
			collapse(R.id.jobSetUp_layout, R.id.stateJobSetUp);
			collapse(R.id.procedure_layout, R.id.stateProcedure);
			collapse(R.id.closeUp_layout, R.id.stateCloseUp);
			collapse(R.id.tools_layout, R.id.stateTools);
			collapse(R.id.pictures_layout, R.id.statePictures);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void expand(final View v) {
		v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targtetHeight = v.getMeasuredHeight();

		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
						: (int) (targtetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int) (targtetHeight / v.getContext().getResources()
				.getDisplayMetrics().density));
		v.startAnimation(a);
	}

	private void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight
							- (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int) (initialHeight / v.getContext().getResources()
				.getDisplayMetrics().density));
		v.startAnimation(a);
	}

	private boolean isCollapsed(int icon) {
		return Integer.parseInt(((ImageView) findViewById(icon)).getTag()
				.toString()) == R.drawable.collapse;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
