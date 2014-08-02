reaktor_zentrale
================

reaktor - brewing beer with Raspberry Pi and Arduino.

reaktor_zentrale is reaktor’s central human interface to control the whole brewing process. Current implementation is done in a jBoss-container running on a Raspberry Pi. reaktor_zentrale is receiving continous sensor-values and decides whether reaktor should heat its mash or not. Configuration and logging is been done via local MySQL-database so that the whole brewing process can be analyzed further.
