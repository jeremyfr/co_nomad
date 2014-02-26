package com.eads.co.nomad;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

public class JavaScriptInterface {
    Context mContext;
    JavaScriptInterface(Context c) {
        mContext = c;
    }
    
    //add other interface methods to be called from JavaScript
    public void receiveValueFromJs(String str) {
    	Log.i("Javascript", str);
    	/* Mettre à jour l'infobulle */
    	AMMAnnexes.setInfobulle(Integer.parseInt(str));
    }
}
