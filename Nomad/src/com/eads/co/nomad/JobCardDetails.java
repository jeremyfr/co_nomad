package com.eads.co.nomad;

public class JobCardDetails {

	private int id;
	private JobCardData jobCard;
	private String acDetails;
	private String taskDetails;

	public JobCardDetails(int id, JobCardData jobCard, String acDetails,
			String taskDetails) {
		super();
		this.id = id;
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
