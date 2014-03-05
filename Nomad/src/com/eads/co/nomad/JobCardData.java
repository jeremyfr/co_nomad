package com.eads.co.nomad;

import java.util.ArrayList;

public class JobCardData {
	private String name;
	private ArrayList<JobCardDetails> jobCardDetails;

	public JobCardData(String name) {
		this.name = name;
		this.jobCardDetails = new ArrayList<JobCardDetails>();
	}

	public JobCardData(String name, ArrayList<JobCardDetails> jobCardDetails) {
		this.name = name;
		this.jobCardDetails = jobCardDetails;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setJobCardDetails(ArrayList<JobCardDetails> jobCardDetails) {
		this.jobCardDetails = jobCardDetails;
	}

	public ArrayList<JobCardDetails> getJobCardDetails() {
		return this.jobCardDetails;
	}
}
