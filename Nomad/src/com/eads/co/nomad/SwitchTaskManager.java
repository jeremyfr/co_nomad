package com.eads.co.nomad;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SwitchTaskManager extends WebViewClient {
	private Context context;
	private static HashMap<String, String> history;

	public SwitchTaskManager(Context context) {
		this.context = context;
		history = new HashMap<String, String>();
	}
	
	public SwitchTaskManager(Context context, String host) {
		this(context);
		history.put(host, "");
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
		/* Ouverture d'une annexe */
		if (url.contains("?y=")) {
			AMMAnnexes.onAnnexeClic(0);
		/* Changement de tache */
		}else{
			Intent i = new Intent(context, AMMAnnexes.class);
			i.putExtra("task", url);
			history.put(url, "");
			context.startActivity(i);
			Set<String> cles = history.keySet();
			Iterator<String> it = cles.iterator();
			while (it.hasNext()){
			   String cle = it.next();
			   String valeur = history.get(cle);
			   System.out.println(cle+" ->"+valeur);
			}
		}
		return true;
	}
	
	

	public HashMap<String, String> getHistory() {
		return history;
	}

	public void setHistory(HashMap<String, String> history) {
		this.history = history;
	}
}
