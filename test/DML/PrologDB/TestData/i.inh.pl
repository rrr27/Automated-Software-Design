dbase(inh,[one,two,three]).

table(one,[a,"b",c]).
one(1,'b',3).
one(2,'bbb',4).
one(3,'bbbb',5).

table(two,[a,"b",c,"d","e",f]).

table(three,[a,"b",c,g,h,i]).

subtable(one,[two,three]).
