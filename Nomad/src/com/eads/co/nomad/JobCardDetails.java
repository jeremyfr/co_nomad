package com.eads.co.nomad;

public class JobCardDetails {

	private JobCardData jobCard;
	private String acDetails;
	private String taskDetails;

	public JobCardDetails(JobCardData jobCard, String acDetails, String taskDetails) {
		super();
		this.jobCard = jobCard;
		this.acDetails = acDetails;
		this.taskDetails = taskDetails;
	}

	public JobCardData getGroupe() {
		return jobCard;
	}

	public void setGroupe(JobCardData jobCard) {
		this.jobCard = jobCard;
	}

	public String getAcDetails() {
		return acDetails;
	}
	
	public void setAcDetails(String acDetails) {
		this.acDetails = acDetails;
	}
	
	public String getTaskDetails() {
		return taskDetails;
	}

	public void setTaskDetails(String taskDetails) {
		this.taskDetails = taskDetails;
	}
}
