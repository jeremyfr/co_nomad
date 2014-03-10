package com.eads.co.nomad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
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
		((EditText) findViewById(R.id.motDePasse))
				.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						toMenu(v);
						return true;
					}
		});
		manageFileProcedureUsed();
	}

	/**
	 * Manage the file that save procedure used.
	 * It is possible to know if a procedure was updated since last use.
	 */
	private void manageFileProcedureUsed() {
		File file = new File(getApplicationContext().getFilesDir(),"proceduresUsed.txt");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void toMenu(View view) {
		Intent intent = new Intent(this, MenuApp.class);
		startActivity(intent);
	}
}
