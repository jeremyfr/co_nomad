package com.eads.co.nomad;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class MainActivityJobCards extends FragmentActivity {
	private ArrayList<JobCardData> jobCards;
	private ArrayList<JobCardData> jobCardsTab2;
	private ArrayList<JobCardData> jobCardsTab3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_main_jobcard);
		
		jobCardsDisplay();
	}

	private void jobCardsDisplay() {
		jobCards = new ArrayList<JobCardData>();
		
		JobCardData j = new JobCardData("Installation of the Wing Anti-ice Control-valve");
		ArrayList<JobCardDetails> jobCardDetails = new ArrayList<JobCardDetails>();
		JobCardDetails jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCards.add(j);
		
		j = new JobCardData("Installation of the Wing Anti-ice Control-valve-filter");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCards.add(j);
		
		j = new JobCardData("Deactivation of the Engine Air Intake Anti-Ice System in the Locked Open Position");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCards.add(j);
		
		j = new JobCardData("Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Open position");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCards.add(j);
		
		j = new JobCardData("Deactivation of the Engine Air Intake Anti-Ice System in the Locked Closed Position");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCards.add(j);
		
		j = new JobCardData("Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Closed position");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCards.add(j);
		
		ExpandableListView listeJobCards1 = (ExpandableListView)findViewById(R.id.listeJobCards1);
		
		JobCardAdapter adapter = new JobCardAdapter(this, jobCards);
		
		listeJobCards1.setAdapter((ExpandableListAdapter) adapter);
		
		
		
		
		jobCardsTab2 = new ArrayList<JobCardData>();
		
		j = new JobCardData("Job Card #1");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCardsTab2.add(j);
		
		j = new JobCardData("Job Card #2");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCardsTab2.add(j);
		
		j = new JobCardData("Job Card #3");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCardsTab2.add(j);
		
		ExpandableListView listeJobCards2 = (ExpandableListView)findViewById(R.id.listeJobCards2);

		JobCardAdapter adapter2 = new JobCardAdapter(this, jobCardsTab2);
		
		listeJobCards2.setAdapter((ExpandableListAdapter) adapter2);
		
		
		
		jobCardsTab3 = new ArrayList<JobCardData>();
		
		j = new JobCardData("Installation of the Door Locking Mechanism Door M4L/M4R");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCardsTab3.add(j);
		
		j = new JobCardData("Adjustment of the Door Locking Mechanism Door M4L/M4R");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCardsTab3.add(j);
		
		j = new JobCardData("Installation of the Door-Locking Mechanism Door M5L/M5R");
		jobCardDetails = new ArrayList<JobCardDetails>();
		jDetails = new JobCardDetails(j, "Date : 06/12/2014\nRef. : EN30115140080100",
				"Documentation : AMM, TSM, IPC\nRequired execution time : 50 min\nRequired operators : 2");
		jobCardDetails.add(jDetails);
		j.setJobCardDetails(jobCardDetails);
		jobCardsTab3.add(j);
		
		ExpandableListView listeJobCards3 = (ExpandableListView)findViewById(R.id.listeJobCards3);

		JobCardAdapter adapter3 = new JobCardAdapter(this, jobCardsTab3);
		
		listeJobCards3.setAdapter((ExpandableListAdapter) adapter3);
	}
	
	public void openJobCard(View view){
        //Intent intent = new Intent(this, JobCard.class);
		Intent intent = new Intent(this, JobCard.class);
        intent.putExtra("task", "EN30115140080100");
        startActivity(intent);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item){       
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
