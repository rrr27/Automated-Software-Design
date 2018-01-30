dbase(vpl,[vBox,vAssociation]).

table(vBox,[id,type,"name","fields","methods",x,y]).
vBox(ClassNode0,c,'c1','','',424.0,125.0).
vBox(ClassNode1,c,'c2','','',757.0,129.0).
vBox(ClassNode2,c,'c3','','',413.0,287.0).
vBox(ClassNode3,c,'c4','','',756.0,293.0).
vBox(ClassNode4,c,'c5','','',1013.0,174.0).
vBox(ClassNode5,c,'c6','','',1025.0,357.0).

table(vAssociation,[id,cid1,type1,"role1","arrow1",cid2,type2,"role2","arrow2","bentStyle","lineStyle","middleLabel"]).
vAssociation(A0,ClassNode0,c,'aaa','V',ClassNode1,c,'','','','DOTTED','').
vAssociation(A1,ClassNode2,c,'','',ClassNode3,c,'aaa','V','','','').
vAssociation(A2,ClassNode3,c,'','DIAMOND',ClassNode1,c,'','TRIANGLE','VHV','','').
vAssociation(A3,ClassNode2,c,'','TRIANGLE',ClassNode0,c,'','DIAMOND','VHV','','').
vAssociation(A4,ClassNode3,c,'','BLACK_DIAMOND',ClassNode5,c,'','','HVH','','').
vAssociation(A5,ClassNode4,c,'','BLACK_DIAMOND',ClassNode1,c,'','','VHV','','').

