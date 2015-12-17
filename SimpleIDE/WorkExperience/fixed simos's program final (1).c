/*
  Blank Simple Project.c
  http://learn.parallax.com/propeller-c-tutorials 
*/

#include "simpletext.h"
#include "simpletools.h"
#include "abdrive.h"

#define rectangleX 400
#define rectangleY 300

#define x1 0 
#define y1 0
#define x2 400
#define y2 150


int findSegment(int x, int y){
  int segment = -1;
  
  if (x==0 && y>=0 && y<rectangleY){
    segment = 1;
  }
  else if (x>=0 && x<rectangleX && y==rectangleY){
    segment = 2;
  }      
  else if (x==rectangleX && y>0 && y<=rectangleY){
    segment = 3;
  }
  else if (x>0 && x<=rectangleX && y==0){
    segment = 4;
  }
  return segment;
}  


void doSegment(int distance){
  print("Moving\t%d\n", distance);
  int ticks = distance/3.25;
  drive_goto(ticks, ticks);
}  


void turn90Right(){
  drive_goto(26, -25);
}  

void turn90Left(){
  drive_goto(-25, 26);
}

void turn180(){
  drive_goto(51, -51);
}  


void moveToPositionCW(int startX, int startY, int endX, int endY){
  //find segments
  int startSegment = findSegment(startX, startY);
  int endSegment   = findSegment(endX, endY);
  
  print("%d\t%d\n", startSegment, endSegment);
 
  if (startSegment != endSegment){
 
    //move to corner of start segment
    if (startSegment == 1 && startY!=0){
      print("adjust 1\n");
      doSegment(rectangleY-startY);
      startSegment++;
      turn90Right();
    }
    else if (startSegment ==2 && startX!=0){
      print("adjust 2\n");
      doSegment(rectangleX-startX);
      startSegment++;
      turn90Right();
    }    
    else if (startSegment==3 && startY!=rectangleY){
      print("adjust 3\n");
      doSegment(startY);
      startSegment++;
      turn90Right();
    } 
    else if (startSegment==4 && startX!=rectangleX){
      print("adjust 4\n");
      doSegment(startX);
      startSegment = 1;
      turn90Right();
    }      
    
    //move to correct segment
    while (startSegment != endSegment){
      if (startSegment == 1 || startSegment == 3){
        print("Segment 1 or 3\n");
        doSegment(rectangleY);
        startSegment++;
      }
      else if (startSegment == 2){
        print("Segment 2\n");
        doSegment(rectangleX);
        startSegment++;
      }
      else if (startSegment == 4){
        print("Segment 4\n");
        doSegment(rectangleX);
        startSegment = 1;
      }
      
      if (startSegment != endSegment){
        turn90Right();
      }          
    }
  }    
  
  //move to correct position
  if (endSegment == 1){
    if (endY!=0){
      turn90Right();
    }      
    doSegment(endY);
  }
  else if (endSegment ==2){
    if (endX!=0){
      turn90Right();
    }      
    doSegment(endX);
  }    
  else if (endSegment == 3){
    if (rectangleY-endY !=0){
      turn90Right();
    }      
    doSegment(rectangleY-endY);
  } 
  else if (endSegment ==4){
    if (rectangleX-endX !=0){
           turn90Right(); 
    }      
    doSegment(rectangleX-endX);
  }    
}


void moveToPositionAntiCW(int startX, int startY, int endX, int endY){
  //find segments
  int startSegment = findSegment(startX, startY);
  int endSegment   = findSegment(endX, endY);
  
  print("%d\t%d\n", startSegment, endSegment);
 
  if (startSegment != endSegment){
 
    //move to corner of start segment
    if (startSegment == 1 && startY!=0){
      doSegment(startY);
      startSegment = 4;
      turn90Left();
    }
    else if (startSegment ==2){
      doSegment(startX);
      startSegment--;
      if (startX != 0){
         turn90Left();
      }      
    }    
    else if (startSegment==3){
      doSegment(rectangleY-startY);
      startSegment--;
      if (rectangleY-startY != 0){
        turn90Left();
      }        
    } 
    else if (startSegment==4){
      doSegment(rectangleX-startX);
      startSegment--;
      if (rectangleX-startX != 0){
        turn90Left();
      }        
    }      
    
    //move to correct segment
    while (startSegment != endSegment){
      if (startSegment == 1) {
        print("Segment 1L \n");
        doSegment(rectangleY);
        startSegment = 4;
      }
      else if (startSegment == 2 || startSegment == 4){
        print("Segment 2L or 4L\n");
        doSegment(rectangleX);
        startSegment--;
      }
      else if (startSegment == 3){
        print("Segment 3L \n");
        doSegment(rectangleY);
        startSegment--;
      }
      if (startSegment != endSegment){
        turn90Left();
      }        
    }
  }    
  
  //move to correct position
  if (endSegment == 1){
    if (rectangleY-endY !=0){
      turn90Left(); 
    }      
    doSegment(rectangleY-endY);
  }
  else if (endSegment ==2){
    if (rectangleX-endX !=0){
      turn90Left(); 
    }      
    doSegment(rectangleX-endX);
  }    
  else if (endSegment == 3){
    if (endY !=0){
      turn90Left(); 
    }    
    doSegment(endY);
  } 
  else if (endSegment ==4){
    if (endY !=0){
      turn90Left(); 
    }    
    doSegment(endX);
  }    
}


int main()                                    // main function
{
    print("Main()\n");
    
      while (1){
        moveToPositionCW(x1, y1, x2, y2);
        turn180();
        moveToPositionAntiCW(x2, y2, x1, y1);
        turn180();
        pause(3000);
     } 
}