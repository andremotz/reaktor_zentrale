package com.andremotz.reaktor.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
	
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://127.0.0.1/reaktor";
	final String USER = "root";
	final String PASS = "";
	
	Connection conn;
	Statement stmt;
	ResultSet rs;
	
	private GlobalData globalData;
	private ArrayList<Zielwert> zielwerte;
	
	public GlobalData getGlobalData() {
		
		GlobalData currentGlobals = new GlobalData();
		
		String sql = "SELECT * FROM globalData";
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				
				currentGlobals.setComment(rs.getString("comment"));
				
				int currentRunningstate = rs.getInt("isRunning");
				if (currentRunningstate==1) {
					currentGlobals.setIsRunning(true);
				} else {
					currentGlobals.setIsRunning(false);
				}
				
				currentGlobals.setSecondsCompleted(rs.getInt("secondsCompleted"));
				
				currentGlobals.setRegressionS1multiplikator(rs.getFloat("regressionS1multiplikator"));
				currentGlobals.setRegressionS2multiplikator(rs.getFloat("regressionS2multiplikator"));
				
				currentGlobals.setRegressionS1offset(rs.getFloat("regressionS1offset"));
				currentGlobals.setRegressionS2offset(rs.getFloat("regressionS2offset"));
				
			}
			
			rs.close();
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return currentGlobals;
	}

	public void setGlobalData(GlobalData globalData) {
		this.globalData = globalData;
	}

	public DatabaseHandler() {
		conn = null;
		stmt = null;
		
		//STEP 2: Register JDBC driver
	    try {
			Class.forName("com.mysql.jdbc.Driver");	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setSecondsCompleted(int secondsCompleted) {
		String sql = "UPDATE globalData SET secondsCompleted = ? WHERE id = 1";
		
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			 PreparedStatement updateQuery = conn.prepareStatement(sql);
			 updateQuery.setInt(1, secondsCompleted);
			 updateQuery.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveSensorDataToDB(String timestamp, float celsius1, float celsius2, 
			boolean isRunning, boolean isHeating, int currentZielTemp) {
		
		String sql = "INSERT INTO messwerte (timestamp, session_id, celsius1, " +
				"celsius2, wasRunning, wasHeating, zielTemp) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		int wasRunning = 0;
		if (isRunning) {
			wasRunning = 1;
		}
		
		int wasHeating = 0;
		if (isHeating) {
			wasHeating = 1;
		}		
		
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			 PreparedStatement insertQuery = conn.prepareStatement(sql);
			 insertQuery.setString(1, timestamp);
			 insertQuery.setInt(2, 1);
			 insertQuery.setFloat(3, celsius1);
			 insertQuery.setFloat(4, celsius2);
			 insertQuery.setInt(5, wasRunning);
			 insertQuery.setInt(6, wasHeating);
			 insertQuery.setInt(7, currentZielTemp);
			 
			 insertQuery.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Zielwert> getZielwerte() {
		ArrayList<Zielwert> zielwerte = new ArrayList<Zielwert>();
		
		String sql = "SELECT * FROM zielwerte";
		
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				Zielwert zielwert = new Zielwert();
				
				zielwert.setId(rs.getInt("id"));
				zielwert.setSession_id(rs.getInt("session_id"));
				zielwert.setCelsius(rs.getInt("celsius"));
				zielwert.setSeconds(rs.getInt("seconds"));
				
				zielwerte.add(zielwert);
			}
			
			rs.close();
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return zielwerte;
	}

	public void setZielwerte(ArrayList<Zielwert> zielwerte) {
		this.zielwerte = zielwerte;
	}

	
}
