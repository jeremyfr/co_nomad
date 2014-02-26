package com.eads.co.nomad;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ScrollView;

public class OurScrollView extends ScrollView {

	String annexe; // Nom de l'annexe affichée (id du HTML).
	WebView webView;
	
	public OurScrollView(Context context) {
		super(context);
	}
	
	public OurScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) 
	{
		switch (AMMAnnexes.state) {
		case NOT_DISPLAYED:
			break;
		case DISPLAYED_FREE:
		    webView.loadUrl("javascript:getPosition('"+annexe+"')");
			break;
		case DISPLAYED_PRESSED:
		    webView.loadUrl("javascript:getPosition('"+annexe+"')");
			break;
		case DISPLAYED_FULLSCREEN:
			break;
		}
	    super.onScrollChanged(l, t, oldl, oldt);
	}
	
	public void setAnnexe(WebView webView, String annexe)
	{
		this.annexe = annexe;
		this.webView = webView;
	}
	
}
