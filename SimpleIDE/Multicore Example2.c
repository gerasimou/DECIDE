/**/

#include "simpletext.h"
#include "fdserial.h"
#include "simpletools.h"
#include "abdrive.h"
#include "Multicore Example2.h"


int main()                                    // main function
{
  direction = '0';

  // Launch adder function into another cog (processor).
  print("main[%c]\n", direction);                     // Display result
  simpleterm_close();
  cogstart(readOneCharacter, NULL, readCharStack, sizeof(readCharStack));
//  print("B[%c]\n", direction);                     // Display result
  cogstart(drive,            NULL, driveStack,    sizeof(driveStack));
//  print("C[%c]\n", direction);                     // Display result


  // Watch what the other cog is doing to the value of n.
//  while(1)
//  {
//    print("[%c]\n", direction);                     // Display result
//    pause(500);                               // Wait 1/10 of a second    
//    readOneCharacter();
//  }    
}



// Function that can continue on its 
// own if launched into another cog.
void drive(){
  int speedL = 64;  
  int speedR = 64;  
//  simpleterm_open();

  do{
//    while (!requestQueue.empty()){
    while (direction!='0'){
      //get the first element
      char ch = direction;//requestQueue.front();
  //    print("Ch:%c\n",ch);
      //remove the first element
//      requestQueue.pop();
      
      high(27);
    
      if (ch==LEFT){
        speedL = -32;
        speedR = 32;
      }  
      else if (ch==RIGHT){
        speedL = 32;
        speedR = -32;
      }    
      else if (ch==UP){
        speedL = 64;
        speedR = 64;
      }
      else if (ch==DOWN){
        speedL = -64;
        speedR = -64;
      }    
      drive_trimSet(0, 0, 0);
      drive_speed(speedL, speedR);
      pause(500);
      drive_speed(0, 0);
      low(27);
      direction='0';
    }            
  }
  while(1);      
}


//Reads one character at a time
void readOneCharacter(){
  simpleterm_open();
  print("readOneCharacter: \n");

   xbee = fdserial_open(6,5,0,9600); // P6 connected to DO, P5 connected to DI
   int i=0;
   while (1){
   if (fdserial_rxReady(xbee)){
      char c = readChar(xbee);
      print("Data: %c\n", c);
      direction = c;
      fdserial_rxFlush(xbee);
//      writeChar(xbee, i++);
//      fdserial_txFlush(xbee);
   }     
   pause (10); 
  } 
}