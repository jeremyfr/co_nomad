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
    
    public void toAMM(View view){
        Intent intent = new Intent(this, AMMAnnexes.class);
        intent.putExtra("task", "EN30115140080100");
        startActivity(intent);
    }
    
    public void toIPC(View view){
        
    }
    
    public void toJobCard(View view){
        Intent intent = new Intent(this, JobCard.class);
        intent.putExtra("task", "EN30115140080100");
        startActivity(intent);
    }
    
    public void toOptions(View view){
        
    }
    
    public void quit(View view){
    	System.exit(0);
    }
}
