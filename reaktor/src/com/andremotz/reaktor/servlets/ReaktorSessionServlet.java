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
@WebServlet("/")
public class ReaktorSessionServlet extends HttpServlet {
	
	ReaktorSessionService reaktorSessionService;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReaktorSessionServlet() {
        super();
        // TODO Auto-generated constructor stub
        reaktorSessionService = new ReaktorSessionService();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		reaktorSessionService.readSerialFromArduino();
		reaktorSessionService.circuitGo();
		
		String bgColor = "333333";
		if (reaktorSessionService.getSensorsTemps().get(0) == 0) {
			bgColor = "FF0000";
		} else {
			bgColor = "FFFFFF";
		}
		
		String PAGE_HEADER = "<html><head>" +
				"<meta charset='utf-8'> " +
				"<style>html, body { background:#4d4d4d; }"
				+ "body{ color:#ccc; margin:10; padding:0; "
				+ "font-family: 'Andale Mono', AndaleMono, monospace;}</style>" + 
				"<meta http-equiv='refresh' content='" + String.valueOf(reaktorSessionService.getGlobalTakt()) + "'>" +
				
				"<body bgcolor=" + bgColor + ">";
		String PAGE_FOOTER = "</body></html>";

		writer.println(PAGE_HEADER);
		writer.println("<h1>Reaktor Schaltzentrale</h1>");
		writer.println("Build date: 2014/08/04");

		List<Float> sensorsTemps = new ArrayList<Float>();
		
		try {
			sensorsTemps = reaktorSessionService.getSensorsTemps();
		} catch (IndexOutOfBoundsException e) {
			sensorsTemps.add(0f);
			sensorsTemps.add(0f);
		}
		writer.println("<h2>Aktuelle Messwerte</h2>");
		writer.println("<p>Sensorwerte: " + reaktorSessionService.getSensorsAverage() + "</p>");
		writer.println("<ul>" + "<li>S1 Celsius: " + sensorsTemps.get(0)
				+ "<li>S2 Celsius: " + sensorsTemps.get(1) + "</ul>");
		

		writer.println("<ul>"

				+ "<li>isRunning: "
				+ String.valueOf(reaktorSessionService.getIsRunning()) + "</li> " 
				+ "<li>secondsCompleted: "
				+ String.valueOf(reaktorSessionService.getSecondsCompleted()) + "</li> " 
				+ "<li>is heating: " + String.valueOf(reaktorSessionService.getIsHeating()) + "</li>" +
				"<li>Aktuelle Zieltemperatur: " + String.valueOf(reaktorSessionService.getCurrentZieltemp()) + "</li>" +
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
