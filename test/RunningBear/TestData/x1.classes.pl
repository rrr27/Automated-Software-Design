dbase(classes,[class,field]).

table(class,[cid,name,superName]).
class(c0,entity,null).
class(c1,student,entity).
class(c2,course,entity).

table(field,[fid,name,type,cid]).
field(f0,name,String,c0).
field(f1,UTEID,String,c1).
field(f2,profName,String,c2).
