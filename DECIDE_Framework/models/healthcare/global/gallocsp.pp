
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

const NROOMST1=4;
const NROOMST2=4;

const Max_cost=300;
const Max_time=300;

const double WR = 1;
const double Zr = 1;

formula done = #for k=1:NROOMTYPES# (allocatedt#k#=0) & #end# true;

formula NROOMS = #for k=1:NROOMTYPES# NROOMST#k# + #end# 0;

//-----------------------------------
// Rooms 
//-----------------------------------

module rooms
 time: [0..Max_time] init 0;
 cost: [0..Max_cost] init 0;
 end: bool init false;

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
  [] done -> (end'=true);
endmodule

//-----------------------------------
// Robots 
//-----------------------------------

#for i=1:NROBOTS#

module r#i#
 r#i#counter: [0..NROOMS] init 0;
 #for j=1:CAPABILITIES#
   #for k=1:NROOMTYPES#
 [r#i#c#j#t#k#] (r#i#counter<NROOMS) -> (r#i#counter'=r#i#counter+1);
   #end#
 #end#
endmodule
#end#

//---------------------------------------
// Global utility
//---------------------------------------

#for i=1:NROBOTS#
 formula diffs#i# = #for j=1:NROBOTS# (max(r#i#counter, r#j#counter) - min(r#i#counter, r#j#counter)) + #end# 0;
#end#

formula diffs = (NROOMS * NROOMS) - (#for i=1:NROBOTS# diffs#i# + #end# 0);

rewards "utility"
#for i=1:NROBOTS#
 #for j=1:CAPABILITIES#
    #for k=1:NROOMTYPES#
  [r#i#c#j#t#k#] true: Wr;
   #end#
  #end#
  done : Zr * diffs;
#end#
endrewards