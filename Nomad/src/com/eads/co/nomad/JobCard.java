package com.eads.co.nomad;

import java.util.ArrayList;
import java.util.List;

import com.eads.co.nomad.StepListAdapter.StepViewHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class JobCard extends Activity{

	ArrayList<String> previousSteps;
	ArrayList<Step> steps;
	ListView listStep;
	ListView listStepPrevious;
	StepListAdapter2 stepAdapt;
	ArrayAdapter<String> stepAdaptPrevious;
	Testlistview t;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Object mInf = LayoutInflater.from(getApplicationContext());
        getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setTitle("Job card n° XXXXXXXXX MSN:XXXX FSN:XX AIRCRAFT ID: X-XXXX 07-02-2014");
        setContentView(R.layout.job);
		
        LinearLayout warnings = (LinearLayout) findViewById(R.id.warnings);
		warnings.setOnClickListener(manageWarnings);
		((ImageView) findViewById(R.id.stateWarning)).setTag(R.drawable.expand);
		
		LinearLayout jobSetUp = (LinearLayout) findViewById(R.id.jobSetUp);
		jobSetUp.setOnClickListener(manageJobSetUp);
		collapse((LinearLayout) findViewById(R.id.jobSetUp_text));
		((ImageView) findViewById(R.id.stateJobSetUp)).setTag(R.drawable.collapse);
		
		LinearLayout procedure = (LinearLayout) findViewById(R.id.procedure);
		procedure.setOnClickListener(manageProcedure);
		collapse((LinearLayout) findViewById(R.id.procedure_text));
		((ImageView) findViewById(R.id.stateProcedure)).setTag(R.drawable.collapse);

		
		LinearLayout previous = (LinearLayout)findViewById(R.id.previousLayout);
		collapse((LinearLayout) findViewById(R.id.previousLayout));
		
		Button previousButton = (Button)findViewById(R.id.previousButton);
		previousButton.setOnClickListener(managePrevious);
		previousButton.setTag(">");
		
		
		//Remplir la liste de step, voir avec la doc comment on va se demerder, surement du parsage XML
		steps = new ArrayList<Step>();
		
		Step s = new Step();
		s.setTask("Test");
		steps.add(s);
		
		Step s2 = new Step();
		s2.setTask("Test 2");
		steps.add(s2);
		
		stepAdapt = new StepListAdapter2(this);
		stepAdapt.setListItems(steps);
		listStep = (ListView) findViewById(R.id.listStep);
		listStep.setAdapter(stepAdapt);
		LayoutParams lpc = listStep.getLayoutParams();
		lpc.height = 200;
		listStep.setLayoutParams(lpc);
		
		previousSteps = new ArrayList<String>();		
		listStepPrevious = (ListView) findViewById(R.id.listPreviousStep);
		stepAdaptPrevious = new ArrayAdapter<String>(getApplicationContext(),R.layout.mytextview,previousSteps);
		listStepPrevious.setAdapter(stepAdaptPrevious);
		lpc = listStepPrevious.getLayoutParams();
		lpc.height = 100;
		listStepPrevious.setLayoutParams(lpc);

		LinearLayout closeUp = (LinearLayout) findViewById(R.id.closeUp);
		closeUp.setOnClickListener(manageCloseUp);
		collapse((LinearLayout) findViewById(R.id.closeUp_text));
		((ImageView) findViewById(R.id.stateCloseUp)).setTag(R.drawable.collapse);
		
		LinearLayout tools = (LinearLayout) findViewById(R.id.tools);
		tools.setOnClickListener(manageTools);
		collapse((LinearLayout) findViewById(R.id.tools_text));
		((ImageView) findViewById(R.id.stateTools)).setTag(R.drawable.collapse);
		
		LinearLayout pictures = (LinearLayout) findViewById(R.id.pictures);
		pictures.setOnClickListener(managePictures);
		collapse((LinearLayout) findViewById(R.id.pictures_text));
		((ImageView) findViewById(R.id.statePictures)).setTag(R.drawable.collapse);
		
    }

    private View.OnClickListener validateStep = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "lol", Toast.LENGTH_LONG).show();
			
		}
	};
    
	private View.OnClickListener manageWarnings = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.warnings_text,R.id.stateWarning);
		}
	};
	private View.OnClickListener manageJobSetUp = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.jobSetUp_text,R.id.stateJobSetUp);
		}
	};
	private View.OnClickListener manageProcedure = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.procedure_text,R.id.stateProcedure);
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
			expandOrCollapse(R.id.closeUp_text,R.id.stateCloseUp);
		}
	};
	private View.OnClickListener manageTools = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.tools_text,R.id.stateTools);
		}
	};
	private View.OnClickListener managePictures = new View.OnClickListener() {
		public void onClick(View v) {
			expandOrCollapse(R.id.pictures_text,R.id.statePictures);
		}
	};
	
    
	/**
	 * Expand or collapse a documentation part.
	 * @param part, the part to expand or collapse
	 * @param icon, the icon to manage
	 */
	
     
	private void expandOrCollapse(int part, int icon) { 
		int tag = Integer.parseInt(((ImageView) findViewById(icon)).getTag().toString());
		if(tag == R.drawable.collapse){
			expand(part, icon);
		}else{
			collapse(part, icon);
		}
	}
	
	/**
	 * Expand a documentation part.
	 * @param part, the part to expand 
	 * @param icon, the icon to manage
	 */
    
	private void expand(int part, int icon) {
		int tag = Integer.parseInt(((ImageView) findViewById(icon)).getTag().toString());
		if(tag != R.drawable.expand){
			expand((LinearLayout) findViewById(part));
			((ImageView) findViewById(icon)).setImageResource(R.drawable.expand);
			((ImageView) findViewById(icon)).setTag(R.drawable.expand);
		}
	}
	
	/**
	 * Collapse a documentation part.
	 * @param part, the part to collapse 
	 * @param icon, the icon to manage
	 */
    
	private void collapse(int part, int icon) {
		int tag = Integer.parseInt(((ImageView) findViewById(icon)).getTag().toString());
		if(tag != R.drawable.collapse){
			collapse((LinearLayout) findViewById(part));
			((ImageView) findViewById(icon)).setImageResource(R.drawable.collapse);
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
    public boolean onOptionsItemSelected(MenuItem item){       
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			case R.id.menu_expandAll:
				expand(R.id.warnings_text, R.id.stateWarning);
				expand(R.id.jobSetUp_text, R.id.stateJobSetUp);
				expand(R.id.procedure_text, R.id.stateProcedure);
				expand(R.id.closeUp_text, R.id.stateCloseUp);
				expand(R.id.tools_text, R.id.stateTools);
				expand(R.id.pictures_text, R.id.statePictures);
				return true;
			case R.id.menu_collapseAll:
				collapse(R.id.warnings_text, R.id.stateWarning);
				collapse(R.id.jobSetUp_text, R.id.stateJobSetUp);
				collapse(R.id.procedure_text, R.id.stateProcedure);
				collapse(R.id.closeUp_text, R.id.stateCloseUp);
				collapse(R.id.tools_text, R.id.stateTools);
				collapse(R.id.pictures_text, R.id.statePictures);
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
		a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
	}
	
	public class StepListAdapter2 extends BaseAdapter{

		private List<Step> mStep;
		private LayoutInflater mInf;
		private Button mButton;
		private Context ct;
		private int position;
		
		public StepListAdapter2(Context c) {
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
				
				arg1 = mInf.inflate(R.layout.step,null);
				h = new StepViewHolder();
				h.mTask = (TextView) arg1.findViewById(R.id.task);
				h.mButton = (Button) arg1.findViewById(R.id.Ok);
				h.mButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(pos == 0){
							LayoutParams lpc = listStep.getLayoutParams();
							lpc.height -= 100;
							listStep.setLayoutParams(lpc);
							lpc = listStepPrevious.getLayoutParams();
							lpc.height +=100;
							listStepPrevious.setLayoutParams(lpc);
							
							Step toDel = steps.remove(pos);
							stepAdapt.notifyDataSetChanged();
							previousSteps.add(toDel.getTask());
							stepAdaptPrevious.notifyDataSetChanged();
							
						}
					}
				});
				arg1.setTag(h);
			}
			else{
				h = (StepViewHolder) arg1.getTag();
			}
			h.setTask(mStep.get(pos).getTask());
			h.setButton(mStep.get(pos).getB());
			
			return arg1;
		}

		public void setListItems(List<Step> l){
			mStep = l;
		}

		private class StepViewHolder{
			private TextView mTask;
			private Button mButton;
			
			public void setTask(String t){
				mTask.setText(t);
			}
			
			public void setButton(Button b){
				mButton = b;			
			}
		}

		
	}
	
	
}
