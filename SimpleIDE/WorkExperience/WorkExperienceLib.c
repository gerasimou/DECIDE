
#include "WorkExperienceLib.h"



void setup(){
    //Open the xbee module
    xbee = fdserial_open(6,5,0,9600); // P6 connected to DO, P5 connected to DI
    
    // Launch functions into another cog (processor).
    cogstart(readFromPayload, NULL, readCharStack, sizeof(readCharStack));
    cogstart(sendPing,        NULL, pingStack,     sizeof(pingStack));

}  


void readFromPayload(){
  char buffer[10];
  char* input;
  
//  simpleterm_open();
  
  if (xbee == NULL){
    //Open the xbee module
    xbee = fdserial_open(6,5,0,9600); // P6 connected to DO, P5 connected to DI
  }    
  
  
  print("Waiting\n");
  while(1)
  {
    if (fdserial_rxReady(xbee)){
      break;
      input = readStr(xbee, buffer, 10);
      //empty the buffer
      fdserial_rxFlush(xbee);

      print("Received: %s\n", buffer);
//      dprint(xbee, "Robot Sending: %s\n", buffer);
    }
 
   pause(10);
  }
}  


/**
 * @brief Send a ping (heartbeat) to the server
 */
void sendPing(){
    char msg[6] = "ping";
    int counter = 0;
    while (xbee==NULL);

    while (1){
      dprint(xbee, "Robot: %s", msg);
      fdserial_txFlush(xbee);
//      writeLine(xbee, msg);
      print("sendPing(%d): %s\n", counter++, msg);
      pause(1000);
    }
}


void readPosition(){
  
}  