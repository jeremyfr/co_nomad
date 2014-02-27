package com.eads.co.nomad;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SwitchTaskManager extends WebViewClient {
	private Context context;
<<<<<<< HEAD
	private String part;

	public SwitchTaskManager(Context context, String p) {
		this.context = context;
		part = p;
=======
	private AMMAnnexes activity;

	public SwitchTaskManager(Context context, AMMAnnexes activity) {
		this.context = context;
		this.activity = activity;
>>>>>>> fec4b5a7f67a92e2bd1cdea2f683f4338836ff10
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Toast.makeText(context, url, Toast.LENGTH_SHORT).show();

		/* Ouverture d'une annexe */
<<<<<<< HEAD
		if (url.contains("?y=")) {
			String[] split = url.split("y=");
			if(part.equals("jobcard")){
				JobCard.onAnnexeClic((WebView)view, split[1]);
			}else{
				AMMAnnexes.onAnnexeClic((WebView)view, split[1]);
			}
				
=======
		if (url.contains("?id=")) {
			String[] split = url.split("id=");
			activity.onAnnexeClic(view, split[1]);
>>>>>>> fec4b5a7f67a92e2bd1cdea2f683f4338836ff10
			/* Changement de tache */
		} else {
			Intent i = new Intent(context, AMMAnnexes.class);
			i.putExtra("task", url);
			context.startActivity(i);
		}
		return true;
	}
}
