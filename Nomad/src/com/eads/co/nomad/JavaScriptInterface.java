package com.eads.co.nomad;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

public class JavaScriptInterface {
    Context mContext;
    WebView webView;
    JavaScriptInterface(Context c, WebView webView) {
        mContext = c;
        this.webView = webView;
    }
    
    //add other interface methods to be called from JavaScript
    @android.webkit.JavascriptInterface
    public void receiveValueFromJs(String str) {
    	Log.i("Javascript", str);
    	/* Mettre à jour l'infobulle
    	AMMAnnexes.setInfobulle(Integer.parseInt(str));*/
    }
}
