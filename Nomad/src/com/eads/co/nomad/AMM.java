package com.eads.co.nomad;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class AMM extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amm);
		/* Warnings part */
		LinearLayout warnings = (LinearLayout) findViewById(R.id.warnings);
		warnings.setOnClickListener(manageWarnings);
		/* Job Setup part */
		LinearLayout jobSetUp = (LinearLayout) findViewById(R.id.jobSetUp);
		jobSetUp.setOnClickListener(manageJobSetUp);
		collapse((LinearLayout) findViewById(R.id.jobSetUp_text));
		/* Procedure part */
		LinearLayout procedure = (LinearLayout) findViewById(R.id.procedure);
		procedure.setOnClickListener(manageProcedure);
		collapse((LinearLayout) findViewById(R.id.procedure_text));
		/* Close Up part */
		LinearLayout closeUp = (LinearLayout) findViewById(R.id.closeUp);
		closeUp.setOnClickListener(manageCloseUp);
		collapse((LinearLayout) findViewById(R.id.closeUp_text));
		/* Tools part */
		LinearLayout tools = (LinearLayout) findViewById(R.id.tools);
		tools.setOnClickListener(manageTools);
		collapse((LinearLayout) findViewById(R.id.tools_text));
		/* Pictures part */
		LinearLayout pictures = (LinearLayout) findViewById(R.id.pictures);
		pictures.setOnClickListener(managePictures);
		collapse((LinearLayout) findViewById(R.id.pictures_text));
	}
	
	View.OnClickListener manageWarnings = new View.OnClickListener() {
		private boolean collapse = false;
		public void onClick(View v) {
			if(collapse){
				expand((LinearLayout) findViewById(R.id.warnings_text));
				collapse = false;
			}else{
				collapse((LinearLayout) findViewById(R.id.warnings_text));
				collapse = true;
			}
		}
	};
	View.OnClickListener manageJobSetUp = new View.OnClickListener() {
		private boolean collapse = true;
		public void onClick(View v) {
			if(collapse){
				expand((LinearLayout) findViewById(R.id.jobSetUp_text));
				collapse = false;
			}else{
				collapse((LinearLayout) findViewById(R.id.jobSetUp_text));
				collapse = true;
			}
		}
	};
	View.OnClickListener manageProcedure = new View.OnClickListener() {
		private boolean collapse = true;
		public void onClick(View v) {
			if(collapse){
				expand((LinearLayout) findViewById(R.id.procedure_text));
				collapse = false;
			}else{
				collapse((LinearLayout) findViewById(R.id.procedure_text));
				collapse = true;
			}
		}
	};
	View.OnClickListener manageCloseUp = new View.OnClickListener() {
		private boolean collapse = true;
		public void onClick(View v) {
			if(collapse){
				expand((LinearLayout) findViewById(R.id.closeUp_text));
				collapse = false;
			}else{
				collapse((LinearLayout) findViewById(R.id.closeUp_text));
				collapse = true;
			}
		}
	};
	View.OnClickListener manageTools = new View.OnClickListener() {
		private boolean collapse = true;
		public void onClick(View v) {
			if(collapse){
				expand((LinearLayout) findViewById(R.id.tools_text));
				collapse = false;
			}else{
				collapse((LinearLayout) findViewById(R.id.tools_text));
				collapse = true;
			}
		}
	};
	View.OnClickListener managePictures = new View.OnClickListener() {
		private boolean collapse = true;
		public void onClick(View v) {
			if(collapse){
				expand((LinearLayout) findViewById(R.id.pictures_text));
				collapse = false;
			}else{
				collapse((LinearLayout) findViewById(R.id.pictures_text));
				collapse = true;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
}
