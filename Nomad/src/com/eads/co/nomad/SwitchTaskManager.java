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
	private String part;

	public SwitchTaskManager(Context context, JobCard activity, String p) {
		this.context = context;
		part = p;
		activityJobCard = activity;
		
		
	}
	
	private AMMAnnexes activityAMM;
	private JobCard activityJobCard;
	public SwitchTaskManager(Context context, AMMAnnexes activity, String p) {
		this.context = context;
		this.activityAMM = activity;
		this.part  = p;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Toast.makeText(context, url, Toast.LENGTH_SHORT).show();

		/* Ouverture d'une annexe */
		if (url.contains("?id=")) {
			String[] split = url.split("id=");
			if(part.equals("jobcard")){
				activityJobCard.onAnnexeClic(view, split [1]);
			}else{
				activityAMM.onAnnexeClic(view, split[1]);	
			}
			
			
			/* Changement de tache */
		} else {
			Intent i = new Intent(context, AMMAnnexes.class);
			i.putExtra("task", url);
			context.startActivity(i);
		}
		return true;
	}
	
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        System.out.println("Scale changed from " + oldScale + " to " + newScale);
    }

}
