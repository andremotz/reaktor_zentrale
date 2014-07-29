package com.andremotz.reaktor.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class ReaktorSession
 */
@WebServlet("/ReaktorSession")
public class ReaktorSession extends HttpServlet {
	
	HelloService helloService;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReaktorSession() {
        super();
        // TODO Auto-generated constructor stub
        helloService = new HelloService();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		helloService.readSerialFromArduino();
		helloService.circuitGo();
		
		String bgColor = "333333";
		if (helloService.getSensorsTemps().get(0) == 0) {
			bgColor = "FF0000";
		} else {
			bgColor = "FFFFFF";
		}
		
		String PAGE_HEADER = "<html><head>" +
				"<meta charset='utf-8'> " +
				"<meta http-equiv='refresh' content='" + String.valueOf(helloService.getGlobalTakt()) + "'>" +
				
//				"<link rel=\"stylesheet\" href=\"http://code.jquery.com/ui/1.9.0/themes/base/jquery-ui.css\" />" + 
//				"<script src=\"http://code.jquery.com/jquery-latest.min.js\" type=\"text/javascript\"></script>" + 
//				"<script src=\"http://code.jquery.com/ui/1.9.0/jquery-ui.js\" type=\"text/javascript\"></script>" +
//				"<script src=\"d3-stuff.js\" type=\"text/javascript\"></script>" +
				"</head>" +
				
				"<body bgcolor=" + bgColor + ">";
		String PAGE_FOOTER = "</body></html>";

		writer.println(PAGE_HEADER);
		writer.println("<h1>Reaktor Schaltzentrale</h1>");
		writer.println(helloService.createHelloMessage("World"));

		List<Float> sensorsTemps = new ArrayList<Float>();
		
		try {
			sensorsTemps = helloService.getSensorsTemps();
		} catch (IndexOutOfBoundsException e) {
			sensorsTemps.add(0f);
			sensorsTemps.add(0f);
		}
		writer.println("<h2>Aktuelle Messwerte</h2>");
		writer.println("<p>Sensorwerte: " + helloService.getSensorsAverage() + "</p>");
		writer.println("<ul>" + "<li>S1 Celsius: " + sensorsTemps.get(0)
				+ "<li>S2 Celsius: " + sensorsTemps.get(1) + "</ul>");
		

		writer.println("<ul>" + "<li>comment: "
				+ helloService.getGlobalData().getComment() + "</li> "
				+ "<li>isRunning: "
				+ String.valueOf(helloService.getGlobalData().getIsRunning()) + "</li> " 
				+ "<li>secondsCompleted: "
				+ String.valueOf(helloService.getGlobalData().getSecondsCompleted()) + "</li> " 
				+ "<li>is heating: " + String.valueOf(helloService.getIsHeating()) + "</li>" +
				"<li>Aktuelle Zieltemperatur: " + String.valueOf(helloService.getCurrentZieltemp()) + "</li>" +
				"</ul>");

		writer.println(PAGE_FOOTER);
		writer.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
