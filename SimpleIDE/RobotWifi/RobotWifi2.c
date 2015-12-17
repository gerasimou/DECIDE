/**/
#include "RobotWifi2.h"


int main()                                    // main function
{
  direction = '0';

  print("Main()\n");

      //xbee = fdserial_open(6,5,0,9600); // P6 connected to DO, P5 connected to DI


  // Launch adder function into another cog (processor).
  simpleterm_close();
  cogstart(readOneCharacter, NULL, readCharStack, sizeof(readCharStack));
  cogstart(drive,            NULL, driveStack,    sizeof(driveStack));
  cogstart(sendPing,         NULL, pingStack,     sizeof(pingStack));


  // Watch what the other cog is doing to the value of n.
//  while(1)
//  {
//    print("[%c]\n", direction);                     // Display result
//    pause(500);                               // Wait 1/10 of a second    
//    readOneCharacter();
//  }   

//    sendPing();
}



// Function that can continue on its 
// own if launched into another cog.
void drive(){
//  simpleterm_open();

  drive_trimSet(0, 0, 0);

  int speedMax = 128;  
  
  int speedInit = 20;

  int speedLCurrent = 0;
  int speedRCurrent = 0;
  
  int step = 10;
  

  while (1){
    
    char ch;
    
    while (direction !='0'){
      
      high(27);
     
     
     //if direction is the same --> increase speed
     if (ch == direction){
      if (ch==LEFT){
        speedLCurrent -= step;
        speedRCurrent += step;
      }  
      else if (ch==RIGHT){
        speedLCurrent += step;
        speedRCurrent -= step;
      }    
      else if (ch==UP){
        speedLCurrent += step*2;
        speedRCurrent += step*2;
      }
      else if (ch==DOWN){
        speedLCurrent -= step*2;
        speedRCurrent -= step*2;
      }
     }
     else{
       ch = direction;
       if (ch==LEFT){
          speedLCurrent = -speedInit;
          speedRCurrent = +speedInit;
        }  
        else if (ch==RIGHT){
          speedLCurrent = +speedInit;
          speedRCurrent = -speedInit;
        }    
        else if (ch==UP){
          speedLCurrent = speedInit*2;
          speedRCurrent = speedInit*2;
        }
        else if (ch==DOWN){
          speedLCurrent = -speedInit*2;
          speedRCurrent = -speedInit*2;
        }     
     }
     
     //check for max speeds
     if (speedLCurrent > speedMax)
        speedLCurrent = speedMax;
     else if (speedLCurrent < -speedMax)
         speedLCurrent = -speedMax;
     if (speedRCurrent > speedMax)
        speedRCurrent = speedMax;
     else if (speedRCurrent < -speedMax)
         speedRCurrent = -speedMax;
       
           
//      print("[%c,%d,%d]\n", ch, speedLCurrent, speedRCurrent);                     // Display result         
      
      drive_ramp(speedLCurrent, speedRCurrent);
//      pause(50);
      direction = '0';      
      low(27);
      pause(200);
    }
    drive_ramp(0, 0);
    
  }  
}  



//Reads one character at a time
void readOneCharacter(){
//  simpleterm_open();
//  print("readOneCharacter: \n");
     
   pause(1000);
//   if (xbee == NULL)
   xbee = fdserial_open(6,5,0,9600); // P6 connected to DO, P5 connected to DI

   int i=0;
   while (1){
//    print("[%c]:read()\n", direction);                     // Display result
    if (fdserial_rxReady(xbee)){
      char c = readChar(xbee);
      fdserial_rxFlush(xbee);
//      print("Data: %c", c);
      direction = c;
      
      //writeChar(xbee, i++);
      //fdserial_txFlush(xbee);

//      dprint(xbee, "You typed: %c [%d]\n", c,i++);
      
//      print("\t%d", i);
   }     
   pause (10); 
  }
}
  
  
  
void sendPing(){
    simpleterm_open();
     
    char test[5] = "Simos";

    print("sendPing(): \n");
    
    
    while (xbee==NULL){
      print ("xbee NULL\n");
      pause(1000);
      if (xbee!=NULL)
        break;
    }
    
    pause(5000);
             
    int counter=3;
    while (1){
      dprint(xbee, "Sending(%d): %s\n", counter, test);
//      fdserial_txFlush(xbee);
      print("sendPing(%d): %s\n", counter++, test);
      pause(1000);
    }      
}    