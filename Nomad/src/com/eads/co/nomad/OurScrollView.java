package com.eads.co.nomad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class OurScrollView extends ScrollView {

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
	    AMMAnnexes.warningWV.loadUrl("javascript:getPosition('test')");
	    super.onScrollChanged(l, t, oldl, oldt);
	}

}
