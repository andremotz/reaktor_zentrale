package com.andremotz.reaktor.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.andremotz.reaktor.dao.Calculator;
import com.andremotz.reaktor.dao.DatabaseHandler;
import com.andremotz.reaktor.dao.FileHandler;
import com.andremotz.reaktor.dao.GlobalData;
import com.andremotz.reaktor.dao.Zielwert;

/**
 * A simple CDI service which is able to say hello to someone
 * 
 * @author Pete Muir
 * 
 */
public class HelloService {
	
	FileHandler fileHandler;
	DatabaseHandler dbHandler;
	Calculator calculator;
	
	private int globalTakt = 5;
	
	public int getGlobalTakt() {
		return globalTakt;
	}

	GlobalData globalData;
	ArrayList<Zielwert> zielwerte;
	
	Float tempInnen;
	Float tempAussen;
	int currentZielTemp;
	private boolean isHeating;
	
	public boolean getIsHeating() {
		return isHeating;
	}
	
	public int getCurrentZieltemp() {
		return currentZielTemp;
	}

	HelloService() {
		dbHandler = new DatabaseHandler();
		fileHandler = new FileHandler();
		calculator = new Calculator();
		
		tempInnen = 0f;
		tempAussen = 0f;
		
		isHeating = false;
		
		globalData = dbHandler.getGlobalData();
		zielwerte = dbHandler.getZielwerte();
	}

	public GlobalData getGlobalData() {
		globalData = dbHandler.getGlobalData();
		return globalData;
	}

	public void setGlobalData(GlobalData globalData) {
		this.globalData = globalData;
	}


	String createHelloMessage(String name) {
		return "Hey " + name + "!!!!! : )";
	}

	String getSensorsAverage() {
		// TODO Pfad relativ
//		String filename = "/home/pi/sensordata/sensorsAverages.temp";
		String filename = "/Users/andremotz/Desktop/sensorsAverages.temp";
		return fileHandler.getContentfromFile(filename);
	}

	public List<Float> getSensorsTemps() {
		List<Float> sensorsTemps = new ArrayList<Float>();
		
		List<Float> sensorsValues = new ArrayList<Float>();
		
			try {
				sensorsValues = fileHandler
						.getSensorValuesAsArrayList(getSensorsAverage());
			} catch (NumberFormatException e) {
				sensorsValues.add(0f);
				
			}
			// Wenn Sensorwerte nicht ausgelsesn werden k??nnen
			if (sensorsValues.size() != 2) {
				sensorsTemps.add(0f);
				sensorsTemps.add(0f);
				isHeating = false;
			} else {
				sensorsTemps = calculator.convertSensorValuesToCelsius(sensorsValues);
				
			}
		return sensorsTemps;
	}
	
	private void switchArduino() {
		try {
			Runtime rt = Runtime.getRuntime(); // step 1
			
			String scriptPath;
			if (isHeating) {
				scriptPath = "/home/pi/sensordata/switchArduinoOn.sh";
			} else {
				scriptPath = "/home/pi/sensordata/switchArduinoOff.sh";
			}
				
			Process process = rt.exec(scriptPath); // step 2

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

	public void circuitGo() {
	
		if(globalData.getIsRunning()) {
			 
			// wenn secondsCompleted mehr ist als gesamt, dann aus
			
			int secondsCompletedGesamt = 0;
			for (int i = 0; i < zielwerte.size(); i++) {
				secondsCompletedGesamt = secondsCompletedGesamt + 
						zielwerte.get(i).getSeconds();
			}
			
			if (globalData.getSecondsCompleted() > secondsCompletedGesamt) {
				isHeating = false;
				switchArduino();
			}
			
			try {
				// wenn tempInnen < currentZieltemp + 2, dann ein
				tempAussen = getSensorsTemps().get(0);
				tempInnen = getSensorsTemps().get(1);
			} catch (IndexOutOfBoundsException e) {
				tempAussen = 0f;
				tempInnen = 0f;
				
			}
			
			int secondsCompleted = globalData.getSecondsCompleted();
			int currentLevel = getLevelFromSecondsCompleted(secondsCompleted, zielwerte);
			currentZielTemp = getCelsiusFromLevel(currentLevel, zielwerte);
			
			if (tempAussen != 0) {
				if (tempAussen < currentZielTemp + 2) {
					isHeating = true;
				}
				
				// Sind wir im gr??nen Bereich?
				if (tempAussen > currentZielTemp - 2 && tempAussen < currentZielTemp + 10) {
					int newSecondsCompleted = dbHandler.getGlobalData().getSecondsCompleted() + globalTakt;
					globalData.setSecondsCompleted(newSecondsCompleted);
					dbHandler.setSecondsCompleted(newSecondsCompleted);
				}
			}
			
		} else {
			isHeating = false;
		}
		
		switchArduino();
		
		String timestamp = calculator.getCurrentTimestamp();
		
		// Logging
		dbHandler.saveSensorDataToDB(timestamp, tempAussen, tempInnen, 
				globalData.getIsRunning(), isHeating, currentZielTemp);
		
	}
	
	private int getCelsiusFromLevel(int level, ArrayList<Zielwert> zielwerte) {
		return zielwerte.get(level).getCelsius();
		
	}

	int getLevelFromSecondsCompleted(int secondsCompleted, ArrayList<Zielwert> zielwerte) {
		// TODO komplett weiche Implementierung, das kann nur unter starker M??digkeit passiert sein
		
		  int level=0;
		  int secondsCounter = zielwerte.get(0).getSeconds();

		  while (secondsCompleted > secondsCounter) {
		    try {
		      level++;
//		      secondsCounter = secondsCounter + (Integer) zielwerteSeconds.get(level);
		      secondsCounter = secondsCounter + zielwerte.get(level).getSeconds();
		    } 
		    catch (ArrayIndexOutOfBoundsException e) {
		      // Seconds Completed h??her als irgendwas,
		      // z??hle einfach Seconds Counter um 1 hoch, damit er fertig wird,
		      // aber nicht level hochz??hlen!!
		      secondsCounter = secondsCounter + 1;
		    }
		  }

		  return level;
		}

	/*
	 * Executes local script to save sensor values into String file
	 * 
	 */
	public void readSerialFromArduino() {
		// TODO Auto-generated method stub
//		ProcessBuilder pb = new ProcessBuilder("/home/pi/sensordata/readSerialFromArduino.sh");
//		ProcessBuilder pb = new ProcessBuilder("/home/pi/sensordata/readSerialFromArduino.py");
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

}
