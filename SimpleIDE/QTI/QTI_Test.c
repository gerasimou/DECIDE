// Include libraries
#include "simpletools.h"                       
#include "abdrive.h"


// Function - getQTI
int getQTI(int pin);


// Function main
int main()                                     // Main function
{
  while(1)                                     // Main loop
  {
    int qtis = getQTIs(10,9);//getQTI(10);                  // Check QTIs
   // int qtis = getQTI(9);                  // Check QTIs
    //print("%c       FMMF \n", HOME);           // Home, display (F)ar, (M)iddle
    //print("       LLRR \n");                   // Display (L)eft, (R)ight 
    print("QTIs = %02b\n", qtis);             // QTI detections as binary val
    high(27);
    
    if (qtis==0b11){
      print ("centre");
      drive_speed(32, 32);
    }      
    else if (qtis==0b10){
      print ("right");
      drive_speed(-16, 16);
    }      
    else if (qtis==0b01){
      print ("left");
      drive_speed(16, -16);
    }      
    
    pause(100);                                // Repeat after 1/5 s
    low(27);
    pause(100);                                // Repeat after 1/5 s
  }
}


// Function - getQTIs
int getQTI(int pin){
  int dt = (CLKFREQ / 1000000) * 230;     // Set up 230 us time increment
  set_output(pin, 0b11);                // highPin...lowPin set high
  set_direction(pin, 0b11);             // highPin...lowPin set to output
  waitcnt(dt + CNT);                      // Wait 230 us
  set_direction(pin, 0b00);             // highPin...lowPin st to input
  waitcnt(dt + CNT);                      // Wait 230 us
  int qti = get_state(pin);               // Get 4-bit pattern QTIs apply 
  return qti;                             // Return val containing pattern
}  


int getQTIs (int highPin, int lowPin){
  int dt = (CLKFREQ / 1000000) * 230;          // Set up 230 us time increment
  set_outputs(highPin, lowPin, 0b1111);        // highPin...lowPin set high
  set_directions(highPin, lowPin, 0b1111);     // highPin...lowPin set to output
  waitcnt(dt + CNT);                           // Wait 230 us
  set_directions(highPin, lowPin, 0b0000);     // highPin...lowPin st to input
  waitcnt(dt + CNT);                           // Wait 230 us
  int qtis = get_states(highPin, lowPin);      // Get 4-bit pattern QTIs apply 
  return qtis;                                 // Return val containing pattern
}