/**/

#include "simpletext.h"
#include "fdserial.h"
#include "simpletools.h"
#include "abdrive.h"

#include "RobotWifiC.h"


int main()                                    // main function
{
  direction = '0';

  print("Main()\n");

  // Launch adder function into another cog (processor).
 // simpleterm_close();
  //cogstart(readOneCharacter, NULL, readCharStack, sizeof(readCharStack));
  //cogstart(drive,            NULL, driveStack,    sizeof(driveStack));


  // Watch what the other cog is doing to the value of n.
  //while(1)
  //{
    print("[%c]\n", direction);                     // Display result
    //pause(500);                               // Wait 1/10 of a second    
    readOneCharacter();
    //drive();
//  }    
}



// Function that can continue on its 
// own if launched into another cog.
void drive(){
//  simpleterm_open();

  int speedL = 64;  
  int speedR = 64;  


//  while (1){
    
    while (direction !='0'){
      
      high(27);
     
      char ch = direction;
      
      print("1[%c]\n", ch);                     // Display result
  
     
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
      print("2[%c]\n", ch);                     // Display result
      
      drive_trimSet(0, 0, 0);
      print("3[%c]\n", ch);                     // Display result
      drive_ramp(speedL, speedR);
      print("4[%c]\n", ch);                     // Display result
      pause(200);
      print("5[%c]\n", ch);                     // Display result
      drive_ramp(0, 0);
      print("6[%c]\n", ch);                     // Display result
      direction = '0';
      print("7[%c]\n", ch);                     // Display result
      
      low(27);
      pause(100);
    }    
 // }  
}  



//Reads one character at a time
void readOneCharacter(){
//  simpleterm_open();
  print("readOneCharacter: \n");
     
   xbee = fdserial_open(6,5,0,9600); // P6 connected to DO, P5 connected to DI
   int i=0;
   while (1){
//    print("[%c]:read()\n", direction);                     // Display result
    if (fdserial_rxReady(xbee)){
      char c = readChar(xbee);
      fdserial_rxFlush(xbee);
      print("Data: %c\n", c);
      direction = c;
      drive();
//      writeChar(xbee, i++);
//      fdserial_txFlush(xbee);
      print("Data: %c\n", 'A');
   }     
   pause (10); 
  } 
}