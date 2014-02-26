package com.eads.co.nomad;

import android.content.Context;
import android.util.Log;

public class JavaScriptInterface {
    Context mContext;
    JavaScriptInterface(Context c) {
        mContext = c;
    }
    //add other interface methods to be called from JavaScript
    @android.webkit.JavascriptInterface
    public void receiveValueFromJs(String str) {
    	AMMAnnexes.setInfobulle(Integer.parseInt(str));
    	Log.i("Javascript", str);
    }
}
