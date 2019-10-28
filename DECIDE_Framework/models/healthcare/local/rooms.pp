// Rooms joint CTMC model template
// ------------------------------------------------------------

ctmc 

#const NRT1#
#const NRT2#

// Model parameters

const mf =100; // Used for calculation of state values in rooms
const GO=0; // Initial state

// Are we there yet?
label "end" = s=r#NRT1+NRT2#END;

// Rooms Type 1 -----------

// Begin Room Constants ---

#for i=1:NRT1#
const r#i#T1=#i#*mf+0; const r#i#T2=#i#*mf+1; const r#i#T2P=#i#*mf+2; const r#i#END=#i#*mf+3;
#end#

// End Room Constants ----


// Rooms Type 2 -----------

// Begin Room Constants ---

#for i=NRT1+1:NRT1+NRT2#
const r#i#T1=#i#*mf+0; const r#i#T2=#i#*mf+1; const r#i#T3=#i#*mf+2; const r#i#T3P=#i#*mf+3; const r#i#END=#i#*mf+4;
#end#

// End Room Constants ----

module rooms
  s:[GO..r#NRT1+NRT2#END] init GO;

// Travel
 [travel] (s=GO) -> 1/travel_time: (s'=r1T1);

// Rooms Type 1

#for i=1:NRT1#
  [r#i#T1done] (s=r#i#T1) -> rt1l1i*rt1p2norm: (s'=r#i#T2) + rt1l1i*(1-rt1p2norm): (s'=r#i#T2P);
  [r#i#T2done] (s=r#i#T2) -> rt1l2i*rt1p2retry: (s'=r#i#T2) + rt1l2i*(1-rt1p2retry): (s'=r#i+1#T1);
  [r#i#T2done] (s=r#i#T2P) -> rt1l2ip*rt1p2retry: (s'=r#i#T2P) + rt1l2ip*(1-rt1p2retry): (s'=r#i+1#T1);
#end#

// Rooms Type 2

#for i=NRT1+1:NRT1+NRT2-1#
[r#i#T1done] (s=r#i#T1) -> rt2l1i*(1-rt2p2req)*(1-rt2p3poss): (s'=r#i+1#T1) +
                     rt2l1i*(1-rt2p2req)*rt2p3poss*rt2p3ifull: (s'=r#i#T3) +
                     rt2l1i*rt2p2req: (s'=r#i#T2) +
                     rt2l1i*(1-rt2p2req)*rt2p3poss*rt2p3ibasic: (s'=r#i#T3P);

[r#i#T2done] (s=r#i#T2) -> rt2l2i*(1-rt2p2retry)*rt2p3poss*rt2p3ifull: (s'=r#i#T3) +
                     rt2l2i*(1-rt2p2retry)*(1-rt2p3poss): (s'=r#i+1#T1) +
                     rt2l2i*(1-rt2p2retry)*rt2p3poss*(1-rt2p3ifull): (s'=r#i#T3P) +
                     rt2l2i*rt2p2retry: (s'=r#i#T2);  

[r#i#T3done] (s=r#i#T3) -> rt2l3i: (s'=r#i+1#T1);

[r#i#T3Pdone] (s=r#i#T3P) -> rt2l3ip: (s'=r#i+1#T1);
#end#

#for i=NRT1+NRT2:NRT1+NRT2#
[r#i#T1done] (s=r#i#T1) -> rt2l1i*(1-rt2p2req)*(1-rt2p3poss): (s'=r#i#END) +
                     rt2l1i*(1-rt2p2req)*rt2p3poss*rt2p3ifull: (s'=r#i#T3) +
                     rt2l1i*rt2p2req: (s'=r#i#T2) +
                     rt2l1i*(1-rt2p2req)*rt2p3poss*rt2p3ibasic: (s'=r#i#T3P);

[r#i#T2done] (s=r#i#T2) -> rt2l2i*(1-rt2p2retry)*rt2p3poss*rt2p3ifull: (s'=r#i#T3) +
                     rt2l2i*(1-rt2p2retry)*(1-rt2p3poss): (s'=r#i#END) +
                     rt2l2i*(1-rt2p2retry)*rt2p3poss*(1-rt2p3ifull): (s'=r#i#T3P) +
                     rt2l2i*rt2p2retry: (s'=r#i#T2);  

[r#i#T3done] (s=r#i#T3) -> rt2l3i: (s'=r#i#END);

[r#i#T3Pdone] (s=r#i#T3P) -> rt2l3ip: (s'=r#i#END);
#end#



endmodule

// Cost reward
rewards "c"
#for i=1:NRT1#
  [r#i#T1done] true: rt1c1i;
  [r#i#T2done] true: rt1c2i;
 #end#
#for i=NRT1+1:NRT1+NRT2#
  [r#i#T1done] true: rt2c1i;
  [r#i#T2done] true: rt2c2i;
  [r#i#T3done] true: rt2c3i;
  [r#i#T3Pdone] true: rt2c3ip;
#end#

endrewards

// Utility reward
rewards "u"
 #for i=NRT1+1:NRT1+NRT2#
  [r#i#T3done] true: rt2u3;
  [r#i#T3Pdone] true: rt2u3p;
 #end#
endrewards

// Time reward
rewards "t"
   true : 1;
endrewards


// Constants to append

//const double t;
//const double travel_time;

// Rooms Type 1 -----------
// const double rt1l1i; const double rt1l2i; const double rt1l2ip;
// const double rt1p2norm; const double rt1p2retry;
// const double rt1c1i; const double rt1c2i;

// Rooms Type 2 -----------
// const double rt2l1i; const double rt2l2i; const double rt2l3i; const double rt2l3ip;
// const double rt2p2req; const double rt2p2retry; 
// const double rt2p3poss; const double rt2p3ibasic; const double rt2p3ifull; 
// const double rt2c1i; const double rt2c2i; const double rt2c3i; const double rt2c3ip;
// const double rt2u3; const double rt2u3p;
