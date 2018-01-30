dbase(inh1,[one,two,three]).

table(one,[a,"b",c,length]).
one(1,'b',3,1).
one(2,'bbb',4,3).
one(3,'bbbb',5,4).

table(two,[a,"b",c,"d","e",f,length]).

table(three,[a,"b",c,g,h,i,length]).

subtable(one,[two,three]).
