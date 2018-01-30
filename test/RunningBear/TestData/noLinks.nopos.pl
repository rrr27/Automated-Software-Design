:-style_check(-discontiguous).

dbase(nopos,[interfaceExtends,association,class,classImplements,entity,interface]).

table(interfaceExtends,[id,base,extends]).
:- dynamic interfaceExtends/3.

table(association,[id,"role1",arrow1,"role2",arrow2,cid1,cid2]).
:- dynamic association/7.

table(class,[id,"name","fields","methods",superid]).
class(c5,'class2','f1;f2','m1();m2()',null).
class(c4,'class1','f1','m1()',null).
class(c3,'class0','','',null).

table(classImplements,[id,cid,iid]).
:- dynamic classImplements/3.

table(entity,[id]).
:- dynamic entity/1.

table(interface,[id,"name","methods"]).
interface(c2,'int2','m1();m2()').
interface(c1,'int1','m1()').
interface(c0,'int0','').
