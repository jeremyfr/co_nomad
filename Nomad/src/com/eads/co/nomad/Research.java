package com.eads.co.nomad;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Research extends Activity {
	public final String p1 = "installation of the wing anti-ice control-valve";
	public final String p2 = "installation of the wing anti-ice control-valve-filter";
	public final String p3 = "deactivation of the engine air intake anti-ice system in the locked open position";
	public final String p4 = "deactivation of the anti-ice pressure-regulating valve of the engine air intake in the locked-open position";
	public final String p5 = "deactivation of the engine air intake anti-ice system in the locked closed position";
	public final String p6 = "deactivation of the anti-ice pressure-regulating valve of the engine air intake in the locked-closed position";
	public final String p7 = "installation of the door locking mechanism door m4l/m4r";
	public final String p8 = "adjustment of the door locking mechanism door m4l/m4r";
	public final String p9 = "installation of the door-locking mechanism door m5l/m5r";
	
	public int pos1 = -1;
	public int pos2 = -1;
	public int pos3 = -1;
	public int pos4 = -1;
	public int pos5 = -1;
	public int pos6 = -1;
	public int pos7 = -1;
	public int pos8 = -1;
	public int pos9 = -1;
	
	public int pos = 0;
	
	public String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.research);
		
//		String msn, fsn, id, plane;
//		Bundle infos = this.getIntent().getExtras();
//		fsn = infos.getString("FSN");
//		msn = infos.getString("MSN");
//		id = infos.getString("ID");
//		plane = infos.getString("Avion");
//		setContentView(R.layout.activity_ataselection);
//		title = "ATA Selection   /   Plane:" + plane + " MSN:" + msn + " FSN:"
//				+ fsn + " ID:" + id;
//		setTitle(title);

		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
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
			System.out.println("item " + item.getItemId());
			return super.onOptionsItemSelected(item);
		}
	}

	private void handleIntent(Intent intent) {
		String query = intent.getStringExtra("query").toLowerCase();
		ListView resultsView = (ListView) findViewById(R.id.resultsSearch);
		
		ArrayList<String> results = new ArrayList<String>();
		
		if (p1.contains(query)){
			pos1 = pos;
			results.add("30 - ICE AND RAIN PROTECTION > - 10 - Airfoil > 51-801 - Installation of the Wing Anti-ice Control-valve");
			pos++;
		}
		if (p2.contains(query)){
			pos2 = pos;
			results.add("30 - ICE AND RAIN PROTECTION > - 10 - Airfoil > 51-801 - Installation of the Wing Anti-ice Control-valve filter");
			pos++;
		}
		if (p3.contains(query)){
			pos3 = pos;
			results.add("30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-801 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Open Position");
			pos++;
		}
		if (p4.contains(query)){
			pos4 = pos;
			results.add("30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-804 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Open position");
			pos++;
		}
		if (p5.contains(query)){
			pos5 = pos;
			results.add("30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-806 - Deactivation of the Engine Air Intake Anti-Ice System in the Locked Closed Position");
			pos++;
		}
		if (p6.contains(query)){
			pos6 = pos;
			results.add("30 - ICE AND RAIN PROTECTION > - 20 - Air intakes > 00-807 - Deactivation of the Anti-Ice Pressure-Regulating Valve of the Engine Air Intake in the Locked-Closed position");
			pos++;
		}
		if (p7.contains(query)){
			pos7 = pos;
			results.add("52 - DOORS > - 10 - Passenger/Crew > 21-801 - Installation of the Door Locking Mechanism Door M4L/M4R");
			pos++;
		}
		if (p8.contains(query)){
			pos8 = pos;
			results.add("52 - DOORS > - 10 - Passenger/Crew > 21-801 - Adjustment of the Door Locking Mechanism Door M4L/M4R");
			pos++;
		}
		if (p9.contains(query)){
			pos9 = pos;
			results.add("52 - DOORS > - 10 - Passenger/Crew > 21-801 - Installation of the Door-Locking Mechanism Door M5L/M5R");
		}
		
		String [] liste = results.toArray(new String[results.size()]);
		
		resultsView.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item_ata, results));
		
		resultsView.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				if (pos > 0){
					Intent intent = new Intent(Research.this, AMM.class);
					if (position == pos1) intent.putExtra("task", "EN30115140080100");
					if (position == pos2) intent.putExtra("task", "EN30115140080200");
					if (position == pos3) intent.putExtra("task", "EN30210004080100");
					if (position == pos4) intent.putExtra("task", "EN30210004080400");
					if (position == pos5) intent.putExtra("task", "EN30210004080600");
					if (position == pos6) intent.putExtra("task", "EN30210004080700");
					if (position == pos7) intent.putExtra("task", "EN52132140080100");
					if (position == pos8) intent.putExtra("task", "EN52132182080100");
					if (position == pos9) intent.putExtra("task", "EN52142140080100");
					
					//intent.putExtra("titre", title);
					startActivity(intent);
				}
			}
		});
	}
}
