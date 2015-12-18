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
    //print("%c       FMMF \n", HOME);           // Home, display (F)ar, (M)iddle
    //print("       LLRR \n");                   // Display (L)eft, (R)ight
    // QTI detections as binary val
    print("QTIs = %02b\t", qtis);             
    //turn on led 27
    low(27);

    //check the QTI input & drive
    if (qtis==0b11){
      print ("centre\n");
//      drive_speed(32, 32);
    }
    else if (qtis==0b10){
      print ("right\n");
  //    drive_speed(-16, 16);
    }
    else if (qtis==0b01){
      print ("left\n");
//      drive_speed(16, -16);
    }
    //Keep moving for 1/2 s
    drive_speed(32, 32);
    pause(1000);                                
    drive_speed(0, 0);
//    drive_ramp(0, 0);
    high(27);
    // Pause after 1s
    pause(2000); 
  }
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