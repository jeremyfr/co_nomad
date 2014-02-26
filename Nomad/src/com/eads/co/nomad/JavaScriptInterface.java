package com.eads.co.nomad;

import android.content.Context;
import android.util.Log;


public class JavaScriptInterface {

	private AMMAnnexes activity;
	
	public JavaScriptInterface(AMMAnnexes activity)
	{
		this.activity = activity;
	}
    
    @android.webkit.JavascriptInterface public void receiveValueFromJs(String str) {
    	activity.setInfobulle((int)Double.parseDouble(str));
    }
}
