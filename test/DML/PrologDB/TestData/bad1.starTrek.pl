:-style_check(-discontiguous).

dbase(starTrek,[crewman, commander, lieutenant]).

table(crewman,[cid,fname,lname]).
crewman(c1,mr,spock).

table(commander,[cid,fname,lname,rank]).
commander(c2,james,kirk,captain,extra).

table(lieutenant,[cid,fname,lname,specialty]).
lieutenant(c3,hikaru,sulu).

subtable(crewman,[commander,lieutenant]).
