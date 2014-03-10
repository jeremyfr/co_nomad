package com.eads.co.nomad;

import android.app.SearchManager;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class AMMATA extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);
        Bundle infos = this.getIntent().getExtras();
		String fsn = infos.getString("FSN");
		String msn = infos.getString("MSN");
		String id = infos.getString("ID");
		String plane = infos.getString("Avion");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		String title = "ATA Selection   /   Plane:" + plane + " MSN:" + msn + " FSN:"
				+ fsn + " ID:" + id;
		setTitle(title);
		if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			String query = getIntent().getStringExtra(SearchManager.QUERY);
			Intent mapIntent = new Intent(this, Research.class);
			mapIntent.putExtra("query", query);
//			mapIntent.putExtra("titre", title);
//			mapIntent.putExtra("Avion", plane);
//			mapIntent.putExtra("FSN", fsn);
//			mapIntent.putExtra("MSN", msn);
//			mapIntent.putExtra("ID", id);
			startActivity(mapIntent);
		}
        /* TabHost will have Tabs */
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        /* TabSpec used to create a new tab. 
         * By using TabSpec only we can able to setContent to the tab.
         * By using TabSpec setIndicator() we can set name to tab. */
        
        /* tid1 is firstTabSpec Id. Its used to access outside. */
        TabSpec firstTabSpec = tabHost.newTabSpec("tid1");
        TabSpec secondTabSpec = tabHost.newTabSpec("tid1");
        
        /* TabSpec setIndicator() is used to set name for the tab. */
        /* TabSpec setContent() is used to set content for a particular tab. */
        Intent ATASelection = new Intent(this, ATASelection.class);
        ATASelection.putExtra("Avion", plane);
        ATASelection.putExtra("FSN", fsn);
        ATASelection.putExtra("MSN", msn);
        ATASelection.putExtra("ID", id);
        
        firstTabSpec.setIndicator("Liste ATA").setContent(ATASelection);
        secondTabSpec.setIndicator("Avion 3D").setContent(new Intent(this,Plane3D.class));
        
        /* Add tabSpec to the TabHost to display. */
        tabHost.addTab(firstTabSpec);
        tabHost.addTab(secondTabSpec);
        
    }
}
