
// Healthcare case study global allocation model template
// ------------------------------------------------------
// This MDP model takes as input the number of robots, capabilities per robot, 
// and room number of every type, and can be used to
// solve the optimization problem via MDP policy synthesis
// Author: Javier Camara / 2019
//---------------------------------------------------------

mdp

#const NROBOTS# // NROBOTS: Number of robots
#const CAPABILITIES# // CAPABILITIES: Number of capabilities/robot (currently fixed)
#const NROOMTYPES# // Number of room types

const NROOMST1=2;
const NROOMST2=2;

const Max_cost=80;
const Max_time=80;

//-----------------------------------
// Rooms 
//-----------------------------------

module rooms
 time: [0..100] init 0;
 cost: [0..100] init 0;
 #for i=1:NROOMTYPES#
  allocatedt#i#: [0..NROOMST#i#] init NROOMST#i#;
 #end#

#for k=1:NROOMTYPES#
  #for i=1:NROBOTS#
   #for j=1:CAPABILITIES#
  [r#i#c#j#t#k#] (allocatedt#k#>0) & (cost+costr#i#c#j#t#k#<=Max_cost) & (time+timer#i#c#j#t#k#<=Max_time)  -> (allocatedt#k#'=allocatedt#k#-1) & (cost'=cost+costr#i#c#j#t#k#) & (time'=time+timer#i#c#j#t#k#);
   #end#
  #end#
#end#
endmodule

//-----------------------------------
// Robots 
//-----------------------------------

#for i=1:NROBOTS#

module r#i#
 #for j=1:CAPABILITIES#
   #for k=1:NROOMTYPES#
 [r#i#c#j#t#k#] true -> true;
   #end#
 #end#
endmodule
#end#



