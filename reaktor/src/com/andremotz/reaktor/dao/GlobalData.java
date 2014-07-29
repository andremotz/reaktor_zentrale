package com.andremotz.reaktor.dao;

public class GlobalData {
	
	private boolean isRunning;
	private String comment;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private int secondsCompleted;
	private float regressionS1multiplikator;
	private float regressionS2multiplikator;
	private float regressionS1offset;
	private float regressionS2offset;
	
	public GlobalData(){
		isRunning = false;
		secondsCompleted = 0;
		regressionS1multiplikator = 0f;
		regressionS1multiplikator = 0f;
		regressionS1offset = 0f;
		regressionS2offset = 0f;
	
	}

	public boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public int getSecondsCompleted() {
		return secondsCompleted;
	}

	public void setSecondsCompleted(int secondsCompleted) {
		this.secondsCompleted = secondsCompleted;
	}

	public float getRegressionS1multiplikator() {
		return regressionS1multiplikator;
	}

	public void setRegressionS1multiplikator(float regressionS1multiplikator) {
		this.regressionS1multiplikator = regressionS1multiplikator;
	}

	public float getRegressionS2multiplikator() {
		return regressionS2multiplikator;
	}

	public void setRegressionS2multiplikator(float regressionS2multiplikator) {
		this.regressionS2multiplikator = regressionS2multiplikator;
	}

	public float getRegressionS1offset() {
		return regressionS1offset;
	}

	public void setRegressionS1offset(float regressionS1offset) {
		this.regressionS1offset = regressionS1offset;
	}

	public float getRegressionS2offset() {
		return regressionS2offset;
	}

	public void setRegressionS2offset(float regressionS2offset) {
		this.regressionS2offset = regressionS2offset;
	}

}
