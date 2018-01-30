dbase(vpl,[vBox,vAssociation]).

table(vBox,[id,type,"name","fields","methods",x,y]).
vBox(ClassNode0,c,'A','','',204.0,80.0).
vBox(ClassNode1,c,'B','','',644.0,136.0).

table(vAssociation,[id,cid1,type1,"role1","arrow1",cid2,type2,"role2","arrow2","bentStyle","lineStyle","middleLabel"]).
vAssociation(A0,ClassNode0,c,'a','TRIANGLE',ClassNode1,c,'b','V','VHV','DOTTED','d').

