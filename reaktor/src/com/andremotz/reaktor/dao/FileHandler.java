package com.andremotz.reaktor.dao;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileHandler {
	
	public FileHandler() {
		
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
		List<Float> arrayListSensorValues= new ArrayList<Float>();
		
		// Format vorher: <12.345|56.789>
		toConvert = toConvert.replace("<", ",");
		toConvert = toConvert.replace(">", ",");
		toConvert = toConvert.replace("|", ",");

		Pattern pattern = Pattern.compile("(.*?),");
		Matcher matcher = pattern.matcher(toConvert);
		
	    while (matcher.find ()) {
	    	String s = matcher.group(1);
	    	if(!s.isEmpty()) {
	    		float f = Float.parseFloat(s);
		    	arrayListSensorValues.add(f);
	    	}
	    	
	    }
		
		return arrayListSensorValues;
	}

}
