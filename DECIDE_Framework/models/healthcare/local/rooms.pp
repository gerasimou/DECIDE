// Rooms joint CTMC model template
// ------------------------------------------------------------

ctmc 

#const NRT1#
#const NRT2#

// Model parameters

//const double t; // Time threshold constant for time-related PCTL property
const mf =100; // Used for calculation of state values in rooms
const GO=0; // Initial state
const travel_time;

// Are we there yet?
#for i=NRT1+NRT2:NRT1+NRT2#
label "end" = s=r#i#END;
#end#

// Rooms Type 1 -----------

#for i=1:NRT1#
// Begin Room Constants ---
const double r#i#l1i; const double r#i#l2i; const double r#i#l2ip;
const double r#i#p2norm; const double r#i#p2retry;
const double r#i#c1i; const double r#i#c2i;

const r#i#T1=#i#*mf+0; const r#i#T2=#i#*mf+1; const r#i#T2P=#i#*mf+2; const r#i#END=#i#*mf+3;
// End Room Constants ----

#end#

// Rooms Type 2 -----------

#for i=NRT1+1:NRT1+NRT2#
// Begin Room Constants ---

const double r#i#l1i; const double r#i#l2i; const double r#i#l3i; const double r#i#l3ip;
const double r#i#p2req; const double r#i#p2retry; 
const double r#i#p3poss; const double r#i#p3ibasic; const double r#i#p3ifull; 
const double r#i#c1i; const double r#i#c2i; const double r#i#c3i; const double r#i#c3ip;
const double r#i#u3; const double r#i#u3p;

const r#i#T1=#i#*mf+0; const r#i#T2=#i#*mf+1; const r#i#T3=#i#*mf+2; const r#i#T3P=#i#*mf+3; const r#i#END=#i#*mf+4;
// End Room Constants ----
#end#

module rooms
  s:[GO..r#NRT1+NRT2#END] init GO;

// Travel
 [travel] (s=GO) -> 1/travel_time: (s'=r1T1);

// Rooms Type 1

#for i=1:NRT1#
  [r#i#T1done] (s=r#i#T1) -> r#i#l1i*r#i#p2norm: (s'=r#i#T2) + r#i#l1i*(1-r#i#p2norm): (s'=r#i#T2P);
  [r#i#T2done] (s=r#i#T2) -> r#i#l2i*r#i#p2retry: (s'=r#i#T2) + r#i#l2i*(1-r#i#p2retry): (s'=r#i+1#T1);
  [r#i#T2done] (s=r#i#T2P) -> r#i#l2ip*r#i#p2retry: (s'=r#i#T2P) + r#i#l2ip*(1-r#i#p2retry): (s'=r#i+1#T1);
#end#

// Rooms Type 2

#for i=NRT1+1:NRT1+NRT2-1#
[r#i#T1done] (s=r#i#T1) -> r#i#l1i*(1-r#i#p2req)*(1-r#i#p3poss): (s'=r#i+1#T1) +
                     r#i#l1i*(1-r#i#p2req)*r#i#p3poss*r#i#p3ifull: (s'=r#i#T3) +
                     r#i#l1i*r#i#p2req: (s'=r#i#T2) +
                     r#i#l1i*(1-r#i#p2req)*r#i#p3poss*r#i#p3ibasic: (s'=r#i#T3P);

[r#i#T2done] (s=r#i#T2) -> r#i#l2i*(1-r#i#p2retry)*r#i#p3poss*r#i#p3ifull: (s'=r#i#T3) +
                     r#i#l2i*(1-r#i#p2retry)*(1-r#i#p3poss): (s'=r#i+1#T1) +
                     r#i#l2i*(1-r#i#p2retry)*r#i#p3poss*(1-r#i#p3ifull): (s'=r#i#T3P) +
                     r#i#l2i*r#i#p2retry: (s'=r#i#T2);  

[r#i#T3done] (s=r#i#T3) -> r#i#l3i: (s'=r#i+1#T1);

[r#i#T3Pdone] (s=r#i#T3P) -> r#i#l3ip: (s'=r#i+1#T1);
#end#

#for i=NRT1+NRT2:NRT1+NRT2#
[r#i#T1done] (s=r#i#T1) -> r#i#l1i*(1-r#i#p2req)*(1-r#i#p3poss): (s'=r#i#END) +
                     r#i#l1i*(1-r#i#p2req)*r#i#p3poss*r#i#p3ifull: (s'=r#i#T3) +
                     r#i#l1i*r#i#p2req: (s'=r#i#T2) +
                     r#i#l1i*(1-r#i#p2req)*r#i#p3poss*r#i#p3ibasic: (s'=r#i#T3P);

[r#i#T2done] (s=r#i#T2) -> r#i#l2i*(1-r#i#p2retry)*r#i#p3poss*r#i#p3ifull: (s'=r#i#T3) +
                     r#i#l2i*(1-r#i#p2retry)*(1-r#i#p3poss): (s'=r#i#END) +
                     r#i#l2i*(1-r#i#p2retry)*r#i#p3poss*(1-r#i#p3ifull): (s'=r#i#T3P) +
                     r#i#l2i*r#i#p2retry: (s'=r#i#T2);  

[r#i#T3done] (s=r#i#T3) -> r#i#l3i: (s'=r#i#END);

[r#i#T3Pdone] (s=r#i#T3P) -> r#i#l3ip: (s'=r#i#END);
#end#



endmodule

// Cost reward
rewards "c"
#for i=1:NRT1#
  [r#i#T1done] true: r#i#c1i;
  [r#i#T2done] true: r#i#c2i;
 #end#
#for i=NRT1+1:NRT1+NRT2#
  [r#i#T1done] true: r#i#c1i;
  [r#i#T2done] true: r#i#c2i;
  [r#i#T3done] true: r#i#c3i;
  [r#i#T3Pdone] true: r#i#c3ip;
#end#

endrewards

// Utility reward
rewards "u"
 #for i=NRT1+1:NRT1+NRT2#
  [r#i#T3done] true: r#i#u3;
  [r#i#T3Pdone] true: r#i#u3p;
 #end#
endrewards

// Time reward
rewards "t"
   true : 1;
endrewards