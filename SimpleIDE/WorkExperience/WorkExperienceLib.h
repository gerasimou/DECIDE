
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
static volatile fdserial *xbee;

// Stack vars for other cog
unsigned int driveStack[40 + 128];

unsigned int readCharStack[40 + 128];

unsigned int pingStack[40 + 128];



/*Functions*/
//////////////////////////////

void drive();

void sendPing();

void setup();

void readFromPayload();

void readPosition();