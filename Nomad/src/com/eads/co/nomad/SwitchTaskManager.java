package com.eads.co.nomad;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SwitchTaskManager extends WebViewClient {
	private Context context;

    public SwitchTaskManager(Context context) {
        this.context = context;
    }
    
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Intent i = new Intent(context, AMM.class);
        i.putExtra("task", url);
        context.startActivity(i);
        return true;
    }
}
