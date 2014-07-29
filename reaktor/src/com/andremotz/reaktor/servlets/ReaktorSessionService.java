package com.andremotz.reaktor.servlets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.andremotz.reaktor.dao.Zielwert;

/**
 * A simple CDI service
 * 
 * 
 */
public class ReaktorSessionService {

	private String scriptpath;

	private int globalTakt = 5;

	private int secondsCompleted;
	private float regressionS1multiplikator;
	private float regressionS2multiplikator;
	private float regressionS1offset;
	private float regressionS2offset;
	private boolean isRunning;

	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://127.0.0.1/reaktor";
	final String USER = "root";
	final String PASS = "";

	static Connection conn;
	static Statement stmt;
	static ResultSet rs;

	Float tempInnen;
	Float tempAussen;
	int currentZielTemp;
	private boolean isHeating;
	private String fileSensorsAverage;

	ReaktorSessionService() {
		getProperties();

		tempInnen = 0f;
		tempAussen = 0f;

		isHeating = false;

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

	public int getGlobalTakt() {
		return globalTakt;
	}

	public boolean getIsHeating() {
		return isHeating;
	}

	public int getCurrentZieltemp() {
		return currentZielTemp;
	}

	public List<Float> convertSensorValuesToCelsius(List<Float> sensorsValues) {
		List<Float> convertedTemps = new ArrayList<Float>();

		Float regressionS1multiplikator = (Float) getRegressionS1multiplikator();

		Float regressionS2multiplikator = (Float) getRegressionS2multiplikator();

		Float regressionS1offset = (Float) getRegressionS1offset();

		Float regressionS2offset = (Float) getRegressionS2offset();

		convertedTemps.add(sensorsValues.get(0) * regressionS1multiplikator
				+ regressionS1offset);

		convertedTemps.add(sensorsValues.get(1) * regressionS2multiplikator
				+ regressionS2offset);

		return convertedTemps;
	}

	public String getCurrentTimestamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd-hh:mm:ss");
		return sdf.format(date);
	}

	String getSensorsAverage() {
		return getContentfromFile(fileSensorsAverage);
	}

