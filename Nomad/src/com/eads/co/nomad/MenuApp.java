package com.eads.co.nomad;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MenuApp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_app);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void toAMM(View view){
        Intent intent = new Intent(this, AMMAnnexes.class);
        intent.putExtra("task", "EN30115140080100");
        startActivity(intent);
    }
    
    public void toJobCard(View view){
        Intent intent = new Intent(this, JobCard.class);
        startActivity(intent);
    }
}
