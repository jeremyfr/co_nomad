package com.eads.co.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuApp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_app);
    }
    
	public void toAMM(View view){
		//Intent intent = new Intent(this, ATASelection.class);
		Intent intent = new Intent(this, PlaneSelection.class);
		startActivity(intent);
	}
	
    public void toIPC(View view){
    	Toast.makeText(this, "Non implémenté", Toast.LENGTH_LONG).show();
    }
    
    public void toJobCard(View view){
        Intent intent = new Intent(this, MainActivityJobCards.class);
        startActivity(intent);
    }

    public void toOptions(View view){
    	Toast.makeText(this, "Non implémenté", Toast.LENGTH_LONG).show();
    }
    
    public void quit(View view){
    	System.exit(0);
    }
}
