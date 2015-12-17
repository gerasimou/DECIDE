/*
Scan to Int.c
*/

#include "simpletools.h"                         // Include simpletools library

int reps;                                        // Declare variable named reps

int main()                                       // Main function
{
  print("Enter reps: ");                         // User prompt to enter reps
  scan("%d\n", &reps);                           // Scan reps user types
  print("\nCounting to %d\n", reps);             // Display value scanned

  for(int n = 1; n <= reps; n++)                 // Count to value scanned
  {
    print("i = %d\n", n);                        // Display each step
  }

  print("\nDone!");                              // Display when done
}