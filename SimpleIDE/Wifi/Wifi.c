/**
 * This is the main xbee-terminal program file.
 */
#include "simpletext.h"
#include "fdserial.h"

int main(void)
{
  extern text_t *dport_ptr; // default debug port pointer gets reassigned to fdserial.
  char ch;
  fdserial *xbee = fdserial_open(13,12,0,9600); // P13 connected to DO, P12 connected to DI
  simpleterm_close();
  dport_ptr = (fdserial_open(31,30,0,115200));

  putLine("Starting terminal");

  while(1) {
    if(fdserial_rxReady(xbee)) {
      ch = readChar(xbee);
      writeChar(xbee,ch);
      putChar(ch);
    }
    if(fdserial_rxReady(dport_ptr)) {
      ch = getChar();
      putChar(ch);
      writeChar(xbee, ch);
    }
  }
  return 0;
}