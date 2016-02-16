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
    // Check QTIs
    int qtis = getQTIs(10,9);
    // QTI detections as binary val
    print("QTIs = %02b\n", qtis);             
    //turn off led 27
//    low(27);

    //check the QTI input & drive
    if (qtis==0b11){
//      print ("centre\n");
      drive_speed(24, 24);
      
    }
    else if (qtis==0b10){
//      print ("right\n");
      drive_speed(0, 16);
    }
    else if (qtis==0b01){
//      print ("left\n");
      drive_speed(16, 0);
    }
    else if (qtis==0b00){
 //     print ("do nothing\n");
       drive_speed(0,0);

    }      

    //Keep moving for 1/2 s
//    drive_speed(32, 32);
//    pause(50);
//    drive_speed(16, 16);
//    drive_ramp(0, 0);
//    high(27);
    // Pause after 1s
    pause(50); 
  }
}


int getQTIs(int highPin, int lowPin)           // Function - getQTIs
{
  int dt = (CLKFREQ / 1000000) * 230;          // Set up 230 us time increment
  set_outputs(highPin, lowPin, 0b11);        // highPin...lowPin set high
  set_directions(highPin, lowPin, 0b11);     // highPin...lowPin set to output
  waitcnt(dt + CNT);                           // Wait 230 us
  set_directions(highPin, lowPin, 0b00);     // highPin...lowPin st to input
  waitcnt(dt + CNT);                           // Wait 230 us
  int qtis = get_states(highPin, lowPin);      // Get 4-bit pattern QTIs apply 
  return qtis;                                 // Return val containing pattern
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