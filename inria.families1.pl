dbase(families1,[family,member]).

table(family,[fid,lastName,dadid,momid]).
family(f1,March,m1,m2).
family(f2,Sailor,m5,m6).

table(member,[mid,firstName,fid,isMale]).
member(m1,Jim,null,true).
member(m2,Cindy,null,false).
member(m3,Brandon,f1,true).
member(m4,Brenda,f1,false).
member(m5,Peter,null,true).
member(m6,Jackie,null,false).
member(m7,David,f2,true).
member(m8,Dylan,f2,true).
member(m9,Kelly,f2,false).

