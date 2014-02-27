package com.eads.co.nomad;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class PlaneSelection extends Activity{

	TextView titre, id,textor,textmsn,textfsn;
	Spinner msn, fsn;
	ArrayAdapter<String> spAdapt;
	ArrayAdapter<String> planeAdapt;
	ListView planes;
	ArrayList<String> listeAvions;
	ArrayList<String> listeFSN; 
	ArrayList<String> listeMSN; 
	Button search;
	
	HashMap<String,ArrayList<String>> listeAvionFSN;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planeselection);
        
        listeAvions = new ArrayList<String>();
        listeAvions.add("A380");
        
        //listeAvionFSN = new HashMap<String, ArrayList<String>>();
        listeFSN = new ArrayList<String>();
        listeFSN.add("");
        listeFSN.add("1");
        listeMSN = new ArrayList<String>();
        listeMSN.add("");
        listeMSN.add("1");
        listeMSN.add("2");
        listeAvionFSN.put("A380", listeFSN);
        titre = (TextView) findViewById(R.id.titlePlane);
        titre.setText("Plane selection");
        id = (TextView) findViewById(R.id.aircraftid);
        id.setText("Aircraft ID");
        msn = (Spinner) findViewById(R.id.spinnerMSN);
        spAdapt = new ArrayAdapter<String>(this, 0);
        msn.setAdapter(spAdapt);
        
        search = (Button) findViewById(R.id.buttonsearch);
        search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//mettre en place un if pour voir si l'id existe
				Intent intent = new Intent(PlaneSelection.this, ATASelection.class);
				startActivity(intent);
				
			}
		});
        
        
        fsn = (Spinner) findViewById(R.id.spinnerFSN);
        //spAdapt = new ArrayAdapter<String>(this, 0);
        //fsn.setAdapter(spAdapt);
        textmsn= (TextView) findViewById(R.id.textmsn);
        textfsn = (TextView) findViewById(R.id.textfsn);
        fsn.setVisibility(View.INVISIBLE);
        msn.setVisibility(View.INVISIBLE);
        textor = (TextView) findViewById(R.id.textor);
        textor.setVisibility(View.INVISIBLE);
        textmsn.setVisibility(View.INVISIBLE);
        textfsn.setVisibility(View.INVISIBLE);
        planes = (ListView) findViewById(R.id.listViewPlanes);
        planeAdapt = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listeAvions);
        planes.setAdapter(planeAdapt);
        planes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Afficher fsn et msn
				spAdapt = new ArrayAdapter<String>(PlaneSelection.this, android.R.layout.simple_list_item_1, listeFSN);

				fsn.setAdapter(spAdapt);
				
				fsn.setVisibility(View.VISIBLE);
				fsn.setSelection(0);
				fsn.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if(arg2 != 0){
							Intent intent = new Intent(PlaneSelection.this, ATASelection.class);
							startActivity(intent);
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
						if(arg2 != 0){
							Intent intent = new Intent(PlaneSelection.this, ATASelection.class);
							startActivity(intent);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				
				spAdapt = new ArrayAdapter<String>(PlaneSelection.this, android.R.layout.simple_list_item_1, listeMSN);
				msn.setAdapter(spAdapt);
				msn.setVisibility(View.VISIBLE);
				textmsn.setVisibility(View.VISIBLE);
				textfsn.setVisibility(View.VISIBLE);
				textor.setVisibility(View.VISIBLE);
				
			}
        	
		});
    }
	
	
}
