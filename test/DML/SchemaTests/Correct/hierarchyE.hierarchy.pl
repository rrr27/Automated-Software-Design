dbase(hierarchy,[one,two,three]).

table(one,[a,"b",c]).

table(two,[a,"b",c,d,"e",f,x]).

table(three,[a,"b",c,d,"e",f,x,g,h,i,z,"r"]).

subtable(one,[two]).
subtable(two,[three]).
