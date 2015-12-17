
//#include <queue>


#define LEFT  'l'
#define RIGHT 'r'
#define UP    'u'
#define DOWN  'd'



/*Global Variable declaration*/
//////////////////////////////

  // Wifi full duplex communication
//  static volatile fdserial *xbee;
  fdserial *xbee;

  // Request queue
//  static volatile std::queue<char> requestQueue;
    static volatile char direction;     

  // Stack vars for other cog
  unsigned int driveStack[40 + 128];

  unsigned int readCharStack[40 + 128];
    


/*Functions*/
//////////////////////////////

void readOneCharacter();

void drive();
