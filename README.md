OpenNetworkMap
==============

Read-me
-------

OpenNetworkMap is a tool allowing to create topological maps from OpenStreetMap data.
The ultimate goal is to provide a simple to use program for final users, making easy
to create any kind of network map (public transport, energy, water, ...) and export them
in several formats (SVG, PNG, Web page, ...). This software is written in Java.

This project is still in progress, the core of the application (mostly) works, there is
no graphical user interface for the moment. You can give it a try by running the unit tests. 

External libraries
------------------

OpenNetworkMap uses the following libraries:
* BasicOSMParser - OpenStreetMap data parser - Adrien Pavie - https://github.com/PanierAvide/BasicOSMParser
* Batik - SVG Java toolkit - The Apache Foundation - https://xmlgraphics.apache.org/batik/
* JUnit - Unit testing framework - JUnit Team - http://junit.org/
* OSBCP CSS Parser - CSS parser - Christoffer Pettersson - https://github.com/corgrath/osbcp-css-parser
* SigmaJS - Graph drawing for web pages - Alexis Jacomy - http://sigmajs.org/

License
-------

Copyright 2014 Adrien PAVIE

See LICENSE for complete GPL3 license.

OpenNetworkMap is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

OpenNetworkMap is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with OpenNetworkMap. If not, see <http://www.gnu.org/licenses/>.