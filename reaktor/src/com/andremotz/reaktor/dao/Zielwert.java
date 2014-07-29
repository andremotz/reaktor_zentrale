package com.andremotz.reaktor.dao;

public class Zielwert {
	private int id;
	private int session_id;
	private int celsius;
	private int seconds;
	
	public Zielwert() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSession_id() {
		return session_id;
	}

	public void setSession_id(int session_id) {
		this.session_id = session_id;
	}

	public int getCelsius() {
		return celsius;
	}

	public void setCelsius(int celsius) {
		this.celsius = celsius;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	

}
