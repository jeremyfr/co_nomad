package com.eads.co.nomad;

import android.content.Context;
import android.util.Log;


public class JavaScriptInterface {

    
    @android.webkit.JavascriptInterface public void receiveValueFromJs(String str) {
    	AMMAnnexes.setInfobulle((int)Double.parseDouble(str));
    }
}
