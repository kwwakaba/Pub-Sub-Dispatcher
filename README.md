# Pub-Sub-Dispatcher

 
 (Work in progress)
- Project needs:
 - Message parser - parse messages and determine if they're valid (handleGossipMsg)
 - GossipMsg object
 - Thread1 - for parsing messages and getting events from subscriber table
 - Thread2 - for listening for new accept requests and starting new thread1 for message processing
 - Thread3 - Timer for waking up and checking the LostBuffer
 - Thread4 - if Thread3 detects missing event send gossip messages and wait for response back.
