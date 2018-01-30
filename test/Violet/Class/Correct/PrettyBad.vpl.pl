dbase(vpl,[vBox,vAssociation]).

table(vBox,[id,type,"name","fields","methods",x,y]).
vBox(ClassNode0,c,'A','','',498.0,114.0).
vBox(ClassNode1,c,'B','','',769.0,114.0).
vBox(ClassNode2,c,'C','','',598.0,248.0).
vBox(ClassNode3,c,'C','','',981.0,231.0).
vBox(Void0,c,'','','',844.0,224.0).

table(vAssociation,[id,cid1,type1,"role1","arrow1",cid2,type2,"role2","arrow2","bentStyle","lineStyle","middleLabel"]).
vAssociation(A0,ClassNode0,c,'','',ClassNode1,c,'','V','HVH','','').
vAssociation(A1,ClassNode0,c,'','',ClassNode2,c,'super','TRIANGLE','VHV','DOTTED','').
vAssociation(A2,ClassNode2,c,'has','V',ClassNode1,c,'','V','','DOTTED','').

