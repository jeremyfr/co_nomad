package com.eads.co.nomad;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ScrollView;

public class OurScrollView extends ScrollView {

	private AMMAnnexes activityAMM;
	private JobCard activityJobCard;
	private String annexe; // Nom de l'annexe affichée (id du HTML).
	private WebView webView;

	public OurScrollView(Context context) {
		super(context);
	}

	public OurScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (activityAMM != null) {
			switch (activityAMM.state) {
			case NOT_DISPLAYED:
				break;
			case DISPLAYED_FREE:
				webView.loadUrl("javascript:getPosition('" + annexe + "')");
				break;
			case DISPLAYED_PRESSED:
				webView.loadUrl("javascript:getPosition('" + annexe + "')");
				break;
			case DISPLAYED_FULLSCREEN:
				break;
			}
		}
		if (activityJobCard != null) {
			switch (activityJobCard.state) {
			case NOT_DISPLAYED:
				break;
			case DISPLAYED_FREE:
				webView.loadUrl("javascript:getPosition('" + annexe + "')");
				break;
			case DISPLAYED_PRESSED:
				webView.loadUrl("javascript:getPosition('" + annexe + "')");
				break;
			case DISPLAYED_FULLSCREEN:
				break;
			}
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	public void setActivity(AMMAnnexes activity) {
		this.activityAMM = activity;
	}

	public void setActivity(JobCard activity) {
		this.activityJobCard = activity;
	}

	public void setAnnexe(WebView webView, String annexe) {
		this.annexe = annexe;
		this.webView = webView;
	}

}
