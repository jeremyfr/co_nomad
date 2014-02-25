package com.eads.co.nomad;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;

public class History extends Application {
    private LinkedHashMap<String, String> history = new LinkedHashMap<String, String>();

    public LinkedHashMap<String, String> getHistory() {
        return history;
    }

    public void setHistory(LinkedHashMap<String, String> history) {
        this.history = history;
    }
    
    public ArrayList<String> getTitles(){
    	ArrayList<String> titles = new ArrayList<String>();
    	for (Map.Entry<String, String> entry : history.entrySet()) {
			titles.add(entry.getValue());
		}
    	return titles;
    }

	public String getKeyAt(int position) {
		List<String> keys = new ArrayList<String>(history.keySet());
		return keys.get(position);
	}
}