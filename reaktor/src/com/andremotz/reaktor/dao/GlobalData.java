package com.andremotz.reaktor.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GlobalData {

	private static boolean isRunning;
	private static String comment;

	public static String getComment() {
		return comment;
	}

	public static void setComment(String _comment) {
		comment = _comment;
	}

	private static int secondsCompleted;
	private static float regressionS1multiplikator;
	private static float regressionS2multiplikator;
	private static float regressionS1offset;
	private static float regressionS2offset;

	static {
		isRunning = false;
		secondsCompleted = 0;
		regressionS1multiplikator = 0f;
		regressionS1multiplikator = 0f;
		regressionS1offset = 0f;
		regressionS2offset = 0f;

		conn = null;
		stmt = null;

		// STEP 2: Register JDBC driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static boolean getIsRunning() {
		return isRunning;
	}

	public static void setIsRunning(boolean _isRunning) {
		isRunning = _isRunning;
	}

	public static int getSecondsCompleted() {
		return secondsCompleted;
	}

	public static void setSecondsCompleted(int _secondsCompleted) {

		String sql = "UPDATE globalData SET secondsCompleted = ? WHERE id = 1";

		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			PreparedStatement updateQuery = conn.prepareStatement(sql);
			updateQuery.setInt(1, _secondsCompleted);
			updateQuery.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		secondsCompleted = _secondsCompleted;
	}
	
	public static ArrayList<Zielwert> getZielwerte() {
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
	
	public static void saveSensorDataToDB(String timestamp, float celsius1, float celsius2, 
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

	public static float getRegressionS1multiplikator() {
		return regressionS1multiplikator;
	}

	public static void setRegressionS1multiplikator(
			float _regressionS1multiplikator) {
		regressionS1multiplikator = _regressionS1multiplikator;
	}

	public static float getRegressionS2multiplikator() {
		return regressionS2multiplikator;
	}

	public static void setRegressionS2multiplikator(
			float _regressionS2multiplikator) {
		regressionS2multiplikator = _regressionS2multiplikator;
	}

	public static float getRegressionS1offset() {
		return regressionS1offset;
	}

	public static void setRegressionS1offset(float _regressionS1offset) {
		regressionS1offset = _regressionS1offset;
	}

	public static float getRegressionS2offset() {
		return regressionS2offset;
	}

	public static void setRegressionS2offset(float _regressionS2offset) {
		regressionS2offset = _regressionS2offset;
	}

	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final static String DB_URL = "jdbc:mysql://127.0.0.1/reaktor";
	final static String USER = "root";
	final static String PASS = "";

	static Connection conn;
	static Statement stmt;
	static ResultSet rs;

	public static void updateGlobalData() {

		String sql = "SELECT * FROM globalData";
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {

				GlobalData.setComment(rs.getString("comment"));

				int currentRunningstate = rs.getInt("isRunning");
				if (currentRunningstate == 1) {
					GlobalData.setIsRunning(true);
				} else {
					GlobalData.setIsRunning(false);
				}

				GlobalData.setSecondsCompleted(rs.getInt("secondsCompleted"));

				GlobalData.setRegressionS1multiplikator(rs
						.getFloat("regressionS1multiplikator"));
				GlobalData.setRegressionS2multiplikator(rs
						.getFloat("regressionS2multiplikator"));

				GlobalData.setRegressionS1offset(rs
						.getFloat("regressionS1offset"));
				GlobalData.setRegressionS2offset(rs
						.getFloat("regressionS2offset"));

			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
