package com.eads.co.nomad;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ATASelection extends Activity {
	private ATASelection instance;
	private ArrayList<ATA> listATA;
	private ListView listeATAView;
	private ListView sousListeATAView;
	private ListView sousSousListeATA;
	private int ataSelected = -1;
	private int lastSubATASelected = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_ataselection);
		
		instance = this;
		getATAList(); // retrieve the list of ATAs and sub ATAs
		
		listeATAView = (ListView)findViewById(R.id.listeATA);

		String[] listeStrings = new String[listATA.size()];
		for (int i = 0; i < listATA.size(); i++){
			listeStrings[i] = listATA.get(i).getDescription();
		}

		listeATAView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listeStrings));
		
		sousListeATAView = (ListView)findViewById(R.id.sousListeATA);
		sousSousListeATA = (ListView)findViewById(R.id.sousSousListeATA);
		
		// Listeners 
		listeATAView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {

	            // selected item
	        	ATA ata = listATA.get(position);
	        	
	        	int c = getResources().getColor(R.color.blue);
	        	listeATAView.getChildAt(position).setBackgroundColor(c);
	            
	        	if (ataSelected != -1){
	        		int color = getResources().getColor(R.color.background1_light);
		        	listeATAView.getChildAt(ataSelected).setBackgroundColor(color);
	        	}
	        	ataSelected = position;
	        	lastSubATASelected = -1;
	            
	            ListView sousListeATA = (ListView)findViewById(R.id.sousListeATA);

	            ArrayList<ATALevel2> listSubATA = ata.getListATALevel2();
	    		String[] sousListeStrings = new String[listSubATA.size()];
	    		for (int i = 0; i < listSubATA.size(); i++){
	    			sousListeStrings[i] = listSubATA.get(i).getDescription();
	    		}

	    		sousListeATA.setAdapter(new ArrayAdapter<String>(instance, android.R.layout.simple_list_item_1,sousListeStrings));
	    		// remove previous sublist
	    		sousListeStrings = new String[0];
	    		sousSousListeATA.setAdapter(new ArrayAdapter<String>(instance, android.R.layout.simple_list_item_1,sousListeStrings));
	        }
	      });
		
		sousListeATAView.setOnItemClickListener(new OnItemClickListener() {
	        @SuppressLint("ResourceAsColor")
			public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {

	            // selected item
	        	ATALevel2 subATA = listATA.get(ataSelected).getListATALevel2().get(position);
	        	int c = getResources().getColor(R.color.blue);
	        	sousListeATAView.getChildAt(position).setBackgroundColor(c);
	            
	        	if (lastSubATASelected != -1){
	        		int color = getResources().getColor(R.color.background1_light);
		        	sousListeATAView.getChildAt(lastSubATASelected).setBackgroundColor(color);
	        	}
	        	lastSubATASelected = position;

	            ArrayList<ATALevel3> listSubSubATA = subATA.getListATALevel3();
	    		String[] sousListeStrings = new String[listSubSubATA.size()];
	    		for (int i = 0; i < listSubSubATA.size(); i++){
	    			sousListeStrings[i] = listSubSubATA.get(i).getDescription();
	    		}

	    		sousSousListeATA.setAdapter(new ArrayAdapter<String>(instance, android.R.layout.simple_list_item_1,sousListeStrings));

	        }
	      });
		
		sousSousListeATA.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
	          Intent intent = new Intent(ATASelection.this, AMMAnnexes.class);
	          ATA ata = listATA.get(ataSelected);
	          ATALevel2 subAta = ata.getListATALevel2().get(lastSubATASelected);
	          if (ata.getDescription() == "30 - ICE AND RAIN PROTECTION"){
	        	  switch(position){
	        	  case 0:
	        		  intent.putExtra("task", "EN30210004080100");
	        		  break;
	        	  case 1:
	        		  intent.putExtra("task", "EN30210004080400");
	        		  break;
	        	  case 2:
	        		  intent.putExtra("task", "EN30210004080600");
	        		  break;
	        	  case 3:
	        		  intent.putExtra("task", "EN30210004080700");
	        		  break;
	        	  case 4:
	        		  intent.putExtra("task", "EN30115140080100");
	        		  break;
	        	  case 5:
	        		  intent.putExtra("task", "EN30115140080200");
	        		  break;
	        	  }
	        	  startActivity(intent);
	        	  
	          }
	          else if (ata.getDescription() == "52 - DOORS"){
	        	  switch(position){
	        	  case 0:
	        		  intent.putExtra("task", "EN52132140080100");
	        		  break;
	        	  case 1:
	        		  intent.putExtra("task", "EN52132182080100");
	        		  break;
	        	  case 2:
	        		  intent.putExtra("task", "EN52142140080100");
	        		  break;
	        	  }
	        	  startActivity(intent);
	          }
	        }
	      });
		// Search field
		EditText editText = (EditText) findViewById(R.id.search);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_SEND) {
		        	Intent intent = new Intent(ATASelection.this, Research.class);
		            startActivity(intent);
		            handled = true;
		        }
		        return handled;
			}
		});
	}
	
	public void getATAList(){
		listATA = new ArrayList<ATA>();
		
		/*
		 *  Level 3 (sub sub ATA)
		 */
		// ata 5
		ATALevel3 chap1 = new ATALevel3("00-801 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Open Position");
		ATALevel3 chap2 = new ATALevel3("00-804 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Open position");
		ATALevel3 chap3 = new ATALevel3("00-806 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Closed Position");
		ATALevel3 chap4 = new ATALevel3("00-807 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Closed position");
		ATALevel3 chap5 = new ATALevel3("51-801 - Installation of the Wing Anti-ice Control-valve");
		ATALevel3 chap6 = new ATALevel3("51-802 - Installation of the Wing Anti-ice Control-valve-filter");
		
		ATALevel3 chap7 = new ATALevel3("section 1");
		ATALevel3 chap8 = new ATALevel3("section 2");
		ATALevel3 chap9 = new ATALevel3("section 3");
		ATALevel3 chap10 = new ATALevel3("section 4");
		
		ATALevel3 chap11 = new ATALevel3("21-801 - Installation of the Door Locking Mechanism Door M4L/M4R");
		ATALevel3 chap12 = new ATALevel3("21-801 - Adjustment of the Door Locking Mechanism Door M4L/M4R");
		ATALevel3 chap13 = new ATALevel3("21-801 - Installation of the Door-Locking Mechanism Door M5L/M5R");
		
		/*
		 *  Level 2 (sub ATA)
		 *  
		 */
		ArrayList<ATALevel3> chaptersATA30 = new ArrayList<ATALevel3>();
		chaptersATA30.add(chap1);
		chaptersATA30.add(chap2);
		chaptersATA30.add(chap3);
		chaptersATA30.add(chap4);
		chaptersATA30.add(chap5);
		chaptersATA30.add(chap6);
		
		ArrayList<ATALevel3> chaptersATA36 = new ArrayList<ATALevel3>();
		chaptersATA36.add(chap7);
		chaptersATA36.add(chap8);
		chaptersATA36.add(chap9);
		chaptersATA36.add(chap10);
		
		ArrayList<ATALevel3> chaptersATA52 = new ArrayList<ATALevel3>();
		chaptersATA52.add(chap11);
		chaptersATA52.add(chap12);
		chaptersATA52.add(chap13);
		
		// ata 30
		ArrayList<ATALevel3> listATALevel3 = new ArrayList<ATALevel3>();
		ATALevel2 ata5General = new ATALevel2("- 00 - General", chaptersATA30);
		ATALevel2 ata5Airfol = new ATALevel2("- 10 - Airfoil", chaptersATA30);
		ATALevel2 ata5AirIntakes = new ATALevel2("- 20 - Air Intakes", chaptersATA30);
		ATALevel2 ata5PitotAndStatic = new ATALevel2("- 30 - Pitot and static", chaptersATA30);
		ATALevel2 ata5Windows = new ATALevel2("- 40 - Windows, Windshields and Doors", chaptersATA30);
		ATALevel2 ata5Antennas = new ATALevel2("- 50 - Antennas", chaptersATA30);
		ATALevel2 ata5Propellers = new ATALevel2("- 60 - Propellers/Rotors", chaptersATA30);
		ATALevel2 ata5WaterLine = new ATALevel2("- 70 - Water Lines", chaptersATA30);
		ATALevel2 ata5Detection = new ATALevel2("- 80 - Detection", chaptersATA30);
		
		// ata 6
		ATALevel2 ata36General = new ATALevel2("- 00 - General", chaptersATA36);
		ATALevel2 ata36Distribution = new ATALevel2("- 10 - Distribution", chaptersATA36);
		ATALevel2 ata36Indicating = new ATALevel2("- 20 - Indicating", chaptersATA36);
		
		// ata 52
		ATALevel2 ata52General = new ATALevel2("- 00 - General", chaptersATA52);
		ATALevel2 ata52Passenger = new ATALevel2("- 10 - Passenger/Crew", chaptersATA52);
		ATALevel2 ata52EmergencyExit = new ATALevel2("- 20 - Emergency Exit", chaptersATA52);
		ATALevel2 ata52Cargo = new ATALevel2("- 30 - Cargo", chaptersATA52);
		ATALevel2 ata52Service = new ATALevel2("- 40 - Service and Miscellaneous", chaptersATA52);
		ATALevel2 ata52FixedInterior = new ATALevel2("- 50 - Fixed Interior", chaptersATA52);
		ATALevel2 ata52EntranceStairs = new ATALevel2("- 60 - Entrance Stairs", chaptersATA52);
		ATALevel2 ata52Monitoring = new ATALevel2("- 70 - Monitoring and Operation", chaptersATA52);
		ATALevel2 ata52LandingGear = new ATALevel2("- 80 - Landing Gear", chaptersATA52);
		
		/*
		 *  Level 1 (ATA)
		 */
		// ata 30
		ArrayList<ATALevel2> listATALevel2 = new ArrayList<ATALevel2>();
		listATALevel2.add(ata5General);
		listATALevel2.add(ata5Airfol);
		listATALevel2.add(ata5AirIntakes);
		listATALevel2.add(ata5PitotAndStatic);
		listATALevel2.add(ata5Windows);
		listATALevel2.add(ata5Antennas);
		listATALevel2.add(ata5Propellers);
		listATALevel2.add(ata5WaterLine);
		listATALevel2.add(ata5Detection);
		ATA ata30 = new ATA("30 - ICE AND RAIN PROTECTION", listATALevel2);
		
		// ata 6
		listATALevel2 = new ArrayList<ATALevel2>();
		listATALevel2.add(ata36General);
		listATALevel2.add(ata36Distribution);
		listATALevel2.add(ata36Indicating);
		ATA ata36 = new ATA("36 - PNEUMATIC", listATALevel2);
		
		// ata 7
		listATALevel2 = new ArrayList<ATALevel2>();
		listATALevel2.add(ata52General);
		listATALevel2.add(ata52Passenger);
		listATALevel2.add(ata52EmergencyExit);
		listATALevel2.add(ata52Cargo);
		listATALevel2.add(ata52Service);
		listATALevel2.add(ata52FixedInterior);
		listATALevel2.add(ata52EntranceStairs);
		listATALevel2.add(ata52Monitoring);
		listATALevel2.add(ata52LandingGear);
		ATA ata52 = new ATA("52 - DOORS", listATALevel2);
		
		
		listATA.add(ata30);
		listATA.add(ata36);
		listATA.add(ata52);
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
