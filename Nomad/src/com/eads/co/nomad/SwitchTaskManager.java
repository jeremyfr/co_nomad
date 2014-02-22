package com.eads.co.nomad;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SwitchTaskManager extends WebViewClient {
	private Context context;

    public SwitchTaskManager(Context context) {
        this.context = context;
    }
    
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        /* Changer d'activity*/
        //Intent i = new Intent(context, AMM.class);
        //i.putExtra("task", url);
        //context.startActivity(i);
        //return true;
        /*      */
    	/* Lancer une methode */
    	Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
    	if(url.contains("?y="))
    	{
    		AMMAnnexes.onAnnexeClic(0);
    	}
    	return true;
    }
}
