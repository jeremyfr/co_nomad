package com.eads.co.nomad;

import android.widget.Button;
import android.widget.ImageButton;

public class Step {
	private String task;
	private ImageButton b;

	public ImageButton getB() {
		return b;
	}

	public void setB(ImageButton b) {
		this.b = b;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String text) {
		this.task = text;
	}

}
