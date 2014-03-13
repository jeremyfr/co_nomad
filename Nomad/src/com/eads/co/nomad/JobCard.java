package com.eads.co.nomad;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class JobCard extends AMM implements PropertyChangeListener,
		Serializable {

	private ListView listStepProc;
	private ListView listStepPreviousProc;
	private ListView listStepWarn;
	private ListView listStepPreviousWarning;
	private ListView listStepJobSetup;
	private ListView listStepPreviousJobSetup;
	private ListView listStepCloseUp;
	private ListView listStepPreviousCloseUp;
	private ArrayList<Step> previousStepsWarn;
	private ArrayList<ActualStep> stepsWarn;
	private ArrayList<Step> previousStepsProc;
	private ArrayList<ActualStep> stepsProc;
	private ArrayList<Step> previousStepsJobSetup;
	private ArrayList<ActualStep> stepsJobSetup;
	private ArrayList<Step> previousStepsCloseUp;
	private ArrayList<ActualStep> stepsCloseUp;
	private ActualStep s;
	private StepListAdapterProcedure stepAdaptProc;
	private PreviousStepListAdapterProcedure stepAdaptPreviousProc;
	private String head = "<html><head><meta name=\"viewport\" content=\"minimum-scale=1\" /><link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><script type=\"text/javascript\">function getPosition(element){var curtop = 0;var obj = document.getElementById(element); if (obj.offsetParent) {	do {curtop += 2*obj.offsetTop;	} while (obj = obj.offsetParent);}MyAndroid.receiveValueFromJs(curtop);}</script></head><body>";
	private String endbody = "</body></html>";
	private StepListAdapterWarn stepAdaptWarn;
	private PreviousStepListAdapterWarn stepAdaptPreviousWarn;
	private StepListAdapterJobSetup stepAdaptJobSetup;
	private PreviousStepListAdapterJobSetup stepAdaptPreviousJobSetup;
	private StepListAdapterCloseUp stepAdaptCloseUp;
	private PreviousStepListAdapterCloseUp stepAdaptPreviousCloseUp;
	private boolean warningFinish = false, setUpFinish = false,
			procedureFinish = false, closeUpFinish = false;
	private ImageButton prevWarn, prevJobSetup, prevProc, prevCloseUp;

	private LayoutParams lpc;


	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.job);

		
		
		layout = (LinearLayout) findViewById(R.id.layout_jobcard);

		separatorLayout = (RelativeLayout) findViewById(R.id.separatorLayout);
		separator_up = (ImageView) findViewById(R.id.separator_up);
		separator_down = (ImageView) findViewById(R.id.separator_down);
		infobulle = (ImageView) findViewById(R.id.infobulle);
		droite = (ImageView) findViewById(R.id.droite);
		droite.setLayoutParams(new LayoutParams(0,
				LayoutParams.MATCH_PARENT));

		scrollView = (OurScrollView) findViewById(R.id.scrollView);
		scrollView.setActivity(this);

		annexLayout = (LinearLayout) findViewById(R.id.annexLayout);
		titreAnnexe = (TextView) findViewById(R.id.annexTitle);
		closeAnnexButton = (ImageButton) findViewById(R.id.closeAnnexButton);
		fullScreenAnnexButton = (ImageButton) findViewById(R.id.fullScreenAnnexButton);

		// Pour le multitouch
		annexImg = (ImageView) findViewById(R.id.annexImage);
		layoutImg = (FrameLayout) findViewById(R.id.layoutImage);
		PanAndZoomListener pan = new PanAndZoomListener(layoutImg, annexImg,
				Anchor.TOPLEFT);
		annexImg.setOnTouchListener(pan);
		pan.addPropertyChangeListener(this);

		// Pour les annexes multiples
		listview = (ListView) findViewById(R.id.listview);
		listview.setSelector(R.drawable.selector);
		listItem = new ArrayList<HashMap<String, Object>>();
		// Remplissage des fonctions sur le navigation drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerLockMode(1, Gravity.END);
		nb_annexe = 0;

		// Récupération de la largeur et de la hauteur du layout
		getWidthHeight();

		// Scroll lors d'un clic sur le titre de l'annexe.
		titreAnnexe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				scrollTo(y_absolue);
			}
		});

		map = new HashMap<String, Object>();
		map.put("titre", "Close All");
		map.put("img", String.valueOf(R.drawable.close));
		map.put("webview", null);
		listItem.add(map);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				if (position == 0) {
					supprimeTout();
				} else {
					onListViewAnnexeClick(position);
					if (state != AnnexesState.DISPLAYED_FULLSCREEN) {
						clickedWB.loadUrl("javascript:getPosition('" + annexe
								+ "')");
						scrollView.setAnnexe(clickedWB, annexe);
					}
				}

			}
		});

		// Listener sur l'infobulle.
		infobulle.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int y = y_absolue - scrollView.getScrollY();
				if (y < ymin || y > ymax) {
					scrollTo(y_absolue);
					return true;
				} else
					return false;
			}
		});

		// Listener sur la barre verticale.
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
					supprimeElt(titreAnnexe.getText().toString(), clickedWB);
					break;
				case DISPLAYED_PRESSED:
					break;
				case DISPLAYED_FULLSCREEN:
					supprimeElt(titreAnnexe.getText().toString(), clickedWB);
					break;
				}
			}
		});

		// Listener sur le bouton plein ecran
		fullScreenAnnexButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (state) {
				case NOT_DISPLAYED:
					break;
				case DISPLAYED_FREE:
					hideSeparator();
					scrollX = scrollView.getScrollX();
					scrollY = scrollView.getScrollY();
					setAnnexeX(xseparator / 3);
					fullScreenAnnexButton
							.setImageResource(R.drawable.btn_offscreen);
					state = AnnexesState.DISPLAYED_FULLSCREEN;
					break;
				case DISPLAYED_PRESSED:
					break;
				case DISPLAYED_FULLSCREEN:
					displaySeparator();
					setAnnexeX(x);
					scrollView.setScrollX(scrollX);
					scrollView.setScrollY(scrollY);
					fullScreenAnnexButton
							.setImageResource(R.drawable.btn_fullscreen);
					clickedWB.loadUrl("javascript:getPosition('" + annexe
							+ "')");
					scrollView.setAnnexe(clickedWB, annexe);
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
			title = bundle.getString("titre");
		}

		InputStream input = null;
		try {
			input = getApplicationContext().getAssets().open(ammPart + ".xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			parser = new DataParsing(input);
			this.setTitle(parser.getTitle() + "   /   " + title);

			SwitchTaskManager taskManager = new SwitchTaskManager(this, this);

			HashMap<String, String> h = ((History) this.getApplication())
					.getHistory();
			h.put(ammPart, parser.getTitle());

			/* Date */
			dateRevisionTV = (TextView)findViewById(R.id.date_text);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			dateRevisionTV.setText("Last revision : "+formatter.format(parser.getLastRevision()));
			/* Warnings part */
			warnings = (LinearLayout) findViewById(R.id.warnings);
			warnings.setOnClickListener(manageWarnings);
			((ImageView) findViewById(R.id.stateWarning))
					.setTag(R.drawable.expand);

			listStepWarn = (ListView) findViewById(R.id.listStepWarn);
			listStepPreviousWarning = (ListView) findViewById(R.id.listPreviousStepWarn);
			prevWarn = (ImageButton) findViewById(R.id.previousButtonWarn);
			prevWarn.setOnClickListener(managePreviousWarn);
			prevWarn.setTag(">");
			prevWarn.setVisibility(View.GONE);
			String warn = parser.getWarnings();

			previousStepsWarn = new ArrayList<Step>();
			stepsWarn = new ArrayList<ActualStep>();
			ArrayList<String> stepWarn = parser.getStepsWarning();
			for (int i = 0; i < stepWarn.size(); i++) {
				s = new ActualStep();
				s.setTask(head + stepWarn.get(i) + endbody);
				stepsWarn.add(s);
			}

			stepAdaptWarn = new StepListAdapterWarn(this);
			stepAdaptWarn.setListItems(stepsWarn);
			listStepWarn.setAdapter(stepAdaptWarn);
			lpc = (LayoutParams) listStepWarn.getLayoutParams();
			lpc.height = 185 * stepsWarn.size();
			listStepWarn.setLayoutParams(lpc);

			listStepPreviousWarning = (ListView) findViewById(R.id.listPreviousStepWarn);
			stepAdaptPreviousWarn = new PreviousStepListAdapterWarn(this);
			stepAdaptPreviousWarn.setListItems(previousStepsWarn);
			listStepPreviousWarning.setAdapter(stepAdaptPreviousWarn);
			lpc = (LayoutParams) listStepPreviousWarning.getLayoutParams();
			lpc.height = 10;
			listStepPreviousWarning.setLayoutParams(lpc);

			collapse((LinearLayout) findViewById(R.id.previousLayoutWarn));
			/* Job Setup part */
			jobSetUp = (LinearLayout) findViewById(R.id.jobSetUp);
			jobSetUp.setOnClickListener(manageJobSetUp);
			collapse((LinearLayout) findViewById(R.id.jobSetUp_layout));
			((ImageView) findViewById(R.id.stateJobSetUp))
					.setTag(R.drawable.collapse);

			listStepJobSetup = (ListView) findViewById(R.id.listStepJobSetup);
			listStepPreviousJobSetup = (ListView) findViewById(R.id.listPreviousStepJobSetup);
			prevJobSetup = (ImageButton) findViewById(R.id.previousButtonJobSetup);
			prevJobSetup.setOnClickListener(managePreviousJobSetup);
			prevJobSetup.setTag(">");
			prevJobSetup.setVisibility(View.GONE);
			String jobSetup = parser.getJobSetUp();

			previousStepsJobSetup = new ArrayList<Step>();
			stepsJobSetup = new ArrayList<ActualStep>();
			ArrayList<String> stepJobSetup = parser.getStepsJobSetup();
			for (int i = 0; i < stepJobSetup.size(); i++) {
				s = new ActualStep();
				s.setTask(head + stepJobSetup.get(i) + endbody);
				stepsJobSetup.add(s);
			}

			stepAdaptJobSetup = new StepListAdapterJobSetup(this);
			stepAdaptJobSetup.setListItems(stepsJobSetup);
			listStepJobSetup.setAdapter(stepAdaptJobSetup);
			lpc = (LayoutParams) listStepJobSetup.getLayoutParams();
			lpc.height = 185 * stepsJobSetup.size();
			listStepJobSetup.setLayoutParams(lpc);

			listStepPreviousJobSetup = (ListView) findViewById(R.id.listPreviousStepJobSetup);
			stepAdaptPreviousJobSetup = new PreviousStepListAdapterJobSetup(
					this);
			stepAdaptPreviousJobSetup.setListItems(previousStepsJobSetup);
			listStepPreviousJobSetup.setAdapter(stepAdaptPreviousJobSetup);
			lpc = (LayoutParams) listStepPreviousJobSetup.getLayoutParams();
			lpc.height = 10;
			listStepPreviousJobSetup.setLayoutParams(lpc);

			collapse((LinearLayout) findViewById(R.id.previousLayoutJobSetup));
			
			/* Procedure part */

			procedure = (LinearLayout) findViewById(R.id.procedure);
			procedure.setOnClickListener(manageProcedure);
			collapse((LinearLayout) findViewById(R.id.procedure_layout));
			((ImageView) findViewById(R.id.stateProcedure))
					.setTag(R.drawable.collapse);

			listStepProc = (ListView) findViewById(R.id.listStepProc);
			listStepPreviousProc = (ListView) findViewById(R.id.listPreviousStepProc);
			prevProc = (ImageButton) findViewById(R.id.previousButtonProc);
			prevProc.setOnClickListener(managePreviousProc);
			prevProc.setTag(">");
			prevProc.setVisibility(View.GONE);
			String proc = parser.getProcedure();

			stepsProc = new ArrayList<ActualStep>();
			ArrayList<String> stepProc = parser.getStepsProcedure();
			for (int i = 0; i < stepProc.size(); i++) {
				s = new ActualStep();
				s.setTask(head + stepProc.get(i) + endbody);
				stepsProc.add(s);
			}

			previousStepsProc = new ArrayList<Step>();
			stepAdaptProc = new StepListAdapterProcedure(this);
			stepAdaptProc.setListItems(stepsProc);
			listStepProc.setAdapter(stepAdaptProc);
			lpc = (LayoutParams) listStepProc.getLayoutParams();
			lpc.height = 185 * stepsProc.size();
			listStepProc.setLayoutParams(lpc);

			listStepPreviousProc = (ListView) findViewById(R.id.listPreviousStepProc);
			stepAdaptPreviousProc = new PreviousStepListAdapterProcedure(this);
			stepAdaptPreviousProc.setListItems(previousStepsProc);
			listStepPreviousProc.setAdapter(stepAdaptPreviousProc);
			lpc = (LayoutParams) listStepPreviousProc.getLayoutParams();
			lpc.height = 10;
			listStepPreviousProc.setLayoutParams(lpc);

			collapse((LinearLayout) findViewById(R.id.previousLayoutProc));
			/* Close Up part */
			closeUp = (LinearLayout) findViewById(R.id.closeUp);
			closeUp.setOnClickListener(manageCloseUp);
			collapse((LinearLayout) findViewById(R.id.closeUp_layout));
			((ImageView) findViewById(R.id.stateCloseUp))
					.setTag(R.drawable.collapse);

			listStepCloseUp = (ListView) findViewById(R.id.listStepCloseUp);
			listStepPreviousCloseUp = (ListView) findViewById(R.id.listPreviousStepCloseUp);
			prevCloseUp = (ImageButton) findViewById(R.id.previousButtonCloseUp);
			prevCloseUp.setOnClickListener(managePreviousCloseUp);
			prevCloseUp.setTag(">");
			prevCloseUp.setVisibility(View.GONE);
			String closeUp = parser.getCloseUp();

			stepsCloseUp = new ArrayList<ActualStep>();
			ArrayList<String> stepCloseUp = parser.getStepsCloseUp();
			for (int i = 0; i < stepCloseUp.size(); i++) {
				s = new ActualStep();
				s.setTask(head + stepCloseUp.get(i) + endbody);
				stepsCloseUp.add(s);
			}

			previousStepsCloseUp = new ArrayList<Step>();
			stepAdaptCloseUp = new StepListAdapterCloseUp(this);
			stepAdaptCloseUp.setListItems(stepsCloseUp);
			listStepCloseUp.setAdapter(stepAdaptCloseUp);
			lpc = (LayoutParams) listStepCloseUp.getLayoutParams();
			lpc.height = 185 * stepsCloseUp.size();
			listStepCloseUp.setLayoutParams(lpc);

			listStepPreviousCloseUp = (ListView) findViewById(R.id.listPreviousStepCloseUp);
			stepAdaptPreviousCloseUp = new PreviousStepListAdapterCloseUp(this);
			stepAdaptPreviousCloseUp.setListItems(previousStepsCloseUp);
			listStepPreviousCloseUp.setAdapter(stepAdaptPreviousCloseUp);
			lpc = (LayoutParams) listStepPreviousCloseUp.getLayoutParams();
			lpc.height = 10;
			listStepPreviousCloseUp.setLayoutParams(lpc);

			collapse((LinearLayout) findViewById(R.id.previousLayoutCloseUp));
			
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

			picturesWV.setInitialScale(100);

			picturesWV.loadDataWithBaseURL("file:///android_asset/",
					parser.getPictures(), "text/html", "UTF-8", null);
			picturesWV.setWebViewClient(taskManager);
			picturesWV.getSettings().setJavaScriptEnabled(true);
			picturesWV.addJavascriptInterface(new JavaScriptInterface(this),
					"MyAndroid");
			picturesWV.getLayoutParams().height = pictures.getLayoutParams().height;
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
						Thread.sleep(t1);
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

	private View.OnClickListener managePreviousProc = new View.OnClickListener() {
		public void onClick(View v) {
			ImageButton b = (ImageButton) findViewById(R.id.previousButtonProc);
			if (v.getTag().equals(">")) {
				expand((LinearLayout) findViewById(R.id.previousLayoutProc));
				v.setTag("v");
				b.setImageDrawable(getResources().getDrawable(
						R.drawable.button_previousderoule));
			} else {
				collapse((LinearLayout) findViewById(R.id.previousLayoutProc));
				v.setTag(">");
				b.setImageDrawable(getResources().getDrawable(
						R.drawable.button_previouspasderoule));
			}
		}
	};

	private View.OnClickListener managePreviousWarn = new View.OnClickListener() {
		public void onClick(View v) {
			ImageButton b = (ImageButton) findViewById(R.id.previousButtonWarn);
			if (v.getTag().equals(">")) {
				expand((LinearLayout) findViewById(R.id.previousLayoutWarn));
				v.setTag("v");
				b.setImageDrawable(getResources().getDrawable(
						R.drawable.button_previousderoule));
			} else {
				collapse((LinearLayout) findViewById(R.id.previousLayoutWarn));
				v.setTag(">");
				b.setImageDrawable(getResources().getDrawable(
						R.drawable.button_previouspasderoule));
			}
		}
	};

	private View.OnClickListener managePreviousJobSetup = new View.OnClickListener() {
		public void onClick(View v) {
			ImageButton b = (ImageButton) findViewById(R.id.previousButtonJobSetup);
			if (v.getTag().equals(">")) {
				expand((LinearLayout) findViewById(R.id.previousLayoutJobSetup));
				v.setTag("v");
				b.setImageDrawable(getResources().getDrawable(
						R.drawable.button_previousderoule));
			} else {
				collapse((LinearLayout) findViewById(R.id.previousLayoutJobSetup));
				v.setTag(">");
				b.setImageDrawable(getResources().getDrawable(
						R.drawable.button_previouspasderoule));
			}
		}
	};

	private View.OnClickListener managePreviousCloseUp = new View.OnClickListener() {
		public void onClick(View v) {
			ImageButton b = (ImageButton) findViewById(R.id.previousButtonCloseUp);
			if (v.getTag().equals(">")) {
				expand((LinearLayout) findViewById(R.id.previousLayoutCloseUp));
				v.setTag("v");
				b.setImageDrawable(getResources().getDrawable(
						R.drawable.button_previousderoule));
			} else {
				collapse((LinearLayout) findViewById(R.id.previousLayoutCloseUp));
				v.setTag(">");
				b.setImageDrawable(getResources().getDrawable(
						R.drawable.button_previouspasderoule));
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_amm, menu);
		return true;
	}

	@Override
	protected void setInfobulle(boolean state, int y, int pos) {
		if (state) {
			y_absolue = 140 + 30 * pos + warnings.getHeight()
					+ (pos >= 1 ? 1 : 0) * jobSetUp.getHeight()
					+ (pos >= 2 ? 1 : 0) * procedure.getHeight()
					+ (pos >= 3 ? 1 : 0) * closeUp.getHeight()
					+ (pos >= 4 ? 1 : 0) * tools.getHeight()
					+ (pos >= 5 ? 1 : 0) * pictures.getHeight()
					- clickedWB.getHeight() + y;
		} else {
			y_absolue = (int) (70 + 30 * pos
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
		displayInfobulle(y_absolue - scrollView.getScrollY());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MenuApp.class);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		Log.e("Dans propertyChange", "Cool");
		if (event.getNewValue().equals("FULLSCREEN")) {
			fullScreenAnnexButton.performClick();
		}
	}

	public class StepListAdapterWarn extends BaseAdapter {

		private List<ActualStep> mStep;
		private LayoutInflater mInf;
		private ImageButton mButton;
		private Context ct;
		private int position;

		public StepListAdapterWarn(Context c) {
			mInf = LayoutInflater.from(c);
			ct = c;
		}

		@Override
		public int getCount() {

			return mStep.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mStep.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int pos, View arg1, ViewGroup arg2) {
			position = pos;
			StepViewHolder h;
			if (arg1 == null) {
				SwitchTaskManager taskManager = new SwitchTaskManager(
						JobCard.this, JobCard.this);
				arg1 = mInf.inflate(R.layout.step, null);
				h = new StepViewHolder();
				h.mWV = (WebView) arg1.findViewById(R.id.webView1);
				h.mWV.setWebViewClient(taskManager);
				h.mWV.getSettings().setJavaScriptEnabled(true);
				h.mWV.addJavascriptInterface(new JavaScriptInterface(
						JobCard.this), "MyAndroid");
				h.mWV.setBackgroundColor(getResources().getColor(
						R.color.background1_light));
				h.mButton = (ImageButton) arg1.findViewById(R.id.Ok);
				if (pos == 0) {
					h.mButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (pos == 0) {
								prevWarn.setVisibility(View.VISIBLE);
								LayoutParams lpc = (LayoutParams) listStepWarn
										.getLayoutParams();
								lpc.height -= 185;
								listStepWarn.setLayoutParams(lpc);
								lpc = (LayoutParams) listStepPreviousWarning
										.getLayoutParams();
								lpc.height += 185;
								listStepPreviousWarning.setLayoutParams(lpc);

								ActualStep toDel = mStep.remove(pos);
								stepAdaptWarn.notifyDataSetChanged();
								Step ps = new Step();
								ps.setTask(toDel.getTask());

								previousStepsWarn.add(ps);
								stepAdaptPreviousWarn = new PreviousStepListAdapterWarn(
										ct);
								stepAdaptPreviousWarn
										.setListItems(previousStepsWarn);
								listStepPreviousWarning
										.setAdapter(stepAdaptPreviousWarn);
								if (mStep.isEmpty()) {
									warningFinish = true;
									stepAdaptJobSetup = new StepListAdapterJobSetup(
											JobCard.this);
									stepAdaptJobSetup
											.setListItems(stepsJobSetup);
									listStepJobSetup
											.setAdapter(stepAdaptJobSetup);
								}

							}
						}

					});
				} else {
					h.mButton.setVisibility(View.INVISIBLE);
				}
				arg1.setTag(h);
			} else {
				h = (StepViewHolder) arg1.getTag();
			}
			// h.setTask(mStep.get(pos).getTask());
			h.setmWV(mStep.get(pos).getTask());
			h.setButton(mStep.get(pos).getB());

			return arg1;
		}

		public void setListItems(List<ActualStep> l) {
			mStep = l;
		}

		private class StepViewHolder {
			private WebView mWV;
			private ImageButton mButton;

			public void setmWV(String t) {
				mWV.loadDataWithBaseURL("file:///android_asset/", t,
						"text/html", "UTF-8", null);
			}

			public void setButton(ImageButton b) {
				mButton = b;
			}
		}

	}

	public class PreviousStepListAdapterWarn extends BaseAdapter {

		private List<Step> mStep;
		private LayoutInflater mInf;

		private Context ct;
		private int position;

		public PreviousStepListAdapterWarn(Context c) {
			mInf = LayoutInflater.from(c);
			ct = c;
		}

		@Override
		public int getCount() {

			return mStep.size();
		}

		@Override
		public Step getItem(int arg0) {
			return mStep.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int pos, View arg1, ViewGroup arg2) {
			position = pos;
			PreviousStepViewHolder h;
			if (arg1 == null) {
				SwitchTaskManager taskManager = new SwitchTaskManager(
						JobCard.this, JobCard.this);

				arg1 = mInf.inflate(R.layout.previousstep, null);
				h = new PreviousStepViewHolder();
				h.mButton = (ImageButton) arg1.findViewById(R.id.invalidate);
				h.mButton.setVisibility(View.INVISIBLE);
				h.mWV = (WebView) arg1.findViewById(R.id.webViewPrevious);
				h.mWV.setWebViewClient(taskManager);
				h.mWV.getSettings().setJavaScriptEnabled(true);
				h.mWV.addJavascriptInterface(new JavaScriptInterface(
						JobCard.this), "MyAndroid");
				h.mWV.setBackgroundColor(getResources().getColor(
						R.color.background2_light));

				if (pos == (mStep.size()) - 1) {

					h.mButton.setVisibility(View.VISIBLE);
					h.mButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (pos == (mStep.size()) - 1) {
								LayoutParams lpc = (LayoutParams) listStepWarn
										.getLayoutParams();
								lpc.height += 185;
								listStepWarn.setLayoutParams(lpc);
								lpc = (LayoutParams) listStepPreviousWarning
										.getLayoutParams();
								lpc.height -= 185;
								listStepPreviousWarning.setLayoutParams(lpc);

								Step toDel = mStep.remove(pos);

								ActualStep s = new ActualStep();
								s.setTask(toDel.getTask());
								stepsWarn.add(0, s);
								stepAdaptWarn.notifyDataSetChanged();
								stepAdaptPreviousWarn = new PreviousStepListAdapterWarn(
										ct);
								stepAdaptPreviousWarn.setListItems(mStep);
								listStepPreviousWarning
										.setAdapter(stepAdaptPreviousWarn);
								warningFinish = false;
								if (mStep.isEmpty()) {
									prevWarn.setVisibility(View.GONE);
									collapse((LinearLayout) findViewById(R.id.previousLayoutWarn));
								}
							}
						}

					});
				} else {
					h.mButton.setVisibility(View.INVISIBLE);
				}
				arg1.setTag(h);
			} else {
				h = (PreviousStepViewHolder) arg1.getTag();
			}
			h.setmWV(mStep.get(pos).getTask());

			return arg1;
		}

		public void setListItems(List<Step> previousSteps) {
			mStep = previousSteps;
		}

		private class PreviousStepViewHolder {
			private WebView mWV;
			private ImageButton mButton;

			public void setmWV(String t) {
				mWV.loadDataWithBaseURL("file:///android_asset/", t,
						"text/html", "UTF-8", null);
			}

			public void setButton(ImageButton b) {
				mButton = b;
				b.setVisibility(View.INVISIBLE);
			}

		}
	}

	public class StepListAdapterJobSetup extends BaseAdapter {

		private List<ActualStep> mStep;
		private LayoutInflater mInf;
		private ImageButton mButton;
		private Context ct;
		private int position;

		public StepListAdapterJobSetup(Context c) {
			mInf = LayoutInflater.from(c);
			ct = c;
		}

		@Override
		public int getCount() {

			return mStep.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mStep.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int pos, View arg1, ViewGroup arg2) {
			position = pos;
			StepViewHolder h;
			if (arg1 == null) {
				SwitchTaskManager taskManager = new SwitchTaskManager(
						JobCard.this, JobCard.this);
				arg1 = mInf.inflate(R.layout.step, null);
				h = new StepViewHolder();
				h.mWV = (WebView) arg1.findViewById(R.id.webView1);
				h.mWV.setWebViewClient(taskManager);
				h.mWV.getSettings().setJavaScriptEnabled(true);
				h.mWV.addJavascriptInterface(new JavaScriptInterface(
						JobCard.this), "MyAndroid");
				h.mWV.setBackgroundColor(getResources().getColor(
						R.color.background1_light));
				h.mButton = (ImageButton) arg1.findViewById(R.id.Ok);

				if (pos == 0) {
					if (!warningFinish) {
						h.mButton.setVisibility(View.INVISIBLE);
					}

					h.mButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (pos == 0) {

								prevJobSetup.setVisibility(View.VISIBLE);
								LayoutParams lpc = (LayoutParams) listStepJobSetup
										.getLayoutParams();
								lpc.height -= 185;
								listStepJobSetup.setLayoutParams(lpc);
								lpc = (LayoutParams) listStepPreviousJobSetup
										.getLayoutParams();
								lpc.height += 185;
								listStepPreviousJobSetup.setLayoutParams(lpc);

								ActualStep toDel = mStep.remove(pos);
								stepAdaptJobSetup.notifyDataSetChanged();
								Step ps = new Step();
								ps.setTask(toDel.getTask());

								previousStepsJobSetup.add(ps);
								stepAdaptPreviousJobSetup = new PreviousStepListAdapterJobSetup(
										ct);
								stepAdaptPreviousJobSetup
										.setListItems(previousStepsJobSetup);
								listStepPreviousJobSetup
										.setAdapter(stepAdaptPreviousJobSetup);
								if (mStep.isEmpty()) {
									setUpFinish = true;
									stepAdaptProc = new StepListAdapterProcedure(
											JobCard.this);
									stepAdaptProc.setListItems(stepsProc);
									listStepProc.setAdapter(stepAdaptProc);
								}
							}
						}

					});
				} else {
					h.mButton.setVisibility(View.INVISIBLE);
				}
				arg1.setTag(h);
			} else {
				h = (StepViewHolder) arg1.getTag();
			}
			// h.setTask(mStep.get(pos).getTask());
			h.setmWV(mStep.get(pos).getTask());
			h.setButton(mStep.get(pos).getB());

			return arg1;
		}

		public void setListItems(List<ActualStep> l) {
			mStep = l;
		}

		private class StepViewHolder {
			// private TextView mTask;
			private WebView mWV;
			private ImageButton mButton;

			/*
			 * public void setTask(String t){ mTask.setText(t); }
			 */

			public void setmWV(String t) {
				mWV.loadDataWithBaseURL("file:///android_asset/", t,
						"text/html", "UTF-8", null);
			}

			public void setButton(ImageButton b) {
				mButton = b;
			}
		}

	}

	public class PreviousStepListAdapterJobSetup extends BaseAdapter {

		private List<Step> mStep;
		private LayoutInflater mInf;

		private Context ct;
		private int position;

		public PreviousStepListAdapterJobSetup(Context c) {
			mInf = LayoutInflater.from(c);
			ct = c;

		}

		@Override
		public int getCount() {

			return mStep.size();
		}

		@Override
		public Step getItem(int arg0) {
			return mStep.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int pos, View arg1, ViewGroup arg2) {
			position = pos;
			PreviousStepViewHolder h;
			if (arg1 == null) {
				SwitchTaskManager taskManager = new SwitchTaskManager(
						JobCard.this, JobCard.this);

				arg1 = mInf.inflate(R.layout.previousstep, null);
				h = new PreviousStepViewHolder();
				h.mButton = (ImageButton) arg1.findViewById(R.id.invalidate);
				h.mButton.setVisibility(View.INVISIBLE);
				h.mWV = (WebView) arg1.findViewById(R.id.webViewPrevious);
				h.mWV.setWebViewClient(taskManager);
				h.mWV.getSettings().setJavaScriptEnabled(true);
				h.mWV.addJavascriptInterface(new JavaScriptInterface(
						JobCard.this), "MyAndroid");
				h.mWV.setBackgroundColor(getResources().getColor(
						R.color.background2_light));

				if (pos == (mStep.size()) - 1) {

					h.mButton.setVisibility(View.VISIBLE);
					h.mButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (pos == (mStep.size()) - 1) {
								LayoutParams lpc = (LayoutParams) listStepJobSetup
										.getLayoutParams();
								lpc.height += 185;
								listStepJobSetup.setLayoutParams(lpc);
								lpc = (LayoutParams) listStepPreviousJobSetup
										.getLayoutParams();
								lpc.height -= 185;
								listStepPreviousJobSetup.setLayoutParams(lpc);

								Step toDel = mStep.remove(pos);

								ActualStep s = new ActualStep();
								s.setTask(toDel.getTask());
								stepsJobSetup.add(0, s);
								stepAdaptJobSetup.notifyDataSetChanged();
								stepAdaptPreviousJobSetup = new PreviousStepListAdapterJobSetup(
										ct);
								stepAdaptPreviousJobSetup.setListItems(mStep);
								listStepPreviousJobSetup
										.setAdapter(stepAdaptPreviousJobSetup);
								if (mStep.isEmpty()) {
									prevJobSetup.setVisibility(View.GONE);
									collapse((LinearLayout) findViewById(R.id.previousLayoutJobSetup));

								}
								setUpFinish = false;
							}
						}

					});
				} else {
					h.mButton.setVisibility(View.INVISIBLE);
				}
				arg1.setTag(h);
			} else {
				h = (PreviousStepViewHolder) arg1.getTag();
			}
			h.setmWV(mStep.get(pos).getTask());

			return arg1;
		}

		public void setListItems(List<Step> previousSteps) {
			mStep = previousSteps;
		}

		private class PreviousStepViewHolder {
			private WebView mWV;
			private ImageButton mButton;

			public void setmWV(String t) {
				mWV.loadDataWithBaseURL("file:///android_asset/", t,
						"text/html", "UTF-8", null);
			}

			public void setButton(ImageButton b) {
				mButton = b;
				b.setVisibility(View.INVISIBLE);
			}

		}
	}

	public class StepListAdapterProcedure extends BaseAdapter {

		private List<ActualStep> mStep;
		private LayoutInflater mInf;
		private ImageButton mButton;
		private Context ct;
		private int position;

		public StepListAdapterProcedure(Context c) {
			mInf = LayoutInflater.from(c);
			ct = c;
		}

		@Override
		public int getCount() {

			return mStep.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mStep.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int pos, View arg1, ViewGroup arg2) {
			position = pos;
			StepViewHolder h;
			if (arg1 == null) {
				SwitchTaskManager taskManager = new SwitchTaskManager(
						JobCard.this, JobCard.this);
				arg1 = mInf.inflate(R.layout.step, arg2, false);
				h = new StepViewHolder();
				// h.mTask = (TextView) arg1.findViewById(R.id.task);
				h.mWV = (WebView) arg1.findViewById(R.id.webView1);
				h.mWV.setWebViewClient(taskManager);
				h.mWV.getSettings().setJavaScriptEnabled(true);
				h.mWV.addJavascriptInterface(new JavaScriptInterface(
						JobCard.this), "MyAndroid");
				h.mWV.setBackgroundColor(getResources().getColor(
						R.color.background1_light));
				h.mButton = (ImageButton) arg1.findViewById(R.id.Ok);
				if (pos == 0) {
					if (!warningFinish && !setUpFinish) {
						h.mButton.setVisibility(View.INVISIBLE);
					}
					h.mButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (pos == 0) {
								prevProc.setVisibility(View.VISIBLE);
								LayoutParams lpc = (LayoutParams) listStepProc
										.getLayoutParams();
								lpc.height -= 185;
								listStepProc.setLayoutParams(lpc);
								lpc = (LayoutParams) listStepPreviousProc
										.getLayoutParams();
								lpc.height += 185;
								listStepPreviousProc.setLayoutParams(lpc);

								ActualStep toDel = mStep.remove(pos);
								stepAdaptProc.notifyDataSetChanged();
								Step ps = new Step();
								ps.setTask(toDel.getTask());

								previousStepsProc.add(ps);
								stepAdaptPreviousProc = new PreviousStepListAdapterProcedure(
										ct);
								stepAdaptPreviousProc
										.setListItems(previousStepsProc);
								listStepPreviousProc
										.setAdapter(stepAdaptPreviousProc);
								if (mStep.isEmpty()) {
									procedureFinish = true;
									stepAdaptCloseUp = new StepListAdapterCloseUp(
											JobCard.this);
									stepAdaptCloseUp.setListItems(stepsCloseUp);
									listStepCloseUp
											.setAdapter(stepAdaptCloseUp);
								}
							}
						}

					});
				} else {
					h.mButton.setVisibility(View.INVISIBLE);
				}
				arg1.setTag(h);
			} else {
				h = (StepViewHolder) arg1.getTag();
			}
			// h.setTask(mStep.get(pos).getTask());
			h.setmWV(mStep.get(pos).getTask());
			h.setButton(mStep.get(pos).getB());

			return arg1;
		}

		public void setListItems(List<ActualStep> l) {
			mStep = l;
		}

		private class StepViewHolder {
			// private TextView mTask;
			private WebView mWV;
			private ImageButton mButton;

			/*
			 * public void setTask(String t){ mTask.setText(t); }
			 */

			public void setmWV(String t) {
				mWV.loadDataWithBaseURL("file:///android_asset/", t,
						"text/html", "UTF-8", null);
			}

			public void setButton(ImageButton b) {
				mButton = b;
			}
		}

	}

	public class PreviousStepListAdapterProcedure extends BaseAdapter {

		private List<Step> mStep;
		private LayoutInflater mInf;

		private Context ct;
		private int position;

		public PreviousStepListAdapterProcedure(Context c) {
			mInf = LayoutInflater.from(c);
			ct = c;
		}

		@Override
		public int getCount() {

			return mStep.size();
		}

		@Override
		public Step getItem(int arg0) {
			return mStep.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int pos, View arg1, ViewGroup arg2) {
			position = pos;
			PreviousStepViewHolder h;
			if (arg1 == null) {
				SwitchTaskManager taskManager = new SwitchTaskManager(
						JobCard.this, JobCard.this);

				arg1 = mInf.inflate(R.layout.previousstep, null);
				h = new PreviousStepViewHolder();
				h.mButton = (ImageButton) arg1.findViewById(R.id.invalidate);
				h.mButton.setVisibility(View.INVISIBLE);
				h.mWV = (WebView) arg1.findViewById(R.id.webViewPrevious);
				h.mWV.setWebViewClient(taskManager);
				h.mWV.getSettings().setJavaScriptEnabled(true);
				h.mWV.addJavascriptInterface(new JavaScriptInterface(
						JobCard.this), "MyAndroid");
				h.mWV.setBackgroundColor(getResources().getColor(
						R.color.background2_light));

				if (pos == (mStep.size()) - 1) {

					h.mButton.setVisibility(View.VISIBLE);
					h.mButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (pos == (mStep.size()) - 1) {
								LayoutParams lpc = (LayoutParams) listStepProc
										.getLayoutParams();
								lpc.height += 185;
								listStepProc.setLayoutParams(lpc);
								lpc = (LayoutParams) listStepPreviousProc
										.getLayoutParams();
								lpc.height -= 185;
								listStepPreviousProc.setLayoutParams(lpc);

								Step toDel = mStep.remove(pos);

								ActualStep s = new ActualStep();
								s.setTask(toDel.getTask());
								stepsProc.add(0, s);
								stepAdaptProc.notifyDataSetChanged();
								stepAdaptPreviousProc = new PreviousStepListAdapterProcedure(
										ct);
								stepAdaptPreviousProc.setListItems(mStep);
								listStepPreviousProc
										.setAdapter(stepAdaptPreviousProc);
								if (mStep.isEmpty()) {
									prevJobSetup.setVisibility(View.GONE);
									collapse((LinearLayout) findViewById(R.id.previousLayoutJobSetup));

								}
								setUpFinish = false;
							}
						}

					});
				} else {
					h.mButton.setVisibility(View.INVISIBLE);
				}
				arg1.setTag(h);
			} else {
				h = (PreviousStepViewHolder) arg1.getTag();
			}
			h.setmWV(mStep.get(pos).getTask());

			return arg1;
		}

		public void setListItems(List<Step> previousSteps) {
			mStep = previousSteps;
		}

		private class PreviousStepViewHolder {
			private WebView mWV;
			private ImageButton mButton;

			public void setmWV(String t) {
				mWV.loadDataWithBaseURL("file:///android_asset/", t,
						"text/html", "UTF-8", null);
			}

			public void setButton(ImageButton b) {
				mButton = b;
				b.setVisibility(View.INVISIBLE);
			}

		}
	}

	public class StepListAdapterCloseUp extends BaseAdapter {

		private List<ActualStep> mStep;
		private LayoutInflater mInf;
		private ImageButton mButton;
		private Context ct;
		private int position;

		public StepListAdapterCloseUp(Context c) {
			mInf = LayoutInflater.from(c);
			ct = c;
		}

		@Override
		public int getCount() {

			return mStep.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mStep.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int pos, View arg1, ViewGroup arg2) {
			position = pos;
			StepViewHolder h;
			if (arg1 == null) {
				SwitchTaskManager taskManager = new SwitchTaskManager(
						JobCard.this, JobCard.this);
				arg1 = mInf.inflate(R.layout.step, null);
				h = new StepViewHolder();

				h.mWV = (WebView) arg1.findViewById(R.id.webView1);
				h.mWV.setWebViewClient(taskManager);
				h.mWV.getSettings().setJavaScriptEnabled(true);
				h.mWV.addJavascriptInterface(new JavaScriptInterface(
						JobCard.this), "MyAndroid");
				h.mWV.setBackgroundColor(getResources().getColor(
						R.color.background1_light));
				h.mButton = (ImageButton) arg1.findViewById(R.id.Ok);
				if (pos == 0) {
					if (!warningFinish && !setUpFinish && !procedureFinish) {
						h.mButton.setVisibility(View.INVISIBLE);
					}
					h.mButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (pos == 0) {

								prevCloseUp.setVisibility(View.VISIBLE);

								LayoutParams lpc = (LayoutParams) listStepCloseUp
										.getLayoutParams();
								lpc.height -= 185;
								listStepCloseUp.setLayoutParams(lpc);
								lpc = (LayoutParams) listStepPreviousCloseUp
										.getLayoutParams();
								lpc.height += 185;
								listStepPreviousCloseUp.setLayoutParams(lpc);

								ActualStep toDel = mStep.remove(pos);
								stepAdaptCloseUp.notifyDataSetChanged();
								Step ps = new Step();
								ps.setTask(toDel.getTask());

								previousStepsCloseUp.add(ps);
								stepAdaptPreviousCloseUp = new PreviousStepListAdapterCloseUp(
										ct);
								stepAdaptPreviousCloseUp
										.setListItems(previousStepsCloseUp);
								listStepPreviousCloseUp
										.setAdapter(stepAdaptPreviousCloseUp);

							}
						}

					});
				} else {
					h.mButton.setVisibility(View.INVISIBLE);
				}
				arg1.setTag(h);
			} else {
				h = (StepViewHolder) arg1.getTag();
			}
			// h.setTask(mStep.get(pos).getTask());
			h.setmWV(mStep.get(pos).getTask());
			h.setButton(mStep.get(pos).getB());

			return arg1;
		}

		public void setListItems(List<ActualStep> l) {
			mStep = l;
		}

		private class StepViewHolder {
			// private TextView mTask;
			private WebView mWV;
			private ImageButton mButton;

			/*
			 * public void setTask(String t){ mTask.setText(t); }
			 */

			public void setmWV(String t) {
				mWV.loadDataWithBaseURL("file:///android_asset/", t,
						"text/html", "UTF-8", null);
			}

			public void setButton(ImageButton b) {
				mButton = b;
			}
		}

	}

	public class PreviousStepListAdapterCloseUp extends BaseAdapter {

		private List<Step> mStep;
		private LayoutInflater mInf;

		private Context ct;
		private int position;

		public PreviousStepListAdapterCloseUp(Context c) {
			mInf = LayoutInflater.from(c);
			ct = c;

		}

		@Override
		public int getCount() {

			return mStep.size();
		}

		@Override
		public Step getItem(int arg0) {
			return mStep.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int pos, View arg1, ViewGroup arg2) {
			position = pos;
			PreviousStepViewHolder h;
			if (arg1 == null) {
				SwitchTaskManager taskManager = new SwitchTaskManager(
						JobCard.this, JobCard.this);

				arg1 = mInf.inflate(R.layout.previousstep, null);
				h = new PreviousStepViewHolder();
				h.mButton = (ImageButton) arg1.findViewById(R.id.invalidate);
				h.mButton.setVisibility(View.INVISIBLE);
				h.mWV = (WebView) arg1.findViewById(R.id.webViewPrevious);
				h.mWV.setWebViewClient(taskManager);
				h.mWV.getSettings().setJavaScriptEnabled(true);
				h.mWV.addJavascriptInterface(new JavaScriptInterface(
						JobCard.this), "MyAndroid");
				h.mWV.setBackgroundColor(getResources().getColor(
						R.color.background2_light));

				if (pos == (mStep.size()) - 1) {

					h.mButton.setVisibility(View.VISIBLE);
					h.mButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (pos == (mStep.size()) - 1) {
								LayoutParams lpc = (LayoutParams) listStepCloseUp
										.getLayoutParams();
								lpc.height += 185;
								listStepCloseUp.setLayoutParams(lpc);
								lpc = (LayoutParams) listStepPreviousCloseUp
										.getLayoutParams();
								lpc.height -= 185;
								listStepCloseUp.setLayoutParams(lpc);

								Step toDel = mStep.remove(pos);

								ActualStep s = new ActualStep();
								s.setTask(toDel.getTask());
								stepsCloseUp.add(0, s);
								stepAdaptCloseUp.notifyDataSetChanged();
								stepAdaptPreviousCloseUp = new PreviousStepListAdapterCloseUp(
										ct);
								stepAdaptPreviousCloseUp.setListItems(mStep);
								listStepPreviousCloseUp
										.setAdapter(stepAdaptPreviousCloseUp);

							}
						}

					});
				} else {
					h.mButton.setVisibility(View.INVISIBLE);
				}
				arg1.setTag(h);
			} else {
				h = (PreviousStepViewHolder) arg1.getTag();
			}
			h.setmWV(mStep.get(pos).getTask());

			return arg1;
		}

		public void setListItems(List<Step> previousSteps) {
			mStep = previousSteps;
		}

		private class PreviousStepViewHolder {
			private WebView mWV;
			private ImageButton mButton;

			public void setmWV(String t) {
				mWV.loadDataWithBaseURL("file:///android_asset/", t,
						"text/html", "UTF-8", null);
			}

			public void setButton(ImageButton b) {
				mButton = b;
				b.setVisibility(View.INVISIBLE);
			}

		}
	}
}
