/* 
  Test QTIs.c
*/

#include "simpletools.h"                       // Include libraries
#include "abdrive.h"

int getQTIs(int highPin, int lowPin);          // Forward declare function

int main()                                     // Main function
{
  while(1)                                     // Main loop
  {
    int qtis = getQTIs(10, 4);                  // Check QTIs
    print("%c       FMMF \n", HOME);           // Home, display (F)ar, (M)iddle
    print("       LLRR \n");                   // Display (L)eft, (R)ight 
    print("QTIs = %04b \n", qtis);             // QTI detections as binary val
    pause(200);                                // Repeat after 1/5 s
  }
}

int getQTIs(int highPin, int lowPin)           // Function - getQTIs
{
  int dt = (CLKFREQ / 1000000) * 230;          // Set up 230 us time increment
  set_outputs(highPin, lowPin, 0b1111);        // highPin...lowPin set high
  set_directions(highPin, lowPin, 0b1111);     // highPin...lowPin set to output
  waitcnt(dt + CNT);                           // Wait 230 us
  set_directions(highPin, lowPin, 0b0000);     // highPin...lowPin st to input
  waitcnt(dt + CNT);                           // Wait 230 us
  int qtis = get_states(highPin, lowPin);      // Get 4-bit pattern QTIs apply 
  return qtis;                                 // Return val containing pattern
}