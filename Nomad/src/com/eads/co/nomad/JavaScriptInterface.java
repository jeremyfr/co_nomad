package com.eads.co.nomad;

import android.content.Context;
import android.util.Log;


public class JavaScriptInterface {

	private AMMAnnexes activityAMM;
	
	public JavaScriptInterface(AMMAnnexes activity)
	{
		this.activityAMM = activity;
	}
    
	private JobCard activityJobCard;
	
	public JavaScriptInterface(JobCard activity)
	{
		this.activityJobCard = activity;
	}
	
	
    @android.webkit.JavascriptInterface public void receiveValueFromJs(String str) {
    	if(!activityAMM.equals(null)){
    		activityAMM.setInfobulle((int)Double.parseDouble(str));    		
    	}
    	if(!activityJobCard.equals(null)){
    		activityJobCard.setInfobulle((int)Double.parseDouble(str));    		
    	}
    }
}
