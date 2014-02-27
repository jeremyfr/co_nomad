package com.eads.co.nomad;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
	        	  intent.putExtra("task", "EN30115140080100");
	          }
	          else intent.putExtra("task", "EN52132140080100");
	          startActivity(intent);
	        }
	      });
	}
	
	public void getATAList(){
		listATA = new ArrayList<ATA>();
		
		/*
		 *  Level 3 (sub sub ATA)
		 */
		// ata 5
		ATALevel3 chap1 = new ATALevel3("section 1");
		ATALevel3 chap2 = new ATALevel3("section 2");
		ATALevel3 chap3 = new ATALevel3("section 3");
		ATALevel3 chap4 = new ATALevel3("section 4");
		ATALevel3 chap5 = new ATALevel3("section 5");
		ATALevel3 chap6 = new ATALevel3("section 6");
		ATALevel3 chap7 = new ATALevel3("section 7");
		ATALevel3 chap8 = new ATALevel3("section 8");
		ATALevel3 chap9 = new ATALevel3("section 9");
		ATALevel3 chap10 = new ATALevel3("section 10");
//		ATALevel3 ata5General1 = new ATALevel3("chapter one");
//		ATALevel3 ata5General2 = new ATALevel3("chapter two");
//		ATALevel3 ata5General3 = new ATALevel3("chapter three");
//		ATALevel3 ata5TimeLimits1 = new ATALevel3("chapter 1");
//		ATALevel3 ata5TimeLimits2 = new ATALevel3("chapter 2");
//		ATALevel3 ata5ScheduledMaintenance1 = new ATALevel3("chapter #1");
//		ATALevel3 ata5ScheduledMaintenance2 = new ATALevel3("chapter #2");
//		ATALevel3 ata5UnScheduledMaintenance1 = new ATALevel3("chapter number 1");
//		ATALevel3 ata5UnScheduledMaintenance2 = new ATALevel3("chapter number 2");
//		ATALevel3 ata5UnScheduledMaintenance3 = new ATALevel3("chapter number 3");
//		
//		// ata 6
//		ATALevel3 ata6General1 = new ATALevel3("chapter 1.");
//		ATALevel3 ata6General2 = new ATALevel3("chapter 2.");
//		ATALevel3 ata6General3 = new ATALevel3("chapter 3.");
//		
//		// ata 7
//		ATALevel3 ata7General1 = new ATALevel3("chapter #one");
//		ATALevel3 ata7General2 = new ATALevel3("chapter #two");
//		ATALevel3 ata7General3 = new ATALevel3("chapter #three");
//		ATALevel3 ata7General4 = new ATALevel3("chapter #four");
//		ATALevel3 ata7Jacking1 = new ATALevel3("chapter 1)");
//		ATALevel3 ata7Jacking2 = new ATALevel3("chapter 2)");
//		ATALevel3 ata7Shoring1 = new ATALevel3("chapter #1)");
//		ATALevel3 ata7Shoring2 = new ATALevel3("chapter #2)");
		
		/*
		 *  Level 2 (sub ATA)
		 *  
		 */
		ArrayList<ATALevel3> chapters = new ArrayList<ATALevel3>();
		chapters.add(chap1);
		chapters.add(chap2);
		chapters.add(chap3);
		chapters.add(chap4);
		chapters.add(chap5);
		chapters.add(chap6);
		chapters.add(chap7);
		chapters.add(chap8);
		chapters.add(chap9);
		chapters.add(chap10);
		
		// ata 30
		ArrayList<ATALevel3> listATALevel3 = new ArrayList<ATALevel3>();
		ATALevel2 ata5General = new ATALevel2("- 00 - General", chapters);
		ATALevel2 ata5Airfol = new ATALevel2("- 10 - Airfoil", chapters);
		ATALevel2 ata5AirIntakes = new ATALevel2("- 20 - Air Intakes", chapters);
		ATALevel2 ata5PitotAndStatic = new ATALevel2("- 30 - Pitot and static", chapters);
		ATALevel2 ata5Windows = new ATALevel2("- 40 - Windows, Windshields and Doors", chapters);
		ATALevel2 ata5Antennas = new ATALevel2("- 50 - Antennas", chapters);
		ATALevel2 ata5Propellers = new ATALevel2("- 60 - Propellers/Rotors", chapters);
		ATALevel2 ata5WaterLine = new ATALevel2("- 70 - Water Lines", chapters);
		ATALevel2 ata5Detection = new ATALevel2("- 80 - Detection", chapters);
		
		// ata 6
		ATALevel2 ata36General = new ATALevel2("- 00 - General", chapters);
		ATALevel2 ata36Distribution = new ATALevel2("- 10 - Distribution", chapters);
		ATALevel2 ata36Indicating = new ATALevel2("- 20 - Indicating", chapters);
		
		// ata 52
		ATALevel2 ata52General = new ATALevel2("- 00 - General", chapters);
		ATALevel2 ata52Passenger = new ATALevel2("- 10 - Passenger/Crew", chapters);
		ATALevel2 ata52EmergencyExit = new ATALevel2("- 20 - Emergency Exit", chapters);
		ATALevel2 ata52Cargo = new ATALevel2("- 30 - Cargo", chapters);
		ATALevel2 ata52Service = new ATALevel2("- 40 - Service and Miscellaneous", chapters);
		ATALevel2 ata52FixedInterior = new ATALevel2("- 50 - Fixed Interior", chapters);
		ATALevel2 ata52EntranceStairs = new ATALevel2("- 60 - Entrance Stairs", chapters);
		ATALevel2 ata52Monitoring = new ATALevel2("- 70 - Monitoring and Operation", chapters);
		ATALevel2 ata52LandingGear = new ATALevel2("- 80 - Landing Gear", chapters);
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ataselection, menu);
		return true;
	}
}
