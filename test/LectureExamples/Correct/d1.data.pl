dbase(data,[class,attribute]).

table(class,[cid,name]).

class(c1,city).
class(c2,account).                        
 
table(attribute,[aid,name,type,cid]).

attribute(a1,name,String,c1).
attribute(a2,state,String,c1).
attribute(a3,number,int,c2).
attribute(a4,balance,double,c2).

