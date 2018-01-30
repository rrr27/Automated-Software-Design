dbase(families2,[family,member]).

table(family,[id,lastName,fatherid,motherid]).
family(f1,March,m1,m2).
family(f2,Sailor,m5,m6).
family(f3,Grechanik,null,m6).
family(f4,Toth,m5,null).

table(member,[mid,firstName,sonOf,daughterOf]).
member(m1,Jim,f3,null).
member(m1,Jim,f4,null).
member(m2,Cindy,f4,f4).
member(m3,Brandon,f1,null).
member(m4,Brenda,null,f1).
member(m5,Peter,null,null).
member(m6,Jackie,null,null).
member(m7,David,f2,null).
member(m8,Dylan,f2,null).
member(m9,Kelly,null,f2).