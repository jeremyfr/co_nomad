package com.eads.co.nomad;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class JobCard extends Activity {

	private DataParsing parser;

	static int x; // abscisse de la séparation entre la zone de texte et
					// l'annexe.
	static int xmax; // largeur maximale de la zone de texte ou de l'annexe.
	static int ymax; // ordonnée d'apparition de la flèche basse.
	static int xmin; // largeur minimale de la zone de texte ou de l'annexe.
	static int ymin; // ordonnée d'apparition de la flèche haute.
	static int xseparator = 160; // largeur de la barre de séparation.
	static int yinfobulle = 185; // hauteur de l'image infobulle.

	LinearLayout layout; // layout global contenant documentation et annexes.

	static OurScrollView scrollView; // scrollview contenant la documentation.

	static public WebView warningWV, jobSetUpWV, procedureWV, closeUpWV,
			toolsWV, picturesWV;
	
	static WebView clickedWB; // WebView contenant le lien de l'annexe cliqué.

	private RelativeLayout separatorLayout; // layout de la barre verticale.
	ImageView separator; // barre verticale.
	static ImageView infobulle; // image de l'infobulle.

	static LinearLayout annexLayout; // layout de l'annexe.
	TextView titreAnnexe; // titre de l'annexe
	Button closeAnnexButton; // bouton femer.
	Button fullScreenAnnexButton; // bouton plein écran.

	// Pour le multitouch
	ImageView annexImg;
	FrameLayout layoutImg;

	public static AnnexesState state = AnnexesState.NOT_DISPLAYED; // état de
																   // l'annexe.

	ListView listStepProc;
	ListView listStepPreviousProc;
	ArrayList<PreviousStep> previousSteps;
	ArrayList<Step> steps;
	StepListAdapter stepAdapt;
	PreviousStepListAdapter stepAdaptPrevious;
	//StepListAdapter stepAdaptPrevious;
	// Affiche l'annexe.
	private static void setAnnexeX(int x) {
		scrollView.setLayoutParams(new LayoutParams(x - xseparator / 3,
				LayoutParams.MATCH_PARENT));
		annexLayout.setLayoutParams(new LayoutParams(xmax - x - xseparator / 3,
				ymax));
		// setInfobulle();
	}

	// Affiche l'annexe et met l'abscisse du séparateur.
	private static void setAnnexeXAndX(int _x) {
		setAnnexeX(x);
		x = _x;
	}

	// Affiche le séparateur et l'infobulle.
	private void displaySeparator() {
		separatorLayout.setVisibility(View.VISIBLE);

	}

	// Cache le séparateur et l'infobulle.
	private void hideSeparator() {
		separatorLayout.setVisibility(View.INVISIBLE);
	}

	// Place l'infobulle à l'ordonnée y.
	public static void setInfobulle(int y) {
		Log.i("SetInfobulle", ""+y);
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

	public static void onAnnexeClic(WebView webView, String annexe) {
		Log.i("JobCard", "Clic Annexe : " + annexe);
		switch (state) {
		case NOT_DISPLAYED:
			setAnnexeXAndX(xmax / 2);
			scrollView.setAnnexe(webView, annexe);
			clickedWB = webView;
			state = AnnexesState.DISPLAYED_FREE;
			break;
		case DISPLAYED_FREE:
			setAnnexeX(xmax + xseparator / 3);
			state = AnnexesState.NOT_DISPLAYED;
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

		setContentView(R.layout.job);

		layout = (LinearLayout) findViewById(R.id.layout_amm);

		separatorLayout = (RelativeLayout) findViewById(R.id.separatorLayout);
		separator = (ImageView) findViewById(R.id.separator);
		infobulle = (ImageView) findViewById(R.id.infobulle);

		scrollView = (OurScrollView) findViewById(R.id.scrollView);
		scrollView.setActivity(this);
		annexLayout = (LinearLayout) findViewById(R.id.annexLayout);
		titreAnnexe = (TextView) findViewById(R.id.annexTitle);
		closeAnnexButton = (Button) findViewById(R.id.closeAnnexButton);
		fullScreenAnnexButton = (Button) findViewById(R.id.fullScreenAnnexButton);

		// Pour le multitouch
		annexImg = (ImageView) findViewById(R.id.annexImage);
		layoutImg = (FrameLayout) findViewById(R.id.layoutImage);
		annexImg.setOnTouchListener(new PanAndZoomListener(layoutImg, annexImg,
				Anchor.TOPLEFT));

		// Récupération de la largeur et de la hauteur du layout.
		getWidthHeight();

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
						clickedWB.loadUrl("javascript:getPosition('test')");
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
			SwitchTaskManager taskManager = new SwitchTaskManager(this,"jobcard");
			HashMap<String, String> h = ((History) this.getApplication())
					.getHistory();
			for (int i = 0; i < 20; i++) {
				h.put(ammPart, parser.getTitle());
			}

			/* Warnings part */
			LinearLayout warnings = (LinearLayout) findViewById(R.id.warnings);
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
			LinearLayout jobSetUp = (LinearLayout) findViewById(R.id.jobSetUp);
			jobSetUp.setOnClickListener(manageJobSetUp);
			collapse((LinearLayout) findViewById(R.id.jobSetUp_layout));
			((ImageView) findViewById(R.id.stateJobSetUp))
					.setTag(R.drawable.collapse);
			jobSetUpWV = ((WebView) findViewById(R.id.jobSetUp_text));
			jobSetUpWV.loadDataWithBaseURL("file:///android_asset/",
					parser.getJobSetUp(), "text/html", "UTF-8", null);
			jobSetUpWV.setWebViewClient(taskManager);
			jobSetUpWV.getSettings().setJavaScriptEnabled(true);
			jobSetUpWV.addJavascriptInterface(new JavaScriptInterface(this),"MyAndroid");
			/* Procedure part */
			LinearLayout procedure = (LinearLayout) findViewById(R.id.procedure);
			procedure.setOnClickListener(manageProcedure);
			collapse((LinearLayout) findViewById(R.id.procedure_text));
			((ImageView) findViewById(R.id.stateProcedure))
					.setTag(R.drawable.collapse);
			/*procedureWV = ((WebView) findViewById(R.id.procedure_text));
			procedureWV.loadDataWithBaseURL("file:///android_asset/",
					parser.getProcedure(), "text/html", "UTF-8", null);
			procedureWV.setWebViewClient(taskManager);
			procedureWV.getSettings().setJavaScriptEnabled(true);
			procedureWV.addJavascriptInterface(new JavaScriptInterface(this),
					"MyAndroid");*/
			listStepProc = (ListView) findViewById(R.id.listStep);
			listStepPreviousProc = (ListView) findViewById(R.id.listPreviousStep);
			Button prevProc = (Button) findViewById(R.id.previousButton);
			prevProc.setOnClickListener(managePrevious);
			prevProc.setTag(">");
			
			String proc = parser.getProcedure();
			Step s;
			steps = new ArrayList<Step>();
			ArrayList<String> stepProc = parser.getStepsProcedure();
			for (int i = 0; i < stepProc.size(); i++) {
				s = new Step();
				s.setTask(stepProc.get(i));
				steps.add(s);
			}
			
			stepAdapt = new StepListAdapter(this);
			stepAdapt.setListItems(steps);
			listStepProc = (ListView) findViewById(R.id.listStep);
			listStepProc.setAdapter(stepAdapt);
			LayoutParams lpc = (LayoutParams) listStepProc.getLayoutParams();
			lpc.height = 100*steps.size();
			listStepProc.setLayoutParams(lpc);
			
			previousSteps = new ArrayList<PreviousStep>();		
			listStepPreviousProc = (ListView) findViewById(R.id.listPreviousStep);
			stepAdaptPrevious = new PreviousStepListAdapter(this);
			stepAdaptPrevious.setListItems(previousSteps);
			listStepPreviousProc.setAdapter(stepAdaptPrevious);
			lpc = (LayoutParams) listStepPreviousProc.getLayoutParams();
			lpc.height = 10;
			listStepPreviousProc.setLayoutParams(lpc);
			
			/* Close Up part */
			LinearLayout closeUp = (LinearLayout) findViewById(R.id.closeUp);
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
			LinearLayout tools = (LinearLayout) findViewById(R.id.tools);
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
			LinearLayout pictures = (LinearLayout) findViewById(R.id.pictures);
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

		/* Gestion du sider gauche (historique) */
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
			expandOrCollapse(R.id.procedure_text, R.id.stateProcedure);
		}
	};
	
	private View.OnClickListener managePrevious = new View.OnClickListener() {
		public void onClick(View v) {
			Button b = (Button) findViewById(R.id.previousButton);
			if(v.getTag().equals(">")){
				expand((LinearLayout) findViewById(R.id.previousLayout));
				v.setTag("v");
				b.setText("v Previous Steps");
			}else{	
				collapse((LinearLayout) findViewById(R.id.previousLayout));
				v.setTag(">");
				b.setText("> Previous Steps");
			}
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	public class StepListAdapter extends BaseAdapter{

		private List<Step> mStep;
		private LayoutInflater mInf;
		private Button mButton;
		private Context ct;
		private int position;
		
		public StepListAdapter(Context c) {
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
			if(arg1 == null){
				SwitchTaskManager taskManager = new SwitchTaskManager(JobCard.this,"jobcard");
				arg1 = mInf.inflate(R.layout.step, null);
				h = new StepViewHolder();
				//h.mTask = (TextView) arg1.findViewById(R.id.task);
				h.mWV = (WebView) arg1.findViewById(R.id.webView1);
				h.mWV.setWebViewClient(taskManager);
				h.mWV.getSettings().setJavaScriptEnabled(true);
				h.mWV.addJavascriptInterface(new JavaScriptInterface(JobCard.this),"MyAndroid");
				h.mWV.setBackgroundColor(getResources().getColor(R.color.background1_light));
				h.mButton = (Button) arg1.findViewById(R.id.Ok);
				if(pos ==0){
					h.mButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							if(pos == 0){
								LayoutParams lpc = (LayoutParams) listStepProc.getLayoutParams();
								lpc.height -= 110;
								listStepProc.setLayoutParams(lpc);
								lpc = (LayoutParams) listStepPreviousProc.getLayoutParams();
								lpc.height +=110;
								listStepPreviousProc.setLayoutParams(lpc);
								
								Step toDel = steps.remove(pos);
								stepAdapt.notifyDataSetChanged();
								PreviousStep ps = new PreviousStep();
								ps.setTask(toDel.getTask());
								previousSteps.add(ps);
								stepAdaptPrevious.notifyDataSetChanged();
							}
						}

						
					});
				}else{
					h.mButton.setVisibility(View.INVISIBLE);
				}
				arg1.setTag(h);
			}
			else{
				h = (StepViewHolder) arg1.getTag();
			}
			//h.setTask(mStep.get(pos).getTask());
			h.setmWV(mStep.get(pos).getTask());
			h.setButton(mStep.get(pos).getB());
			
			return arg1;
		}

		public void setListItems(List<Step> l){
			mStep = l;
		}

		private class StepViewHolder{
			//private TextView mTask;
			private WebView mWV;
			private Button mButton;
			
			/*public void setTask(String t){
				mTask.setText(t);
			}*/
			
			public void setmWV(String t){
				mWV.loadDataWithBaseURL("file:///android_asset/", t, "text/html", "UTF-8", null);
			}
			
			public void setButton(Button b){
				mButton = b;			
			}
		}

		
	}
	
	public class PreviousStepListAdapter extends BaseAdapter{

		private List<PreviousStep> mStep;
		private LayoutInflater mInf;

		private Context ct;
		private int position;
		
		public PreviousStepListAdapter(Context c) {
			mInf = LayoutInflater.from(c);
			ct = c;
		}

		@Override
		public int getCount() {
			
			return mStep.size();
		}

		@Override
		public PreviousStep getItem(int arg0) {
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
			if(arg1 == null){
				SwitchTaskManager taskManager = new SwitchTaskManager(JobCard.this,"jobcard");
			
				arg1 = mInf.inflate(R.layout.previousstep, null);
				h = new PreviousStepViewHolder();
				h.mButton = (Button) arg1.findViewById(R.id.invalidate);
				h.mButton.setVisibility(View.INVISIBLE);
				h.mWV = (WebView) arg1.findViewById(R.id.webViewPrevious);
				h.mWV.setWebViewClient(taskManager);
				h.mWV.getSettings().setJavaScriptEnabled(true);
				h.mWV.addJavascriptInterface(new JavaScriptInterface(JobCard.this),"MyAndroid");
				h.mWV.setBackgroundColor(getResources().getColor(R.color.background2_light));

				if(pos ==previousSteps.size()-1){

					h.mButton.setVisibility(View.VISIBLE);
					h.mButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							if(pos == previousSteps.size()-1){
								LayoutParams lpc = (LayoutParams) listStepProc.getLayoutParams();
								lpc.height += 110;
								listStepProc.setLayoutParams(lpc);
								lpc = (LayoutParams) listStepPreviousProc.getLayoutParams();
								lpc.height -=110;
								listStepPreviousProc.setLayoutParams(lpc);
								
								PreviousStep toDel = previousSteps.remove(pos);
								
								Step s = new Step();
								s.setTask(toDel.getTask());
								steps.add(0,s);
								stepAdapt.notifyDataSetChanged();
								stepAdaptPrevious.notifyDataSetChanged();
								
							}
						}

						
					});
				}else{
					h.mButton.setVisibility(View.INVISIBLE);
				}
				arg1.setTag(h);
			}
			else{
				h = (PreviousStepViewHolder) arg1.getTag();
			}
			h.setmWV(mStep.get(pos).getTask());
			
			return arg1;
		}

		public void setListItems(ArrayList<PreviousStep> previousSteps){
			mStep = previousSteps;
		}

		private class PreviousStepViewHolder{
			private WebView mWV;
			private Button mButton;
			
			public void setmWV(String t){
				mWV.loadDataWithBaseURL("file:///android_asset/", t, "text/html", "UTF-8", null);
			}
			
			public void setButton(Button b){
				mButton = b;			
			}
		
	}
	}
	
}
