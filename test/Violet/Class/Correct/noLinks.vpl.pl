dbase(vpl,[vBox,vAssociation]).

table(vBox,[id,type,"name","fields","methods",x,y]).
vBox(ClassNode0,c,'class0','','',210.0,111.0).
vBox(ClassNode1,c,'class1','a','m()',215.0,304.0).
vBox(ClassNode2,c,'class2','a%b','m1(a)%m2(b)',230.0,455.0).
vBox(InterfaceNode0,i,'int0','','',625.0,115.0).
vBox(InterfaceNode1,i,'int1','','m()',653.0,294.0).
vBox(InterfaceNode2,i,'int2','','m()%n()',640.0,472.0).
vBox(ClassNode3,c,'class3','a%"b"%c%','m1(a)%m2(b)%m(3)',221.0,647.0).
vBox(InterfaceNode3,i,'int3','','m()%n()%z()',638.0,623.0).
vBox(NoteNode0,n,'one','','',921.0,133.0).
vBox(NoteNode1,n,'two','','',937.0,277.0).
vBox(Void0,n,'','','',965.0,399.0).

table(vAssociation,[id,cid1,type1,"role1","arrow1",cid2,type2,"role2","arrow2","bentStyle","lineStyle","middleLabel"]).

