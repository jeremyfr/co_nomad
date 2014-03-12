package com.eads.co.nomad;

import android.app.SearchManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class AMMATA extends TabActivity implements OnTabChangeListener{
	private TabHost tabHost;
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
        tabHost = (TabHost)findViewById(android.R.id.tabhost);
        
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
        
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#e5dbbe")); // unselected
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.blue)); // selected
        tabHost.setOnTabChangedListener(this);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, PlaneSelection.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

	@Override
	public void onTabChanged(String tabId) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#e5dbbe")); // unselected
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.blue)); // selected
		
	}
}
