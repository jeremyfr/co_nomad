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
	private AMMAnnexes activity;

	public SwitchTaskManager(Context context, AMMAnnexes activity) {
		this.context = context;
		this.activity = activity;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
		/* Ouverture d'une annexe */
		if (url.contains("?y=")) {
			String[] split = url.split("y=");
			activity.onAnnexeClic(view, "test");
			/* Changement de tache */
		} else {
			Intent i = new Intent(context, AMMAnnexes.class);
			i.putExtra("task", url);
			context.startActivity(i);
		}
		return true;
	}
}
