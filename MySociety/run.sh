#!/bin/sh
cd webapp
echo Starting server on 9090 port, press any key to continue
pause
mvn jetty:run -Djetty.port=9090 -s ../settings.xml

