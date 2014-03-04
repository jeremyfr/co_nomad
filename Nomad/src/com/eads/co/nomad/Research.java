package com.eads.co.nomad;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Research extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.research);
		
		handleIntent(getIntent());
	}
	 @Override
	    protected void onNewIntent(Intent intent) {
		 	super.onNewIntent(intent);      
		    setIntent(intent);
	        handleIntent(intent);
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
				System.out.println("item " + item.getItemId());
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void handleIntent(Intent intent) {
	  String query = intent.getStringExtra("query");
      ListView resultsView = (ListView)findViewById(R.id.resultsSearch);
      
      if (query.contains("general")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 00 - General",
    	  		"36 - PNEUMATIC > - 00 - General", "52 - DOORS > - 00 - General"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
      }
      else if (query.contains("airfoil")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 10 - Airfoil",
    			  "30 - ICE AND RAIN PROTECTION > - 10 - Airfoil > 51-801 - Installation of the Wing Anti-ice Control-valve",
    			  "30 - ICE AND RAIN PROTECTION > - 10 - Airfoil > 51-802 - Installation of the Wing Anti-ice Control-valve filter"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
    	  resultsView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
	        	Intent intent = new Intent(Research.this, AMMAnnexes.class);
	        	switch(position){
	        	  case 1:
	        		  intent.putExtra("task", "EN30115140080100");
	        		  startActivity(intent); 
	        		  break;
				  case 2:
	        		  intent.putExtra("task", "EN30115140080200");
	        		  startActivity(intent); 
	        		  break;
	        	  }
	        }
    	  });
      }
      else if (query.contains("air intakes")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 20 - Air intakes",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-801 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Open Position",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-804 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Open position",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-806 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Closed Position",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-807 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Closed position"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
    	  resultsView.setOnItemClickListener(new OnItemClickListener() {
  	        public void onItemClick(AdapterView<?> parent, View view,
  	            int position, long id) {
  	        	Intent intent = new Intent(Research.this, AMMAnnexes.class);
  	        	switch(position){
  	        	  case 1:
  	        		  intent.putExtra("task", "EN30210004080100");
  	        		  startActivity(intent); 
  	        		  break;
  				  case 2:
  	        		  intent.putExtra("task", "EN30210004080400");
  	        		  startActivity(intent); 
  	        		  break;
  				  case 3:
	        		  intent.putExtra("task", "EN30210004080600");
	        		  startActivity(intent); 
	        		  break;
  				  case 4:
	        		  intent.putExtra("task", "EN30210004080700");
	        		  startActivity(intent); 
	        		  break;
  	        	  }
  	        }
      	  });
      }
      else if (query.contains("pitot") || query.contains("static")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 30 - Pitot and static"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
      }
      else if (query.contains("windows") || query.contains("windshields")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 40 - Windows, Windshields and Doors"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
      }
      else if (query.contains("antennas")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 50 - Antennas"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
      }
      else if (query.contains("propellers") || query.contains("rotors")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 60 - Propellers/Rotors"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
      }
      else if (query.contains("water lines")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 70 - Water lines"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
      }
      else if (query.contains("detection")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 80 - Detection"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
      }
      else if (query.contains("passenger") || query.contains("crew")){
    	  String[] results = {"52 - DOORS > - 10 - Passenger/Crew",
    			  "52 - DOORS > - 10 - Passenger/Crew > 21-801 - Installation of the Door Locking Mechanism Door M4L/M4R",
    			  "52 - DOORS > - 10 - Passenger/Crew > 21-801 - Adjustment of the Door Locking Mechanism Door M4L/M4R",
    			  "52 - DOORS > - 10 - Passenger/Crew > 21-801 - Installation of the Door-Locking Mechanism Door M5L/M5R"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
    	  resultsView.setOnItemClickListener(new OnItemClickListener() {
    	        public void onItemClick(AdapterView<?> parent, View view,
    	            int position, long id) {
    	        	Intent intent = new Intent(Research.this, AMMAnnexes.class);
    	        	switch(position){
    	        	  case 1:
    	        		  intent.putExtra("task", "EN52132140080100");
    	        		  startActivity(intent); 
    	        		  break;
    				  case 2:
    	        		  intent.putExtra("task", "EN52132182080100");
    	        		  startActivity(intent); 
    	        		  break;
    				  case 3:
	  	        		  intent.putExtra("task", "EN52142140080100");
	  	        		  startActivity(intent); 
  	        		  break;
    	        	  }
    	        }
        	  });
      }
      else if (query.contains("installation") || query.contains("wing anti-ice") || query.contains("valve")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 10 - Airfoil > 51-801 - Installation of the Wing Anti-ice Control-valve",
    			  "30 - ICE AND RAIN PROTECTION > - 10 - Airfoil > 51-802 - Installation of the Wing Anti-ice Control-valve filter"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
    	  resultsView.setOnItemClickListener(new OnItemClickListener() {
    	        public void onItemClick(AdapterView<?> parent, View view,
    	            int position, long id) {
    	        	Intent intent = new Intent(Research.this, AMMAnnexes.class);
    	        	switch(position){
    	        	case 0:
  	        		  intent.putExtra("task", "EN30115140080100");
  	        		  startActivity(intent); 
  	        		  break;
  				  	case 1:
  	        		  intent.putExtra("task", "EN30115140080200");
  	        		  startActivity(intent); 
  	        		  break;
    	        	}
    	        }
        	  });
      }
      else if (query.contains("deactivation") || query.contains("air intake") || query.contains("engine air intake")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-801 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Open Position",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-804 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Open position",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-806 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Closed Position",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-807 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Closed position"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
    	  resultsView.setOnItemClickListener(new OnItemClickListener() {
  	        public void onItemClick(AdapterView<?> parent, View view,
  	            int position, long id) {
  	        	Intent intent = new Intent(Research.this, AMMAnnexes.class);
  	        	switch(position){
  	        	  case 0:
  	        		  intent.putExtra("task", "EN30210004080100");
  	        		  startActivity(intent); 
  	        		  break;
  				  case 1:
  	        		  intent.putExtra("task", "EN30210004080400");
  	        		  startActivity(intent); 
  	        		  break;
  				  case 2:
	        		  intent.putExtra("task", "EN30210004080600");
	        		  startActivity(intent); 
	        		  break;
  				  case 3:
	        		  intent.putExtra("task", "EN30210004080700");
	        		  startActivity(intent); 
	        		  break;
  	        	  }
  	        }
      	  });
      }
      else if (query.contains("door locking") || query.contains("mechanism door") || query.contains("M4L") || query.contains("M5L")){
    	  String[] results = {"52 - DOORS > - 10 - Passenger/Crew > 21-801 - Installation of the Door Locking Mechanism Door M4L/M4R",
    			  "52 - DOORS > - 10 - Passenger/Crew > 21-801 - Adjustment of the Door Locking Mechanism Door M4L/M4R",
    			  "52 - DOORS > - 10 - Passenger/Crew > 21-801 - Installation of the Door-Locking Mechanism Door M5L/M5R"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
    	  resultsView.setOnItemClickListener(new OnItemClickListener() {
    	        public void onItemClick(AdapterView<?> parent, View view,
    	            int position, long id) {
    	        	Intent intent = new Intent(Research.this, AMMAnnexes.class);
    	        	switch(position){
    	        	  case 0:
    	        		  intent.putExtra("task", "EN52132140080100");
    	        		  startActivity(intent); 
    	        		  break;
    				  case 1:
    	        		  intent.putExtra("task", "EN52132182080100");
    	        		  startActivity(intent); 
    	        		  break;
    				  case 2:
	  	        		  intent.putExtra("task", "EN52142140080100");
	  	        		  startActivity(intent); 
  	        		  break;
    	        	  }
    	        }
        	  });
      }
      else if (query.contains("ice") || query.contains("rain")){
    	  String[] results = {"30 - ICE AND RAIN PROTECTION > - 10 - Airfoil",
    			  "30 - ICE AND RAIN PROTECTION > - 10 - Airfoil > 51-801 - Installation of the Wing Anti-ice Control-valve",
    			  "30 - ICE AND RAIN PROTECTION > - 10 - Airfoil > 51-802 - Installation of the Wing Anti-ice Control-valve filter",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-801 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Open Position",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-804 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Open position",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-806 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Closed Position",
    			  "30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-807 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Closed position"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
    	  resultsView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
	        	Intent intent = new Intent(Research.this, AMMAnnexes.class);
	        	switch(position){
	        	  case 1:
	        		  intent.putExtra("task", "EN30115140080100");
	        		  startActivity(intent); 
	        		  break;
				  case 2:
	        		  intent.putExtra("task", "EN30115140080200");
	        		  startActivity(intent); 
	        		  break;
				  case 4:
  	        		  intent.putExtra("task", "EN30210004080100");
  	        		  startActivity(intent); 
  	        		  break;
  				  case 5:
  	        		  intent.putExtra("task", "EN30210004080400");
  	        		  startActivity(intent); 
  	        		  break;
  				  case 6:
	        		  intent.putExtra("task", "EN30210004080600");
	        		  startActivity(intent); 
	        		  break;
  				  case 7:
	        		  intent.putExtra("task", "EN30210004080700");
	        		  startActivity(intent); 
	        		  break;
	        	  }
	        }
    	  });
      }
      else if (query.contains("doors")){
    	  String[] results = {"52 - DOORS > - 10 - Passenger/Crew",
    			  "52 - DOORS > - 10 - Passenger/Crew > 21-801 - Installation of the Door Locking Mechanism Door M4L/M4R",
    			  "52 - DOORS > - 10 - Passenger/Crew > 21-801 - Adjustment of the Door Locking Mechanism Door M4L/M4R",
    			  "52 - DOORS > - 10 - Passenger/Crew > 21-801 - Installation of the Door-Locking Mechanism Door M5L/M5R"};
    	  resultsView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
    	  resultsView.setOnItemClickListener(new OnItemClickListener() {
    	        public void onItemClick(AdapterView<?> parent, View view,
    	            int position, long id) {
    	        	Intent intent = new Intent(Research.this, AMMAnnexes.class);
    	        	switch(position){
    	        	  case 1:
    	        		  intent.putExtra("task", "EN52132140080100");
    	        		  startActivity(intent); 
    	        		  break;
    				  case 2:
    	        		  intent.putExtra("task", "EN52132182080100");
    	        		  startActivity(intent); 
    	        		  break;
    				  case 3:
	  	        		  intent.putExtra("task", "EN52142140080100");
	  	        		  startActivity(intent); 
  	        		  break;
    	        	  }
    	        }
        	  });
      }
    }
}
