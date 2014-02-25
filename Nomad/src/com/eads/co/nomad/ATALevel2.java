package com.eads.co.nomad;

import java.util.ArrayList;

public class ATALevel2 {
	private String description;
	
	private ArrayList<ATALevel3> listATALevel3;
	
	public ATALevel2(String description){
		this.description = description;
		listATALevel3 = new ArrayList<ATALevel3>();
	}
	
	public ATALevel2(String description, ArrayList<ATALevel3> listATALevel3){
		this.description = description;
		this.listATALevel3 = listATALevel3;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public ArrayList<ATALevel3> getListATALevel3(){
		return listATALevel3;
	}
	
	public void setListATALevel3 (ArrayList<ATALevel3> listATALevel3){
		this.listATALevel3 = listATALevel3;
	}

}
