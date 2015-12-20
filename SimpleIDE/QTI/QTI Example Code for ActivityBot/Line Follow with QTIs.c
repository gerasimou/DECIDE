/* 
  Line Follow with QTIs.c
*/

#include "simpletools.h"                       // Include libraries
#include "abdrive.h"

int getQTIs(int highPin, int lowPin);          // Forward declare function

int main()                                     // Main function
{
  while(1)                                     // Main loop
  {
    int qtis = getQTIs(10, 9);
    print("QTIs = %02b \n", qtis);             // QTI detections as binary val
                  // Check stripe position
    if(qtis == 0b1000) drive_speed(-64, 64);   // Far left, rotate left
    if(qtis == 0b1100) drive_speed(0, 64);     // Left, pivot left
    if(qtis == 0b0100) drive_speed(32, 64);    // A little left, curve left
    if(qtis == 0b0110) drive_speed(64, 64);    // Stripe centered, forward
    if(qtis == 0b0010) drive_speed(64, 32);    // A little right, curve right
    if(qtis == 0b0011) drive_speed(64, 0);     // Right, pivot right
    if(qtis == 0b0001) drive_speed(64, -64);   // Far right, rotate right
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