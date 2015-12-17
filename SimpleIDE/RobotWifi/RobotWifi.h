
//#include <queue>
#include "simpletext.h"
#include "fdserial.h"
#include "simpletools.h"
#include "abdrive.h"

#define LEFT  'l'
#define RIGHT 'r'
#define UP    'u'
#define DOWN  'd'



/*Global Variable declaration*/
//////////////////////////////

  // Wifi full duplex communication
  fdserial *xbee;

  // Stack vars for other cog
  unsigned int driveStack[40 + 25];
    
  // Request queue
//  static volatile std::queue<char> requestQueue;
  static volatile char direction;              

/*Functions*/
//////////////////////////////

int readOneCharacter();

int readStringofChars();

int readString();

int drive(char c);
