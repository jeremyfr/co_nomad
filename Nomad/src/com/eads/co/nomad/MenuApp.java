package com.eads.co.nomad;

import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MenuApp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_app);
		// Clear history
		((History) this.getApplication()).setHistory(new LinkedHashMap<String, String>());
		setMarginsIcons();
	}

	/**
	 * Set margins between icon buttons.
	 * The margins are determined with the screen orientation :
	 * - LANDSCAPE : 140
	 * - PORTRAIT : 10
	 */
	private void setMarginsIcons() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams param;
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			param = new LinearLayout.LayoutParams(493, 493);
			param.setMargins(140, 0, 140, 0);
			((ImageView) findViewById(R.id.amm)).setLayoutParams(param);
			((ImageView) findViewById(R.id.ipc)).setLayoutParams(param);
			((ImageView) findViewById(R.id.jobCard)).setLayoutParams(param);
			((ImageView) findViewById(R.id.options)).setLayoutParams(param);
			((ImageView) findViewById(R.id.quit)).setLayoutParams(param);
		}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
			param = new LinearLayout.LayoutParams(493, 493);
			param.setMargins(10, 0, 10, 0);
			((ImageView) findViewById(R.id.amm)).setLayoutParams(param);
			((ImageView) findViewById(R.id.ipc)).setLayoutParams(param);
			((ImageView) findViewById(R.id.jobCard)).setLayoutParams(param);
			param.setMargins(20, 0, 20, 0);
			((ImageView) findViewById(R.id.options)).setLayoutParams(param);
			((ImageView) findViewById(R.id.quit)).setLayoutParams(param);
		}
	}

	public void toAMM(View view) {
		// Intent intent = new Intent(this, ATASelection.class);
		Intent intent = new Intent(this, PlaneSelection.class);
		startActivity(intent);
	}

	public void toIPC(View view) {
		Toast.makeText(this, "Non implémenté", Toast.LENGTH_LONG).show();
	}

	public void toJobCard(View view) {
		Intent intent = new Intent(this, MainActivityJobCards.class);
		startActivity(intent);
	}

	public void toOptions(View view) {
		Toast.makeText(this, "Non implémenté", Toast.LENGTH_LONG).show();
	}

	public void quit(View view) {
		System.exit(0);
	}
}
