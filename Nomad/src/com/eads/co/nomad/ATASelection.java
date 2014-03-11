package com.eads.co.nomad;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class ATASelection extends Activity {
	private ATASelection instance;
	private ArrayList<ATA> listATA;
	private ListView listeATAView;
	private ListView sousListeATAView;
	private ListView sousSousListeATA;
	private int ataSelected = -1;
	private int lastSubATASelected = -1;
	private String title;
	private String msn, fsn, aid, plane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle infos = this.getIntent().getExtras();
		fsn = infos.getString("FSN");
		msn = infos.getString("MSN");
		aid = infos.getString("ID");
		plane = infos.getString("Avion");
		setContentView(R.layout.activity_ataselection);
		title = "ATA Selection   /   Plane:" + plane + " MSN:" + msn + " FSN:"
				+ fsn + " ID:" + aid;
		//setTitle(title);
		/*if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			String query = getIntent().getStringExtra(SearchManager.QUERY);
			Intent mapIntent = new Intent(this, Research.class);
			mapIntent.putExtra("query", query);
//			mapIntent.putExtra("titre", title);
//			mapIntent.putExtra("Avion", plane);
//			mapIntent.putExtra("FSN", fsn);
//			mapIntent.putExtra("MSN", msn);
//			mapIntent.putExtra("ID", id);
			startActivity(mapIntent);
		}*/

		instance = this;
		getATAList(); // retrieve the list of ATAs and sub ATAs

		listeATAView = (ListView) findViewById(R.id.listeATA);

		String[] listeStrings = new String[listATA.size()];
		for (int i = 0; i < listATA.size(); i++) {
			listeStrings[i] = listATA.get(i).getDescription();
		}

		listeATAView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.simple_list_item_ata, listeStrings));

		sousListeATAView = (ListView) findViewById(R.id.sousListeATA);
		sousSousListeATA = (ListView) findViewById(R.id.sousSousListeATA);

		// Listeners
		listeATAView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// selected item
				ATA ata = listATA.get(position);

				int c = getResources().getColor(R.color.blue);

				if (ataSelected != -1) {
					int color = getResources().getColor(
							R.color.background1_light);
					listeATAView.getChildAt(ataSelected).setBackgroundColor(
							color);
				}
				listeATAView.getChildAt(position).setBackgroundColor(c);
				ataSelected = position;
				lastSubATASelected = -1;

				ListView sousListeATA = (ListView) findViewById(R.id.sousListeATA);

				ArrayList<ATALevel2> listSubATA = ata.getListATALevel2();
				String[] sousListeStrings = new String[listSubATA.size()];
				for (int i = 0; i < listSubATA.size(); i++) {
					sousListeStrings[i] = listSubATA.get(i).getDescription();
				}

				sousListeATA.setAdapter(new ArrayAdapter<String>(instance,
						R.layout.simple_list_item_ata, sousListeStrings));
				// remove previous sublist
				sousListeStrings = new String[0];
				sousSousListeATA.setAdapter(new ArrayAdapter<String>(instance,
						R.layout.simple_list_item_ata, sousListeStrings));
			}
		});

		sousListeATAView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("ResourceAsColor")
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// selected item
				ATALevel2 subATA = listATA.get(ataSelected).getListATALevel2()
						.get(position);
				int c = getResources().getColor(R.color.blue);

				if (lastSubATASelected != -1) {
					int color = getResources().getColor(
							R.color.background1_light);
					sousListeATAView.getChildAt(lastSubATASelected)
							.setBackgroundColor(color);
				}
				sousListeATAView.getChildAt(position).setBackgroundColor(c);
				lastSubATASelected = position;

				ArrayList<ATALevel3> listSubSubATA = subATA.getListATALevel3();
				String[] sousListeStrings = new String[listSubSubATA.size()];
				for (int i = 0; i < listSubSubATA.size(); i++) {
					sousListeStrings[i] = listSubSubATA.get(i).getDescription();
				}

				sousSousListeATA.setAdapter(new ArrayAdapter<String>(instance,
						R.layout.simple_list_item_ata, sousListeStrings));

			}
		});

		sousSousListeATA.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ATASelection.this, AMMAnnexes.class);
				intent.putExtra("MSN", msn);
				intent.putExtra("FSN", fsn);
				intent.putExtra("ID", aid);
				intent.putExtra("Avion",plane);
				ATA ata = listATA.get(ataSelected);
				ATALevel2 subAta = ata.getListATALevel2().get(
						lastSubATASelected);
				if (ata.getDescription() == "30 - ICE AND RAIN PROTECTION") {
					if (subAta.getDescription() == "- 10 - Airfoil") {
						switch (position) {
						case 0:
							intent.putExtra("task", "EN30115140080100");
							break;
						case 1:
							intent.putExtra("task", "EN30115140080200");
							break;
						}
						intent.putExtra("titre", title);
						startActivity(intent);
					} else if (subAta.getDescription() == "- 20 - Air Intakes") {
						switch (position) {
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
						}
						intent.putExtra("titre", title);
						startActivity(intent);
					}
				} else if (ata.getDescription() == "52 - DOORS") {
					if (subAta.getDescription() == "- 10 - Passenger/Crew") {
						switch (position) {
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
						intent.putExtra("titre", title);
						startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menusearch, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;
	}

	public void getATAList() {
		listATA = new ArrayList<ATA>();

		/*
		 * Level 3 (sub sub ATA)
		 */
		// ata 5
		ATALevel3 chap1 = new ATALevel3(
				"00-801 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Open Position");
		ATALevel3 chap2 = new ATALevel3(
				"00-804 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Open position");
		ATALevel3 chap3 = new ATALevel3(
				"00-806 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Closed Position");
		ATALevel3 chap4 = new ATALevel3(
				"00-807 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Closed position");
		ATALevel3 chap5 = new ATALevel3(
				"51-801 - Installation of the Wing Anti-ice Control-valve");
		ATALevel3 chap6 = new ATALevel3(
				"51-802 - Installation of the Wing Anti-ice Control-valve-filter");

		ATALevel3 chap11 = new ATALevel3(
				"21-801 - Installation of the Door Locking Mechanism Door M4L/M4R");
		ATALevel3 chap12 = new ATALevel3(
				"21-801 - Adjustment of the Door Locking Mechanism Door M4L/M4R");
		ATALevel3 chap13 = new ATALevel3(
				"21-801 - Installation of the Door-Locking Mechanism Door M5L/M5R");

		/*
		 * Level 2 (sub ATA)
		 */
		ArrayList<ATALevel3> chaptersATA30AirIntakes = new ArrayList<ATALevel3>();
		chaptersATA30AirIntakes.add(chap1);
		chaptersATA30AirIntakes.add(chap2);
		chaptersATA30AirIntakes.add(chap3);
		chaptersATA30AirIntakes.add(chap4);

		ArrayList<ATALevel3> chaptersATA30Airfoil = new ArrayList<ATALevel3>();
		chaptersATA30Airfoil.add(chap5);
		chaptersATA30Airfoil.add(chap6);

		ArrayList<ATALevel3> chaptersATA36 = new ArrayList<ATALevel3>();

		ArrayList<ATALevel3> chaptersATA52 = new ArrayList<ATALevel3>();
		chaptersATA52.add(chap11);
		chaptersATA52.add(chap12);
		chaptersATA52.add(chap13);

		ArrayList<ATALevel3> empty = new ArrayList<ATALevel3>();

		// ata 30
		ATALevel2 ata5General = new ATALevel2("- 00 - General", empty);
		ATALevel2 ata5Airfol = new ATALevel2("- 10 - Airfoil",
				chaptersATA30Airfoil);
		ATALevel2 ata5AirIntakes = new ATALevel2("- 20 - Air Intakes",
				chaptersATA30AirIntakes);
		ATALevel2 ata5PitotAndStatic = new ATALevel2("- 30 - Pitot and static",
				empty);
		ATALevel2 ata5Windows = new ATALevel2(
				"- 40 - Windows, Windshields and Doors", empty);
		ATALevel2 ata5Antennas = new ATALevel2("- 50 - Antennas", empty);
		ATALevel2 ata5Propellers = new ATALevel2("- 60 - Propellers/Rotors",
				empty);
		ATALevel2 ata5WaterLine = new ATALevel2("- 70 - Water Lines", empty);
		ATALevel2 ata5Detection = new ATALevel2("- 80 - Detection", empty);

		// ata 6
		ATALevel2 ata36General = new ATALevel2("- 00 - General", chaptersATA36);
		ATALevel2 ata36Distribution = new ATALevel2("- 10 - Distribution",
				chaptersATA36);
		ATALevel2 ata36Indicating = new ATALevel2("- 20 - Indicating",
				chaptersATA36);

		// ata 52
		ATALevel2 ata52General = new ATALevel2("- 00 - General", empty);
		ATALevel2 ata52Passenger = new ATALevel2("- 10 - Passenger/Crew",
				chaptersATA52);
		ATALevel2 ata52EmergencyExit = new ATALevel2("- 20 - Emergency Exit",
				empty);
		ATALevel2 ata52Cargo = new ATALevel2("- 30 - Cargo", empty);
		ATALevel2 ata52Service = new ATALevel2(
				"- 40 - Service and Miscellaneous", empty);
		ATALevel2 ata52FixedInterior = new ATALevel2("- 50 - Fixed Interior",
				empty);
		ATALevel2 ata52EntranceStairs = new ATALevel2("- 60 - Entrance Stairs",
				empty);
		ATALevel2 ata52Monitoring = new ATALevel2(
				"- 70 - Monitoring and Operation", empty);
		ATALevel2 ata52LandingGear = new ATALevel2("- 80 - Landing Gear", empty);

		/*
		 * Level 1 (ATA)
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MenuApp.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
