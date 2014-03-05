package com.eads.co.nomad;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class PlaneSelection extends Activity {

	TextView titre, id, textor;
	Spinner msn, fsn;
	ArrayAdapter<String> spAdapt;
	ArrayAdapter<String> planeAdapt;
	ListView planes, lastplanes;
	ArrayList<String> listeAvions;
	ArrayList<String> listeFSN;
	ArrayList<String> listeMSN;
	static ArrayList<String> listLastPlanes;
	String avionid, selectedPlane;
	Button search;
	EditText theID;
	HashMap<String, ArrayList<String>> listeAvionFSN;
	HashMap<String, ArrayList<String>> listeAvionMSN;

	int planeSelected = -1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.planeselection);
		listLastPlanes = new ArrayList<String>();
		listLastPlanes.add("A380 FSN:35 MSN:40 ID:F-GFKQ");
		listLastPlanes.add("A320 FSN:353 MSN:226 ID:F-GBOP");
		lastplanes = (ListView) findViewById(R.id.listlastPlanes);
		findViewById(R.id.mainLayout).requestFocus();
		lastplanes.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listLastPlanes));

		lastplanes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(PlaneSelection.this,
						ATASelection.class);
				String[] listinfo = listLastPlanes.get(arg2).split(" ");
				intent.putExtra("Avion", listinfo[0]);
				intent.putExtra("FSN", listinfo[1].replace("FSN:", ""));
				intent.putExtra("MSN", listinfo[2].replace("MSN:", ""));
				intent.putExtra("ID", listinfo[3].replace("ID:", ""));
				startActivity(intent);

			}
		});

		getActionBar().setDisplayHomeAsUpEnabled(true);
		listeAvions = new ArrayList<String>();
		listeAvions.add("A380");
		listeAvions.add("A320");
		theID = (EditText) findViewById(R.id.searchID);
		listeAvionFSN = new HashMap<String, ArrayList<String>>();
		listeAvionMSN = new HashMap<String, ArrayList<String>>();
		listeFSN = new ArrayList<String>();
		listeFSN.add("FSN");
		listeFSN.add("2");
		listeFSN.add("4");
		listeMSN = new ArrayList<String>();
		listeMSN.add("MSN");
		listeMSN.add("40");
		listeMSN.add("69");
		listeAvionFSN.put("A380", listeFSN);
		listeAvionMSN.put("A380", listeMSN);

		listeFSN = new ArrayList<String>();
		listeFSN.add("FSN");
		listeFSN.add("10");

		listeMSN = new ArrayList<String>();
		listeMSN.add("MSN");
		listeMSN.add("226");

		listeAvionFSN.put("A320", listeFSN);
		listeAvionMSN.put("A320", listeMSN);

		id = (TextView) findViewById(R.id.aircraftid);
		id.setText("Aircraft ID");
		msn = (Spinner) findViewById(R.id.spinnerMSN);
		spAdapt = new ArrayAdapter<String>(this, 0);
		msn.setAdapter(spAdapt);

		search = (Button) findViewById(R.id.buttonsearch);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// mettre en place un if pour voir si l'id existe
				String fsnToSend = "40";
				String msnToSend = "69";
				Intent intent = new Intent(PlaneSelection.this,
						ATASelection.class);
				intent.putExtra("Avion", "A380");
				intent.putExtra("FSN", fsnToSend);
				intent.putExtra("MSN", msnToSend);
				intent.putExtra("ID", "F-GFKQ");
				startActivity(intent);

			}
		});

		fsn = (Spinner) findViewById(R.id.spinnerFSN);

		// textmsn= (TextView) findViewById(R.id.textmsn);
		// textfsn = (TextView) findViewById(R.id.textfsn);
		fsn.setVisibility(View.INVISIBLE);
		msn.setVisibility(View.INVISIBLE);
		textor = (TextView) findViewById(R.id.textor);
		textor.setVisibility(View.INVISIBLE);
		// textmsn.setVisibility(View.INVISIBLE);
		// textfsn.setVisibility(View.INVISIBLE);
		planes = (ListView) findViewById(R.id.listViewPlanes);
		planeAdapt = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listeAvions);
		planes.setAdapter(planeAdapt);
		planes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				int c = getResources().getColor(R.color.blue);
				planes.getChildAt(arg2).setBackgroundColor(c);

				if (planeSelected != -1) {
					int color = getResources().getColor(
							R.color.background1_light);
					planes.getChildAt(planeSelected).setBackgroundColor(color);
				}
				planeSelected = arg2;
				selectedPlane = listeAvions.get(arg2);

				// Afficher fsn et msn
				spAdapt = new ArrayAdapter<String>(PlaneSelection.this,
						android.R.layout.simple_list_item_1, listeAvionFSN
								.get(listeAvions.get(arg2)));
				fsn.setAdapter(spAdapt);
				fsn.setVisibility(View.VISIBLE);
				fsn.setSelection(0);
				fsn.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (arg2 != 0) {
							String fsnToSend = (String) fsn.getSelectedItem();
							String msnToSend = listeAvionMSN.get(selectedPlane)
									.get(arg2);
							Intent intent = new Intent(PlaneSelection.this,
									ATASelection.class);
							intent.putExtra("Avion", selectedPlane);
							intent.putExtra("FSN", fsnToSend);
							intent.putExtra("MSN", msnToSend);
							intent.putExtra("ID", "F-GFKQ");
							startActivity(intent);
							listLastPlanes.add(spAdapt.getItem(arg2));
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

				msn.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (arg2 != 0) {
							String msnToSend = (String) msn.getSelectedItem();
							String fsnToSend = listeAvionFSN.get(selectedPlane)
									.get(arg2);
							Intent intent = new Intent(PlaneSelection.this,
									ATASelection.class);
							intent.putExtra("Avion", selectedPlane);
							intent.putExtra("FSN", fsnToSend);
							intent.putExtra("MSN", msnToSend);
							intent.putExtra("ID", "F-GFKQ");
							startActivity(intent);
							startActivity(intent);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

				spAdapt = new ArrayAdapter<String>(PlaneSelection.this,
						android.R.layout.simple_list_item_1, listeAvionMSN
								.get(listeAvions.get(arg2)));
				msn.setAdapter(spAdapt);
				msn.setVisibility(View.VISIBLE);

				textor.setVisibility(View.VISIBLE);

			}

		});

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
