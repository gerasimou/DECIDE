/**
 * This is the main xbee-terminal program file.
 */
#include "simpletext.h"
#include "fdserial.h"
#include "simpletools.h"
#include "abdrive.h"


#define LEFT 'l'
#define RIGHT 'r'
#define UP    'u'
#define DOWN  'd'



int readOneCharacter();
int readStringofChars();
int readString();
int drive(char c);

fdserial *xbee;

int main(void)
{
  extern text_t *dport_ptr; // default debug port pointer gets reassigned to fdserial.
  char ch;
  xbee = fdserial_open(6,5,0,9600); // P6 connected to DO, P5 connected to DI

//  simpleterm_close();  
//  dport_ptr = (fdserial_open(31,30,0,115200));
//  low (26); low (27);

  print("Receiving: \n");
  readOneCharacter();
//  readStringofChars();
//    readString();  
  
  return 0;
}    



//Reads one character at a time
int readOneCharacter(){
   int i=0;
   while (1){
   if (fdserial_rxReady(xbee)){
      char c = readChar(xbee);
      print("Data: %c\n", c);
      drive(c);
      fdserial_rxFlush(xbee);
      writeChar(xbee, i++);
      fdserial_txFlush(xbee);
   }     
   pause (10); 
  } 
}




int drive(char ch){
  high(27);
  int speedL = 64;  
  int speedR = 64;  

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