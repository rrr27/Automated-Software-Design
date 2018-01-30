dbase(families2,[family,member]).

table(family,[id,lastName,fatherid,motherid]).
family(f1,March,m1,m2).
family(f2,Sailor,m5,m6).

table(member,[mid,firstName,sonOf,daughterOf]).
member(m1,Jim,null,null).
member(m2,Cindy,null,null).
member(m3,Brandon,f1,null).
member(m4,Brenda,null,f1).
member(m5,Peter,null,null).
member(m6,Jackie,null,null).
member(m7,David,f2,null).
member(m8,Dylan,f2,null).
member(m9,Kelly,null,f2).