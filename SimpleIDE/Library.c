/*
  Awesome Messages.c
*/

#include "simpletools.h"                      // Include simple tools

void awesome(void);                           // Forward declarations
void epic(void);

int main()                                    // main function
{
  print("Nick");                              // Print a name
  awesome();                                  // Append with "is awesome!\n"

  print("Jessica");                           // Print another name
  epic();                                     // Append with "is epic!\n"
}

void awesome(void)                            // awesome function
{
  print(" is awesome!\n");
}

void epic(void)                               // epic function
{
  print(" is epic!\n");
}