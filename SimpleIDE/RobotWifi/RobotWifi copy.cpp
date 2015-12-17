/**
* This is the main Blank Simple C++ Project program file.
*/

#include "RobotWifi.h"

static volatile int t, n;                     // Global vars for cogs to share
unsigned int stack[44 + 128];                 // Stack vars for other cog

void adder();                        // Forward declaration


int main(void){
  cogstart(adder, NULL, stack, sizeof(stack));

  char ch;
  xbee = fdserial_open(6,5,0,9600); // P6 connected to DO, P5 connected to DI

  // Launch drive function into another cog (processor).
  //cogstart(drive, NULL, stack, sizeof(stack));

//  print("Receiving: \n");
 // readOneCharacter();

  
  return 0;
}    


// Function that can continue on its 
// own if launched into another cog.
void adder()                         // Adder keeps going on its own
{
  while(1)                                    // Endless loop
  {
    n = n + 1;                                // n + 1 each time loop repeats
    pause(t);                                 // Wait for t ms between updates
  }                            
}

//Reads one character at a time
int readOneCharacter(){
   int i=0;
   while (1){
   if (fdserial_rxReady(xbee)){
      char c = readChar(xbee);
      print("Data: %c\n", c);
      direction = c;
      fdserial_rxFlush(xbee);
      writeChar(xbee, i++);
      fdserial_txFlush(xbee);
   }     
   pause (10); 
  } 
}



/*
void drive(){
      int speedL = 64;  
      int speedR = 64;  

  do{
    
//    while (!requestQueue.empty()){
    while (direction!='\0'){
      //get the first element
      char ch = direction;//requestQueue.front();
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
    }            
  }
  while(1);      
}


*/








//Read a string
/*
int readStringofChars(){
  char* str = malloc(sizeof(char)*30);
  int i=0;
  while (1){
    if (fdserial_rxReady(xbee)){
      while (fdserial_rxReady(xbee)){
        char c = readChar(xbee);
        print("Char: %c\n", c);
        str[i++] = c;
      }
      print("Data: %s\n", str);
      fdserial_rxFlush(xbee);
      writeChar(xbee, 's');
      fdserial_txFlush(xbee);
      memset(str, 0, sizeof(str));
      i=0;
    }         
    pause (10); 
  }   
}
*/

//Read a line
/*
int readString(){
  char* str = malloc(sizeof(char)*30);
  int i=0;
  while (1){
    if (fdserial_rxReady(xbee)){
      char* input = readStr(xbee, str, 20);
      print("readString(): %s\n", input);
      print("readString(): %s\n\n", str);
      fdserial_rxFlush(xbee);
      writeChar(xbee, 'S');
      fdserial_txFlush(xbee);
      memset(str, 0, sizeof(str));
    }
    pause (10); 
  }    
}
*/


/*
    if(fdserial_rxReady(xbee)) {
      ch = readChar(xbee);
      high(26);
      print("Received: [%c]\n", ch);
      writeChar(xbee,ch);
      putChar(ch);
      low(26);
    }
*/ 