	public String getContentfromFile(String filename) {
		String fileContent = null;
		FileInputStream fstream = null;

		try {
			fstream = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine = null;

		try {
			while ((strLine = br.readLine()) != null) {
				fileContent = strLine;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Close the input stream
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileContent;
	}

	public List<Float> getSensorValuesAsArrayList(String toConvert) {
		List<Float> arrayListSensorValues = new ArrayList<Float>();

		// Format vorher: <12.345|56.789>
		toConvert = toConvert.replace("<", ",");
		toConvert = toConvert.replace(">", ",");
		toConvert = toConvert.replace("|", ",");

		Pattern pattern = Pattern.compile("(.*?),");
		Matcher matcher = pattern.matcher(toConvert);

		while (matcher.find()) {
			String s = matcher.group(1);
			if (!s.isEmpty()) {
				float f = Float.parseFloat(s);
				arrayListSensorValues.add(f);
			}

		}

		return arrayListSensorValues;
	}

	/*
	 * Get properties from Property-file
	 */
	private void getProperties() {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "config.properties";
			input = ReaktorSessionService.class.getClassLoader()
					.getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				// return null;
			}

			// load a properties file from class path, inside static method
			prop.load(input);

			// get the property values
			this.scriptpath = prop.getProperty("scriptPath");
			this.fileSensorsAverage = prop.getProperty("fileSensorsAverage");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public List<Float> getSensorsTemps() {
		List<Float> sensorsTemps = new ArrayList<Float>();

		List<Float> sensorsValues = new ArrayList<Float>();

		try {
			sensorsValues = getSensorValuesAsArrayList(getSensorsAverage());
		} catch (NumberFormatException e) {
			sensorsValues.add(0f);

		}
		// Wenn Sensorwerte nicht ausgelsesn werden k??nnen
		if (sensorsValues.size() != 2) {
			sensorsTemps.add(0f);
			sensorsTemps.add(0f);
			isHeating = false;
		} else {
			sensorsTemps = convertSensorValuesToCelsius(sensorsValues);

		}
		return sensorsTemps;
	}

	private void switchArduino() {
		try {
			Runtime rt = Runtime.getRuntime(); // step 1

			String script;
			if (isHeating) {
				script = "switchArduinoOn.sh";
			} else {
				script = "switchArduinoOff.sh";
			}

			Process process = rt.exec(scriptpath + "/" + script); // step 2

			InputStreamReader reader = // step 3
			new InputStreamReader(process.getInputStream());

			BufferedReader buf_reader = new BufferedReader(reader); // step 4.

			String line;
			while ((line = buf_reader.readLine()) != null)
				System.out.println(line);

		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private void getGlobalData() {
		
		String sql = "SELECT * FROM globalData";
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				
				
				int currentRunningstate = rs.getInt("isRunning");
				if (currentRunningstate==1) {
					setIsRunning(true);
				} else {
					setIsRunning(false);
				}
				
				setSecondsCompleted(rs.getInt("secondsCompleted"));
				
				setRegressionS1multiplikator(rs.getFloat("regressionS1multiplikator"));
				setRegressionS2multiplikator(rs.getFloat("regressionS2multiplikator"));
				
				setRegressionS1offset(rs.getFloat("regressionS1offset"));
				setRegressionS2offset(rs.getFloat("regressionS2offset"));
				
			}
			
			rs.close();
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void circuitGo() {
		ArrayList<Zielwert> zielwerte = getZielwerte();

		getGlobalData();

		if (getIsRunning()) {

			// wenn secondsCompleted mehr ist als gesamt, dann aus

			int secondsCompletedGesamt = 0;
			for (int i = 0; i < zielwerte.size(); i++) {
				secondsCompletedGesamt = secondsCompletedGesamt
						+ zielwerte.get(i).getSeconds();
			}

			// Schalte circuit off, wenn Programm fertig ist
			if (getSecondsCompleted() > secondsCompletedGesamt) {
				isHeating = false;
				switchArduino();
			}

			try {
				tempAussen = getSensorsTemps().get(0);
				tempInnen = getSensorsTemps().get(1);
			} catch (IndexOutOfBoundsException e) {
				tempAussen = 0f;
				tempInnen = 0f;

			}

			int secondsCompleted = getSecondsCompleted();
			int currentLevel = getLevelFromSecondsCompleted(secondsCompleted,
					zielwerte);
			currentZielTemp = getCelsiusFromLevel(currentLevel, zielwerte);

			if (tempAussen != 0) {
				if (tempAussen < currentZielTemp + 2) {
					isHeating = true;
				}

				// Sind wir im gr??nen Bereich?
				if (tempAussen > currentZielTemp - 2
						&& tempAussen < currentZielTemp + 100) {

					int newSecondsCompleted = getSecondsCompleted()
							+ globalTakt;

					setSecondsCompleted(newSecondsCompleted);
				}
				
				// wenn drüber von currentZieltem - 2 dann schalte aus
				if (tempAussen > currentZielTemp - 2) {
					isHeating = false;
				}
			}

		} else {
			isHeating = false;
		}

		switchArduino();

		String timestamp = getCurrentTimestamp();

		// Logging
		saveSensorDataToDB(timestamp, tempAussen, tempInnen,
				getIsRunning(), isHeating, currentZielTemp);

	}

	private int getCelsiusFromLevel(int level, ArrayList<Zielwert> zielwerte) {
		return zielwerte.get(level).getCelsius();

	}

	int getLevelFromSecondsCompleted(int secondsCompleted,
			ArrayList<Zielwert> zielwerte) {
		// TODO komplett weiche Implementierung, das kann nur unter starker
		// M??digkeit passiert sein

		int level = 0;
		int secondsCounter = zielwerte.get(0).getSeconds();

		while (secondsCompleted > secondsCounter) {
			try {
				level++;
				// secondsCounter = secondsCounter + (Integer)
				// zielwerteSeconds.get(level);
				secondsCounter = secondsCounter
						+ zielwerte.get(level).getSeconds();
			} catch (ArrayIndexOutOfBoundsException e) {
				// Seconds Completed h??her als irgendwas,
				// z??hle einfach Seconds Counter um 1 hoch, damit er fertig
				// wird,
				// aber nicht level hochz??hlen!!
				secondsCounter = secondsCounter + 1;
			}
		}

		return level;
	}

	/*
	 * Executes local script to save sensor values into String file
	 */
	public void readSerialFromArduino() {
		// TODO Auto-generated method stub
		// ProcessBuilder pb = new
		// ProcessBuilder("/home/pi/sensordata/readSerialFromArduino.sh");
		// ProcessBuilder pb = new
		// ProcessBuilder("/home/pi/sensordata/readSerialFromArduino.py");
		ProcessBuilder pb = null;
		try {
			Process p = pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public  boolean getIsRunning() {
		return isRunning;
	}

	public  void setIsRunning(boolean _isRunning) {
		isRunning = _isRunning;
	}

	public  int getSecondsCompleted() {
		return secondsCompleted;
	}

	public  void setSecondsCompleted(int _secondsCompleted) {

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
	
	public  ArrayList<Zielwert> getZielwerte() {
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
	
	public  void saveSensorDataToDB(String timestamp, float celsius1, float celsius2, 
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

	public  float getRegressionS1multiplikator() {
		return regressionS1multiplikator;
	}

	public  void setRegressionS1multiplikator(
			float _regressionS1multiplikator) {
		regressionS1multiplikator = _regressionS1multiplikator;
	}

	public  float getRegressionS2multiplikator() {
		return regressionS2multiplikator;
	}

	public  void setRegressionS2multiplikator(
			float _regressionS2multiplikator) {
		regressionS2multiplikator = _regressionS2multiplikator;
	}

	public  float getRegressionS1offset() {
		return regressionS1offset;
	}

	public  void setRegressionS1offset(float _regressionS1offset) {
		regressionS1offset = _regressionS1offset;
	}

	public  float getRegressionS2offset() {
		return regressionS2offset;
	}

	public  void setRegressionS2offset(float _regressionS2offset) {
		regressionS2offset = _regressionS2offset;
	}

}
