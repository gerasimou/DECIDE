/*
  Multicore Example.c
  
  Launch a function into another cog (processor) and display what it does
  to the global n variable over time. 
  
  http://learn.parallax.com/propeller-c-functions/multicore-example
*/

#include "simpletools.h"                      // Include simpletools
#include <queue>
//#include <mutex>


void adder(void *arg);                        // Forward declaration
void subtracter (void *arg);

std::queue<int> requestQueue;
static int t, n;                     // Global vars for cogs to share
unsigned int stackAdd[44 + 256];                 // Stack vars for other cog
//unsigned int stackSub[44 + 256];                 // Stack vars for other cog

int num = 0;

int main()                                    // main function
{
  t = 50;                                     // Set values of t & n
  n = 5000;
  int arg;
  
  print("main() = %d\n", requestQueue.size());                     // Display result

  // Launch adder function into another cog (processor).
  cogstart(adder, (void *) arg, stackAdd, sizeof(stackAdd));
  //cogstart(subtracter, (void *) arg, stackSub, sizeof(stackSub));

  // Watch what the other cog is doing to the value of n.
  while(1)
  {
    print("n = %d\n", requestQueue.size());                     // Display result
    if (num != n){
//      requestQueue.push(n);
      num = n;
    }      
    pause(50);                               // Wait 1/10 of a second    
  }    
}

// Function that can continue on its 
// own if launched into another cog.
void adder(void *arg){                         // Adder keeps going on its own
  while(1)                                    // Endless loop
  {
    n = n + 1;                                // n + 1 each time loop repeats
    num = n;
    pause(t);                                 // Wait for t ms between updates
  }                            
}

void subtracter (void *arg){
  while(1)                                    // Endless loop
  {
    n = n - 1;                                // n + 1 each time loop repeats
    pause(n/90);                                 // Wait for t ms between updates
  }                            
}