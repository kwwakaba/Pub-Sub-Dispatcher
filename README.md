Coen 317 - Winter 2019
Final Project - Pub-Sub-Dispatcher
=========================================================
Team Members:

Vineet Joshi    W1170181    vjoshi@scu.edu
Carlos Rivera   W0756026    crivera@scu.edu
Ken Wakaba      W1096003    kwakaba@scu.edu
Julie Wasiuk    W1174936    jwasiuk@scu.edu
=========================================================
Set-up:
1. For running this system, you will need three different machines.
2. Clone the repo to your choice directory
3. Get the ipAddress of each machine. "ifconfig | grep inet" will give you your machines ipAddress
4. In Dispatcher.java, update the neighbor table with each of the ipAdress's aquired as well as the port assisgned to it.
5. In the main() method, make sure to change the "ID" and "Port" of each Dispatcher object created to the current machine
6. In the root director (Pub-Sub-Dispatcher), run "javac @sources.txt" to compile the code
7. Steps 2 through 6 should be done on each machine you wish to run
8. To run the code on each machine, run "java src/Dispatcher"
