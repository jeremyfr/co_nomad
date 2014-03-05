package com.eads.co.nomad;

import java.util.ArrayList;

public class ATA {
	private String description;

	private ArrayList<ATALevel2> listATALevel2;

	public ATA(String description) {
		this.description = description;
		listATALevel2 = new ArrayList<ATALevel2>();
	}

	public ATA(String description, ArrayList<ATALevel2> listATALevel2) {
		this.description = description;
		this.listATALevel2 = listATALevel2;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<ATALevel2> getListATALevel2() {
		return listATALevel2;
	}

	public void setListATALevel2(ArrayList<ATALevel2> listATALevel2) {
		this.listATALevel2 = listATALevel2;
	}
}
