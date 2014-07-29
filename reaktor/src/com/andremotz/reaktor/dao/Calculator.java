package com.andremotz.reaktor.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calculator {
	
	
	public Calculator(){
		
	}
	
	public List<Float> convertSensorValuesToCelsius(List<Float> sensorsValues) {
		List<Float> convertedTemps = new ArrayList<Float>();
		
		Float  regressionS1multiplikator = (Float) GlobalData.getRegressionS1multiplikator();
		
		Float  regressionS2multiplikator = (Float) GlobalData.getRegressionS2multiplikator();
		
		Float  regressionS1offset = (Float) GlobalData.getRegressionS1offset();
		
		Float  regressionS2offset = (Float) GlobalData.getRegressionS2offset();
		
		convertedTemps.add(sensorsValues.get(0) 
				* regressionS1multiplikator 
				+ regressionS1offset);
		
		convertedTemps.add(sensorsValues.get(1) 
				* regressionS2multiplikator 
				+ regressionS2offset);
		
		return convertedTemps;
	}

	public String getCurrentTimestamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd-hh:mm:ss");
		return sdf.format(date);
	}
	
	

}
