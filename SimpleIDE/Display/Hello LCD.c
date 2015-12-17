/*
  Hello LCD.c

  Under development; Version 0.94 for use with SimpleIDE 9.40 and its Simple Libraries

  http://learn.parallax.com/propeller-c-simple-protocols/half-duplex-serial
*/

#include "simpletools.h"

const int ON  = 22;
const int CLR = 12;

int main()
{
  
  while (1){
    high(1);
    pause(1000);
    low(1);
    pause(1000);
  }  
  /*
  serial *lcd = serial_open(1, 1, 0, 9600);
  
  writeChar(lcd, ON);
  writeChar(lcd, CLR);
  pause(5);
  dprint(lcd, "Hello LCD!!!");
  */
  print("End");
}

