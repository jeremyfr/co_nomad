package com.eads.co.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Verification du mot de passe lors du clic sur 'Ok' du clavier */
        ((EditText) findViewById(R.id.motDePasse)).setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				toMenu(v);
				return true;
			}
		});
    }
    
    public void toMenu(View view){
        Intent intent = new Intent(this, MenuApp.class);
        startActivity(intent);
    }
}
