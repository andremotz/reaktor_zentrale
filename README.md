reaktor_zentrale
================

reaktor - brewing beer with Raspberry Pi and Arduino.

reaktor_zentrale is reaktor’s central human interface to control the whole brewing process. Current implementation is done in a jBoss-container running on a Raspberry Pi. reaktor_zentrale is receiving continous sensor-values and decides whether reaktor should heat its mash or not. Configuration and logging is been done via local MySQL-database so that the whole brewing process can be analyzed further.

reaktor_zentrale is a jBoss container designed to run on a Rasbperry Pi to control our brewing machine reaktor.
A Servlet is been used to to fetch sensor-values via serial port (USB).
The parameters of the current brewing program is been stored in a MySQL-database.
Each time a HttpRequest is been send, reaktorSessionService.circuitGo() evaluates current sensor-data to decide whether reaktor_schaltwerk should heat or not.

Checkout http://www.andremotz.com/reaktor/ for further details and also visit http://beerberry.at/ for our advanced beer-brewing project.
