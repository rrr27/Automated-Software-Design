dbase(ce,[Person,Employer,ChildOf]).

table(Person,[pid,name,eid]).
table(Employer,[eid,name]).
table(ChildOf,[oid,cid,pid]).

Person(p1,don,e1).
Person(p2,hanna,e3).
Person(p3,priscila,e4).
Person(p4,karen,e2).
Person(p5,steve,e4).
Person(p6,ricky,e4).

Employer(e1,utcs).
Employer(e2,TMA).
Employer(e3,westCoast).
Employer(e4,microsoft).

ChildOf(c1,p5,p3).
ChildOf(c2,p6,p3).
ChildOf(c3,p2,p1).
ChildOf(c4,p2,p4).

