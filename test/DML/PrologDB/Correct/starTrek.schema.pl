dbase(starTrek,[crewman,commander,lieutenant]).

table(crewman,[cid,fname,lname]).
table(commander,[cid,fname,lname,rank]).
table(lieutenant,[cid,fname,lname,specialty]).

subtable(crewman,[commander,lieutenant]).
