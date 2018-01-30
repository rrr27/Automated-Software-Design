dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,c,'class0','','').
yumlBox(b1,c,'class1','f1','m1()').
yumlBox(b2,c,'class2','f1;f2','m1();m2()').
yumlBox(b3,i,'int2','','m1();m2()').
yumlBox(b4,i,'int0','','').
yumlBox(b5,i,'int1','','m1()').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).

